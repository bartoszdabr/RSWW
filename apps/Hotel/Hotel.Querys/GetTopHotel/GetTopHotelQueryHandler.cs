using Hotel.Entity.Dto;
using Hotel.MongoDb;
using MediatR;

namespace Hotel.Querys.GetHotelById
{
    public class GetTopHotelQueryHandler : IRequestHandler<GetTopHotelQuery, List<TopDto>>
    {
        private readonly IESRepository _repository;
        private readonly IHotelRepository _repository2;

        public GetTopHotelQueryHandler(IESRepository _repository, IHotelRepository repository2)
        {
            this._repository = _repository;
            this._repository2 = repository2;
        }

        public async Task<List<TopDto>> Handle(GetTopHotelQuery request, CancellationToken cancellationToken)
        {
            var hotelEntity = await _repository.GetTopHotel(request.count);

            foreach (var hotel in hotelEntity)
            {
                hotel.Name = _repository2.GetById(hotel.Name).Result.HotelName;
            }
            
            return await Task.FromResult(hotelEntity);
        }
    }
}