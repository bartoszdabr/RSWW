using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.EventSorucing
{
    public class HoteltemCompletedException : Exception
    {
        public HoteltemCompletedException() : base("Hotel Item został już ukończony!")
        {


        }
    }
}
