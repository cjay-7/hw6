# Self-Review Rubric Annotation Guide

## Part 1: Design and Implementation Checks

### 1. Model Interface Change (up to 20 annotations)

Annotate these locations showing how your model interface changed from version 1:

#### CalendarModelInterface.java
- **Line 75** (`editEvent(UUID eventId, EditSpec spec)`)
  - ANNOTATION: "CHANGE: Modified to use EditSpec pattern instead of individual parameters, allowing partial updates without knowing all field values"

- **Line 93** (`editSeriesFrom(UUID seriesId, LocalDate fromDate, EditSpec spec)`)
  - ANNOTATION: "CHANGE: Added method to edit recurring events from a specific date forward, supporting partial series modifications"

- **Line 102** (`editEntireSeries(UUID seriesId, EditSpec spec)`)
  - ANNOTATION: "CHANGE: Added method to edit all events in a series atomically"

- **Line 127** (`getEventsInRange(LocalDateTime startDateTime, LocalDateTime endDateTime)`)
  - ANNOTATION: "CHANGE: Added range-based query to support viewing events within a time period"

- **Line 110** (`getEventsOnDate(LocalDate date)`)
  - ANNOTATION: "CHANGE: Added date-based query to support calendar day views"

- **Line 134** (`getAllEvents()`)
  - ANNOTATION: "CHANGE: Added to support exporting and displaying all events"

- **Line 142** (`isBusy(LocalDateTime dateTime)`)
  - ANNOTATION: "CHANGE: Added to check if user is busy at specific time"

- **Line 151** (`findEventById(UUID eventId)`)
  - ANNOTATION: "CHANGE: Added UUID-based lookup to support direct event access from GUI"

- **Line 168** (`findEventByProperties(...)`)
  - ANNOTATION: "CHANGE: Added property-based lookup for finding events without UUID"

#### CalendarInterface.java (New Interface)
- **Line 1-55** (Entire interface)
  - ANNOTATION: "CHANGE: Added CalendarInterface to support multiple calendars with names and timezones. Previously, the model only supported a single calendar."

- **Line 24** (`getName()`)
  - ANNOTATION: "CHANGE: Added to distinguish between multiple calendars"

- **Line 39** (`getTimezone()`)
  - ANNOTATION: "CHANGE: Added timezone support for multi-timezone calendar management"

- **Line 54** (`getModel()`)
  - ANNOTATION: "CHANGE: Each calendar now has its own model, enabling multi-calendar support"

#### EventInterface.java
- **Line 71** (`getSeriesId()`)
  - ANNOTATION: "CHANGE: Added to track recurring event series membership"

- **Line 78** (`isAllDayEvent()`)
  - ANNOTATION: "CHANGE: Added to identify 8am-5pm all-day events for display purposes"

- **Line 111** (`withModifications(...)`)
  - ANNOTATION: "CHANGE: Added immutable update pattern to create modified copies while preventing mutation"

#### GuiViewInterface.java (New Interface)
- **Line 1** (Entire interface)
  - ANNOTATION: "CHANGE: Added GUI view interface to support graphical calendar display. Original design only supported text-based console view."

#### Features.java (New Interface)
- **Line 12** (`public interface Features`)
      - ANNOTATION: "CHANGE: Added Features interface defining all GUI controller operations. This separates the controller's contract from its implementation, allowing different views to call the same controller methods."

- **Line 20** (`void createCalendar(String name, ZoneId timezone)`)
  - ANNOTATION: "CHANGE: Added to support creating calendars from GUI with timezone selection"

- **Line 79** (`void createEvent(...)`)
  - ANNOTATION: "CHANGE: Added GUI-specific event creation that accepts primitive types and String instead of domain objects, better suited for GUI input"

- **Line 95** (`void createEventSeries(...)`)
  - ANNOTATION: "CHANGE: Added to support creating recurring events from GUI, including weekday selection"

- **Line 111** (`void editEvent(EventInterface event, ...)`)
  - ANNOTATION: "CHANGE: Added GUI event editing that works with EventInterface objects selected from the view"

- **Line 126** (`void editSeries(String seriesId, ...)`)
  - ANNOTATION: "CHANGE: Added to support editing entire recurring series from GUI"

