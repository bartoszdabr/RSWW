using Hotel.Entity.Filter;
using Hotel.MongoDb;
using Hotel.Querys.GetAllHotels;
using Hotel.Querys.GetHotelById;
using MediatR;
using Microsoft.AspNetCore.Mvc;

namespace Hotel.API.Controllers
{
    [Route("api/hotels/v1")]
    [ApiController]
    public class EventController : ControllerBase
    {
        private readonly IESRepository _rep;
        public EventController(IESRepository rep)
        {
            _rep = rep;
        }

        [HttpGet("events")]
        public async Task<IActionResult> GetAll()
        {

            var events = await _rep.findAll();
            return Ok(events);
        }

        [HttpGet("events/{id}")]
        public async Task<IActionResult> GetById(string id)
        {
            var ev = await _rep.findAllByIdHotel(id);
            if (ev == null)
            {
                return NotFound();
            }

            return Ok(ev);
        }
    }
}