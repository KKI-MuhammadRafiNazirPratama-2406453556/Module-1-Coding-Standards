# Reflections

## Reflection 1: Clean Code Principles & Improvements

### ✅ Implemented Clean Code Principles
During this module, I focused on making the codebase more maintainable and readable by applying the following:

- **Single Responsibility Principle (SRP)**: Refactored classes to ensure each serves a single purpose.
- **Interface Segregation**: Kept interfaces lean and specific to the needs of the implementers.
- **Meaningful Naming**: Used descriptive naming conventions for variables and methods to improve self-documentation.
- **Lombok Usage**: Integrated Lombok to eliminate boilerplate code like getters, setters, and constructors.
- **Spring Annotations**: Properly utilized `@Controller`, `@Service`, and `@Repository` for better dependency management.

### 🛠️ Technical Debt & Future Improvements
I have identified several areas where the code quality can be further improved:

| Issue | Description | Proposed Solution |
|-------|-------------|-------------------|
| **Inconsistent Casing** | Controller returns "createProduct" while the file is `CreateProduct.html`. | Standardize all view names to use `kebab-case` or `camelCase` consistently. |
| **Missing Null Checks** | `editProductPage` calls `service.findById(id)` without validating existence. | Add null-safety checks and appropriate error handling or redirects. |
| **NPE Risk** | Potential for `NullPointerException` in `findById` operations. | Use `Optional<T>` to handle missing entities gracefully. |
| **Field Injection** | Using `@Autowired` on fields makes testing more difficult. | Switch to **Constructor Injection** to ensure immutability and easier unit testing. |
| **Redundant Modifiers** | Interfaces explicitly using `public` for methods. | Remove redundant `public` modifiers as interface methods are public by default. |

---

## Reflection 2: Testing & Quality Standards

### 1. Code Coverage vs. Software Quality

> *"Does 100% code coverage mean no bugs?"*

Reaching a state where code runs without error is a milestone, but not the finish line. In my view:

- **No "Magic Number"**: The ideal number of unit tests is determined by the complexity of logic paths, not a fixed count.
- **Coverage Limits**: 100% coverage means every line was executed, but it doesn't guarantee logic correctness across all edge cases.
- **Integration Gaps**: Unit tests focus on isolated units. Even with high coverage, components might fail when combined due to integration issues.

### 2. Enhancing Functional Test Quality

The current implementation of functional tests contains architectural "smells" that reduce overall code quality:

- **DRY (Don't Repeat Yourself) Violation**: Shared setup logic like `serverPort`, `testBaseUrl`, and `setupTest()` is duplicated across every test class.
- **Shotgun Surgery**: Because of the duplication, a single change (e.g., changing the base URL format) requires manual edits across multiple files.
- **Violation of Single Source of Truth**: There is no centralized place defining how a functional test should be configured.

#### Proposed Solution
Pull all shared setup and configuration into an **Abstract Base Class**. Every functional test class should extend this base class to inherit common configurations, ensuring a single point of maintenance.