- **Line 142** (`void editSeriesFromDate(String seriesId, LocalDate fromDate, ...)`)
  - ANNOTATION: "CHANGE: Added to support editing recurring series from specific date forward, a GUI-specific workflow"

### 2. Model Mutation (up to 5 annotations)

Annotate these locations showing the model prevents view mutation:

#### EventInterface.java
- **Line 8** ("Represents a calendar event with immutable properties")
  - ANNOTATION: "MUTATION PREVENTION: EventInterface only provides getters, no setters. Events are immutable - the view cannot modify event objects."

- **Line 22** (`String getSubject()`)
  - ANNOTATION: "MUTATION PREVENTION: Returns immutable String. View cannot modify the event's subject."

- **Line 29** (`LocalDateTime getStartDateTime()`)
  - ANNOTATION: "MUTATION PREVENTION: LocalDateTime is immutable in Java. View receives a copy that cannot affect the model's data."

- **Line 43** (`Optional<String> getDescription()`)
  - ANNOTATION: "MUTATION PREVENTION: Returns Optional<String> - both Optional and String are immutable, preventing view from modifying event data."

#### CalendarModel.java
- **Line 280** (`return events.stream()...collect(Collectors.toList())` in `getAllEvents()`)
  - ANNOTATION: "MUTATION PREVENTION: Returns defensive copy via stream().collect(). View receives a new ArrayList, not the model's internal Set. View can modify this list without affecting the model."

- **Line 268** (`return events.stream()...collect(Collectors.toList())` in `getEventsOnDate()`)
  - ANNOTATION: "MUTATION PREVENTION: Returns new list created by Collectors.toList(). Even if view modifies the returned list, model's internal events Set remains unchanged."

### 3. Multiple GUI Views

#### GuiController.java
- **Line 24** (`public class GuiController implements Features`)
  - ANNOTATION: "GUI CONTROLLER: This controller is specifically designed for Swing GUI. To support a different GUI library (e.g., JavaFX, Android), you would need to: (1) Create a new view interface for that library (e.g., JavaFxViewInterface), (2) Create a new controller that implements the same Features interface but calls methods on the new view interface, (3) The model and core logic remain unchanged - only the view and controller change. The Features interface and CalendarManager would stay the same, demonstrating separation of concerns."

- **Line 26** (`private final GuiViewInterface view`)
  - ANNOTATION: "To support a different GUI library, this would be replaced with a different view interface (e.g., JavaFxViewInterface), but the same CalendarManager would be reused."

### 4. Code Duplication Controller (up to 5 annotations)

#### Controller.java
- **Line 36** (`private final CalendarManager manager`)
  - ANNOTATION: "DUPLICATION REDUCTION: Both text (Controller) and GUI (GuiController) controllers use CalendarManager, eliminating duplication of calendar management logic."

#### GuiController.java
- **Line 254** (`validateSubject(subject)`)
  - ANNOTATION: "DUPLICATION REDUCTION: Validation logic extracted into private helper methods (validateSubject, validateEventTimes, etc.) that could be shared. Consider extracting to a ValidationHelper class to share between controllers."

- **Line 498** (`private void validateSubject(String subject)`)
  - ANNOTATION: "Validation helper that reduces duplication within GuiController. Similar validation exists in the Command classes for the text controller."

- **Line 511** (`private void validateEventTimes(...)`)
  - ANNOTATION: "Shared validation logic used by both createEvent and createEventSeries, reducing code duplication."

#### CommandHelper.java (Check if this file exists)
- This might contain shared helper methods between commands

### 5. File IO (up to 3 annotations)

#### ExportCommand.java
- **Line 69** (`Files.writeString(filePath, csvContent)`)
  - ANNOTATION: "FILE IO: Writes CSV export to filesystem. Defined in command layer because file I/O is a user operation, not core business logic. The model remains I/O-free and focuses on domain logic."

- **Line 78** (`Files.writeString(filePath, icalContent)`)
  - ANNOTATION: "FILE IO: Writes iCal export to filesystem. Separated from model to maintain single responsibility - model manages events, commands handle I/O."

- **Line 56** (`Path filePath = Paths.get(fileName).toAbsolutePath().normalize()`)
  - ANNOTATION: "FILE IO: Path resolution for export file. Located in command layer to keep model independent of file system concerns, enabling easier testing and reuse."

