using Hotel.Entity.Dto;
using MediatR;


namespace Hotel.Querys.GetHotelById
{
    public class GetTopHotelQuery
        : IRequest<List<TopDto>>
    {
        public int count { get; set; }
    }
}
