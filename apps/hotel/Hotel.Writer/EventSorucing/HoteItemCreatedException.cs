using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.EventSorucing
{
    internal class HoteItemCreatedException : Exception
    {
        public HoteItemCreatedException() : base("Hotel Item został stworzony!")
        {


        }
    }
}