---

## Part 2: Test Adequacy Checks

Based on GuiControllerTest.java, annotate these test methods:

### TC1: Create calendar with timezone from GUI
- **Line 61-76** (`testCreateCalendarSuccess`)
  - ANNOTATION: "TC1: This test verifies that a new calendar with a timezone (America/Los_Angeles) can be created from a GUI-based view. It confirms the calendar is created, the view is refreshed, and success message is shown."

### TC2: Create single event from GUI
- **Line 230-242** (`testCreateEventSuccess`)
  - ANNOTATION: "TC2: This test verifies that a new single event can be created in a calendar from a GUI-based view. It creates an event with start/end times, location, and description, then confirms the view is refreshed."

### TC3: Modify specific event from GUI
- **Line 571-586** (`testEditEventSuccess`)
  - ANNOTATION: "TC3: This test verifies that a specific event can be modified in a calendar from a GUI-based view. It creates an event, retrieves it, edits the subject, and confirms the update succeeds."

### TC4: View event from GUI
- **Line 188-192** (`testSelectDay`)
  - ANNOTATION: "TC4: This test verifies that an event can be viewed in a calendar from a GUI-based view. It selects a date and confirms the view displays events for that date."

### TC5: Select calendar from GUI
- **Line 103-108** (`testSwitchCalendarSuccess`)
  - ANNOTATION: "TC5: This test verifies that a calendar can be selected from a GUI-based view. It creates a second calendar, switches to it, and confirms it becomes the current calendar."

### TC6: View default calendar from GUI
- **Line 42-47** (`testDefaultCalendarCreatedOnInit`)
  - ANNOTATION: "TC6: This test verifies that a default calendar can be viewed from a GUI-based view. When GuiController initializes with no calendars, it creates a default 'My Calendar' and displays it."

### TC7: Create recurring event with specific weekdays from GUI
- **Line 387-395** (`testCreateEventSeriesWithEndDate`)
  - ANNOTATION: "TC7: This test verifies that a new recurring event that repeats on specific weekdays (MONDAY) can be created in a calendar from a GUI-based view, with an end date."

Alternative for TC7:
- **Line 370-384** (`testCreateEventSeriesWithOccurrences`)
  - ANNOTATION: "TC7: This test verifies that a new recurring event that repeats on specific weekdays (MONDAY, WEDNESDAY) can be created in a calendar from a GUI-based view."

### TC8: Create recurring event that repeats N times from GUI
- **Line 370-384** (`testCreateEventSeriesWithOccurrences`)
  - ANNOTATION: "TC8: This test verifies that a new recurring event that repeats N times (5 occurrences) can be created in a calendar from a GUI-based view."

### TC9: Edit multiple events with same name from GUI
- **Line 646-663** (`testEditSeriesSuccess`)
  - ANNOTATION: "TC9: This test verifies that multiple events with the same name (part of a series) can be edited from a GUI-based view. It creates a weekly series and edits all occurrences."

### TC10: Edit multiple events from specific point in time from GUI
- **Line 679-690** (`testEditSeriesFromDateSuccess`)
  - ANNOTATION: "TC10: This test verifies that multiple events from a specific point in time (2025-06-23) can be edited from a GUI-based view. It uses editSeriesFromDate to modify events from that date forward."

---

## How to Add Annotations

For each location above:

1. Open the file in your IDE
2. Navigate to the specified line number
3. Add a comment BEFORE or ON the line with the annotation text
4. The comment should clearly explain WHY this represents the check

Example format:
```java
// DESIGN CHECK - Model Interface Change #1:
// CHANGE: Modified to use EditSpec pattern instead of individual parameters,
// allowing partial updates without knowing all field values
boolean editEvent(UUID eventId, EditSpec spec);
```

or

```java
// DESIGN CHECK - Mutation Prevention #1:
// MUTATION PREVENTION: EventInterface only provides getters, no setters.
// Events are immutable - the view cannot modify event objects.
public interface EventInterface {
```

For tests, add above the @Test annotation:
```java
// TEST ADEQUACY CHECK - TC1:
// This test verifies that a new calendar with a timezone can be created
// from a GUI-based view.
@Test
public void testCreateCalendarSuccess() {
```
