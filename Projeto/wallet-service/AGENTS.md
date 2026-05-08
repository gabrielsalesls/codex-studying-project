# AGENTS.md - Spring Boot Project Guidelines

## 🛠 Tech Stack

* Language: Java 24
* Framework: Spring Boot 3+
* Build Tool: Maven
* Database: JPA / Hibernate

---

## 💎 Development Standards

### Dependency Injection

* Always use constructor-based injection
* Avoid field injection with `@Autowired`

### Modern Java

* Prefer `record` for DTOs
* Use `var` where the type is obvious
* Prefer immutable objects when possible
* Leverage Virtual Threads for asynchronous workloads when applicable

### Error Handling

* Centralize exception handling with `@RestControllerAdvice`
* Keep error responses consistent

### Code Style

* Prefer simple and explicit implementations
* Avoid premature abstractions and overengineering
* Keep classes and methods small and focused
* Use descriptive naming

---

## 🚀 Implementation Flow

When implementing a new feature:

1. Entity / Model
2. Repository
3. DTOs
4. Service
5. Controller
6. Validation
7. Exception Handling
8. Tests

---

## 🤖 AI Generation Rules

* Generate code incrementally
* Implement only one endpoint or feature at a time
* Never generate more than 500 lines of code per step
* Keep commits small and reviewable
* Do not modify unrelated files
* Do not implement unrequested features
* Always explain the implementation plan before generating code
* Keep the project compiling after every step

---

## 🧪 Testing Rules

* Do not generate tests together with the initial implementation
* Generate tests only after the feature is working
* Create tests incrementally by layer:

  * Controller
  * Service
  * Repository / Integration
