using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Message
{
    public class MessageFromReservation
    {
        public Guid guid { get; set; }
        public string username { get; set; }
        public string roomReservationId { get; set; }
        public int numberOfPeople {get; set; }

        public string type { get; set; }
    }
}
