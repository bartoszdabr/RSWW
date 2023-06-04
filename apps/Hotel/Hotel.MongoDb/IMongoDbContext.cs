using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MongoDB.Driver;

namespace Hotel.MongoDb
{
    public interface IMongoDbContext
    {
        public IMongoDatabase _db { get; }

        public IClientSessionHandle Session { get; }
        IMongoCollection<T> GetCollection<T>(string tabele);
    }
}
