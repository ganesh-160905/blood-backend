# Lifeline Link Backend

This is the Spring Boot backend for the Lifeline Link blood donation platform.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Setup Instructions

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE lifeline_link_db;
```

### 2. Configure Database Connection

Update the database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Build and Run

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/validate-token` - Validate JWT token

### User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/users/donations` - Get user donation history
- `GET /api/users/dashboard-stats` - Get dashboard statistics

### Blood Search
- `GET /api/blood-search/blood-banks` - Search blood banks
- `GET /api/blood-search/donors` - Search blood donors
- `GET /api/blood-search/compatible-donors` - Find compatible donors

### Feedback
- `POST /api/feedback/submit` - Submit feedback
- `GET /api/feedback/my-feedback` - Get user's feedback
- `GET /api/feedback/recent` - Get recent feedback
- `GET /api/feedback/stats` - Get feedback statistics

## Sample Data

The application includes sample data for testing:
- Sample blood banks with inventory
- Test users (password: `password123`)
- Admin user (email: `admin@lifelinelink.com`, password: `admin123`)
- Sample donations and feedback

## Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS configured for frontend integration
- Role-based authorization (USER, ADMIN)

## Technologies Used

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- Maven
"# blood-backend" 
"# blood-backend" 
