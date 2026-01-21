# Open Source Conversion Guide for Krista Extensions

**A comprehensive guide to convert existing Krista extensions to world-class open source projects**

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Step-by-Step Conversion Process](#step-by-step-conversion-process)
4. [Documentation Requirements](#documentation-requirements)
5. [Code Quality Standards](#code-quality-standards)
6. [Testing Requirements](#testing-requirements)
7. [Validation and Error Handling](#validation-and-error-handling)
8. [License Requirements](#license-requirements)
9. [Final Checklist](#final-checklist)
10. [Example Prompt for AI Assistants](#example-prompt-for-ai-assistants)

---

## Overview

This guide provides a standardized approach to converting Krista extensions into production-ready, open source projects with:

- ✅ Comprehensive documentation (12+ files)
- ✅ GPLv3 license headers on all source files
- ✅ Robust validation and error handling
- ✅ High test coverage (95%+)
- ✅ Clean architecture following best practices
- ✅ No mocks policy for testing

**Reference Implementation:** AssemblyAI Extension for Krista

---

## Prerequisites

Before starting the conversion, ensure you have:

1. ✅ Existing Krista extension with working functionality
2. ✅ Access to the codebase repository
3. ✅ Understanding of the extension's features and architecture
4. ✅ API documentation for the external service (if applicable)
5. ✅ Test environment for validation

---

## Step-by-Step Conversion Process

### Phase 1: Documentation Creation (Priority: HIGH)

#### 1.1 Core Documentation Files

Create the following files in the root directory:

**README.md** (Main landing page)
- Project overview and description
- Key features (bullet list)
- Badges (license, Java version, build status, coverage)
- Quick start guide
- Installation instructions
- Configuration guide
- Usage examples
- Architecture overview
- API reference links
- Testing information
- Contributing guidelines
- License information
- Support resources

**QUICKSTART.md** (Getting started in <10 minutes)
- Prerequisites
- Clone and build instructions
- Configuration steps
- First usage example
- Supported formats/features
- Response structures
- Troubleshooting basics
- Next steps

**ARCHITECTURE.md** (Technical architecture)
- System overview
- Layer architecture (Catalog, Service, Connector)
- Design patterns used (CQRS, Template Method, etc.)
- Component interactions
- Data flow diagrams
- Technology stack
- Dependencies
- Extension points

**CONTRIBUTING.md** (Contribution guidelines)
- Code of conduct reference
- How to report bugs
- How to suggest enhancements
- Development setup
- Coding standards
- Testing guidelines
- Pull request process
- License requirements

**CODE_OF_CONDUCT.md** (Community standards)
- Use Contributor Covenant Code of Conduct v2.1
- Standards for behavior
- Enforcement responsibilities
- Scope
- Enforcement guidelines

**CHANGELOG.md** (Version history)
- Follow "Keep a Changelog" format
- Document all versions
- Categories: Added, Changed, Deprecated, Removed, Fixed, Security
- Include dates and version numbers

**SECURITY.md** (Security policy)
- Supported versions
- Vulnerability reporting process
- Security update process
- Best practices for users
- Best practices for developers
- Known security considerations
- Security features
- Compliance information

**CONTRIBUTORS.md** (Recognition)
- Core team members
- Code contributors
- Documentation contributors
- Bug reporters
- Feature requesters
- How to become a contributor

#### 1.2 Advanced Documentation (docs/ folder)

**docs/API_REFERENCE.md** (Complete API documentation)
- Catalog layer methods
- Service layer methods
- Model classes
- Error codes
- Constants
- Usage patterns with code examples

**docs/EXAMPLES.md** (Comprehensive usage examples)
- Basic examples
- Advanced features
- Error handling patterns
- Integration patterns
- Real-world scenarios
- Complete working code

**docs/TROUBLESHOOTING.md** (Common issues and solutions)
- Authentication issues
- Connection issues
- Configuration issues
- Performance issues
- Integration issues
- Testing issues
- Debugging tips
- Common error messages table

#### 1.3 Extension-Specific Documentation (src/main/resources/docs/)

**Keep existing Krista extension documentation:**
- README.md (extension overview)
- _sidebar.md (navigation)
- pages/*.md (catalog request documentation)
- QUALITY_CHECKLIST.md
- Any other extension-specific docs

**DO NOT DELETE** these files - they are required for Krista platform help system.

---

### Phase 2: License Implementation (Priority: HIGH)

#### 2.1 Add GPLv3 License

**Create LICENSE file:**
```bash
curl -s https://www.gnu.org/licenses/gpl-3.0.txt > LICENSE
```

#### 2.2 Add License Headers to All Source Files

**Create script: add-license-headers.sh**
```bash
#!/bin/bash

LICENSE_HEADER="/*
 * [Extension Name] for Krista
 * Copyright (C) $(date +%Y) [Your Organization]
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

# Find all Java files and add header
find src -name "*.java" -type f | while read file; do
    if ! grep -q "GNU General Public License" "$file"; then
        echo "Adding license header to: $file"
        echo "$LICENSE_HEADER" | cat - "$file" > temp && mv temp "$file"
    fi
done
```

**Run the script:**
```bash
chmod +x add-license-headers.sh
./add-license-headers.sh
```

---

### Phase 3: Code Quality Improvements (Priority: HIGH)

#### 3.1 Add Comprehensive Validation

**For every public method, add validation:**

```java
public ReturnType methodName(Parameter param) {
    // 1. Null checks
    if (param == null) {
        throw new IllegalArgumentException("Parameter cannot be null");
    }
    
    // 2. Empty string checks
    if (stringParam == null || stringParam.trim().isEmpty()) {
        throw new IllegalArgumentException("Parameter cannot be null or empty");
    }
    
    // 3. Numeric range checks
    if (numericParam <= 0) {
        throw new IllegalArgumentException("Parameter must be positive");
    }
    
    // 4. File validation
    if (file == null || !file.exists() || !file.isFile()) {
        throw new IllegalArgumentException("Invalid file");
    }
    
    // ... method logic
}
```

**Validation Checklist:**
- ✅ Constructor parameters
- ✅ All public method parameters
- ✅ Critical private method parameters
- ✅ File operations (null, exists, isFile, size, format)
- ✅ String parameters (null, empty, whitespace)
- ✅ Numeric parameters (range, positive, non-zero)
- ✅ Collection parameters (null, empty)

#### 3.2 Improve Error Handling

**Follow Fail-Fast Principle:**
```java
// GOOD - Fail fast
if (!operation()) {
    LOG.error("Operation failed");
    return false; // Stop immediately
}

// BAD - Continue on failure
boolean success = true;
if (!operation()) {
    success = false; // Continue anyway - masks problems
}
```

**Provide Detailed Error Messages:**
```java
// GOOD - Actionable error message
throw new IllegalArgumentException(
    "Audio file exceeds maximum size of 500 MB. " +
    "Current size: " + fileSizeMB + " MB"
);

// BAD - Vague error message
throw new IllegalArgumentException("Invalid file");
```

#### 3.3 Add Comprehensive Logging

**Logging Levels:**
- **ERROR**: Actual failures that stop operations
- **WARN**: Recoverable issues or unexpected conditions
- **INFO**: Successful operations and major workflow steps
- **DEBUG**: Detailed tracing for troubleshooting
- **TRACE**: Very detailed execution flow

**Example:**
```java
LOG.info("Starting transcription for file: {}", fileName);
LOG.debug("File size: {} MB, Format: {}", sizeMB, format);
LOG.trace("Validation passed: {}", validationDetails);
```

---

### Phase 4: Testing Requirements (Priority: HIGH)

#### 4.1 Follow "No Mocks Policy"

**NEVER use mocks** - they hide real issues:

```java
// WRONG - Using mocks
@Test
public void testMethod() {
    Service mockService = mock(Service.class);
    when(mockService.call()).thenReturn(result);
    // This doesn't test real behavior!
}

// CORRECT - Real implementation or skip test
@Test
@Disabled("Requires Krista platform runtime")
public void testMethod() {
    // Test with real service or disable if not possible
}
```

#### 4.2 Test Coverage Goals

**Target Coverage:**
- Service Layer: 95%+
- Bridge Layer: 100%
- Catalog Layer: 100%
- Overall: 95%+

**Test Categories:**
1. **Unit Tests**: Business logic and validation
2. **Integration Tests**: Real API calls (mark with @Disabled if requires platform)
3. **Validation Tests**: Input validation and error handling
4. **Edge Case Tests**: Boundary conditions

#### 4.3 Test Naming Convention

```java
@Test
void test[MethodName]_[Scenario]() {
    // Example: testUploadFile_NullFile()
    // Example: testCreateTranscript_EmptyUrl()
    // Example: testPollForCompletion_Timeout()
}
```

---

### Phase 5: Documentation Cleanup (Priority: MEDIUM)

#### 5.1 Remove Redundant Files

**Files to Remove:**
- Old implementation notes (IMPLEMENTATION_COMPLETE.md, etc.)
- Redundant summaries (PROJECT_SUMMARY.md, etc.)
- Old refactoring notes (REFACTORING_SUMMARY.md, etc.)
- Duplicate build guides (BUILD_AND_DEPLOY.md - merge into CONTRIBUTING.md)

**Files to Keep:**
- All core documentation (README, QUICKSTART, etc.)
- All docs/ folder files
- **All src/main/resources/docs/ files** (required for Krista)

#### 5.2 Verify Documentation Accuracy

- ✅ All code examples work
- ✅ All links are valid
- ✅ Version numbers are correct
- ✅ API documentation matches code
- ✅ Error messages match actual code
- ✅ Screenshots are up-to-date

---

## Documentation Requirements

### Minimum Required Files (12)

1. ✅ README.md
2. ✅ QUICKSTART.md
3. ✅ ARCHITECTURE.md
4. ✅ CONTRIBUTING.md
5. ✅ CODE_OF_CONDUCT.md
6. ✅ CHANGELOG.md
7. ✅ SECURITY.md
8. ✅ CONTRIBUTORS.md
9. ✅ LICENSE
10. ✅ docs/API_REFERENCE.md
11. ✅ docs/EXAMPLES.md
12. ✅ docs/TROUBLESHOOTING.md

### Documentation Quality Standards

- **Clear and Concise**: No jargon unless necessary
- **Actionable**: Provide specific steps and examples
- **Complete**: Cover all features and use cases
- **Accurate**: Match actual code behavior
- **Professional**: Well-formatted and error-free
- **User-Focused**: Written for developers using the extension

---

## Code Quality Standards

### Architecture Patterns

Follow established Krista extension patterns:

1. **Three-Layer Architecture**
   - Catalog Layer: Krista orchestration
   - Service Layer: Business logic
   - Connector Layer: External API communication

2. **CQRS Pattern**
   - Separate Command and Query operations
   - Clear separation of concerns

3. **Template Method Pattern**
   - Reusable connection management
   - Eliminate boilerplate code

4. **Fail-Fast Principle**
   - Stop on first error
   - Don't mask failures

### Package Structure

```
app.krista.extensions.{ecosystem}.{domain}.{extension}.{subpackage}

Example:
app.krista.extensions.ai.nlp.assemblyai.catalog
app.krista.extensions.ai.nlp.assemblyai.service
app.krista.extensions.ai.nlp.assemblyai.bridge
```

### Common Subpackages

- `catalog`: Main catalog request classes
- `catalog.area`: Area classes
- `catalog.query`: Query classes
- `catalog.command`: Command classes (if applicable)
- `service`: Service implementations
- `service.model`: Model classes
- `bridge`: Response mappers
- `util`: Utility classes and constants

---

## Testing Requirements

### Test Structure

```
src/test/java/
├── integration/          # Integration tests
│   ├── IntegrationTestBase.java
│   └── *IntegrationTest.java
└── service/             # Unit tests
    └── *Test.java
```

### Test Configuration

**src/test/resources/config.properties:**
```properties
# API Configuration
service.api.key=${SERVICE_API_KEY}
service.base.url=https://api.service.com/v1

# Test Configuration
test.enable.real.api.calls=false
test.audio.file.path=src/test/resources/test-file.ext
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "ServiceTest"

# Run with coverage
./gradlew test jacocoTestReport
```

---

## Validation and Error Handling

### Validation Patterns

**Pattern 1: Null Check**
```java
if (param == null) {
    throw new IllegalArgumentException("Parameter cannot be null");
}
```

**Pattern 2: Empty String Check**
```java
if (param == null || param.trim().isEmpty()) {
    throw new IllegalArgumentException("Parameter cannot be null or empty");
}
```

**Pattern 3: Positive Number Check**
```java
if (value <= 0) {
    throw new IllegalArgumentException("Value must be positive");
}
```

**Pattern 4: Null with Default**
```java
if (param == null) {
    param = defaultValue;
}
```

**Pattern 5: Ternary with Default**
```java
result = param != null ? param : defaultValue;
```

### Error Response Standards

```java
public static Map<String, Object> createErrorResponse(
        String operation, String message, String code) {
    Map<String, Object> result = new HashMap<>();
    result.put("success", false);
    result.put("operation", operation);
    result.put("message", message);
    result.put("errorCode", code);
    result.put("timestamp", System.currentTimeMillis());
    return result;
}
```

---

## License Requirements

### GPLv3 License Header Template

```java
/*
 * [Extension Name] for Krista
 * Copyright (C) [Year] [Organization Name]
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
```

### Files Requiring License Headers

- ✅ All .java files in src/main
- ✅ All .java files in src/test
- ✅ Build scripts (if applicable)
- ✅ Configuration files (if applicable)

---

## Final Checklist

### Documentation ✅
- [ ] README.md created with all sections
- [ ] QUICKSTART.md created
- [ ] ARCHITECTURE.md created
- [ ] CONTRIBUTING.md created
- [ ] CODE_OF_CONDUCT.md created
- [ ] CHANGELOG.md created
- [ ] SECURITY.md created
- [ ] CONTRIBUTORS.md created
- [ ] LICENSE file added (GPLv3)
- [ ] docs/API_REFERENCE.md created
- [ ] docs/EXAMPLES.md created
- [ ] docs/TROUBLESHOOTING.md created
- [ ] src/main/resources/docs/ preserved

### Code Quality ✅
- [ ] GPLv3 headers on all Java files
- [ ] Comprehensive validation in all public methods
- [ ] Fail-fast error handling implemented
- [ ] Detailed error messages provided
- [ ] Comprehensive logging added
- [ ] No mocks in tests
- [ ] Test coverage >95%
- [ ] All tests passing

### Build and Deploy ✅
- [ ] Build successful: `./gradlew clean build`
- [ ] Tests passing: `./gradlew test`
- [ ] No compilation errors
- [ ] No test failures
- [ ] Documentation accurate

### Final Review ✅
- [ ] All links in documentation work
- [ ] All code examples tested
- [ ] Version numbers consistent
- [ ] No sensitive information in code
- [ ] No hardcoded credentials
- [ ] Ready for public release

---

## Example Prompt for AI Assistants

Use this prompt when working with AI assistants (like Claude, ChatGPT) to convert your Krista extension:

```
I want to convert my existing Krista extension to a world-class open source project. 
Please follow these requirements:

1. **Documentation (Priority: HIGH)**
   - Create comprehensive README.md with overview, features, quick start, installation, 
     configuration, usage examples, architecture, API reference, testing, contributing, 
     license, and support
   - Create QUICKSTART.md with step-by-step getting started guide (<10 minutes)
   - Create ARCHITECTURE.md with technical architecture details
   - Create CONTRIBUTING.md with contribution guidelines
   - Create CODE_OF_CONDUCT.md (use Contributor Covenant v2.1)
   - Create CHANGELOG.md (follow Keep a Changelog format)
   - Create SECURITY.md with security policy
   - Create CONTRIBUTORS.md for recognition
   - Create docs/API_REFERENCE.md with complete API documentation
   - Create docs/EXAMPLES.md with comprehensive usage examples
   - Create docs/TROUBLESHOOTING.md with common issues and solutions
   - **IMPORTANT**: Keep all existing src/main/resources/docs/ files 
     (required for Krista extension help)

2. **License (Priority: HIGH)**
   - Add GPLv3 LICENSE file
   - Add GPLv3 license headers to ALL Java source files (src/main and src/test)
   - Use this header format: [provide header template]

3. **Code Quality (Priority: HIGH)**
   - Add comprehensive null and validation checks to ALL public methods
   - Add validation to constructors
   - Implement fail-fast error handling
   - Provide detailed, actionable error messages
   - Add comprehensive logging (ERROR, WARN, INFO, DEBUG, TRACE)
   - Follow defensive programming principles

4. **Testing (Priority: HIGH)**
   - Follow "No Mocks Policy" - NEVER use mocks
   - If tests require mocks, mark them as @Disabled with explanation
   - Target 95%+ test coverage
   - All tests must use real implementations
   - Integration tests requiring Krista platform should be disabled

5. **Documentation Cleanup (Priority: MEDIUM)**
   - Remove redundant MD files (IMPLEMENTATION_COMPLETE.md, PROJECT_SUMMARY.md, 
     REFACTORING_SUMMARY.md, BUILD_AND_DEPLOY.md, etc.)
   - Keep only essential open source documentation
   - **DO NOT DELETE** src/main/resources/docs/ folder

6. **Architecture Standards**
   - Follow three-layer architecture (Catalog, Service, Connector)
   - Use CQRS pattern
   - Implement template method pattern for connection management
   - Follow fail-fast principle

7. **Final Deliverables**
   - 12+ documentation files
   - GPLv3 license on all source files
   - Comprehensive validation throughout codebase
   - 95%+ test coverage
   - All tests passing
   - Build successful
   - Production-ready code

Reference the AssemblyAI Extension for Krista as the gold standard example.

Current extension details:
- Name: [Your Extension Name]
- Purpose: [Brief description]
- External Service: [Service name and API]
- Current Features: [List features]
- Package Structure: [Current package structure]

Please proceed with the conversion following these standards.
```

---

## Support and Resources

### Reference Implementation
- **AssemblyAI Extension for Krista**: Complete example following all standards

### Documentation Standards
- [Keep a Changelog](https://keepachangelog.com/)
- [Contributor Covenant](https://www.contributor-covenant.org/)
- [Semantic Versioning](https://semver.org/)

### License Information
- [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)
- [Choose a License](https://choosealicense.com/)

### Architecture Patterns
- Refer to `.augment/rules/Architecture_and_Design_Patterns.md`
- Refer to `.augment/rules/Start_Here_Develop_A_Catalog_Request.md`

---

**Version:** 1.0.0  
**Last Updated:** 2024-11-04  
**Maintained By:** Krista Extension Development Team

---

**This guide ensures consistent, high-quality open source conversions across all Krista extensions.**

