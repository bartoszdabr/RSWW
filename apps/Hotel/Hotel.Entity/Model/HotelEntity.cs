using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MongoDB.Bson;
using Hotel.Entity.Json;

namespace Hotel.Entity.Model
{
    public class HotelEntity
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        public string HotelName { get; set; }

        public string? Destination { get; set; }

        public DateTime? FromDate { get; set; }

        public DateTime? ToDate { get; set; }

        public int? NumberOfGuestsInAllRooms { get; set; }


        public List<HotelRoom>? Rooms { get; set; }


        public HotelDescription? Description { get; set; }

        public List<string>? Tags { get; set; }

        public double? OpinionScore { get; set; }

        public double? Stars { get; set; }

        public List<string>? Images { get; set; }
    }
}
