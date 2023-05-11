using Newtonsoft.Json;
using RabbitMQ.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.RabbtiMQ
{
    internal class RabbitMqMessageSender<T>
    {
        private readonly IModel _channel;

        public RabbitMqMessageSender(IModel channel)
        {
            _channel = channel;
        }

        public void SendMessage(string queueName, T message)
        {
            var jsonMessage = JsonConvert.SerializeObject(message);
            var body = Encoding.UTF8.GetBytes(jsonMessage);

            _channel.BasicPublish(exchange: "", routingKey: queueName, basicProperties: null, body: body);
        }
    }
}
