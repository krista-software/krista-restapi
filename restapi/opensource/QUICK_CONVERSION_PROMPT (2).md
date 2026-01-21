# Quick Open Source Conversion Prompt

**Copy and paste this prompt to AI assistants (Claude, ChatGPT, etc.) to convert your Krista extension to open source standards.**

---

## The Prompt

```
I want to convert my Krista extension to a world-class open source project following the 
AssemblyAI Extension for Krista as the reference standard.

EXTENSION DETAILS:
- Name: [Your Extension Name]
- Purpose: [Brief description]
- External Service: [Service name]
- Package: app.krista.extensions.[ecosystem].[domain].[extension]

REQUIREMENTS:

1. DOCUMENTATION (12+ files):
   ✅ README.md - Complete overview with badges, features, quick start, installation, 
      configuration, usage, architecture, API reference, testing, contributing, license, support
   ✅ QUICKSTART.md - Get started in <10 minutes
   ✅ ARCHITECTURE.md - Technical architecture, layers, patterns, components, data flow
   ✅ CONTRIBUTING.md - How to contribute, coding standards, PR process
   ✅ CODE_OF_CONDUCT.md - Contributor Covenant v2.1
   ✅ CHANGELOG.md - Keep a Changelog format
   ✅ SECURITY.md - Security policy and vulnerability reporting
   ✅ CONTRIBUTORS.md - Recognition of contributors
   ✅ docs/API_REFERENCE.md - Complete API documentation with examples
   ✅ docs/EXAMPLES.md - Comprehensive usage examples
   ✅ docs/TROUBLESHOOTING.md - Common issues and solutions
   ✅ KEEP src/main/resources/docs/ - Required for Krista platform help

2. LICENSE (GPLv3):
   ✅ Add LICENSE file: curl -s https://www.gnu.org/licenses/gpl-3.0.txt > LICENSE
   ✅ Add GPLv3 header to ALL Java files (src/main and src/test)
   ✅ Header format:
   /*
    * [Extension Name] for Krista
    * Copyright (C) 2024 [Organization]
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

3. VALIDATION (Comprehensive):
   ✅ Add null checks to ALL public method parameters
   ✅ Add validation to ALL constructors
   ✅ Validate: null, empty strings, positive numbers, file existence, ranges
   ✅ Throw IllegalArgumentException with clear, actionable messages
   ✅ Example:
   if (param == null || param.trim().isEmpty()) {
       throw new IllegalArgumentException("Parameter cannot be null or empty");
   }

4. ERROR HANDLING (Fail-Fast):
   ✅ Stop on first error - don't continue on failure
   ✅ Provide detailed error messages with context
   ✅ Log errors with appropriate levels (ERROR, WARN, INFO, DEBUG, TRACE)
   ✅ Example:
   if (!operation()) {
       LOG.error("Operation failed: {}", details);
       return false; // Stop immediately
   }

5. TESTING (No Mocks Policy):
   ✅ NEVER use mocks - they hide real issues
   ✅ If test requires mocks, mark @Disabled with explanation
   ✅ Target 95%+ test coverage
   ✅ Integration tests requiring Krista platform: @Disabled
   ✅ Example:
   @Test
   @Disabled("Requires Krista platform runtime - No Mocks Policy")
   void testMethod() { ... }

6. CLEANUP:
   ✅ Remove: IMPLEMENTATION_COMPLETE.md, PROJECT_SUMMARY.md, REFACTORING_SUMMARY.md,
      BUILD_AND_DEPLOY.md, DEVELOPER_GUIDE.md, INTEGRATION_TESTING_SUMMARY.md
   ✅ Keep: All core docs, docs/ folder, src/main/resources/docs/

7. ARCHITECTURE:
   ✅ Three-layer: Catalog (orchestration) → Service (business logic) → Connector (API)
   ✅ CQRS pattern: Separate commands and queries
   ✅ Template method pattern: Reusable connection management
   ✅ Fail-fast principle: Stop on first error

8. DELIVERABLES:
   ✅ 12+ documentation files
   ✅ GPLv3 license on all source files
   ✅ Comprehensive validation throughout
   ✅ 95%+ test coverage
   ✅ All tests passing
   ✅ Build successful
   ✅ Production-ready

PROCESS:
1. Create all documentation files
2. Add LICENSE and headers to all Java files
3. Add comprehensive validation to all public methods and constructors
4. Implement fail-fast error handling
5. Update/remove tests following No Mocks Policy
6. Remove redundant documentation
7. Verify build and tests pass
8. Create summary document

Reference: AssemblyAI Extension for Krista (gold standard)

Please proceed with the conversion.
```

