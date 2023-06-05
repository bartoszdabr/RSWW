using Hotel.Entity.Dto;
using Hotel.MongoDb;
using MediatR;


namespace Hotel.Querys.GetHotelById
{
    public class GetTopDestinationQueryHandler : IRequestHandler<GetTopDestinationQuery, List<TopDto>>
    {
        private readonly IHotelRepository _repository;

        public GetTopDestinationQueryHandler(IHotelRepository _repository)
        {
            this._repository = _repository;
        }

        public async Task<List<TopDto>> Handle(GetTopDestinationQuery request, CancellationToken cancellationToken)
        {
            var hotelEntity = await _repository.GetTopDestinations(request.count);
            return await Task.FromResult(hotelEntity);
        }
    }
}