using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Dto
{
    public class RoomDto
    {
        public int? RoomNumber { get; set; }

        public int? NumberOfGuests { get; set; }

        public bool? IsReserved { get; set; }
    }
}
