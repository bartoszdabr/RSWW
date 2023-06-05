using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Filter
{
    public class HotelFilter
    {
        public DateTime? FromDate { get; set; }
        public DateTime? ToDate { get; set; }
        public string? Destination { get; set; }
        public int? NumberOfGuestsInAllRooms { get; set; }

    }
}
