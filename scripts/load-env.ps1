
$envFile = Join-Path (Get-Location) '.env'
if (-not (Test-Path $envFile)) {
  Write-Host "No .env file found at $envFile. Copy .env.example to .env and fill values." -ForegroundColor Yellow
  exit 1
}

Get-Content $envFile | ForEach-Object {
  if ($_ -match '^[\s]*([^#=]+)=(.*)$') {
    $name = $matches[1].Trim()
    $value = $matches[2].Trim()
    try {
      Set-Item -Path "Env:$name" -Value $value -ErrorAction Stop
      Write-Host "Set env $($name)"
    } catch {
      Write-Host "Failed to set env $($name): $_" -ForegroundColor Red
    }
  }
}

Set-Location (Join-Path (Get-Location) 'apps/backend')
Write-Host "Starting backend with DB_PASSWORD: $env:DB_PASSWORD"
mvn -Dspring-boot.run.profiles=dev spring-boot:run
