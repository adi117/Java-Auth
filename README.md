# Java-Auth-BE

A secure authentication backend built with Spring Boot, JWT, and role-based access control. Designed to integrate with modern frontend frameworks like Next.js (e.g., deployed on Vercel).

---

## 🚀 Features

- User registration and login
- Secure password hashing with BCrypt
- JWT-based stateless authentication
- Refresh token support
- Token blacklist for logout
- PostgreSQL database support
- Modular and clean architecture
- CORS ready for frontend consumption

---

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT**
- **PostgreSQL**
- **Maven**

---

## 📦 Setup

### 1. Clone the project

```bash
git clone https://github.com/adi117/Java-Auth-BE.git
cd Java-Auth-BE
```

### 2. Configure environment variables

Set the following in your system, `.env`, or deploy environment:

```env
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password

JWT_SECRET=your_jwt_secret
JWT_REFRESH_SECRET=your_refresh_secret
```

Make sure `application.properties` uses these `${}` placeholders, as already configured.

### 3. Build and run the application

```bash
./mvnw clean package
java -jar target/*.jar
```

Or run through your IDE with the proper profile and environment variables set.

---

## 🛠️ API Endpoints

| Method | Endpoint           | Description              | Auth Required |
|--------|--------------------|--------------------------|----------------|
| POST   | `/auth/register`   | Register a new user      | ❌ No          |
| POST   | `/auth/login`      | Login and receive tokens | ❌ No          |
| POST   | `/auth/logout`     | Logout + blacklist token | ✅ Yes         |
| GET    | `/user/me`         | Get current user profile | ✅ Yes         |

> All protected routes require a valid `Authorization: Bearer <token>` header.

---

## 🧪 Services Overview

- `UserServices`: Handles registration and user queries
- `AuthServices`: Handles login and credentials verification
- `TokenGeneratorService`: Generates and verifies access/refresh tokens

---

## 🐳 Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t java-auth-be .
docker run -e POSTGRES_HOST=... -e JWT_SECRET=... -p 8080:8080 java-auth-be
```

---

## 📎 Integrating with Frontend

Make sure your frontend (e.g., Vercel-hosted Next.js app) sends CORS-compliant requests to your deployed backend:

```ts
const api = "https://your-backend-url.com";
fetch(`${api}/auth/login`, { ... })
```

---

## 📄 License

MIT — feel free to use and adapt.

---

## 🙋‍♂️ Author

**Muhammad Adi Wicaksono**  
Feel free to reach out for collaboration or feedback!