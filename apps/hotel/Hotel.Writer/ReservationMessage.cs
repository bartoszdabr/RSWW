using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer
{
    public class ReservationMessage
    {
        public Guid Guid { get; set; }
        public string Username { get; set; }
        public string IdReservationRoom { get; set; }
        public Person Osoby { get; set; }
    }
}
