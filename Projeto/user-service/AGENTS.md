# AGENTS.md - Spring Boot Project Guidelines

## 🛠 Tech Stack
* **Language:** Java 24 (Utilizing latest features like Records, Pattern Matching, and Virtual Threads).
* **Framework:** Spring Boot 3+
* **Dependency Manager:** Maven
* **Database:** JPA / Hibernate

## 💎 Development Standards

### Dependency Injection
* **Always** use constructor-based injection.
* Avoid using `@Autowired` on private fields.

### Java 24 & Modern Java
* Prefer `record` for data transfer classes (DTOs).
* Use `var` for local variables where the type is obvious.
* Leverage Virtual Threads performance improvements for asynchronous processing.

### Error Handling
* Centralized in a `@RestControllerAdvice`.
* Error responses must follow a consistent JSON pattern (e.g., timestamp, status, error, path).

## 🚀 CRUD Implementation Flow
When creating a new resource (e.g., `Product`), follow this order:
1.  **Model:** Define the `@Entity`.
2.  **Repository:** Create the data access interface.
3.  **DTOs:** Create records for Request and Response.
4.  **Service:** Implement the logic (Create, Read, Update, Delete).
5.  **Controller:** Expose the endpoints and map them to the Service.

---