---

## Usage Instructions

### Step 1: Prepare Your Information

Before using the prompt, gather:
- Extension name
- Brief purpose description
- External service name (if applicable)
- Package structure
- Current features list

### Step 2: Customize the Prompt

Replace placeholders:
- `[Your Extension Name]` → Your actual extension name
- `[Brief description]` → What your extension does
- `[Service name]` → External service (e.g., "Google Calendar API")
- `[Organization]` → Your organization name
- `[ecosystem].[domain].[extension]` → Your package path

### Step 3: Use with AI Assistant

1. Copy the customized prompt
2. Paste into Claude, ChatGPT, or other AI assistant
3. Provide access to your codebase
4. Let the AI assistant guide you through the conversion

### Step 4: Review and Validate

After conversion:
- ✅ Review all generated documentation
- ✅ Verify license headers on all files
- ✅ Run tests: `./gradlew test`
- ✅ Build project: `./gradlew clean build`
- ✅ Check test coverage
- ✅ Validate all links in documentation

---

## Example Usage

### Before Customization:
```
- Name: [Your Extension Name]
- Purpose: [Brief description]
- External Service: [Service name]
```

### After Customization:
```
- Name: Google Calendar Extension
- Purpose: Integrate Google Calendar with Krista for event management
- External Service: Google Calendar API v3
```

---

## Expected Timeline

| Phase | Duration | Tasks |
|-------|----------|-------|
| **Documentation** | 2-3 hours | Create 12+ documentation files |
| **License** | 30 minutes | Add LICENSE and headers |
| **Validation** | 1-2 hours | Add comprehensive validation |
| **Testing** | 1-2 hours | Update tests, remove mocks |
| **Cleanup** | 30 minutes | Remove redundant files |
| **Verification** | 1 hour | Build, test, review |
| **TOTAL** | **6-9 hours** | Complete conversion |

---

## Success Criteria

Your conversion is complete when:

- ✅ All 12+ documentation files exist and are accurate
- ✅ GPLv3 LICENSE file exists
- ✅ All Java files have GPLv3 headers
- ✅ All public methods have validation
- ✅ All constructors have validation
- ✅ No mocks in test code
- ✅ Test coverage ≥95%
- ✅ All tests passing
- ✅ Build successful
- ✅ No redundant documentation files
- ✅ src/main/resources/docs/ preserved

---

## Common Pitfalls to Avoid

### ❌ DON'T:
- Delete src/main/resources/docs/ (required for Krista)
- Use mocks in tests
- Continue operations after failures
- Provide vague error messages
- Skip validation on "internal" methods
- Create tests that can't run without mocks

### ✅ DO:
- Keep all Krista extension documentation
- Disable tests that require mocks
- Stop immediately on first error
- Provide detailed, actionable error messages
- Validate all public method inputs
- Mark platform-dependent tests as @Disabled

---

## Support

### Questions?
- Review: `OPEN_SOURCE_CONVERSION_GUIDE.md` (detailed guide)
- Reference: AssemblyAI Extension for Krista (complete example)
- Check: `.augment/rules/Architecture_and_Design_Patterns.md`

### Issues?
- Verify all placeholders are replaced
- Check that codebase is accessible to AI assistant
- Ensure you have write permissions
- Validate Java and Gradle versions

---

## Quick Reference Card

```
DOCUMENTATION: 12+ files (README, QUICKSTART, ARCHITECTURE, CONTRIBUTING, 
               CODE_OF_CONDUCT, CHANGELOG, SECURITY, CONTRIBUTORS, LICENSE,
               API_REFERENCE, EXAMPLES, TROUBLESHOOTING)

LICENSE:       GPLv3 on all Java files

VALIDATION:    All public methods + constructors
               - Null checks
               - Empty string checks
               - Range validation
               - File validation

ERROR:         Fail-fast principle
               - Stop on first error
               - Detailed messages
               - Comprehensive logging

TESTING:       No Mocks Policy
               - 95%+ coverage
               - Real implementations only
               - @Disabled if requires mocks

CLEANUP:       Remove redundant docs
               Keep src/main/resources/docs/

BUILD:         ./gradlew clean build test
               All tests passing
               No compilation errors
```

---

**Version:** 1.0.0  
**Last Updated:** 2024-11-04  
**Reference:** AssemblyAI Extension for Krista

---

**Copy the prompt above and start converting your Krista extension to open source!** 🚀

