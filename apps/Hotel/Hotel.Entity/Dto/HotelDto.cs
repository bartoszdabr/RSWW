using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Dto
{
    public class HotelDto
    {
        public string? id { get; set; }

        public string? hotelName { get; set; }
        public string? fromDate { get; set; }
        public string? toDate { get; set; }

        public string? destination { get; set; }
        public int? numberOfGuestsInAllRoom { get; set; }
        public List<RoomDto>? rooms { get; set; }
    }
}
