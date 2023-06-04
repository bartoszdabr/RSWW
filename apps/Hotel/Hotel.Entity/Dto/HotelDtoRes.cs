using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Dto
{
    public class HotelDtoRes
    {
        public string? hotelId { get; set; }

        public string? name { get; set; }
        public double? rating { get; set; }
        public double? stars { get; set; }

        public string? location { get; set; }
        public int? numberOfGuestsInAllRoom { get; set; }
        public string? startDate { get; set; }
        public string? endDate { get; set; }
        public int? numOfPeople { get; set; }
    }
}
