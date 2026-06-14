# Java App Builder and Runner

# Ensure the 'bin' directory exists for compiled classes
if (-not (Test-Path -Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "Compiling Java application..." -ForegroundColor Cyan
# Compile App.java with classpath libraries and output to bin/
javac -d bin -cp "lib/*" src/App.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful. Launching application..." -ForegroundColor Green
    # Run application with bin/ and libs in classpath
    java -cp "bin;lib/*" App
} else {
    Write-Host "Compilation failed. Please check the logs." -ForegroundColor Red
}
