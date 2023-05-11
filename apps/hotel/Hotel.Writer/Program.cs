using System.Text;
using Hotel.Writer;
using Hotel.Writer.RabbtiMQ;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

public class Program
{
    private static async Task Main(string[] args)
    {
        //SendMessage();
        //return;
        var configuration = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
            .Build();

        var options = configuration.GetSection("RabbitMQOptions").Get<RabbitMqOptions>();
        Random rnd = new Random();
        var rabbitMQConnection = new RabbitMQConnection<object>(options);
        var connection = rabbitMQConnection.GetConnection();

        var channel = connection.CreateModel();
        var channel2 = connection.CreateModel();
        channel.QueueDeclare(queue: "hotelQueue", durable: true, exclusive: false, autoDelete: false, arguments: null);
        channel2.QueueDeclare(queue: "reservationQueue", durable: true, exclusive: false, autoDelete: false, arguments: null);


        Console.WriteLine("Połączono");
        String? guid = null;
        var consumer = new EventingBasicConsumer(channel);
        consumer.Received += (model, ea) =>
        {
            var body = ea.Body.ToArray();
            var message = Encoding.UTF8.GetString(body);

            var reservationMessage = JsonConvert.DeserializeObject<ReservationMessage>(message);
            guid = reservationMessage.Guid;
            Console.WriteLine($"[Hotel] Odebrano wiadomość {reservationMessage.Guid} na pokój {reservationMessage.IdReservationRoom}");

            var messageSender = new RabbitMqMessageSender<RetrunMessage>(channel2);
            

            if (rnd.Next(1, 100) > 10)
            {
                messageSender.SendMessage("reservationQueue", new RetrunMessage() { guid = guid, status = "FAILURE" });
                Console.WriteLine($"[Hotel] Rezerwacja sie udała na id {guid}");
            }
            else
            {
                messageSender.SendMessage("reservationQueue", new RetrunMessage() { guid = guid, status = "OK" });
                Console.WriteLine($"[Hotel] Rezerwacja sie nie udała na id {guid}");
            }

            channel.BasicAck(ea.DeliveryTag, multiple: false);
        };

        channel.BasicConsume(queue: "hotelQueue", autoAck: false, consumer: consumer);

        while (true)
        {

        }
    }

    public static void SendMessage()
    {
        var configuration = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
            .Build();

        var options = configuration.GetSection("RabbitMQOptions").Get<RabbitMqOptions>();

        var rabbitMQConnection = new RabbitMQConnection<object>(options);
        var connection = rabbitMQConnection.GetConnection();

        var channel = connection.CreateModel();
        channel.QueueDeclare(queue: "hotelQueue", durable: false, exclusive: false, autoDelete: false, arguments: null);

        Console.WriteLine("Połączono");

        for (int i = 1; i <= 2; i++)
        {
            //var reservationMessage = new ReservationMessage
            //{
            //    Guid = new Guid(),
            //    Username = "username_" + i,
            //    IdReservationRoom = "id_rezerwacji_pokoju_" + i,
            //    Osoby = new Person
            //    {
            //        DoLat3 = i,
            //        DoLat10 = i * 2,
            //DoLat18 = i * 3,
            //        Dorosli = i * 4
            //    }
            //};

            var messageObject = new
            {
                Guid = Guid.NewGuid(),
                Username = "abc",
                IdReservationRoom = "roomReservationId",
                Osoby = new
                {
                    DoLat3 = 1,
                    DoLat10 = 2,
                    DoLat18 = 0,
                    Dorosli = 2
                }
            };

            var messageBytes = Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(messageObject));
            channel.BasicPublish(exchange: "", routingKey: "hotelQueue", basicProperties: null, body: messageBytes);

            Console.WriteLine("Wysłano wiadomość " + i);
        }

        connection.Close();
    }
}