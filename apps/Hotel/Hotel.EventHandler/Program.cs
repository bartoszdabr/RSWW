using System.Runtime.CompilerServices;
using System.Text;
using Hotel.EventHandler;
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
            .AddSingleton<IHotelRepository, HotelRepository>()
            .BuildServiceProvider();


        var connectionString = configuration.GetSection("MongoDB").Get<MongoDbOptions>();
        var rabbitMQSettings = configuration.GetSection("RabbitMQ").Get<RabbitMQSettings>();

        _rep = serviceProvider.GetRequiredService<IHotelRepository>();

        //var test = hotelRepository.findAll(new HotelFilter()).Result;
        Console.WriteLine(rabbitMQSettings.HostName);
        Console.WriteLine(rabbitMQSettings.Port);
        Console.WriteLine(rabbitMQSettings.UserName);
        Console.WriteLine(rabbitMQSettings.Password);
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
            channel.QueueDeclare(queue: rabbitMQSettings.QueueName, durable: true, exclusive: false, autoDelete: false, arguments: null);

            var consumer = new EventingBasicConsumer(channel);
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var message = System.Text.Encoding.UTF8.GetString(body);
                var receivedMessage = JsonConvert.DeserializeObject<MessageHotel>(message);

                // Przetwarzanie wiadomości w zależności od typu
                MessageType messageType;
                Console.WriteLine($"{receivedMessage.type}");
                switch (receivedMessage.type)
                {
                    case MessageType.Create:
                        CreateRecord(receivedMessage.hotel);
                        break;
                    case MessageType.Update:
                        UpdateRecord(receivedMessage.hotel);
                        break;
                    case MessageType.Delete:
                        DeleteRecord(receivedMessage.hotel);
                        break;
                    default:
                        Console.WriteLine("Nieobsługiwany typ wiadomości: " + receivedMessage.type);
                        break;
                }

                // Potwierdzenie przetworzenia wiadomości
                channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
            };

            channel.BasicConsume(queue: rabbitMQSettings.QueueName, autoAck: false, consumer: consumer);

            Console.WriteLine("Aplikacja oczekuje na wiadomości...");
            Console.ReadLine();

            while (true)
            {

            }
        }

    }
    private static void CreateRecord(HotelEntity message)
    {
        _rep.save(message);
        Console.WriteLine("Tworzenie rekordu w bazie danych: " + message);
    }

    private static void UpdateRecord(HotelEntity message)
    {
        _rep.update(message);
        Console.WriteLine("Aktualizacja rekordu w bazie danych: " + message);
    }

    private static void DeleteRecord(HotelEntity message)
    {
        _rep.delete(message);
        Console.WriteLine("Usuwanie rekordu z bazy danych: " + message);
    }

}
