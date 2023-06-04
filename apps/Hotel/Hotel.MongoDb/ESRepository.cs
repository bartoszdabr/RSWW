using Hotel.Entity.Filter;
using Hotel.Entity.Model;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity;

namespace Hotel.MongoDb
{
    public class ESRepository : IESRepository
    {
        private readonly IMongoDbContext _mongoContext;
        private readonly IMongoCollection<Event> _dbCollection;

        public ESRepository(IMongoDbContext dbConnection)
        {
            _mongoContext = dbConnection;
            _dbCollection = _mongoContext.GetCollection<Event>(typeof(Event).Name);
        }

        public async Task save(Event ev)
        {
            await _dbCollection.InsertOneAsync(ev);
        }

        public async Task<IEnumerable<Event>>? findAllByIdReservation(Event ev)
        {
            var filter = Builders<Event>.Filter.Eq(h => h.IdReservation, ev.IdReservation);
            var all = await _dbCollection.FindAsync(filter);
            return await all.ToListAsync();
        }

        public async Task<IEnumerable<Event>>? findAll()
        {
            var all = await _dbCollection.FindAsync(_ => true);
            return await all.ToListAsync();
        }

        public async Task<IEnumerable<Event>>? findAllByIdHotel(string id)
        {
            var filter = Builders<Event>.Filter.Eq(h => h.IdHotel, id);
            var all = await _dbCollection.FindAsync(filter);
            return await all.ToListAsync();
        }
    }
}
