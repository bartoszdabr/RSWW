using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Filter;
using MongoDB.Driver;
using Hotel.Entity.Model;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Hotel.MongoDb
{
    public class HotelRepository : IHotelRepository
    {
        private readonly IMongoDbContext _mongoContext;
        private readonly IMongoCollection<HotelEntity> _dbCollection;

        public HotelRepository(IMongoDbContext dbConnection)
        {
            _mongoContext = dbConnection;
            _dbCollection = _mongoContext.GetCollection<HotelEntity>(typeof(HotelEntity).Name);
        }

        public async Task save(HotelEntity hotel)
        {
            await _dbCollection.InsertOneAsync(hotel);
        }

        public async Task update(HotelEntity hotel)
        {
            var filter = Builders<HotelEntity>.Filter.Eq(h => h.Id, hotel.Id);
            _dbCollection.ReplaceOne(filter, hotel);
        }

        public async Task delete(HotelEntity hotel)
        {
            var filter = Builders<HotelEntity>.Filter.Eq(h => h.Id, hotel.Id);
            _dbCollection.DeleteOne(hotel.Id);
        }

        public async Task<IEnumerable<HotelEntity>>? findAll(HotelFilter hotelFilter)
        {
            var filter = this.BuildFilter(hotelFilter);

            var all = await _dbCollection.FindAsync(filter);
            return await all.ToListAsync();
        }

        public async Task<HotelEntity> GetById(string id)
        {
            var filter = Builders<HotelEntity>.Filter.Eq(h => h.Id, id);
            return await _dbCollection.Find(filter).FirstOrDefaultAsync();
        }

        private FilterDefinition<HotelEntity> BuildFilter(HotelFilter hotelFilter)
        {
            var filterBuilder = Builders<HotelEntity>.Filter;
            var filter = filterBuilder.Empty;

            // Filtrowanie dla daty od-do
            if (hotelFilter.FromDate.HasValue && hotelFilter.ToDate.HasValue)
            {
                filter &= filterBuilder.Gte(h => h.FromDate, hotelFilter.FromDate) &
                          filterBuilder.Lte(h => h.ToDate, hotelFilter.ToDate);
            }
            else if (hotelFilter.FromDate.HasValue)
            {
                filter &= filterBuilder.Gte(h => h.FromDate, hotelFilter.FromDate);
            }
            else if (hotelFilter.ToDate.HasValue)
            {
                filter &= filterBuilder.Lte(h => h.ToDate, hotelFilter.ToDate);
            }

            // Filtrowanie dla miejsca docelowego
            if (!string.IsNullOrEmpty(hotelFilter.Destination))
            {
                filter &= filterBuilder.Eq(h => h.Destination, hotelFilter.Destination);
            }

            // Filtrowanie dla ilości osób
            if (hotelFilter.NumberOfGuestsInAllRooms.HasValue)
            {
                filter &= filterBuilder.Gte(h => h.NumberOfGuestsInAllRooms, hotelFilter.NumberOfGuestsInAllRooms);
            }

            return filter;
        }
    }

}