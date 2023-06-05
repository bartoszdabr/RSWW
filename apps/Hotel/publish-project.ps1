# Define the root directory of your solution
$rootDirectory = "C:\Programowanie\RSWW\RSWW\apps\Hotel"

# Define the projects to publish
$projects = @(
    "Hotel.API",
    "Hotel.EventHandler",
    "Hotel.Commands"
)

# Iterate through each project and publish
foreach ($project in $projects) {
    $projectPath = Join-Path -Path $rootDirectory -ChildPath $project
    $outputPath = Join-Path -Path $projectPath -ChildPath "bin/Release/net7.0/publish"

    Write-Host "Publishing project: $projectPath"
    Write-Host "Output directory: $outputPath"

    dotnet publish $projectPath -c Release -o $outputPath
}
