using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Dto;
using Hotel.MongoDb;
using Hotel.Querys.GetAllHotels2;
using MediatR;

namespace Hotel.Querys.GetAllHotels
{
    public class GetAllHotel2QueryHandler : IRequestHandler<GetAllHotels2Query, IEnumerable<HotelDtoRes>>
    {
        private readonly IHotelRepository _repository;

        public GetAllHotel2QueryHandler(IHotelRepository _repository)
        {
            this._repository = _repository;
        }

        public Task<IEnumerable<HotelDtoRes>> Handle(GetAllHotels2Query request, CancellationToken cancellationToken)
        {
            var hotels = _repository.findAll(request.filter).Result
                .Select(h => new HotelDtoRes()
                {
                    hotelId = h.Id,
                    name = h.HotelName,
                    rating = h.OpinionScore,
                    stars = h.Stars,
                    location = h.Destination,
                    startDate = h.FromDate?.ToString("yyyy-MM-dd"),
                    endDate = h.ToDate?.ToString("yyyy-MM-dd"),
                    numberOfGuestsInAllRoom = h.NumberOfGuestsInAllRooms,
                    numOfPeople = h.NumberOfGuestsInAllRooms
                });

            return Task.FromResult(hotels);
        }
    }
}