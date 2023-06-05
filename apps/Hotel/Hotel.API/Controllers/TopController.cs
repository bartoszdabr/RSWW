using Hotel.Entity.Dto;
using Hotel.Entity.Filter;
using Hotel.Querys.GetAllHotels;
using Hotel.Querys.GetAllHotels2;
using Hotel.Querys.GetHotelById;
using MediatR;
using Microsoft.AspNetCore.Mvc;

namespace Hotel.API.Controllers
{
    [Route("api/hotels/v1")]
    [ApiController]
    public class TopController : ControllerBase
    {
        private readonly IMediator _mediator;
        public TopController(IMediator mediator)
        {
            _mediator = mediator;
        }


        [HttpGet("read/hotels/top")]
        public async Task<ActionResult<List<TopDto>>> GetAll2()
        {
            var query = new GetTopHotelQuery { count = 10 };
            var result = await _mediator.Send(query);

            if (result == null)
                return NotFound();

            return Ok(result);
        }

        [HttpGet("read/destination/top")]
        public async Task<ActionResult<List<TopDto>>> GetById()
        {
            var query = new GetTopDestinationQuery { count = 10 };
            var result = await _mediator.Send(query);

            if (result == null)
                return NotFound();

            return Ok(result);
        }
    }
}
