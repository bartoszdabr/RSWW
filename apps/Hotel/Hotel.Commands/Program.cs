using System.Runtime.CompilerServices;
using System.Text;
using Hotel.Commands;
using Hotel.MongoDb;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Configuration.Json;
using Microsoft.Extensions.DependencyInjection;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Hotel.Entity;
using Newtonsoft.Json;
using Hotel.Entity.Message;
using Hotel.Entity.Model;
using static System.Net.Mime.MediaTypeNames;

public class Program
{
    public static IHotelRepository _rep;
    public static void Main(string[] args)
    {
        var configuration = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();


        var serviceProvider = new ServiceCollection()
            .Configure<MongoDbOptions>(configuration.GetSection("MongoDB"))
            .AddSingleton<IMongoDbContext, MongoDbContext>()
            .AddSingleton<IESRepository, ESRepository>()
            .AddSingleton<EventSourcing>()
            .BuildServiceProvider();


        var connectionString = configuration.GetSection("MongoDB").Get<MongoDbOptions>();
        var rabbitMQSettings = configuration.GetSection("RabbitMQ").Get<RabbitMQSettings>();
        var QueueMQSettings = configuration.GetSection("Queue").Get<Queue>();

        var test = serviceProvider.GetRequiredService<EventSourcing>();

        var factory = new ConnectionFactory
        {
            HostName = rabbitMQSettings.HostName,
            UserName = rabbitMQSettings.UserName,
            Password = rabbitMQSettings.Password,
            Port = rabbitMQSettings.Port
        };

        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
            channel.QueueDeclare(queue: QueueMQSettings.ReadFromReservation, durable: true, exclusive: false, autoDelete: false, arguments: null);
            channel.QueueDeclare(queue: QueueMQSettings.SendToReservation, durable: true, exclusive: false, autoDelete: false, arguments: null);
            channel.QueueDeclare(queue: QueueMQSettings.SendToHandler, durable: true, exclusive: false, autoDelete: false, arguments: null);

            var consumer = new EventingBasicConsumer(channel);
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var message = System.Text.Encoding.UTF8.GetString(body);
                var receivedMessage = JsonConvert.DeserializeObject<MessageFromReservation>(message);

                // Przetwarzanie wiadomości w zależności od typu
                MessageType messageType;
                Console.WriteLine($"{receivedMessage.type}");
                switch (receivedMessage.type)
                {
                    case "ADD":
                        var isSucces = test.Reservation(receivedMessage);

                        var successMessage = new MessageToReservation();
                        successMessage.guid = receivedMessage.guid;
                        successMessage.status = isSucces.Item1 ? "SUCCEEDED" : "FAILURE";

                        var successMessageJson = JsonConvert.SerializeObject(successMessage);
                        var successMessageBytes = Encoding.UTF8.GetBytes(successMessageJson);
                        channel.BasicPublish(exchange: "", routingKey: QueueMQSettings.SendToReservation, basicProperties: null, body: successMessageBytes);

                        if (isSucces.Item1)
                        {
                            var itemHotel = new MessageHotel
                            {
                                hotel = isSucces.Item2,
                                type = MessageType.Update
                            };
                            var itemHotelMessageJson = JsonConvert.SerializeObject(itemHotel);
                            var itemHotelMessageBytes = Encoding.UTF8.GetBytes(itemHotelMessageJson);
                            channel.BasicPublish(exchange: "", routingKey: QueueMQSettings.SendToHandler, basicProperties: null, body: itemHotelMessageBytes);

                        }

                        break;
                    case "CANCEL":
                        var isSucces2 = test.Cancel(receivedMessage);

                        if (isSucces2.Item1)
                        {
                            var itemHotel = new MessageHotel
                            {
                                hotel = isSucces2.Item2,
                                type = MessageType.Update
                            };
                            var itemHotel2MessageJson = JsonConvert.SerializeObject(itemHotel);
                            var itemHotel2MessageBytes = Encoding.UTF8.GetBytes(itemHotel2MessageJson);
                            channel.BasicPublish(exchange: "", routingKey: QueueMQSettings.SendToHandler, basicProperties: null, body: itemHotel2MessageBytes);
                        }
                        break;
                    case "CREATE":
                        ///
                        break;
                    default:
                        Console.WriteLine("Nieobsługiwany typ wiadomości: " + receivedMessage.type);
                        break;
                }

                // Potwierdzenie przetworzenia wiadomości
                channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
            };

            channel.BasicConsume(queue: QueueMQSettings.ReadFromReservation, autoAck: false, consumer: consumer);

            Console.WriteLine("Aplikacja oczekuje na wiadomości...");
            Console.ReadLine();

            while (true)
            {

            }
        }

    }
}
