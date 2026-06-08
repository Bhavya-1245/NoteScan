# NoteScan Backend API Testing Guide

## Server Setup
1. Start the server:
   ```bash
   mvn spring-boot:run
   ```
   Server runs on: `http://localhost:8080`

---

## API Endpoints

### 1. Health Check
**Endpoint:** `GET /api/v1/health`

Check if the server is running.

**cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/health
```

**Response:**
```json
{
  "status": "UP",
  "message": "NoteScan Backend is running",
  "version": "1.0.0"
}
```

---

### 2. Get Service Status
**Endpoint:** `GET /api/v1/status`

Get available endpoints and service information.

**cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/status
```

**Response:**
```json
{
  "status": "ready",
  "service": "NoteScan Desktop Integration",
  "endpoints": [
    "/api/v1/health",
    "/api/v1/status",
    "/api/v1/scan (POST)",
    "/api/v1/scan/batch (POST)"
  ]
}
```

---

### 3. Single Scan (Main Endpoint)
**Endpoint:** `POST /api/v1/scan`

Send text content from phone to the desktop notepad application.

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "content": "This is the text from my phone that I want to paste to my desktop notepad!"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello from phone!"}'
```

**Success Response (200):**
```json
{
  "status": "success",
  "message": "Successfully pasted to PC!"
}
```

**Error Response (400):**
```json
{
  "status": "error",
  "message": "Content cannot be empty"
}
```

**Error Response (500):**
```json
{
  "status": "error",
  "message": "Failed to process scan: [error details]"
}
```

---

### 4. Batch Scan
**Endpoint:** `POST /api/v1/scan/batch`

Send multiple text items at once.

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
[
  { "content": "First item to scan" },
  { "content": "Second item to scan" },
  { "content": "Third item to scan" }
]
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/scan/batch \
  -H "Content-Type: application/json" \
  -d '[{"content":"Item 1"},{"content":"Item 2"},{"content":"Item 3"}]'
```

**Response:**
```json
{
  "status": "success",
  "processed": 3,
  "total": 3
}
```

---

## Testing with Postman

### Import this as a Postman Collection:

```json
{
  "info": {
    "name": "NoteScan API",
    "description": "Test collection for NoteScan Backend",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/v1/health"
      }
    },
    {
      "name": "Get Status",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/v1/status"
      }
    },
    {
      "name": "Single Scan",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "http://localhost:8080/api/v1/scan",
        "body": {
          "mode": "raw",
          "raw": "{\"content\":\"Hello from phone!\"}"
        }
      }
    },
    {
      "name": "Batch Scan",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "http://localhost:8080/api/v1/scan/batch",
        "body": {
          "mode": "raw",
          "raw": "[{\"content\":\"Item 1\"},{\"content\":\"Item 2\"},{\"content\":\"Item 3\"}]"
        }
      }
    }
  ]
}
```

---

## Testing Sequence

1. **Start the server:**
   ```bash
   mvn spring-boot:run
   ```

2. **Check health:**
   ```bash
   curl http://localhost:8080/api/v1/health
   ```

3. **Get status:**
   ```bash
   curl http://localhost:8080/api/v1/status
   ```

4. **Test single scan:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/scan \
     -H "Content-Type: application/json" \
     -d '{"content":"Test message"}'
   ```

5. **Test batch scan:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/scan/batch \
     -H "Content-Type: application/json" \
     -d '[{"content":"Msg 1"},{"content":"Msg 2"}]'
   ```

---

## Example Test Cases

### Test Case 1: Basic Single Scan
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":"Scanned from my mobile device!"}'
```

### Test Case 2: Empty Content
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":""}'
```
Expected: 400 Bad Request

### Test Case 3: Special Characters
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":"Special chars: !@#$%^&*()_+-=[]{}|;:',\'\"<>?,./~`"}'
```

### Test Case 4: Long Text
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation..."}'
```

### Test Case 5: Multiple Line Text
```bash
curl -X POST http://localhost:8080/api/v1/scan \
  -H "Content-Type: application/json" \
  -d '{"content":"Line 1\nLine 2\nLine 3\nLine 4"}'
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Connection refused | Make sure server is running with `mvn spring-boot:run` |
| 404 Not Found | Check endpoint URL spelling |
| 400 Bad Request | Verify JSON format and required fields |
| 500 Server Error | Check console logs for detailed error message |
| GUI not opening | Ensure display/graphics are available on your system |

---

## Notes
- The server integrates with a desktop GUI application
- Text sent via the API will appear in the desktop notepad
- All endpoints return JSON responses
- Error handling includes detailed error messages
