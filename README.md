# Reflections

## Reflection 1: Clean Code Principles & Improvements

### Implemented Clean Code Principles
During this module, I focused on making the codebase more maintainable and readable by applying the following:

- **Single Responsibility Principle (SRP)**: Refactored classes to ensure each serves a single purpose.
- **Interface Segregation**: Kept interfaces lean and specific to the needs of the implementers.
- **Meaningful Naming**: Used descriptive naming conventions for variables and methods to improve self-documentation.
- **Lombok Usage**: Integrated Lombok to eliminate boilerplate code like getters, setters, and constructors.
- **Spring Annotations**: Properly utilized `@Controller`, `@Service`, and `@Repository` for better dependency management.

### Technical Debt & Future Improvements
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

---

## Module 2: CI/CD Pipeline & Code Quality

### 1. Code Quality Issues Fixed

The following issues were identified through static analysis (SonarCloud) during the CI pipeline and subsequently resolved:

#### Redundant `public` Modifiers on Interface Methods
**File:** `ProductService.java`

Interface methods are implicitly `public` in Java. Declaring them explicitly with the `public` keyword is redundant and adds visual noise that can mislead readers into thinking visibility is non-default.

**Strategy:** Removed the explicit `public` modifier from all method declarations in the `ProductService` interface, relying on Java's implicit interface method visibility.

---

#### Field Injection via `@Autowired`
**Files:** `ProductController.java`, `ProductServiceImpl.java`

Using `@Autowired` directly on private fields makes the classes harder to unit test (dependencies cannot be injected without a Spring context), hides mandatory dependencies, and prevents the fields from being declared `final`.

**Strategy:** Replaced field injection with **constructor injection**. Dependencies are declared as `final` fields and injected through a single constructor, which Spring auto-wires. This makes dependencies explicit, enables immutability, and allows plain-Java instantiation in tests without a Spring context.

---

#### `findById` Returning `null`
**Files:** `ProductRepository.java`, `ProductServiceImpl.java`, `ProductController.java`

`findById` returns `null` when no product matches the given ID. Any caller that does not explicitly check for `null` risks a `NullPointerException` at runtime, which is a silent contract violation.

**Strategy:** Changed the return type of `findById` to `Optional<Product>` throughout the repository, service interface, and service implementation. The controller now handles the empty case explicitly with a redirect, eliminating the implicit `null` contract.

---

#### Inconsistent View Name Casing
**File:** `ProductController.java`

The controller returned `"createProduct"` and `"productList"` in camelCase, but `"EditProduct"` in PascalCase. Inconsistent naming makes it harder to trace which template a controller method maps to.

**Strategy:** Standardised all view name return values to match the exact filename casing of their corresponding Thymeleaf templates, ensuring a one-to-one, predictable mapping between controller return values and template files.

---

### 2. CI/CD Pipeline Implementation

Two automated pipelines are configured via GitHub Actions:

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `ci.yml` | Every push and pull request | Compiles the project and runs all unit tests using `./gradlew test` on Java 21 (Temurin) |
| `sonarcloud.yml` | Push/PR to `master`, manual dispatch | Runs SonarCloud static analysis and surfaces issues as PR decorations and dashboard alerts |

#### Strategy
The CI workflow acts as a **quality gate**: no code reaches `master` without passing the full unit test suite. SonarCloud then provides a second, deeper layer of analysis covering code smells, duplication, coverage, and security hotspots. By separating the two concerns into distinct workflows, a failing build (compilation or test error) is immediately distinguishable from a quality warning, keeping feedback fast and actionable.

---

### 3. Reflection on CI/CD

**Does CI/CD make you feel more confident when pushing code?**

Yes. The pipeline acts as an automated safety net that catches regressions before they reach the main branch. Knowing that every push triggers compilation checks and the full test suite means a green status on `master` carries a meaningful signal, not just the developer's word. The addition of SonarCloud further raises the bar by catching issues that tests alone would not surface, such as dead code, overly complex methods, or insecure patterns. Together, they shift quality assurance left in the development cycle, making each merge more deliberate and trustworthy.

---

### 4. Have I Implemented CI/CD?

**Yes.** The implementation satisfies both halves of the CI/CD definition:

**Continuous Integration (CI)** is confirmed by `ci.yml`:
- Triggers automatically on every `push` and `pull_request` — no manual intervention required.
- Checks out the code, sets up Java 21 (Temurin), and runs the full unit test suite via `./gradlew test` in a clean, isolated environment.
- A failing test causes the workflow to fail, acting as a gate that prevents broken code from being merged.

**Continuous Deployment (CD)** is confirmed by the Koyeb integration:
- The Koyeb service is connected directly to the `master` branch of the GitHub repository.
- Every push to `master` that passes CI automatically triggers a new deployment to the live environment.
- No manual deploy step is required — a successful merge is sufficient to ship the change.

The combination means the full path from a code change to a running deployment is automated and gated by tests.
