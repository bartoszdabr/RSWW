using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Writer.EventSorucing
{
    internal class ItemHotel: Aggregate
    {
        public string Name { get; private set; }

        public string Description { get; private set; }

        public string ActiveColumn { get; private set; }

        public bool IsCompleted { get; private set; }
        protected override void When(object @event)
        {
            throw new NotImplementedException();
        }
    }
}
