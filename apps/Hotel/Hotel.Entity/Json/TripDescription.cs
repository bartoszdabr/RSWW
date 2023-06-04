using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity.Json
{
    public class TripDescription
    {
        [JsonProperty("POŁOŻENIE")]
        public string Location { get; set; }

        [JsonProperty("PLAŻA")]
        public string Beach { get; set; }

        [JsonProperty("HOTEL")]
        public string Hotel { get; set; }

        [JsonProperty("POKÓJ")]
        public string Room { get; set; }

        [JsonProperty("SPORT I ROZRYWKA")]
        public string SportsAndEntertainment { get; set; }

        [JsonProperty("SPA")]
        public string Spa { get; set; }

        [JsonProperty("ULTRA ALL INCLUSIVE")]
        public string UltraAllInclusive { get; set; }

        [JsonProperty("KONTAKT")]
        public string Contact { get; set; }
    }
}
