# Contributing to REST API Extension

Thank you for your interest in contributing to the Krista REST API Extension! We're excited to have you join our community of developers working to make API integrations seamless and powerful.

This guide will help you get started with contributing to the project, whether you're fixing bugs, adding features, improving documentation, or helping with testing.

---

## 📜 Code of Conduct

We are committed to providing a welcoming and inclusive environment for all contributors. By participating in this project, you agree to abide by our community standards:

- **Be respectful**: Treat all community members with respect and kindness
- **Be collaborative**: Work together constructively and help others learn
- **Be inclusive**: Welcome newcomers and diverse perspectives
- **Be professional**: Keep discussions focused and productive

For detailed guidelines, please refer to the [Contributor Covenant Code of Conduct](https://www.contributor-covenant.org/version/2/1/code_of_conduct/).

---

## 🐛 Reporting Bugs

Found a bug? Help us improve by reporting it! Good bug reports are extremely valuable.

### Before Reporting

1. **Search existing issues** to avoid duplicates
2. **Test with the latest version** to ensure the bug still exists
3. **Gather relevant information** about your environment

### Bug Report Template

When creating a bug report, please include:

```markdown
**Extension Version**: (e.g., 2.0.16)
**Java Version**: (e.g., Java 21)
**Krista Platform Version**: (e.g., 1.0.118)
**Operating System**: (e.g., Ubuntu 22.04, macOS 14.0, Windows 11)

**Description**
A clear and concise description of the bug.

**Steps to Reproduce**
1. Configure authentication with...
2. Make a GET request to...
3. Observe the error...

**Expected Behavior**
What you expected to happen.

**Actual Behavior**
What actually happened.

**Error Messages/Logs**
```
Paste any relevant error messages or log output here
```

**Additional Context**
- Authentication method used (Basic, Token, OAuth 2.0)
- Request details (URL, headers, payload)
- Screenshots if applicable
```

### Where to Report

- **GitHub Issues**: Create a new issue in the project repository
- **Security Issues**: For security vulnerabilities, please email security@kristasoft.com instead of creating a public issue

---

## 💡 Suggesting Enhancements

Have an idea to make the REST API Extension better? We'd love to hear it!

### Enhancement Request Template

```markdown
**Feature Title**: Brief, descriptive title

**Problem Statement**
What problem does this enhancement solve? Who would benefit from it?

**Proposed Solution**
Describe your proposed solution in detail.

**Use Case Examples**
Provide 1-3 real-world examples of how this feature would be used.

**Alternative Solutions**
Have you considered any alternative approaches?

**Additional Context**
- Related features or extensions
- Links to similar implementations
- Mockups or diagrams (if applicable)
```

### Enhancement Guidelines

- **Align with project goals**: Ensure the enhancement fits the extension's purpose
- **Consider scope**: Start with focused, well-defined features
- **Think about users**: How will this improve the user experience?
- **Be specific**: Provide concrete examples and use cases

---

## 🚀 Development Setup

Ready to contribute code? Follow these steps to set up your development environment.

### Prerequisites

Before you begin, ensure you have:

- **Java 21** or higher ([Download OpenJDK](https://openjdk.org/projects/jdk/21/))
- **Gradle 8.x** (included via Gradle wrapper)
- **Git** for version control
- **Krista Platform 1.0.118+** for testing (optional but recommended)
- **IDE** with Java support (IntelliJ IDEA, Eclipse, VS Code)

### Clone the Repository

```bash
# Fork the repository on GitHub first, then clone your fork
git clone https://github.com/YOUR-USERNAME/krista-global-catalog.git
cd krista-global-catalog/restapi

# Add upstream remote to sync with the main repository
git remote add upstream https://github.com/krista-ai/krista-global-catalog.git
```

### Build the Project

```bash
# Clean and build the extension
./gradlew clean build

# The built JAR will be in: build/libs/RestAPI-<version>.jar
```

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

### Verify Your Setup

```bash
# Ensure everything compiles and tests pass
./gradlew clean build test

# Expected output: BUILD SUCCESSFUL
```

---

## 📝 Coding Standards

We maintain high code quality standards to ensure the extension is reliable, maintainable, and easy to understand.

### Java Conventions

Follow standard Java coding conventions and best practices:

#### Code Style
- **Java Version**: Target Java 21 features and syntax
- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Braces**: Use K&R style (opening brace on same line)
- **Imports**: Organize imports, remove unused imports
- **Formatting**: Use IDE auto-formatting (IntelliJ IDEA recommended)

#### Naming Conventions
- **Classes**: PascalCase (e.g., `RestApiExtension`, `HTTPRequest`)
- **Methods**: camelCase (e.g., `executeRequest`, `parseResponse`)
- **Variables**: camelCase (e.g., `apiUrl`, `authToken`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `JAXRS_ID`, `DEFAULT_TIMEOUT`)
- **Packages**: lowercase (e.g., `app.krista.extensions.development.api.rest`)

#### Code Organization
- **Package Structure**: Follow the existing three-layer architecture
  - `catalog`: Domain-specific request handling
  - `impl`: Business logic and HTTP operations
  - `connectors`: Client providers and factories
  - `auth`: Authentication and OAuth flows
  - `stores`: Data persistence
  - `util`: Utility classes and constants
- **Class Size**: Keep classes focused and under 500 lines
- **Method Size**: Keep methods focused and under 50 lines

### Documentation Standards

#### JavaDoc Comments
All public classes, methods, and fields must have JavaDoc comments:

```java
/**
 * Executes an HTTP GET request to retrieve data from the specified URL.
 * <p>
 * This method handles authentication, custom headers, and query parameters.
 * It supports pagination and automatic retry with exponential backoff.
 * </p>
 *
 * @param url the target API endpoint URL
 * @param headers optional HTTP headers to include in the request
 * @param queryParams optional query parameters for filtering
 * @return the parsed response data as a list of maps
 * @throws IllegalArgumentException if url is null or empty
 * @throws IOException if the request fails after all retries
 */
public List<Map<String, Object>> executeGetRequest(
    String url,
    Map<String, String> headers,
    Map<String, String> queryParams
) throws IOException {
    // Implementation
}
```

#### Inline Comments
- Use inline comments to explain **why**, not **what**
- Comment complex algorithms or business logic
- Avoid obvious comments that restate the code

#### Code Examples
```java
// Good: Explains the reasoning
// Use exponential backoff to avoid overwhelming the API during rate limiting
int retryDelay = (int) Math.pow(2, attemptNumber) * 1000;

// Bad: States the obvious
// Set retry delay to 2 raised to attempt number times 1000
int retryDelay = (int) Math.pow(2, attemptNumber) * 1000;
```

### Validation and Error Handling

#### Input Validation
Always validate inputs at the beginning of methods:

```java
public void processRequest(String url, String payload) {
    // Validate required parameters
    if (url == null || url.trim().isEmpty()) {
        throw new IllegalArgumentException("URL cannot be null or empty");
    }

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        throw new IllegalArgumentException("URL must start with http:// or https://");
    }

    // Continue with processing
}
```

#### Error Messages
Provide clear, actionable error messages:

```java
// Good: Specific and actionable
throw new IllegalArgumentException(
    "Invalid page size: " + pageSize + ". Must be between 1 and 1000"
);

// Bad: Vague and unhelpful
throw new IllegalArgumentException("Invalid input");
```

#### Logging
Use appropriate log levels:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class);

// ERROR: Actual failures that prevent operation
LOGGER.error("Failed to authenticate with API: {}", errorMessage, exception);

// WARN: Recoverable issues or deprecated usage
LOGGER.warn("Retry attempt {} failed, retrying in {}ms", attempt, delay);

// INFO: Successful operations and important state changes
LOGGER.info("Successfully authenticated with OAuth 2.0");

// DEBUG: Detailed information for troubleshooting
LOGGER.debug("Request headers: {}", headers);

// TRACE: Very detailed execution flow
LOGGER.trace("Entering method executeRequest with url: {}", url);
```

---

## 🧪 Testing Guidelines

Testing is critical to maintaining code quality. We follow strict testing standards.

### No Mocks Policy

**We do not use mocks in our tests.** This is a core principle of our testing strategy.

#### Why No Mocks?
- **Real behavior**: Tests verify actual implementation behavior
- **Integration confidence**: Tests catch real integration issues
- **Refactoring safety**: Tests don't break when internal implementation changes
- **Simplicity**: No complex mock setup and verification

#### What to Do Instead
```java
// ✅ GOOD: Use real implementations
@Test
void testCreate_WithBasicAuthPayload_ShouldCreateRestApiAttributes() {
    // Arrange - Create real objects
    AuthPayload payload = new AuthPayload();
    payload.setAuthType(AuthPayload.AuthType.Basic);
    payload.setUserName("api_user");
    payload.setPassword("api_password");

    // Act - Call real method
    RestApiAttributes attributes = RestApiAttributes.create(payload);

    // Assert - Verify real behavior
    assertNotNull(attributes);
    assertEquals(AuthPayload.AuthType.Basic, attributes.getAuthType());
}

// ❌ BAD: Using mocks
@Test
void testWithMocks() {
    AuthPayload mockPayload = mock(AuthPayload.class);
    when(mockPayload.getAuthType()).thenReturn(AuthPayload.AuthType.Basic);
    // Don't do this!
}
```

#### When Tests Require External Dependencies
If a test requires external services (APIs, databases), mark it as disabled:

```java
@Test
@Disabled("Requires live API connection - run manually for integration testing")
void testRealApiCall() {
    // Integration test that calls real external API
}
```

### Test Coverage Requirements

We maintain **95%+ code coverage** across the project:

- **Service Layer**: 95%+ coverage
- **Catalog Layer**: 100% coverage
- **Connector Layer**: 100% coverage
- **Overall Project**: 95%+ coverage

### Test Structure

Follow the Arrange-Act-Assert (AAA) pattern:

```java
@Test
void testMethodName_Scenario_ExpectedBehavior() {
    // Arrange - Set up test data and preconditions
    String url = "https://api.example.com/users";
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer token");

    // Act - Execute the method being tested
    Response response = httpClient.get(url, headers);

    // Assert - Verify the results
    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertTrue(response.hasData());
}
```

### Test Naming Convention

Use descriptive test names that clearly indicate what is being tested:

```java
// Pattern: test[MethodName]_[Scenario]_[ExpectedBehavior]

@Test
void testExecuteGetRequest_WithValidUrl_ShouldReturnData() { }

@Test
void testExecuteGetRequest_WithNullUrl_ShouldThrowException() { }

@Test
void testExecuteGetRequest_WithInvalidUrl_ShouldThrowException() { }

@Test
void testExecuteGetRequest_WithPagination_ShouldReturnPagedResults() { }
```

### Test Categories

Organize tests into clear categories:

```java
// ========== Constructor Tests ==========
@Test
void testConstructor_WithAllParameters_ShouldCreateInstance() { }

// ========== Validation Tests ==========
@Test
void testValidateUrl_WithNullUrl_ShouldThrowException() { }

// ========== Business Logic Tests ==========
@Test
void testExtractPaginatedResults_WithValidParameters_ShouldReturnCorrectSubset() { }

// ========== Real-World Scenario Tests ==========
@Test
void testCreate_WithBasicAuthPayload_ShouldCreateRestApiAttributes() { }
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "RestApiAttributesTest"

# Run specific test method
./gradlew test --tests "RestApiAttributesTest.testCreate_WithBasicAuthPayload_ShouldCreateRestApiAttributes"

# Run tests with coverage
./gradlew test jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

---

## 🔄 Pull Request Process

Follow these steps to submit your contribution for review.

### Branch Naming

Use descriptive branch names that indicate the type and purpose of your changes:

```bash
# Feature branches
git checkout -b feature/add-retry-configuration
git checkout -b feature/support-graphql-apis

# Bug fix branches
git checkout -b fix/oauth-token-refresh-error
git checkout -b fix/pagination-boundary-issue

# Documentation branches
git checkout -b docs/update-authentication-guide
git checkout -b docs/add-troubleshooting-examples

# Refactoring branches
git checkout -b refactor/simplify-http-client
git checkout -b refactor/extract-validation-logic
```

### Commit Messages

Write clear, descriptive commit messages following the conventional commits format:

```bash
# Format: <type>(<scope>): <subject>

# Examples:
git commit -m "feat(auth): add support for API key authentication"
git commit -m "fix(pagination): correct off-by-one error in page calculation"
git commit -m "docs(readme): update installation instructions for Java 21"
git commit -m "test(http): add tests for retry logic with exponential backoff"
git commit -m "refactor(catalog): extract common validation logic"
git commit -m "chore(deps): update OkHttp to version 4.12.0"
```

#### Commit Types
- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **test**: Adding or updating tests
- **refactor**: Code refactoring without changing behavior
- **perf**: Performance improvements
- **chore**: Maintenance tasks, dependency updates
- **style**: Code style changes (formatting, whitespace)

#### Commit Message Guidelines
- Use present tense ("add feature" not "added feature")
- Use imperative mood ("move cursor to..." not "moves cursor to...")
- Keep subject line under 72 characters
- Add detailed description in commit body if needed
- Reference issue numbers when applicable

### Before Submitting

Complete this checklist before creating a pull request:

- [ ] **Code compiles**: `./gradlew clean build` succeeds
- [ ] **All tests pass**: `./gradlew test` succeeds
- [ ] **Coverage maintained**: Coverage remains at 95%+ (check with `./gradlew jacocoTestReport`)
- [ ] **No mocks used**: Tests follow the no-mocks policy
- [ ] **Code formatted**: Code follows Java conventions
- [ ] **JavaDoc added**: Public APIs have complete JavaDoc comments
- [ ] **License headers**: All new files include GPLv3 license headers
- [ ] **Documentation updated**: README.md and relevant docs updated
- [ ] **Changelog updated**: Add entry to release notes if applicable
- [ ] **No warnings**: Build completes without warnings
- [ ] **Branch updated**: Synced with latest upstream changes

### Creating the Pull Request

1. **Push your branch** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create Pull Request** on GitHub:
   - Navigate to the main repository
   - Click "New Pull Request"
   - Select your fork and branch
   - Fill out the PR template

3. **PR Title**: Use the same format as commit messages:
   ```
   feat(auth): add support for API key authentication
   ```

4. **PR Description Template**:
   ```markdown
   ## Description
   Brief description of what this PR does and why.

   ## Related Issue
   Fixes #123 (if applicable)

   ## Changes Made
   - Added API key authentication support
   - Updated authentication documentation
   - Added tests for API key validation

   ## Testing
   - [ ] Unit tests added/updated
   - [ ] Integration tests added/updated (if applicable)
   - [ ] Manual testing completed
   - [ ] Coverage maintained at 95%+

   ## Documentation
   - [ ] JavaDoc comments added
   - [ ] README.md updated
   - [ ] User documentation updated
   - [ ] Architecture docs updated (if applicable)

   ## Checklist
   - [ ] Code follows project coding standards
   - [ ] All tests pass
   - [ ] No mocks used in tests
   - [ ] License headers added to new files
   - [ ] Branch is up to date with main
   ```

### Review Process

1. **Automated Checks**: CI/CD pipeline runs tests and coverage checks
2. **Code Review**: Maintainers review your code for:
   - Code quality and style
   - Test coverage and quality
   - Documentation completeness
   - Adherence to project standards
3. **Feedback**: Address review comments and push updates
4. **Approval**: Once approved, maintainers will merge your PR
5. **Cleanup**: Delete your feature branch after merge

### Review Timeline

- **Initial Review**: Within 3-5 business days
- **Follow-up Reviews**: Within 2 business days
- **Merge**: After approval and passing all checks

### Tips for Faster Review

- Keep PRs focused and reasonably sized (< 500 lines preferred)
- Write clear descriptions and commit messages
- Respond promptly to review feedback
- Ensure all automated checks pass
- Add screenshots or examples for UI/UX changes

---

## 📄 License Requirements

This project is licensed under the **GNU General Public License v3.0 (GPLv3)**.

### License Headers

**All source files must include the GPLv3 license header.** Add this header to the top of every new Java file:

```java
/*
 * REST API Extension for Krista
 * Copyright (C) 2026 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.development.api.rest;

// Your code here
```

### Adding License Headers Automatically

Use this script to add license headers to all Java files:

```bash
#!/bin/bash
# add-license-headers.sh

LICENSE_HEADER="/*
 * REST API Extension for Krista
 * Copyright (C) $(date +%Y) Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */"

# Find all Java files and add header if not present
find src -name "*.java" -type f | while read file; do
    if ! grep -q "GNU General Public License" "$file"; then
        echo "Adding license header to: $file"
        echo "$LICENSE_HEADER" | cat - "$file" > temp && mv temp "$file"
    fi
done

echo "License headers added successfully!"
```

Run the script:
```bash
chmod +x add-license-headers.sh
./add-license-headers.sh
```

### License Compliance

By contributing to this project, you agree that:

- Your contributions will be licensed under GPLv3
- You have the right to submit the code under this license
- You understand that derivative works must also be GPLv3
- You will not include code with incompatible licenses

### Third-Party Dependencies

When adding dependencies:

- Ensure the dependency license is compatible with GPLv3
- Document the dependency and its license in the project
- Avoid proprietary or restrictive licenses

**Compatible Licenses**: Apache 2.0, MIT, BSD, LGPL, GPLv2+, GPLv3+
**Incompatible Licenses**: Proprietary, some Creative Commons licenses

---

## 🤝 Community Guidelines

We're building a welcoming, inclusive community. Here's how you can be a great community member.

### Communication Channels

- **GitHub Issues**: Bug reports, feature requests, technical discussions
- **Pull Requests**: Code reviews and implementation discussions
- **Email**: security@kristasoft.com (for security issues only)

### Best Practices

#### Be Respectful
- Treat everyone with respect and professionalism
- Welcome newcomers and help them get started
- Assume good intentions
- Disagree constructively

#### Be Collaborative
- Share knowledge and help others learn
- Provide constructive feedback in code reviews
- Acknowledge contributions from others
- Work together to find the best solutions

#### Be Clear
- Write clear, descriptive issues and PRs
- Provide context and examples
- Ask questions when something is unclear
- Document your decisions and reasoning

#### Be Patient
- Remember that maintainers are often volunteers
- Allow time for reviews and responses
- Be understanding of different time zones and schedules

### Getting Help

If you need help:

1. **Check the documentation**: Review README.md, QUICKSTART.md, and docs/
2. **Search existing issues**: Someone may have had the same question
3. **Ask in GitHub Issues**: Create a new issue with the "question" label
4. **Be specific**: Provide context, code examples, and error messages

### Helping Others

You can contribute by:

- Answering questions in GitHub Issues
- Reviewing pull requests
- Improving documentation
- Sharing your use cases and examples
- Reporting bugs you encounter
- Testing new features and providing feedback

### Recognition

We value all contributions! Contributors will be:

- Listed in the project's contributors list
- Acknowledged in release notes for significant contributions
- Invited to join the maintainers team for sustained contributions

---

## 🎯 What to Contribute

Not sure where to start? Here are some ideas:

### Good First Issues
- Fix typos in documentation
- Add missing JavaDoc comments
- Improve error messages
- Add test cases for edge cases
- Update examples in documentation

### Feature Ideas
- Support for additional authentication methods
- Enhanced error handling and retry strategies
- Performance optimizations
- New request types or operations
- Integration with popular APIs

### Documentation Improvements
- Add more usage examples
- Create video tutorials
- Improve troubleshooting guides
- Translate documentation
- Add architecture diagrams

### Testing Improvements
- Increase test coverage
- Add integration tests
- Add performance tests
- Improve test documentation

---

## 📚 Additional Resources

### Project Documentation
- [README.md](README.md) - Project overview and quick start
- [QUICKSTART.md](QUICKSTART.md) - 10-minute setup guide
- [ARCHITECTURE.md](ARCHITECTURE.md) - Technical architecture details
- [docs/](docs/) - Complete user documentation

### External Resources
- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [GPLv3 License](https://www.gnu.org/licenses/gpl-3.0.html)

---

## 🙏 Thank You!

Thank you for contributing to the REST API Extension! Your contributions help make API integrations easier and more powerful for everyone.

We appreciate your time, effort, and expertise. Together, we're building something great! 🚀

---

## 📞 Contact

- **Issues & Questions**: [GitHub Issues](https://github.com/krista-ai/krista-global-catalog/issues)
- **Security Issues**: security@kristasoft.com
- **General Support**: support@kristasoft.com

---

**Happy Contributing!** 🎉


