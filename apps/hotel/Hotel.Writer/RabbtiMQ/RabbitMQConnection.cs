using RabbitMQ.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.RabbtiMQ
{
    internal class RabbitMQConnection<T>
    {
        private readonly RabbitMqOptions _options;
        private IConnection _connection;

        public RabbitMQConnection(RabbitMqOptions options)
        {
            _options = options;
        }

        public IConnection GetConnection()
        {
            while (true)
            {
                try
                {
                    var factory = new ConnectionFactory
                    {
                        HostName = _options.HostName,
                        UserName = _options.UserName,
                        Password = _options.Password,
                        Port = _options.Port,
                        VirtualHost = _options.VirtualHost
                    };
                    _connection = factory.CreateConnection();
                    return _connection;
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"////////////////////////////");
                    Console.WriteLine($"Error connecting to RabbitMQ: {ex.Message}");
                    Console.WriteLine($"Error connecting to RabbitMQ: {_options.HostName} {_options.Port}");
                    Console.WriteLine($"Retrying connection to RabbitMQ in {_options.RetryIntervalSeconds} seconds...");
                    Thread.Sleep(TimeSpan.FromSeconds(_options.RetryIntervalSeconds));
                }
            }
        }
    }
}
