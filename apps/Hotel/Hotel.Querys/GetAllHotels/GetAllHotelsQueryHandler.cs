using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Dto;
using Hotel.MongoDb;
using MediatR;

namespace Hotel.Querys.GetAllHotels
{
    public class GetAllHotelQueryHandler : IRequestHandler<GetAllHotelsQuery, IEnumerable<HotelDto>>
    {
        private readonly IHotelRepository _repository;

        public GetAllHotelQueryHandler(IHotelRepository _repository)
        {
            this._repository = _repository;
        }

        public Task<IEnumerable<HotelDto>> Handle(GetAllHotelsQuery request, CancellationToken cancellationToken)
        {
            var hotels = _repository.findAll(request.filter).Result
                .Select(h => new HotelDto()
                {
                    id = h.Id,
                    hotelName = h.HotelName,
                    destination = h.Destination,
                    fromDate = h.FromDate?.ToString("yyyy-MM-dd"),
                    toDate = h.ToDate?.ToString("yyyy-MM-dd"),
                    numberOfGuestsInAllRoom = h.NumberOfGuestsInAllRooms,
                    rooms = h.Rooms?.Where(r=> r.IsReserved != true).Select(r => new RoomDto()
                    {
                        RoomNumber = r.RoomNumber,
                        IsReserved = r.IsReserved
                    }).ToList(),
                });

            return Task.FromResult(hotels);
        }
    }
}
