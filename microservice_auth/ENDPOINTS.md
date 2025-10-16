# API Endpoints Documentation

Base URL: `http://localhost:8080`

## Authentication Endpoints

### 1. Register User
**POST** `/api/auth/register`

**Request:**
```json
{
  "username": "testuser",
  "password": "password123",
  "userProfileId": 1
}
```

**Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 900,
  "user": {
    "id": 2,
    "username": "testuser",
    "isActive": true,
    "userProfileId": 1,
    "roles": [
      {
        "id": 2,
        "name": "USER"
      }
    ]
  }
}
```

---

### 2. Login
**POST** `/api/auth/login`

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 900,
  "user": {
    "id": 1,
    "username": "admin",
    "isActive": true,
    "userProfileId": null,
    "roles": [
      {
        "id": 1,
        "name": "ADMIN"
      }
    ]
  }
}
```

---

### 3. Refresh Token
**POST** `/api/auth/refresh-token`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 900
}
```

---

### 4. Logout
**POST** `/api/auth/logout`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

---

## User Management Endpoints

**Note:** All user management endpoints require JWT authentication.

**Authorization Header:**
```
Authorization: Bearer {accessToken}
```

---

### 5. Get All Users
**GET** `/api/users`

**Authorization:** Required (ADMIN or USER role)

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "isActive": true,
    "userProfileId": null,
    "roles": [
      {
        "id": 1,
        "name": "ADMIN"
      }
    ]
  },
  {
    "id": 2,
    "username": "testuser",
    "isActive": true,
    "userProfileId": 1,
    "roles": [
      {
        "id": 2,
        "name": "USER"
      }
    ]
  }
]
```

---

### 6. Get User by ID
**GET** `/api/users/{id}`

**Authorization:** Required (ADMIN or USER role)

**Example:** `GET /api/users/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "admin",
  "isActive": true,
  "userProfileId": null,
  "roles": [
    {
      "id": 1,
      "name": "ADMIN"
    }
  ]
}
```

---

### 7. Update User
**PUT** `/api/users/{id}`

**Authorization:** Required (ADMIN role only)

**Example:** `PUT /api/users/2`

**Request:**
```json
{
  "username": "updateduser",
  "isActive": false,
  "userProfileId": 5
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "username": "updateduser",
  "isActive": false,
  "userProfileId": 5,
  "roles": [
    {
      "id": 2,
      "name": "USER"
    }
  ]
}
```

---

### 8. Delete User
**DELETE** `/api/users/{id}`

**Authorization:** Required (ADMIN role only)

**Example:** `DELETE /api/users/2`

**Response (200 OK):**
```json
{
  "message": "User deleted successfully"
}
```

---

### 9. Assign Role to User
**POST** `/api/users/{userId}/roles/{roleName}`

**Authorization:** Required (ADMIN role only)

**Example:** `POST /api/users/2/roles/MODERATOR`

**Response (200 OK):**
```json
{
  "message": "Role assigned successfully"
}
```

---

### 10. Remove Role from User
**DELETE** `/api/users/{userId}/roles/{roleName}`

**Authorization:** Required (ADMIN role only)

**Example:** `DELETE /api/users/2/roles/MODERATOR`

**Response (200 OK):**
```json
{
  "message": "Role removed successfully"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "validationErrors": {
    "username": "Username is required",
    "password": "Password must be between 6 and 100 characters"
  }
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied"
}
```

### 404 Not Found
```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User already exists with username: testuser"
}
```

---

## Available Roles

- **ADMIN**: Full access to all endpoints
- **USER**: Read access to user endpoints
- **MODERATOR**: Custom role (can be assigned)

---

## Default Credentials

After starting the application, use these credentials to test:

- **Username:** `admin`
- **Password:** `admin123`
- **Role:** `ADMIN`
