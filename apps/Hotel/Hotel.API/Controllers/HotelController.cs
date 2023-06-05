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
    public class HotelController : ControllerBase
    {
        private readonly IMediator _mediator;
        public HotelController(IMediator mediator)
        {
            _mediator = mediator;
        }

        [HttpGet("hotels")]
        public async Task<IActionResult> GetAll([FromQuery] HotelFilter? hotelFilter)
        {
            var query = new GetAllHotelsQuery() {filter = hotelFilter};
            var result = await _mediator.Send(query);

            return Ok(result);
        }

        [HttpGet("read/hotels")]
        public async Task<IActionResult> GetAll2([FromQuery] HotelFilter? hotelFilter)
        {
            var query = new GetAllHotels2Query() { filter = hotelFilter };
            var result = await _mediator.Send(query);

            return Ok(result);
        }

        [HttpGet("hotels/{id}")]
        public async Task<IActionResult> GetById(string id)
        {
            var query = new GetHotelByIdQuery { Id = id };
            var result = await _mediator.Send(query);

            if (result == null)
                return NotFound();

            return Ok(result);
        }
    }
}
