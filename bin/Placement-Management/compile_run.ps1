# PowerShell Automation Script for College Placement Management System
# Compiles code, manages MySQL JDBC driver path, and handles database initialization.

# Force script to run in its own directory
Set-Location $PSScriptRoot

Clear-Host
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host "     College Placement Management System - Build & Run Tool      " -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan

$binDir = "bin"
$libJar = "lib/mysql-connector-j-9.0.0.jar"

function Show-Menu {
    Write-Host ""
    Write-Host "1) Initialize/Reset Database (Import schema.sql)" -ForegroundColor Yellow
    Write-Host "2) Build and Run Application" -ForegroundColor Green
    Write-Host "3) Build Only" -ForegroundColor Cyan
    Write-Host "4) Exit"
    Write-Host ""
}

$running = $true
while ($running) {
    Show-Menu
    $choice = Read-Host "Select an option (1-4)"
    
    switch ($choice) {
        "1" {
            Write-Host "`nInitializing database..." -ForegroundColor Yellow
            if (Test-Path "schema.sql") {
                Write-Host "Please enter your MySQL credentials to import the schema."
                $user = Read-Host "MySQL Username (default: root)"
                if ([string]::IsNullOrEmpty($user)) { $user = "root" }
                
                Write-Host "Executing schema.sql in MySQL..."
                # Run mysql command
                & mysql -u $user -p -e "source schema.sql"
                if ($LASTEXITCODE -eq 0) {
                    Write-Host "[+] Database 'placement_db' and tables initialized successfully!" -ForegroundColor Green
                } else {
                    Write-Warning "[!] Database import failed. Ensure MySQL is running and credentials are correct."
                }
            } else {
                Write-Error "[!] schema.sql not found in current directory."
            }
        }
        
        "2" {
            Write-Host "`nBuilding project..." -ForegroundColor Cyan
            # Create bin directory if not exists
            if (!(Test-Path $binDir)) {
                New-Item -ItemType Directory -Path $binDir | Out-Null
            }
            
            # Check for driver
            if (!(Test-Path $libJar)) {
                Write-Warning "[!] MySQL Connector JAR not found in lib/."
                Write-Host "Attempting to download JDBC driver..."
                if (!(Test-Path "lib")) { New-Item -ItemType Directory -Path "lib" | Out-Null }
                Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar" -OutFile $libJar
                Write-Host "[+] Downloaded JDBC driver." -ForegroundColor Green
            }
            
            # Compile
            Write-Host "Compiling Java sources..."
            javac -d $binDir -cp "lib/*" src/*.java
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "[+] Compilation successful!" -ForegroundColor Green
                Write-Host "Starting College Placement Management System...`n" -ForegroundColor Green
                # Run java with classpath
                java -cp "bin;lib/*" src.Main
            } else {
                Write-Error "[!] Compilation failed. Please check compiler errors."
            }
        }
        
        "3" {
            Write-Host "`nBuilding project..." -ForegroundColor Cyan
            if (!(Test-Path $binDir)) {
                New-Item -ItemType Directory -Path $binDir | Out-Null
            }
            
            javac -d $binDir -cp "lib/*" src/*.java
            if ($LASTEXITCODE -eq 0) {
                Write-Host "[+] Compilation successful! Class files located in 'bin/' folder." -ForegroundColor Green
            } else {
                Write-Error "[!] Compilation failed."
            }
        }
        
        "4" {
            $running = $false
            Write-Host "`nGoodbye!" -ForegroundColor Cyan
        }
        
        default {
            Write-Warning "[!] Invalid option, please enter 1, 2, 3 or 4."
        }
    }
}
