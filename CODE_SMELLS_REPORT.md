# Code Smells Report

This document identifies code smells found in the codebase that should be addressed to improve code quality, maintainability, and adherence to best practices.

## 🔴 Critical Issues

### 1. Generic Exception Catching
**Location**: Multiple command classes and `CalendarRunner.java`

**Problem**: Catching generic `Exception` instead of specific exception types makes error handling imprecise and can hide bugs.

**Affected Files**:
- `src/main/java/calendar/command/CreateEventCommand.java:53`
- `src/main/java/calendar/command/CreateAllDayEventCommand.java:58`
- `src/main/java/calendar/command/EditEventCommand.java:71`
- `src/main/java/calendar/command/EditSeriesCommand.java:85`
- `src/main/java/calendar/command/EditEventsCommand.java:86`
- `src/main/java/calendar/command/CreateEventSeriesFromToCommand.java:98`
- `src/main/java/calendar/command/CreateAllDayEventSeriesCommand.java:96`
- `src/main/java/calendar/command/ExportCommand.java:82`
- `src/main/java/calendar/command/PrintAllEventsCommand.java:37`
- `src/main/java/calendar/command/PrintEventsOnCommand.java:44`
- `src/main/java/calendar/command/PrintEventsRangeCommand.java:48`
- `src/main/java/calendar/command/ShowStatusCommand.java:43`
- `src/main/java/CalendarRunner.java:65, 113`
- `src/main/java/calendar/controller/Controller.java:82`

**Example**:
```java
} catch (Exception e) {
    view.displayError("Failed to create event: " + e.getMessage());
    return false;
}
```

**Recommendation**: Catch specific exceptions:
- `IllegalArgumentException` for validation errors
- `IOException` for I/O operations
- `DateTimeParseException` for date parsing errors
- etc.

---

### 2. Swallowed Exceptions
**Location**: `src/main/java/calendar/command/CreateEventCommand.java:56`

**Problem**: Exception is caught and ignored, with only a fallback to `System.out.println`.

**Code**:
```java
} catch (Exception ignore) {
    System.out.println("Failed to create event: " + e.getMessage());
}
```

**Recommendation**: Either:
- Remove the nested try-catch if not needed
- Log the exception properly
- Re-throw as a runtime exception if it's truly unexpected

---

### 3. Direct Console Output
**Location**: Multiple files

**Problem**: Using `System.out.println` and `System.err.println` instead of proper logging framework makes it difficult to control output, filter messages, and configure log levels.

**Affected Files**:
- `src/main/java/CalendarRunner.java` (45+ instances)
- `src/main/java/calendar/command/CreateEventCommand.java:57`

**Recommendation**: 
- Use a logging framework (e.g., `java.util.logging`, SLF4J, Log4j)
- For `CalendarRunner`, console output may be acceptable for CLI usage, but consider using a logger for error messages

---

### 4. Temporary File in Repository
**Location**: `src/main/java/calendar/view/CreateEventSeriesDialog.java.tmp`

**Problem**: Temporary files should not be committed to version control.

**Recommendation**: Delete this file and add `*.tmp` to `.gitignore` if not already present.

---

## 🟡 Moderate Issues

### 5. Deprecated Code Still in Use
**Location**: `src/main/java/calendar/model/EditSpec.java:56`

**Problem**: The deprecated constructor is still available and may be used elsewhere, making it unclear which API to use.

**Code**:
```java
@Deprecated
public EditSpec(String newSubject, LocalDateTime newStart, ...)
```

**Recommendation**: 
- Search for all usages of the deprecated constructor
- Replace with `EditSpec.builder()` pattern
- Remove deprecated constructor in next major version

---

### 6. Code Duplication in Validation Logic
**Location**: `src/main/java/calendar/controller/GuiController.java`

**Problem**: Similar validation patterns are repeated across multiple methods:
- `createEvent()` and `createEventSeries()` both validate subject, start/end times
- `editEvent()`, `editSeries()`, and `editSeriesFromDate()` have similar validation

**Examples**:
- Subject validation: `if (subject == null || subject.trim().isEmpty())`
- Time validation: `if (start == null || end == null)`
- End after start: `if (!end.isAfter(start))`

**Recommendation**: Extract validation logic into private helper methods:
```java
private void validateEventInput(String subject, LocalDateTime start, LocalDateTime end) {
    if (subject == null || subject.trim().isEmpty()) {
        throw new IllegalArgumentException("Event subject cannot be empty.");
    }
    if (start == null || end == null) {
        throw new IllegalArgumentException("Event start and end times must be specified.");
    }
    if (!end.isAfter(start)) {
        throw new IllegalArgumentException("Event end time must be after start time.");
    }
}
```

