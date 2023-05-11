# Użyj obrazu bazowego .NET SDK 7.0
FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build-env

# Ustaw katalog roboczy na katalog projektu
WORKDIR /app

# Skopiuj pliki projektu do katalogu roboczego
COPY *.csproj ./
COPY . ./

# Wykonaj kompilację projektu
RUN dotnet restore
RUN dotnet publish -c Release -o out

# Użyj obrazu bazowego .NET Runtime 7.0
FROM mcr.microsoft.com/dotnet/runtime:7.0

# Ustaw katalog roboczy na katalog aplikacji
WORKDIR /app

# Skopiuj skompilowane pliki z poprzedniego obrazu
COPY --from=build-env /app/out .

# Uruchom aplikację po uruchomieniu kontenera
ENTRYPOINT ["dotnet", "Hotel.Writer.dll"]
