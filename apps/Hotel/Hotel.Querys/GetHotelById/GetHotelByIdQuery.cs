using Hotel.Entity.Dto;
using Hotel.Entity.Filter;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hotel.Querys.GetHotelById
{
    public class GetHotelByIdQuery
        : IRequest<HotelDetailsDto>
    {
        public string? Id { get; set; }
    }
}
