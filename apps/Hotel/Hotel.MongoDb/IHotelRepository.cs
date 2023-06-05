using Hotel.Entity.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Filter;
using Hotel.Entity.Dto;

namespace Hotel.MongoDb
{
    public interface IHotelRepository
    {
        Task save(HotelEntity hotel);
        Task<IEnumerable<HotelEntity>>? findAll(HotelFilter filter);
        Task<HotelEntity> GetById(string id);
        Task delete(HotelEntity hotel);
        Task update(HotelEntity hotel);
        Task<List<TopDto>> GetTopDestinations(int count);


    }
}