using Hotel.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.MongoDb
{
    public interface IESRepository
    {
       Task save(Event ev);
       Task<IEnumerable<Event>>? findAllByIdReservation(Event ev);
       Task<IEnumerable<Event>>? findAllByIdHotel(string id);
       Task<IEnumerable<Event>>? findAll();
    }
}
