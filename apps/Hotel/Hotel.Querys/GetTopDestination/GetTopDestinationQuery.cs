using Hotel.Entity.Dto;
using MediatR;


namespace Hotel.Querys.GetHotelById
{
    public class GetTopDestinationQuery
        : IRequest<List<TopDto>>
    {
        public int count { get; set; }
    }
}
