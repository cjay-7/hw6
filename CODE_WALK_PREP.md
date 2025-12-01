# Code Walk 3 Preparation Guide
**Assignment:** HW6 - Calendar Application with GUI
**Date:** TBD
**Time Limit:** 3-minute overview + 10-15 minutes Q&A

---

## Table of Contents
1. [3-Minute Overview Script](#3-minute-overview-script)
2. [Quick Reference: Key Design Decisions](#quick-reference-key-design-decisions)
3. [Expected Questions & Answers](#expected-questions--answers)
4. [Code Location Cheat Sheet](#code-location-cheat-sheet)
5. [Design Evolution Timeline](#design-evolution-timeline)
6. [SOLID Principles Applied](#solid-principles-applied)
7. [Last-Minute Checklist](#last-minute-checklist)

---

## 3-Minute Overview Script

**Practice this until you can deliver it in under 3 minutes!**

> "Our calendar application follows strict MVC architecture with complete separation of concerns.
>
> **Model Layer:** The core is `CalendarModel`, which stores events in a HashSet for O(1) duplicate detection. Events are immutable value objects identified by subject, start time, and end time. We use the immutable pattern with a `withModifications()` method to create modified copies. This ensures thread-safety and predictable behavior.
>
> **Multi-Calendar Support:** In HW5, we added `CalendarManager` to manage multiple calendars. Each `Calendar` wraps a `CalendarModel` and has a timezone. The manager tracks the current calendar context, so commands like 'create event' operate on the active calendar. Calendar names are unique and case-sensitive.
>
> **Command Pattern:** We use the Command pattern with Chain of Responsibility for parsing. `CommandParser` has 23 different `CommandMatcher` objects that try matching input in sequence. More specific patterns come first. Each command encapsulates its own execution logic, making the system extensible - adding new commands doesn't require modifying the parser.
>
> **Controller Unification:** In HW4, we had separate controllers for interactive and headless modes. In HW5, we unified them into a single `Controller` that accepts any `Readable` source. This eliminated 90% code duplication and improved testability.
>
> **GUI Architecture:** For HW6, we introduced the `Features` interface following the Stage 5 MVC pattern. The existing `Controller` uses `Readable` which doesn't work for button clicks. `Features` provides high-level callbacks like `createEvent()` and `navigateToMonth()`. `GuiController` implements `Features` and has zero Swing dependencies, making it fully testable with mock views. `GuiView` translates low-level Swing events into Features calls.
>
> **View Layer:** We have two view implementations - `ConsoleView` for text output and `GuiView` using Java Swing. The GUI supports month and week views, calendar color-coding, and dialog-based event creation and editing. All three modes - GUI, interactive, and headless - share the same model and business logic."

**Time Check:** This should take 2:30-2:45. Leave 15-30 seconds buffer.

---

## Quick Reference: Key Design Decisions

### 1. Event Uniqueness (HashSet + equals/hashCode)
**Location:** `Event.java:166-186`

**Decision:** Events equal if subject + start + end are the same.

**Why:**
- Assignment requirement: no duplicate events
- HashSet provides O(1) duplicate checking
- Natural business key for calendar events

**Trade-offs:**
- ✅ Fast duplicate detection
- ✅ Simple, intuitive equality
- ❌ Can't have identical events at same time (even with different locations)
- ❌ Exposes identity mechanism via public API

**Defense:** "This aligns with real calendar apps like Google Calendar - you can't have two identically named events at the exact same time."

---

### 2. Immutable Events with withModifications()
**Location:** `Event.java:130-151`, `EventInterface.java:100-113`

**Decision:** Events are immutable; edits create new instances.

**Why:**
- Thread-safety without synchronization
- No defensive copying needed
- Predictable behavior (no hidden side effects)
- Easier to test and debug

**Trade-offs:**
- ✅ Safe to share across layers
- ✅ Accidental modification impossible
- ❌ Memory overhead (new object per edit)
- ❌ Must remove old and add new to set

**Defense:** "Memory is cheap, bugs are expensive. Immutability prevents entire classes of concurrency bugs."

---

### 3. Controller Unification (HW4 → HW5)
**Location:** `Controller.java:35-59`

**Before:** `InteractiveController` and `HeadlessController` (90% duplicate code)

**After:** Single `Controller` with `Readable` parameter

**Why:**
- DRY principle - single source of truth
- `Readable` abstraction works for any input source
- Easier testing with `StringReader`
- Only difference is whether to show prompts (`interactive` flag)

**Trade-offs:**
- ✅ Eliminated code duplication
- ✅ More flexible (works with files, strings, network)
- ✅ Easier to maintain
- ❌ Slightly less obvious than two classes with descriptive names

**Defense:** "We valued correctness over superficial clarity. Duplicated code means duplicated bugs."

---

### 4. Features Interface for GUI (HW6)
**Location:** `Features.java`, `GuiController.java:24-55`

**Decision:** Separate `Features` interface instead of reusing `Controller`.

**Why:**
- GUI is event-driven (callbacks), not sequential
- `Controller` depends on `Readable` (incompatible with button clicks)
- High-level operations (`createEvent`) not low-level (`actionPerformed`)
- Follows Stage 5 MVC pattern from lecture

**Trade-offs:**
- ✅ `GuiController` has zero Swing dependencies
- ✅ Fully testable with mock view
- ✅ Clean separation of concerns
- ❌ Some logic duplication between controllers
- ❌ Two controller classes to maintain

**Defense:** "We chose testability and clean architecture over avoiding a second controller class."

---

### 5. Command Pattern + Chain of Responsibility
**Location:** `CommandParser.java:35-90`, `CommandInterface.java`

**Decision:** Each command type has own `CommandMatcher`, parser tries them in sequence.

**Why:**
- Eliminates massive if-else chain
- Each command encapsulates parsing and execution
- Easy to add new commands (Open/Closed Principle)
- Order matters: specific patterns before general ones

**Trade-offs:**
- ✅ Extensible without modifying parser
- ✅ Each command is self-contained
- ✅ Easy to test individual matchers
- ❌ 23+ matcher classes (lots of files)
- ❌ Order-dependent (fragile if reordered incorrectly)

**Defense:** "We prioritized extensibility and maintainability over file count."

---

### 6. CalendarManager Wrapper (HW5)
**Location:** `CalendarManager.java:24-100`

**Decision:** Introduce manager layer to handle multiple calendars.

**Why:**
- Assignment requirement: support multiple calendars
- Encapsulates calendar creation, lookup, and current context
- Enforces unique calendar names
- Associates timezone with each calendar

**Trade-offs:**
- ✅ Clean multi-calendar support
- ✅ Name uniqueness enforced
- ✅ Clear separation of concerns
- ❌ Extra indirection layer
- ❌ Commands must call `manager.getCurrentCalendar().getModel()`

**Defense:** "The extra layer pays for itself in clarity - calendar management is orthogonal to event management."

---

### 7. Series Editing Splits Events
**Location:** `CalendarModel.java` (editSeriesFrom, editEntireSeries methods)

**Decision:** Editing a series event's start time splits it from the series.

**Why:**
- Assignment constraint: all events in series must have same start time
- Changing time violates the series invariant
- Must either reject the edit or split the event out

**Trade-offs:**
- ✅ Maintains series integrity
- ✅ Allows flexibility (can change individual occurrences)
- ❌ Can be confusing for users
- ❌ Series can fragment over time

**Defense:** "This matches Google Calendar's behavior - try it yourself. It's unintuitive but correct."

---

### 8. Data Structure Choices

| Structure | Location | Why |
|-----------|----------|-----|
| `HashSet<EventInterface>` | CalendarModel.java:30 | O(1) duplicate detection |
| `HashMap<UUID, EventSeries>` | CalendarModel.java:33 | Fast series lookup by ID |
| `HashMap<String, CalendarInterface>` | CalendarManager.java:25 | Fast calendar lookup by name |
| `List<EventInterface>` (returned) | Various query methods | Sorted chronological results |

**Defense:** "We chose structures based on access patterns - sets for uniqueness, maps for lookups, lists for ordered results."

---

## Expected Questions & Answers

### Q1: Why did you choose HashSet to store events?

**Answer:**

"We use `HashSet<EventInterface>` for O(1) duplicate checking. Since events are equal by subject+start+end (via our `equals()` implementation), when we try to add a duplicate, `HashSet.add()` returns false immediately.

The alternative would be iterating through a list and comparing each event - that's O(n). With HashSet, duplicate checking is constant time regardless of calendar size.

The trade-off is we lose insertion order, but we don't need it. When returning events from queries, we sort them chronologically using `Comparator.comparing()`. This gives us the best of both worlds - fast duplicate checking and ordered results when needed."

**Code Reference:** `CalendarModel.java:30` (field declaration), `CalendarModel.java:48-50` (duplicate check)

**Follow-up - "What about memory?"**

"HashSet has some memory overhead compared to ArrayList (about 1.75x), but events are small objects - maybe 200 bytes each. Even 10,000 events is only about 2MB extra. The speed benefit is worth it."

---

### Q2: Explain your decision to use the Command pattern.

**Answer:**

"The Command pattern decouples command parsing from execution. Each command class implements `CommandInterface` with an `execute()` method. This follows Single Responsibility Principle - the parser only parses, commands only execute.

For parsing, we use Chain of Responsibility. `CommandParser` has a list of `CommandMatcher` objects that try matching in sequence. Each matcher knows its own syntax pattern. More specific patterns come first to prevent false matches.

For example, `CreateEventSeriesFromToUntilCommandMatcher` must come before `CreateEventCommandMatcher`, otherwise the simpler pattern would match first and fail.

This makes the system extensible. To add a new command, you just create a new matcher and add it to the chain in `CommandParser`'s constructor. The parser itself doesn't change. This is the Open/Closed Principle - open for extension, closed for modification."

**Code Reference:** `CommandParser.java:43-65` (chain setup), `CommandInterface.java` (command contract)

**Follow-up - "Isn't 23 matchers excessive?"**

"It's verbose but maintainable. Each matcher is 20-30 lines and tests exactly one command syntax. The alternative is a 500-line switch statement with complex regex. We chose many small classes over one giant method."

---

### Q3: How did you handle the transition from single to multiple calendars?

**Answer:**

"In HW4, commands worked directly with `CalendarModelInterface`. In HW5, we introduced two new classes:

1. **CalendarManager** - Manages the collection of calendars, enforces unique names, tracks which calendar is currently active.

2. **Calendar** - Wraps a `CalendarModel` and adds a name and timezone. This is composition - a Calendar *has-a* model rather than *is-a* model.

Commands now access the model indirectly: `manager.getCurrentCalendar().getModel()`. We added validation - commands like 'create event' require a calendar to be in use first, otherwise they fail with a clear error message.

We also added three new commands: `create calendar`, `edit calendar`, and `use calendar`. These work directly with CalendarManager, not with individual calendar models.

The key insight was that calendar management is a separate concern from event management. By separating them, the core `CalendarModel` class didn't need to change at all."

**Code Reference:** `CalendarManager.java:24-62`, `CreateEventCommand.java` (showing manager usage)

**Follow-up - "Why not just have CalendarModel manage multiple calendars?"**

"That would violate Single Responsibility. `CalendarModel` handles event logic - creation, editing, queries. Adding multi-calendar logic would mix two concerns. By separating them, each class is simpler and more testable."

---

### Q4: Why did you create a separate Features interface for the GUI?

**Answer:**

"The existing `Controller` uses `Readable` to read commands line-by-line. This doesn't work for GUI button clicks - you can't 'read' a button press as text.

We needed an event-driven approach. The `Features` interface provides high-level, application-specific callbacks like `createEvent()`, `navigateToMonth()`, and `switchCalendar()`. These are expressed in domain language, not GUI language.

`GuiView` translates low-level Swing events into Features calls. For example, when the user clicks 'Create Event', the view shows a dialog, collects input, validates it, then calls `features.createEvent()` with the parameters.

Critically, `GuiController` has no Swing dependencies - no `javax.swing` imports at all. This makes it fully testable with a mock view. We can simulate user actions by calling Features methods directly in tests, without needing actual GUI components.

This follows the Stage 5 MVC pattern from lecture - the controller only knows about abstract view operations, not concrete Swing widgets."

**Code Reference:** `Features.java` (interface), `GuiController.java:43-55` (constructor + initialization), Look in `GuiView.java` for where it calls features

**Follow-up - "Why not just extend Controller?"**

"Inheritance would couple the GUI controller to `Readable`, which it doesn't need. We used composition - `GuiController` implements `Features`, not extends `Controller`. Favor composition over inheritance."

---

### Q5: How do you ensure events remain unique when editing a series?

**Answer:**

"When editing events, we must prevent creating duplicates. The model validates that no event with the new subject+start+end already exists before applying changes.

For series editing, there are three scopes:

1. **Edit single event** (`editEvent`) - Modifies one occurrence, leaves others unchanged.

2. **Edit from date forward** (`editSeriesFrom`) - Modifies all events in the series starting from a date. For example, if you have 6 occurrences and edit starting from the 3rd, it modifies 4 events.

3. **Edit entire series** (`editEntireSeries`) - Modifies all events in the series.

The complexity comes from start time edits. If you change the start time, affected events can no longer be part of the series because all series events must have the same start time. So we remove them from the series by setting their `seriesId` to null or a new UUID.

The validation happens before any changes - if the edit would create duplicates, we return false and make no changes. This is all-or-nothing atomicity."

**Code Reference:** `CalendarModelInterface.java:68-102` (edit methods), `CalendarModel.java` (implementations)

**Follow-up - "Couldn't you just update the series template?"**

"That wouldn't work for partial edits. If you edit only events from the 3rd occurrence forward, they need a different start time than the first two occurrences. They can't be in the same series anymore. This is why they split."

---

### Q6: How did you handle timezone conversions when copying events?

**Answer:**

"Each `Calendar` has a `ZoneId` timezone field. When copying events between calendars with different timezones, we convert times using Java's `ZonedDateTime`.

Here's the process:

1. Get the source event's start/end as `LocalDateTime` (no timezone info)
2. Combine with source calendar's timezone to get `ZonedDateTime`
3. Convert to target timezone using `withZoneSameInstant()`
4. Extract the `LocalDateTime` in target timezone
5. Create the new event with converted times

For example, copying an event at 2:00 PM from an EST calendar to a PST calendar:
- 2:00 PM EST = 14:00 EST
- Convert to instant: 2023-05-15T14:00-05:00
- Convert to PST: 2023-05-15T11:00-08:00
- Result: 11:00 AM PST

The physical moment in time is the same, but the displayed time changes. This is handled in the copy commands."

**Code Reference:** `CopyEventCommand.java`, `CopyEventsOnDayCommand.java` (timezone conversion logic)

**Follow-up - "What about daylight saving time?"**

"`ZoneId` handles DST automatically. America/New_York accounts for EST/EDT transitions. Java's time API handles the complexity for us."

---

### Q7: What design patterns did you use, and why?

**Answer:**

"We used several patterns, each solving a specific problem:

1. **MVC Pattern** - Separates data, presentation, and logic. Model has no view dependencies, view has no model dependencies, controller mediates.

2. **Command Pattern** - Encapsulates commands as objects. Allows extensibility, potential undo/redo, command logging.

3. **Chain of Responsibility** - Command matchers form a chain. Each tries to match, passes to next if it fails. Makes parser extensible.

4. **Strategy Pattern** - Different export strategies (CSV via `CsvExporter`, iCal via `IcalExporter`) implement the same export interface.

5. **Immutable Object Pattern** - Events are immutable, using `withModifications()` to create copies. Ensures thread-safety and predictability.

6. **Features/Callback Pattern** - GUI uses callbacks instead of polling. View calls controller methods when events occur.

7. **Dependency Injection** - Controllers receive model and view via constructor, not creating them. Makes testing easier.

We didn't force patterns - we used them where they naturally fit. Every pattern choice has a clear reason tied to a design goal like extensibility, testability, or separation of concerns."

**Code Reference:** Various classes implementing these patterns

---

### Q8: How did you make your design extensible for future requirements?

**Answer:**

"Several design choices support extensibility:

1. **Interface-based design** - Model, view, and controller all use interfaces. You could swap implementations without changing clients. For example, you could replace `CalendarModel` with a database-backed version without touching commands.

2. **Command pattern** - Adding a new command just requires creating a new `CommandMatcher` and adding it to the chain. No modification to existing code.

3. **Export strategy** - Adding a new export format (like JSON or XML) just requires implementing the export interface and adding a case to detect the file extension.

4. **Separate model/view/controller** - Can change the view (add mobile app, web interface) without touching model or business logic.

5. **Timezone abstraction** - Using Java's `ZoneId` means supporting new timezones is automatic. The IANA database is maintained externally.

6. **Features interface** - Can create different GUI implementations (JavaFX, web-based) that call the same Features methods.

However, we avoided over-engineering. We didn't add plugin systems, configuration files, or abstract factories for things that don't need them yet. We followed YAGNI - You Ain't Gonna Need It."

**Code Reference:** Interfaces throughout: `CalendarModelInterface`, `EventInterface`, `ViewInterface`, `GuiViewInterface`, `Features`, `CommandInterface`

---

### Q9: Walk me through what happens when a user creates an event in the GUI.

**Answer:**

"Here's the complete flow:

1. **User clicks 'Create Event' button in GuiView** - This is a JButton with an ActionListener.

2. **View shows CreateEventDialog** - A JDialog that collects: subject, date, start/end times, location, description, privacy status.

3. **User fills form and clicks 'Create'** - Dialog validates input (non-empty subject, valid date/time formats, end > start).

4. **Dialog calls features.createEvent()** - Passes validated parameters to the controller. No model interaction happens in the view.

5. **GuiController.createEvent() executes** - Gets current calendar model, creates Event object with UUID, calls `model.createEvent()`.

6. **Model validates and stores event** - Checks for duplicates via HashSet, returns true/false.

7. **Controller updates view** - Calls `view.refreshMonthView()` to show the new event, `view.showMessage()` to confirm success, or `view.showError()` if duplicate.

8. **View redraws calendar grid** - Days with events show in bold blue. User sees the change immediately.

The key is that the view never touches the model directly. All mutations go through the controller. The view is just dumb rendering plus event collection."

**Code Reference:** `GuiView.java` (button listeners), `CreateEventDialog.java`, `GuiController.java:79-95` (createEvent method)

---

### Q10: How do you test the GUI without running it manually?

**Answer:**

"We don't test Swing components directly - that's brittle and slow. Instead, we test the `GuiController` with a mock view.

Here's how:

1. Create a mock class implementing `GuiViewInterface`
2. Mock methods track what was called (e.g., `showError()` called with what message?)
3. Test by calling Features methods and verifying mock interactions

For example, to test creating a duplicate event:

```java
MockGuiView mockView = new MockGuiView();
GuiController controller = new GuiController(manager, mockView);

controller.createEvent("Meeting", start, end, null, null, false);
// First create succeeds
assertEquals("Event created", mockView.lastMessage);

controller.createEvent("Meeting", start, end, null, null, false);
// Duplicate fails
assertTrue(mockView.lastError.contains("duplicate"));
```

This tests all business logic without needing actual GUI components. It's fast (runs in milliseconds), deterministic (no flaky UI interactions), and focused on behavior.

We also have integration tests that verify the model and controller work together correctly."

**Code Reference:** `GuiControllerTest.java` (if it exists in tests)

---

## Code Location Cheat Sheet

### Core Model Classes
- **Event** - `src/main/java/calendar/model/Event.java:19`
- **EventInterface** - `src/main/java/calendar/model/EventInterface.java`
- **CalendarModel** - `src/main/java/calendar/model/CalendarModel.java:27`
- **CalendarModelInterface** - `src/main/java/calendar/model/CalendarModelInterface.java:24`
- **EventSeries** - `src/main/java/calendar/model/EventSeries.java:21`
- **Calendar** - `src/main/java/calendar/model/Calendar.java`
- **CalendarManager** - `src/main/java/calendar/model/CalendarManager.java:24`

### Controllers
- **Controller** (text-based) - `src/main/java/calendar/controller/Controller.java:35`
- **GuiController** - `src/main/java/calendar/controller/GuiController.java:24`
- **Features** (interface) - `src/main/java/calendar/controller/Features.java`

### Views
- **ViewInterface** - `src/main/java/calendar/view/ViewInterface.java`
- **ConsoleView** - `src/main/java/calendar/view/ConsoleView.java`
- **GuiViewInterface** - `src/main/java/calendar/view/GuiViewInterface.java`
- **GuiView** - `src/main/java/calendar/view/GuiView.java`

### Command Pattern
- **CommandInterface** - `src/main/java/calendar/command/CommandInterface.java`
- **CommandParser** - `src/main/java/calendar/command/CommandParser.java:35`
- **CommandMatcher** - `src/main/java/calendar/command/CommandMatcher.java`
- **Example: CreateEventCommand** - `src/main/java/calendar/command/CreateEventCommand.java`

### Entry Point
- **CalendarRunner** - `src/main/java/CalendarRunner.java:22`

### Key Algorithms
- **Event Equality** - `Event.java:157-169` (equals), `Event.java:184-186` (hashCode)
- **Duplicate Detection** - `CalendarModel.java:44-54` (createEvent)
- **Series Generation** - `CalendarModel.java` (generateOccurrences method)
- **Command Parsing** - `CommandParser.java:74-90` (parse method)

---

## Design Evolution Timeline

### HW4 (Single Calendar, Text-only)
**Added:**
- CalendarModel with HashSet storage
- Event and EventSeries classes
- Separate InteractiveController and HeadlessController
- CommandParser with if-else chain
- ConsoleView for output
- CSV export

**Architecture:**
```
Model: CalendarModel → Set<Event>
View: ConsoleView
Controller: InteractiveController, HeadlessController
Commands: CreateEvent, EditEvent, PrintEvents, Export, etc.
```

---

### HW5 (Multiple Calendars, Timezones)
**Added:**
- CalendarManager to manage multiple calendars
- Calendar wrapper with timezone
- Copy commands (copyEvent, copyEventsOnDay, copyEventsRange)
- iCal export (in addition to CSV)
- Timezone conversion utilities

**Changed:**
- Unified InteractiveController + HeadlessController → Controller with Readable
- Refactored CommandParser to use Chain of Responsibility (CommandMatcher classes)
- Commands now work with CalendarManager instead of CalendarModelInterface directly

**Architecture:**
```
Model: CalendarManager → Map<String, Calendar> → CalendarModel → Set<Event>
View: ConsoleView
Controller: Controller (unified)
Commands: +CreateCalendar, +UseCalendar, +EditCalendar, +Copy commands
```

---

### HW6 (GUI)
**Added:**
- GuiView (JFrame with Swing components)
- GuiController implementing Features interface
- Dialog classes (CreateEventDialog, CreateCalendarDialog, EditEventDialog)
- Month view and Week view
- Calendar color-coding
- NavigationPanel, CalendarGridPanel, EventDisplayPanel

**Changed:**
- None to existing text-based functionality (backward compatible)

**Architecture:**
```
Model: CalendarManager → Map<String, Calendar> → CalendarModel → Set<Event>
Views: ConsoleView (text), GuiView (Swing)
Controllers: Controller (text), GuiController (GUI)
Entry: CalendarRunner detects mode and launches appropriate view/controller
```

---

## SOLID Principles Applied

### Single Responsibility Principle (SRP)
- ✅ **CalendarModel** - Only manages events, doesn't handle I/O or parsing
- ✅ **CalendarManager** - Only manages calendars, doesn't handle events
- ✅ **CommandParser** - Only parses, doesn't execute
- ✅ **Commands** - Each command class handles one command type
- ✅ **Views** - Only display, don't contain business logic
- ✅ **Controllers** - Mediate between view and model, don't duplicate model logic

**Example:** Separating CalendarManager from CalendarModel keeps responsibilities distinct.

---

### Open/Closed Principle (OCP)
- ✅ **Command Pattern** - Add new commands without modifying parser
- ✅ **Export Strategies** - Add new export formats without modifying core logic
- ✅ **Interface-based design** - Can swap implementations without changing clients

**Example:** Adding a new command requires creating a new CommandMatcher and adding it to the chain. CommandParser itself doesn't change.

---

### Liskov Substitution Principle (LSP)
- ✅ **EventInterface** - Event can be substituted wherever EventInterface is used
- ✅ **ViewInterface** - ConsoleView can be substituted for any ViewInterface
- ✅ **GuiViewInterface** - Any GUI implementation can substitute for GuiViewInterface

**Example:** Tests use mock views that implement ViewInterface - code doesn't know the difference.

---

### Interface Segregation Principle (ISP)
- ✅ **Separate interfaces** - ViewInterface vs GuiViewInterface (different clients, different needs)
- ✅ **Features interface** - Only GUI-relevant operations, not text-based operations
- ✅ **CalendarModelInterface** - Only event operations, not calendar management

**Example:** GuiViewInterface has methods like `displayMonth()` that don't make sense for ConsoleView, so they're in a separate interface.

---

### Dependency Inversion Principle (DIP)
- ✅ **Controller depends on interfaces** - Depends on ViewInterface, not ConsoleView
- ✅ **Commands depend on interfaces** - Depend on CalendarModelInterface, not CalendarModel
- ✅ **Dependency Injection** - Dependencies passed via constructor, not created internally

**Example:** Controller constructor takes ViewInterface, so it can work with any view implementation (console, GUI, mock for testing).

---

## Last-Minute Checklist

### Before Code Walk
- [ ] Review Misc.md design changes table
- [ ] Open codebase in IDE, ready to navigate
- [ ] Practice 3-minute overview with timer (must be under 3:00)
- [ ] Skim Event.java, CalendarModel.java, Controller.java, GuiController.java
- [ ] Review Features interface methods
- [ ] Know how CalendarManager works
- [ ] Understand series splitting behavior
- [ ] Read the series editing example in HW4 README (lines 45-57)

### During Overview
- [ ] Speak clearly and confidently
- [ ] Don't read code line-by-line
- [ ] Use domain language (events, calendars, series) not implementation terms
- [ ] Reference specific classes by name
- [ ] Stay under 3 minutes (you'll be timed!)

### During Q&A
- [ ] Listen carefully to the full question before answering
- [ ] Reference specific code locations
- [ ] Acknowledge trade-offs when discussing decisions
- [ ] Connect choices to SOLID principles when relevant
- [ ] If uncertain, explain your reasoning process
- [ ] Don't apologize for design choices - defend them

### Things NOT to Say
- ❌ "We just followed the assignment"
- ❌ "I don't know, my partner wrote that part"
- ❌ "We probably should have done it differently"
- ❌ "I'm not sure why we did it that way"
- ❌ *Apologizing for trade-offs*

### Things TO Say
- ✅ "We chose X because Y, with the trade-off that Z"
- ✅ "Let me show you that code" (navigate to file)
- ✅ "That aligns with [SOLID principle]"
- ✅ "The alternative would be X, but we preferred Y because..."
- ✅ "This follows the pattern from lecture/the textbook"

---

## Quick Q&A Practice

**Q:** Why HashSet?
**A:** O(1) duplicate detection using equals/hashCode. Worth the memory overhead.

**Q:** Why immutable?
**A:** Thread-safety, predictability, no side effects. Memory is cheap.

**Q:** Why Command pattern?
**A:** Decouples parsing from execution. Extensible via Open/Closed Principle.

**Q:** Why Features interface?
**A:** GUI is event-driven, Readable doesn't work. Zero Swing dependencies in controller.

**Q:** Why CalendarManager?
**A:** Single Responsibility - calendar management separate from event management.

**Q:** How to add new command?
**A:** Create CommandMatcher subclass, add to chain in CommandParser constructor. Parser doesn't change.

**Q:** How do you test GUI?
**A:** Mock GuiViewInterface, test controller logic without Swing components.

**Q:** Why did series split?
**A:** Series events must have same start time. Changing time violates invariant.

**Q:** Main MVC boundaries?
**A:** Model has no view/controller knowledge. View has no model knowledge. Controller mediates.

**Q:** Best design decision?
**A:** Controller unification. Eliminated 90% duplication between interactive/headless.

---

## Final Tips

1. **Confidence is key** - You made thoughtful decisions. Own them.
2. **Trade-offs are normal** - Every design has them. Acknowledge and explain.
3. **It's a conversation** - Staff want to see you think, not recite memorized answers.
4. **Navigate your code** - Know where things are. Show, don't just tell.
5. **Relate to course concepts** - Connect to SOLID, design patterns, MVC.
6. **Time management** - If a question is taking too long, offer to continue after if needed.
7. **Breathe** - You know this code. You wrote it. You've got this.

**Remember:** The point isn't perfection. It's demonstrating that you understand your design choices and can defend them with clear reasoning.

Good luck! 🍀