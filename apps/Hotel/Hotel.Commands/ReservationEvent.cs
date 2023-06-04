using Hotel.Entity.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Commands
{
    public class ReservationEvent
    {
        public int? NumberOfGuestsInAllRooms { get; set; }
        public List<HotelRoom>? Rooms { get; set; }
    }
}
