using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.EventSorucing
{
    public class HoteltemNotFoundException : Exception
    {
        public HoteltemNotFoundException() : base("Hotel Item nie został już ukończony!")
        {


        }
    }
}
