using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Model;

namespace Hotel.Entity.Message
{
    public class MessageHotel
    {
        public MessageType type { get; set; }
        public HotelEntity hotel { get; set; }
    }
}
