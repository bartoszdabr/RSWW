using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Entity
{
    public class Event
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        public Guid? IdReservation {get; set; }

        public string? IdHotel { get; set; }

        public TypeEvent? Type { get; set; }

        public DateTime? DateTime {get; set; }
        public string? Data { get; set; }

    }
}
