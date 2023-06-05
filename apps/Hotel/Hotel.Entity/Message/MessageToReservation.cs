using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Message
{
    public class MessageToReservation
    {
        public Guid guid { get; set; }
        public string status { get; set; }
    }
}
