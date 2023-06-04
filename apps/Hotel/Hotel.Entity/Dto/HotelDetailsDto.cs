using Hotel.Entity.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Dto
{
    public class HotelDetailsDto
    {
        public string? id { get; set; }

        public string? hotelName { get; set; }

        public string? destination { get; set; }
        public string? fromDate { get; set; }
        public string? toDate { get; set; }

        public int? numberOfGuestsInAllRoom { get; set; }
        public List<RoomDto>? rooms { get; set; }

        public HotelDescription? Description { get; set; }

        public List<string>? Tags { get; set; }

        public double? OpinionScore { get; set; }

        public double? Stars { get; set; }

        public List<string>? Images { get; set; }
    }
}
