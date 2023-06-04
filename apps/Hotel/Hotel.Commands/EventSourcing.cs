using Hotel.Entity.Model;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using static System.Net.Mime.MediaTypeNames;
using static System.Runtime.InteropServices.JavaScript.JSType;
using System.Collections;
using Hotel.Entity;
using Hotel.MongoDb;
using Hotel.Entity.Message;
using RabbitMQ.Client;

namespace Hotel.Commands
{
    public class EventSourcing
    {
        public List<Event> TestList { get; set; }
        private readonly IESRepository _rep;

        public EventSourcing(IESRepository rep)
        {
            this._rep = rep;
        }

        public void Create(HotelEntity newHotel)
        {
            var createEvent = new Event()
            {
                IdReservation = null,
                IdHotel = newHotel.Id,
                Type = TypeEvent.Create,
                DateTime = DateTime.UtcNow,
                Data = JsonConvert.SerializeObject(newHotel)
            };

            _rep.save(createEvent);
        }

        public (bool,HotelEntity) Reservation(MessageFromReservation res)
        {
            var hotelEvents = _rep.findAllByIdHotel(res.roomReservationId)?.Result;
            var hotel = ApplyEvent(hotelEvents);

            Console.WriteLine(hotel.NumberOfGuestsInAllRooms);
            if (hotel.NumberOfGuestsInAllRooms < res.numberOfPeople || hotel.Rooms.Count(c => c.IsReserved == false) == 0)
            {
                Console.WriteLine($"Rezerwacja {hotel.Id} na {res.numberOfPeople} - NIE UDANA");
                return (false,new HotelEntity());
            }

            HotelRoom? room = hotel.Rooms?.FirstOrDefault(c => c.IsReserved == false);
            room.IsReserved = true;

            var reservation = new ReservationEvent
            {
                Rooms = new List<HotelRoom>(),
                NumberOfGuestsInAllRooms = hotel.NumberOfGuestsInAllRooms - res.numberOfPeople
            };
            reservation.Rooms.Add(room);

            var reservationEvent = new Event()
            {
                IdReservation = res.guid,
                IdHotel = hotel.Id,
                Type = TypeEvent.Reservation,
                DateTime = DateTime.UtcNow,
                Data = JsonConvert.SerializeObject(reservation)
            };
            hotel = ApplyEvent(hotelEvents);
            _rep.save(reservationEvent);
            Console.WriteLine($"Rezerwacja {hotel.Id} na {res.numberOfPeople} - UDANA");
            return (true,hotel);
        }

        public (bool, HotelEntity) Cancel(MessageFromReservation res)
        {
            var hotelEvents = _rep.findAllByIdHotel(res.roomReservationId)?.Result;
            if (hotelEvents.Any(c => c.IdReservation == res.guid && c.Type == TypeEvent.Cancel))
            {
                Console.WriteLine($"Anulowanie rezerwacji {res.roomReservationId} na {res.numberOfPeople} - NIE UDANA");
                return (false, new HotelEntity());
            }
            var hotel = ApplyEvent(hotelEvents);

            var eve = hotelEvents.Where(c => c.IdReservation == res.guid && c.Type == TypeEvent.Reservation).First();
            var cancelData = JsonConvert.DeserializeObject<ReservationEvent>(eve.Data);
            cancelData.NumberOfGuestsInAllRooms += res.numberOfPeople;
            foreach (var room in cancelData.Rooms)
            {
                room.IsReserved = false;
            }

            var cancelEvent = new Event()
            {
                IdReservation = res.guid,
                IdHotel = hotel.Id,
                Type = TypeEvent.Cancel,
                DateTime = DateTime.UtcNow,
                Data = JsonConvert.SerializeObject(cancelData)
            };
            hotel = ApplyEvent(hotelEvents);
            _rep.save(cancelEvent);
            Console.WriteLine($"Anulowanie rezerwacji {res.roomReservationId} na {res.numberOfPeople} - UDANA");
            return (true, hotel);
        }

        public void Delete(HotelEntity hotel)
        {
            // Logika usuwania hotelu

            var deleteEvent = new Event()
            {
                IdReservation = new Guid(),
                IdHotel = hotel.Id,
                Type = TypeEvent.Reservation,
                DateTime = DateTime.UtcNow,
                Data = JsonConvert.SerializeObject(hotel)
            };

            TestList.Add(deleteEvent);
        }

        private HotelEntity ApplyEvent(IEnumerable<Event> hotelEvents)
        {
            var Hotel = new HotelEntity();
            foreach (var ev in hotelEvents)
            {
                switch (ev.Type)
                {
                    case TypeEvent.Create:
                        var createData = JsonConvert.DeserializeObject<HotelEntity>(ev.Data);
                        Hotel.Id = createData.Id;
                        Hotel.HotelName = createData.HotelName;
                        Hotel.Destination = createData.Destination;
                        Hotel.FromDate = createData.FromDate;
                        Hotel.ToDate = createData.ToDate;
                        Hotel.NumberOfGuestsInAllRooms = createData.NumberOfGuestsInAllRooms;
                        Hotel.Rooms = createData.Rooms;
                        Hotel.Description = createData.Description;
                        Hotel.Tags = createData.Tags;
                        Hotel.OpinionScore = createData.OpinionScore;
                        Hotel.Stars = createData.Stars;
                        Hotel.Images = createData.Images;
                        break;

                    case TypeEvent.Reservation:
                        var updateData = JsonConvert.DeserializeObject<ReservationEvent>(ev.Data);
                        Hotel.NumberOfGuestsInAllRooms = updateData.NumberOfGuestsInAllRooms;
                        foreach (var room in Hotel.Rooms)
                        {
                            if (room.RoomNumber == updateData.Rooms.First().RoomNumber)
                            {
                                room.IsReserved = updateData.Rooms.First().IsReserved;
                            }
                        }
                        break;

                    case TypeEvent.Cancel:
                        var cancelData = JsonConvert.DeserializeObject<ReservationEvent>(ev.Data);
                        Hotel.NumberOfGuestsInAllRooms = cancelData.NumberOfGuestsInAllRooms;
                        foreach (var room in Hotel.Rooms)
                        {
                            if (room.RoomNumber == cancelData.Rooms.First().RoomNumber)
                            {
                                room.IsReserved = cancelData.Rooms.First().IsReserved;
                            }
                        }
                        break;
                    case TypeEvent.Delete:
                        // Logika usuwania hotelu
                        break;
                }
            }
            return Hotel;
        }
    }
}
