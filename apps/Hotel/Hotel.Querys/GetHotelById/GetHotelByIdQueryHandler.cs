using Hotel.Entity.Dto;
using Hotel.MongoDb;
using Hotel.Querys.GetAllHotels;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Querys.GetHotelById
{
    public class GetHotelById : IRequestHandler<GetHotelByIdQuery, HotelDetailsDto>
    {
        private readonly IHotelRepository _repository;

        public GetHotelById(IHotelRepository _repository)
        {
            this._repository = _repository;
        }

        public async Task<HotelDetailsDto> Handle(GetHotelByIdQuery request, CancellationToken cancellationToken)
        {
            var hotelEntity = await _repository.GetById(request.Id);
            var hotelDto = new HotelDetailsDto()
            {
                id = hotelEntity.Id,
                hotelName = hotelEntity.HotelName,
                destination = hotelEntity.Destination,
                fromDate = hotelEntity.FromDate?.ToString("yyyy-MM-dd"),
                toDate = hotelEntity.ToDate?.ToString("yyyy-MM-dd"),
                numberOfGuestsInAllRoom = hotelEntity.NumberOfGuestsInAllRooms,
                Stars = hotelEntity.Stars,
                rooms = hotelEntity.Rooms?.Select(r => new RoomDto
                {
                    RoomNumber = r.RoomNumber,
                    IsReserved = r.IsReserved,
                }).ToList(),
                Description = hotelEntity.Description,
                Tags = hotelEntity.Tags,
                OpinionScore = hotelEntity.OpinionScore,
                Images = hotelEntity.Images
            };

            return await Task.FromResult(hotelDto);
        }
    }
}
