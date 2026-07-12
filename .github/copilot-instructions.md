# MomentLit AI Development Instructions

## Project Context

This repository is part of the MomentLit backend service.

Each repository is treated as an independent MSA-style service.
Do not assume that this service owns logic or database tables from another service.

Current service examples:

* `Auth`: authentication and authorization responsibility
* `User`: user-domain responsibility

When generating code, keep the service boundary clear.

## Tech Stack

Use the existing project stack.

* Java 21
* Spring Boot
* Gradle
* PostgreSQL
* Spring Data JPA
* Lombok
* JUnit Platform

Do not introduce new frameworks or libraries unless they are clearly necessary.

## Package Structure

Follow the existing base package style.

```text
src/main/java/com/example/{service}
```

When adding features, use a layered structure.

```text
com.example.{service}
├── controller
├── service
├── repository
├── domain
├── dto
├── config
├── exception
└── global
```

Use this structure consistently.

## Naming Rules

Use clear, domain-based names.

Examples:

```text
UserController
UserService
UserRepository
User
UserCreateRequest
UserResponse
AuthController
AuthService
LoginRequest
TokenResponse
```

Do not use vague names such as:

```text
Manager
Processor
Handler
Data
Info
Util
```

unless there is a strong reason.

## Layer Responsibility

Controller should only handle HTTP request and response.

Controller must not contain business logic.

Service should contain business rules and transaction logic.

Repository should only access the database.

Entity should represent the database model.

DTO should be used for request and response.

Do not expose Entity directly from Controller.

## DTO Rules

Separate request DTO and response DTO.

Use names like:

```text
CreateUserRequest
UpdateUserRequest
UserResponse
LoginRequest
LoginResponse
```

Do not reuse Entity as API response.

Request DTO should contain validation annotations when needed.

Response DTO should only contain data that the client needs.

## Entity Rules

Entities should be placed in the `domain` package.

Use JPA annotations consistently.

Prefer protected no-args constructors for JPA.

Use static factory methods or meaningful constructors instead of setting fields everywhere.

Avoid business logic leaking into Controller.

Example style:

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
```

## Service Rules

Use `@Service`.

Use `@Transactional` on methods that change data.

Use `@Transactional(readOnly = true)` on read-only methods.

Do not call Repository directly from Controller.

Example:

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return UserResponse.from(user);
    }
}
```

## Controller Rules

Use RESTful endpoint naming.

Use plural nouns for resource paths.

Examples:

```text
GET /users/{userId}
POST /users
PATCH /users/{userId}
DELETE /users/{userId}
```

For Auth service:

```text
POST /auth/signup
POST /auth/login
POST /auth/logout
POST /auth/refresh
```

Return `ResponseEntity` when status control is needed.

Do not return raw strings for real API responses.

## Exception Rules

Do not scatter `IllegalArgumentException` everywhere in final code.

Prefer custom exceptions when the domain becomes stable.

Example:

```text
UserNotFoundException
DuplicateEmailException
InvalidTokenException
```

Handle exceptions in a global exception handler.

```text
global/GlobalExceptionHandler.java
```

API error responses should have a consistent format.

Example:

```json
{
  "code": "USER_NOT_FOUND",
  "message": "User not found"
}
```

## Transaction Rules

Any method that creates, updates, or deletes data must use `@Transactional`.

Any method that only reads data should use `@Transactional(readOnly = true)`.

Do not perform external API calls inside a transaction unless necessary.

Keep transaction scope as small as possible.

## Security Rules

Never hard-code secrets, tokens, passwords, or database credentials.

Use environment variables or configuration files ignored by Git.

Never log passwords, access tokens, refresh tokens, or authorization headers.

Auth service owns authentication logic.

Other services should not duplicate password validation or token issuing logic.

## MSA Boundary Rules

Do not directly access another service's database table.

Do not create cross-service JPA relationships.

Do not use `@ManyToOne` or `@OneToMany` across service boundaries.

Use IDs for references to other services.

Example:

```java
private Long userId;
```

not:

```java
@ManyToOne
private User user;
```

## Test Rules

When adding or changing business logic, add tests.

At minimum, create service-layer unit tests.

For repository logic, use JPA tests.

For controller endpoints, use web-layer tests when possible.

Test names should explain behavior.

Example:

```java
@Test
void createUser_shouldSaveUser_whenEmailIsUnique() {
}
```

Do not skip tests for important logic.

## Commit Convention

Follow the repository convention.

Allowed types:

```text
feat: new feature
fix: bug fix
refactor: refactoring without behavior change
test: test code
chore: minor maintenance
docs: documentation
delete: remove unused code or files
build: build configuration
```

Commit message format:

```text
type: summary
```

Example:

```text
feat: add user signup API
fix: resolve duplicate email validation
test: add auth service tests
```

Branch format:

```text
type/#issue-number
```

Example:

```text
feat/#1
fix/#3
```

## AI Code Generation Rules

Before generating code, inspect the existing package and naming style.

Do not rewrite unrelated files.

Do not change build settings unless the task requires it.

Do not introduce unnecessary abstraction.

Prefer simple, readable Spring Boot code.

When creating a new feature, generate files in this order:

1. Entity
2. Repository
3. DTO
4. Service
5. Controller
6. Exception handling
7. Test

After generating code, check:

* Is the service boundary respected?
* Is business logic outside Controller?
* Is transaction handling correct?
* Are DTOs separated from Entity?
* Are tests included?
* Are secrets excluded?
* Does naming match the existing style?

## Default Response Style for AI

When explaining code or changes, respond in Korean.

Keep explanations practical and concise.

When showing code, include the file path above each code block.

Do not only provide code; briefly explain why the change is needed.
