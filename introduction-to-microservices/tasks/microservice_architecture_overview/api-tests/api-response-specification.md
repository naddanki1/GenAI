# API Response Specification for Resource and Song Services: Expected Results for Test Validation

<!-- TOC -->
* [**Happy path**](#happy-path)
  * [**Upload valid MP3 resource (200)**](#upload-valid-mp3-resource-200)
  * [**Get existing resource (200)**](#get-existing-resource-200)
  * [**Get existing song metadata (200)**](#get-existing-song-metadata-200)
  * [**Delete resources with metadata (200)**](#delete-resources-with-metadata-200)
  * [**Get deleted resource (404)**](#get-deleted-resource-404)
  * [**Get deleted song metadata (404)**](#get-deleted-song-metadata-404)
* [**Error cases: Resource Service**](#error-cases-resource-service)
  * [**Upload invalid resource (400)**](#upload-invalid-resource-400)
  * [**Get non-existent resource (404)**](#get-non-existent-resource-404)
  * [**Get invalid ID - letters (400)**](#get-invalid-id---letters-400)
  * [**Get invalid ID - decimal (400)**](#get-invalid-id---decimal-400)
  * [**Get invalid ID - negative (400)**](#get-invalid-id---negative-400)
  * [**Get invalid ID - zero (400)**](#get-invalid-id---zero-400)
  * [**Delete non-existent resource (200)**](#delete-non-existent-resource-200)
  * [**Delete invalid CSV - letters (400)**](#delete-invalid-csv---letters-400)
  * [**Delete invalid CSV - length exceeded (400)**](#delete-invalid-csv---length-exceeded-400)
* [**Error cases: Song Service**](#error-cases-song-service)
  * [**Create song metadata - invalid fields (400)**](#create-song-metadata---invalid-fields-400)
  * [**Create song metadata - missing fields (400)**](#create-song-metadata---missing-fields-400)
  * [**Create song metadata - already exists (409)**](#create-song-metadata---already-exists-409)
  * [**Get non-existent song metadata (404)**](#get-non-existent-song-metadata-404)
  * [**Delete non-existent song metadata (200)**](#delete-non-existent-song-metadata-200)
  * [**Delete invalid song metadata CSV - letters (400)**](#delete-invalid-song-metadata-csv---letters-400)
  * [**Delete invalid song metadata CSV - length exceeded (400)**](#delete-invalid-song-metadata-csv---length-exceeded-400)
<!-- TOC -->

---

## **Happy path**

---

### **Upload valid MP3 resource (200)**
- **Method & endpoint:** `POST /resources`
- **Expected status code:** `200 OK`
- **Expected response body:**
  ```json
  {
    "id": 1
  }
  ```

---

### **Get existing resource (200)**
- **Method & endpoint:** `GET /resources/{id}`
- **Expected status code:** `200 OK`
- **Expected response body:**
    - Binary MP3 data

---

### **Get existing song metadata (200)**
- **Method & endpoint:** `GET /songs/{id}`
- **Expected status code:** `200 OK`
- **Expected response body:**
  ```json
  {
    "id": 1,
    "name": "Test Title",
    "artist": "Test Artist",
    "album": "Test Album",
    "duration": "00:07",
    "year": "2025"
  }
  ```

---

### **Delete resources with metadata (200)**
- **Method & endpoint:** `DELETE /resources?id=1,101,102`
- **Expected status code:** `200 OK`
- **Expected response body:**
  ```json
  {
    "ids": [1]
  }
  ```

---

### **Get deleted resource (404)**
- **Method & endpoint:** `GET /resources/{id}`
- **Expected status code:** `404 Not Found`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Resource with ID=1 not found",
    "errorCode": "404"
  }
  ```

---

### **Get deleted song metadata (404)**
- **Method & endpoint:** `GET /songs/{id}`
- **Expected status code:** `404 Not Found`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Song metadata for ID=1 not found",
    "errorCode": "404"
  }
  ```

---

## **Error cases: Resource Service**

### **Upload invalid resource (400)**
- **Method & endpoint:** `POST /resources`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid file format: application/json. Only MP3 files are allowed",
    "errorCode": "400"
  }
  ```

---

### **Get non-existent resource (404)**
- **Method & endpoint:** `GET /resources/99999`
- **Expected status code:** `404 Not Found`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Resource with ID=99999 not found",
    "errorCode": "404"
  }
  ```

---

### **Get invalid ID - letters (400)**
- **Method & endpoint:** `GET /resources/ABC`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid value 'ABC' for ID. Must be a positive integer",
    "errorCode": "400"
  }
  ```

---

### **Get invalid ID - decimal (400)**
- **Method & endpoint:** `GET /resources/1.1`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid value '1.1' for ID. Must be a positive integer",
    "errorCode": "400"
  }
  ```

---

### **Get invalid ID - negative (400)**
- **Method & endpoint:** `GET /resources/-1`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid value '-1' for ID. Must be a positive integer",
    "errorCode": "400"
  }
  ```

---

### **Get invalid ID - zero (400)**
- **Method & endpoint:** `GET /resources/0`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid value '0' for ID. Must be a positive integer",
    "errorCode": "400"
  }
  ```

---

### **Delete non-existent resource (200)**
- **Method & endpoint:** `DELETE /resources?id=99999`
- **Expected status code:** `200 OK`
- **Expected response body:**
  ```json
  {
    "ids": []
  }
  ```

---

### **Delete invalid CSV - letters (400)**
- **Method & endpoint:** `DELETE /resources?id=1,2,3,4,V`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid ID format: 'V'. Only positive integers are allowed",
    "errorCode": "400"
  }
  ```

---

### **Delete invalid CSV - length exceeded (400)**
- **Method & endpoint:** `DELETE /resources?id=2147483647,2147483646,2147483645,2147483644,2147483643,2147483642,2147483641,2147483640,2147483639,2147483638,2147483637,2147483636,2147483635,2147483634,2147483633,2147483632,2147483631,2147483630,2147483629`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "CSV string is too long: received 208 characters, maximum allowed is 200",
    "errorCode": "400"
  }
  ```

---

## **Error cases: Song Service**

### **Create song metadata - invalid fields (400)**
- **Method & endpoint:** `POST /songs`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Validation error",
    "details": {
      "duration": "Duration must be in mm:ss format with leading zeros",
      "year": "Year must be between 1900 and 2099"
    },
    "errorCode": "400"
  }
  ```

---

### **Create song metadata - missing fields (400)**
- **Method & endpoint:** `POST /songs`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Validation error",
    "details": {
      "name": "Song name is required"
    },
    "errorCode": "400"
  }
  ```

---

### **Create song metadata - already exists (409)**
- **Method & endpoint:** `POST /songs`
- **Expected status code:** `409 Conflict`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Metadata for resource ID=2 already exists",
    "errorCode": "409"
  }
  ```

---

### **Get non-existent song metadata (404)**
- **Method & endpoint:** `GET /songs/99999`
- **Expected status code:** `404 Not Found`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Song metadata for ID=99999 not found",
    "errorCode": "404"
  }
  ```

---

### **Delete non-existent song metadata (200)**
- **Method & endpoint:** `DELETE /songs?id=99999`
- **Expected status code:** `200 OK`
- **Expected response body:**
  ```json
  {
    "ids": []
  }
  ```

---

### **Delete invalid song metadata CSV - letters (400)**
- **Method & endpoint:** `DELETE /songs?id=1,2,3,4,V`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "Invalid ID format: 'V'. Only positive integers are allowed",
    "errorCode": "400"
  }
  ```

---

### **Delete invalid song metadata CSV - length exceeded (400)**
- **Method & endpoint:** `DELETE /songs?id=2147483647,2147483646,2147483645,2147483644,2147483643,2147483642,2147483641,2147483640,2147483639,2147483638,2147483637,2147483636,2147483635,2147483634,2147483633,2147483632,2147483631,2147483630,2147483629`
- **Expected status code:** `400 Bad Request`
- **Expected response body:**
  ```json
  {
    "errorMessage": "CSV string is too long: received 208 characters, maximum allowed is 200",
    "errorCode": "400"
  }
  ```
