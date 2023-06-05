using Hotel.Entity.Dto;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Hotel.Entity.Filter;

namespace Hotel.Querys.GetAllHotels2
{
    public class GetAllHotels2Query
        : IRequest<IEnumerable<HotelDtoRes>>
    {
        public HotelFilter? filter { get; set; }
    }
}
