using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Hotel.Entity.Json
{
    public class TripOffer
    {
        [JsonProperty("trip_offers")]
        public Dictionary<string, List<TripDetails>> TripOffers { get; set; }

    }
}
