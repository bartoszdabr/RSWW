# Wybierz obraz bazowy dla uruchomienia projektu API (np. .NET Runtime)
FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build-env

# Ustaw folder roboczy wewnątrz kontenera
WORKDIR /app

# Skopiuj skompilowane pliki projektu API do folderu roboczego wewnątrz kontenera
COPY bin/Release/net7.0/publish .

# Ustaw punkt wejścia dla kontenera
ENTRYPOINT ["dotnet", "Hotel.EventHandler.dll"]
