using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Options;

namespace Hotel.MongoDb
{
    public class MongoDbContext : IMongoDbContext
    {
        public IMongoDatabase _db { get; set; }
        public IClientSessionHandle Session { get; }
        private MongoClient _mongoClient { get; set; }

        public MongoDbContext(IOptions<MongoDbOptions> options)
        {
            _mongoClient = new MongoClient(options.Value.ConnectionString);
            _db = _mongoClient.GetDatabase(options.Value.Database);
        }

        public IMongoCollection<T> GetCollection<T>(string tabele)
        {
            return _db.GetCollection<T>(tabele);
        }
    }
}
