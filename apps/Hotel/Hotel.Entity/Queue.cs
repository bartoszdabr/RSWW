using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity
{
    public class Queue
    {
        public string ReadFromReservation { get; set; }
        public string SendToReservation { get; set; }
        public string SendToHandler { get; set; }
        public string ReadFromHandler { get; set; }
    }
}
