using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Hotel.Entity.Json
{
    public class TripDetails
    {
        [JsonProperty("start_date")]
        public string StartDate { get; set; }

        [JsonProperty("end_date")]
        public string EndDate { get; set; }

        [JsonProperty("cost")]
        public int Cost { get; set; }

        [JsonProperty("start_location")]
        public string StartLocation { get; set; }

        [JsonProperty("destination")]
        public string Destination { get; set; }

        [JsonProperty("description")]
        public TripDescription Description { get; set; }

        [JsonProperty("tags")]
        public List<string> Tags { get; set; }

        [JsonProperty("opinion_score")]
        public double OpinionScore { get; set; }

        [JsonProperty("stars")]
        public double Stars { get; set; }

        [JsonProperty("images")]
        public List<string> Images { get; set; }

        [JsonProperty("hotel_name")]
        public string HotelName { get; set; }
    }
}
