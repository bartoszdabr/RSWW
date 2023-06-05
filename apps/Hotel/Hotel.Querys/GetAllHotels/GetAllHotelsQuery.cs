using Hotel.Entity.Dto;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Filter;

namespace Hotel.Querys.GetAllHotels
{
    public class GetAllHotelsQuery
        : IRequest<IEnumerable<HotelDto>>
    {
        public HotelFilter? filter { get; set; }
    }
}
