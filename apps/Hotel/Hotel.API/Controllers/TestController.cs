using Hotel.Entity.Filter;
using Hotel.Entity.Json;
using Hotel.Entity.Model;
using Hotel.MongoDb;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Globalization;
using Hotel.Entity;
using MongoDB.Bson;
using System.Security.Cryptography;

namespace Hotel.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TestController : Controller
    {
        private readonly ILogger<HotelController> _logger;
        private readonly IHotelRepository _rep;
        private readonly IESRepository _rep2;


        public TestController(ILogger<HotelController> logger, IHotelRepository rep, IESRepository rep2)
        {
            _logger = logger;
            _rep = rep;
            _rep2 = rep2;
        }

        [HttpGet]
        public async Task<IActionResult> SaveAll()
        {
            var rng = new Random();
            var entity = new HotelEntity()
            {
                Destination = "Des",
                FromDate = DateTime.Now - TimeSpan.FromDays(rng.Next(10)),
                ToDate = DateTime.Now - TimeSpan.FromDays(rng.Next(10)),
                NumberOfGuestsInAllRooms = rng.Next(10),
            };
        
            _rep.save(entity);
            return Ok(entity);
        }

        [HttpPost("api/Test/Init")]
        public async Task<ObjectResult> ZrobCos()
        {
            //
            try
            {
                //string jsonFilePath = @"C:\RSWW\apps\hotel\Hotel\Hotel.API\file.json";
                string jsonFilePath = "/app/file.json";
                string jsonContent = await System.IO.File.ReadAllTextAsync(jsonFilePath);

                TripOffer tripOffersData = JsonConvert.DeserializeObject<TripOffer>(jsonContent);

                await SaveTripOffersData(tripOffersData);

                return Ok("Dane z pliku JSON zostały zapisane do bazy danych.");
            }
            catch (IOException ex)
            {
                return StatusCode(500, $"Wystąpił błąd podczas odczytu pliku JSON: {ex.Message}");
            }
            catch (JsonException ex)
            {
                return StatusCode(500, $"Wystąpił błąd podczas deserializacji danych JSON: {ex.Message}");
            }
        }

        private async Task SaveTripOffersData(TripOffer tripOffer)
        {
            foreach (var offer in tripOffer.TripOffers["all-inclusive"])
            {
                var rng = new Random();
                var cout = 0;
                var coutRoom = rng.Next(1, 5);
                var ListRoom = new List<HotelRoom>();
                for (var i = 0; i < coutRoom; i++)
                {
                    var room = new HotelRoom()
                    {
                        RoomNumber = rng.Next(100 + i),
                        IsReserved = false
                    };
                    ListRoom.Add(room);
                }

                var id = ObjectId.GenerateNewId();
                Console.WriteLine(id.ToString());
                var hotel = new HotelEntity()
                {
                    Id = id.ToString(),
                    HotelName = offer.HotelName,
                    FromDate = DateTime.ParseExact(offer.StartDate+".2023", "dd.MM.yyyy", CultureInfo.InvariantCulture),
                    ToDate = DateTime.ParseExact(offer.EndDate+".2023", "dd.MM.yyyy", CultureInfo.InvariantCulture),
                    Destination = offer.Destination.Split('/')[0]?.Trim(),
                    NumberOfGuestsInAllRooms = rng.Next(2,5) * ListRoom.Count(),
                    Rooms = ListRoom,
                    Description = new HotelDescription()
                    {
                        Beach = offer.Description.Beach,
                        Contact = offer.Description.Contact,
                        Spa = offer.Description.Spa,
                        SportsAndEntertainment = offer.Description.SportsAndEntertainment,
                        UltraAllInclusive = offer.Description.UltraAllInclusive
                    },
                    Stars = offer.Stars,
                    Tags = offer.Tags,
                    OpinionScore = offer.OpinionScore,
                    Images = offer.Images

                };

                var ev = new Event()
                {
                    IdReservation = null,
                    IdHotel = id.ToString(),
                    Type = TypeEvent.Create,
                    DateTime = DateTime.UtcNow,
                    Data = JsonConvert.SerializeObject(hotel)
                };

                _rep.save(hotel);
                _rep2.save(ev);
            }
        }
    }
}
