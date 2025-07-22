# PorchPick API Documentation for Frontend Developers

## 1. Overview

Welcome to the PorchPick API! This document provides all the information you need to build a frontend application that interacts with our backend services. PorchPick allows users to sign up, log in, create items, and manage virtual yard sales.

**Base URL**: All API endpoints are relative to the base URL, which is typically `http://localhost:8080` for local development.

---

## 2. Authentication

The PorchPick API uses JSON Web Tokens (JWT) to handle authentication. Accessing protected endpoints requires a valid token.

### Step 1: User Signup

To create a new user, send a `POST` request to the `/auth/signup` endpoint.

- **Endpoint**: `POST /auth/signup`
- **Request Body**: `application/json`

```json
{
  "email": "test@example.com",
  "password": "yourStrongPassword123"
}
```

- **Success Response**: `201 Created`

```json
{
  "success": true,
  "message": "User registered successfully. Please login."
}
```

### Step 2: User Login

To authenticate and receive a JWT, send a `POST` request to the `/auth/login` endpoint.

- **Endpoint**: `POST /auth/login`
- **Request Body**: `application/json`

```json
{
  "email": "test@example.com",
  "password": "yourStrongPassword123"
}
```

- **Success Response**: `200 OK`

```json
{
  "success": true,
  "message": "Login successful.",
  "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "email": "test@example.com",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
> **Important**: After logging in, store the `id` and `token` securely. The `id` will be used in URL paths and parameters, and the `token` must be sent with all protected requests.

### Step 3: Making Authenticated Requests

For all protected endpoints, you must include the JWT in the `Authorization` header with the `Bearer` prefix.

- **Header**: `Authorization: Bearer <your_jwt_token>`

If the token is missing, invalid, or expired, the API will respond with a `401 Unauthorized` error.

---

## 3. Error Handling

The API provides standardized error responses. If a request fails, you will receive a JSON object with details about the error.

- **Sample Error Response**: `400 Bad Request` (e.g., for a validation error)

```json
{
  "timestamp": "2025-07-21T18:30:00.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "validationErrors": {
    "email": "must be a well-formed email address"
  }
}
```

---

## 4. API Endpoints

### Profiles

A user's profile contains their personal details and address.

- **Create a Profile**: `POST /users/{userId}/profile`
  - **Auth**: Required
  - **URL Parameter**: `userId` (The UUID of the user from the login response).
  - **Request Body**: `ProfileRequestDto`

    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "bio": "Yard sale enthusiast.",
      "street": "123 Main St",
      "city": "Anytown",
      "state": "CA",
      "country": "USA",
      "zipCode": "12345"
    }
    ```

- **Get a Profile**: `GET /users/{userId}/profile`
  - **Auth**: Required
  - **URL Parameter**: `userId`

- **Update a Profile**: `PUT /users/{userId}/profile`
  - **Auth**: Required
  - **URL Parameter**: `userId`
  - **Request Body**: Same as the create request.
  - **Success Response**: `202 Accepted`

### Items

Items are the individual products a user wants to sell.

- **Create an Item**: `POST /items`
  - **Auth**: Required
  - **Request Body**: `ItemRequestDto` (Note: `userId` is required in the body).

    ```json
    {
      "userId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
      "name": "Vintage Armchair",
      "description": "A beautiful mid-century modern armchair.",
      "price": 150.00
    }
    ```

- **Get All of a User's Items**: `GET /items?userId={userId}`
  - **Auth**: Required
  - **Query Parameter**: `userId`

- **Get a Single Item**: `GET /items/{itemId}`
  - **Auth**: Required

- **Update an Item**: `PUT /items/{itemId}`
  - **Auth**: Required
  - **Request Body**: Same as the create request.
  - **Success Response**: `202 Accepted`

- **Delete an Item**: `DELETE /items/{itemId}`
  - **Auth**: Required
  - **Success Response**: `204 No Content`

### Yard Sales

Yard sales are events that group items together for sale.

- **Create a Yard Sale**: `POST /yardsales`
  - **Auth**: Required
  - **Request Body**: `YardSaleRequestDto` (The `itemIds` array should contain the UUIDs of existing items).

    ```json
    {
      "userId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
      "title": "Annual Summer Yard Sale",
      "description": "Everything must go!",
      "startTime": "2025-08-01T08:00:00-05:00",
      "endTime": "2025-08-01T17:00:00-05:00",
      "itemIds": [
        "item_uuid_1",
        "item_uuid_2"
      ]
    }
    ```

- **Get All Yard Sales**: `GET /yardsales`
  - **Auth**: Required

- **Get a Single Yard Sale**: `GET /yardsales/{yardSaleId}`
  - **Auth**: Required

- **Update a Yard Sale**: `PUT /yardsales/{yardSaleId}`
  - **Auth**: Required
  - **Request Body**: Same as the create request.
  - **Success Response**: `202 Accepted`

- **Delete a Yard Sale**: `DELETE /yardsales/{yardSaleId}?userId={userId}`
  - **Auth**: Required
  - **Query Parameter**: `userId` (Required for authorization check).
  - **Success Response**: `204 No Content`

- **Add an Item to a Yard Sale**: `POST /yardsales/{yardSaleId}/items/{itemId}?userId={userId}`
  - **Auth**: Required
  - **Query Parameter**: `userId` (Required for authorization check).

- **Remove an Item from a Yard Sale**: `DELETE /yardsales/{yardSaleId}/items/{itemId}?userId={userId}`
  - **Auth**: Required
  - **Query Parameter**: `userId` (Required for authorization check).