---

### 7. Long Methods
**Location**: `src/main/java/calendar/controller/GuiController.java`

**Problem**: Some methods are quite long and handle multiple responsibilities:

- `createEventSeries()`: ~90 lines (282-374)
  - Validates input (multiple checks)
  - Creates event template
  - Creates series configuration
  - Handles success/error cases

**Recommendation**: Break down into smaller methods:
- `validateSeriesInput()`
- `createSeriesTemplate()`
- `createSeriesConfiguration()`
- `handleSeriesCreationResult()`

---

### 8. Duplicate Loop in `createEventSeries()`
**Location**: `src/main/java/calendar/model/CalendarModel.java:62-70`

**Problem**: Two separate loops iterate over the same collection:
```java
for (EventInterface occurrence : occurrences) {
    if (events.contains(occurrence)) {
        return false;
    }
}

for (EventInterface occurrence : occurrences) {
    events.add(occurrence);
}
```

**Recommendation**: Combine into a single loop or use a more efficient approach:
```java
for (EventInterface occurrence : occurrences) {
    if (events.contains(occurrence)) {
        return false;
    }
    events.add(occurrence);
}
```

**Note**: This is actually a performance optimization - checking all before adding prevents partial state. However, if performance is not critical, combining loops reduces duplication.

---

## 🟢 Minor Issues

### 9. Magic Numbers (Partially Addressed)
**Location**: Various files

**Status**: Most magic numbers are already constants:
- ✅ `RFC5545_LINE_FOLD_WIDTH = 75` in `IcalExporter.java`
- ✅ `ALL_DAY_EVENT_START_HOUR = 8` in `Event.java`
- ✅ `ALL_DAY_EVENT_END_HOUR = 17` in `Event.java`
- ✅ `SERIES_MAX_YEARS = 10` in `CalendarModel.java`

**Remaining**: Some hardcoded values in view classes (e.g., `1000x700` window size, `8` pixel spacing) could be extracted to constants if they're used in multiple places.

---

### 10. Complex Conditionals
**Location**: `src/main/java/calendar/controller/GuiController.java:326`

**Problem**: Nested conditionals can be hard to read:
```java
if (endDate != null && occurrences != null) {
    
} else if (endDate != null) {
    
} else if (occurrences != null) {
    
} else {
    
}
```

**Recommendation**: Consider using early returns or extracting to a method:
```java
private void validateSeriesEndCondition(LocalDate endDate, Integer occurrences) {
    if (endDate != null && occurrences != null) {
        throw new IllegalArgumentException("Please specify either an end date OR number of occurrences, not both.");
    }
    if (endDate == null && occurrences == null) {
        throw new IllegalArgumentException("Please specify either an end date or number of occurrences.");
    }
    
}
```

---

### 11. Empty Comments
**Location**: `src/main/java/calendar/model/CalendarModel.java:293, 307, 331`

**Problem**: Empty comment lines that don't add value:
```java
.filter(e -> {
    
    return e.getStartDateTime().isBefore(endDateTime)
        && e.getEndDateTime().isAfter(startDateTime);
})
```

**Recommendation**: Remove empty comment lines or add meaningful comments explaining complex logic.

---

## ✅ Good Practices Observed

1. **Builder Pattern**: `EditSpec` uses builder pattern to avoid long parameter lists
2. **Helper Classes**: `CommandHelper` reduces duplication across command classes
3. **Constants**: Most magic numbers are extracted to named constants
4. **Private Utility Constructors**: Utility classes properly prevent instantiation
5. **Comprehensive JavaDoc**: Most public methods have good documentation
6. **Null Safety**: Good use of `Objects.requireNonNull()` for validation

---

## Summary

**Priority Actions**:
1. 🔴 Replace generic exception catching with specific exceptions
2. 🔴 Remove temporary file from repository
3. 🔴 Replace `System.out.println` with proper logging (where appropriate)
4. 🟡 Extract validation logic to reduce duplication in `GuiController`
5. 🟡 Break down long methods in `GuiController`
6. 🟡 Address deprecated `EditSpec` constructor usage

**Estimated Impact**:
- **Maintainability**: High (reducing duplication, improving error handling)
- **Debugging**: High (better exception handling, proper logging)
- **Code Quality**: Medium (cleaner structure, better separation of concerns)






