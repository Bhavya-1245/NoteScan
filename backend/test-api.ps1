# NoteScan API Test Script
# Run this after starting: mvn spring-boot:run

$baseUrl = "http://localhost:8080/api/v1"

Write-Host "===== NoteScan Backend API Testing =====" -ForegroundColor Cyan
Write-Host ""

# Test 1: Health Check
Write-Host "1. Testing Health Check..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/health" -Method Get
    Write-Host "✓ Health Check PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Health Check FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

# Test 2: Status Check
Write-Host "2. Testing Status Endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/status" -Method Get
    Write-Host "✓ Status Check PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Status Check FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

# Test 3: Single Scan
Write-Host "3. Testing Single Scan Endpoint..." -ForegroundColor Yellow
try {
    $body = @{ content = "Hello from PowerShell test script!" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/scan" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✓ Single Scan PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Single Scan FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

# Test 4: Empty Content (Should Fail)
Write-Host "4. Testing Empty Content (Expected to Fail)..." -ForegroundColor Yellow
try {
    $body = @{ content = "" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/scan" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✗ Should have failed but didn't" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode.Value -eq 400) {
        Write-Host "✓ Correctly rejected empty content (400)" -ForegroundColor Green
    } else {
        Write-Host "✗ Unexpected error: $($_.Exception.Message)" -ForegroundColor Red
    }
}
Write-Host ""

# Test 5: Batch Scan
Write-Host "5. Testing Batch Scan Endpoint..." -ForegroundColor Yellow
try {
    $body = @(
        @{ content = "First batch item" },
        @{ content = "Second batch item" },
        @{ content = "Third batch item" }
    ) | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/scan/batch" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✓ Batch Scan PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Batch Scan FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

# Test 6: Special Characters
Write-Host "6. Testing Special Characters..." -ForegroundColor Yellow
try {
    $body = @{ content = "Special chars: !@#$%^&*()_+-=[]{}|;:'`"<>?,./~" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/scan" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✓ Special Characters PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Special Characters FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

# Test 7: Multi-line Text
Write-Host "7. Testing Multi-line Text..." -ForegroundColor Yellow
try {
    $body = @{ content = "Line 1`nLine 2`nLine 3`nLine 4" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/scan" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✓ Multi-line Text PASSED" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "✗ Multi-line Text FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
Write-Host ""

Write-Host "===== Test Suite Complete =====" -ForegroundColor Cyan
