# Wybierz obraz bazowy dla uruchomienia projektu API (np. .NET Runtime)
FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS runtime

# Ustaw folder roboczy wewnątrz kontenera
WORKDIR /app

# Skopiuj skompilowane pliki projektu API do folderu roboczego wewnątrz kontenera
COPY bin/Release/net7.0/publish .

COPY file.json /app/file.json

# Ustaw punkt wejścia dla kontenera
ENTRYPOINT ["dotnet", "Hotel.API.dll"]
