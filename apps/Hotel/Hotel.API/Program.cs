using Hotel.MongoDb;
using Hotel.Querys.GetAllHotels;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();



//Mongo
var configuration = builder.Configuration;
var mongoDbOptions = configuration.GetSection("MongoDB").Get<MongoDbOptions>();

builder.Services.Configure<MongoDbOptions>(options =>
{
    options.ConnectionString = mongoDbOptions.ConnectionString;
    options.Database = mongoDbOptions.Database;
});

builder.Services.AddSingleton<IMongoDbContext, MongoDbContext>();
builder.Services.AddSingleton<IHotelRepository, HotelRepository>();
builder.Services.AddSingleton<IESRepository, ESRepository>();


//MediatoR
builder.Services.AddMediatR(cfg => cfg.RegisterServicesFromAssemblyContaining(typeof(GetAllHotelsQuery)));

var app = builder.Build();

// Configure the HTTP request pipeline.
app.UseCors("Access-Control-Allow-Origin");

app.UseSwagger();
app.UseSwaggerUI();

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
