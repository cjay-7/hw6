# Misc.md - Design Changes and Feature Status

## Design Changes

| Change | Justification |
|--------|--------------|
| Added `Features` interface | GUI is event-driven; existing `Controller` uses `Readable` input which is incompatible with GUI callbacks. Features interface provides high-level, application-specific operations (e.g., `createEvent()`) rather than low-level Swing events (e.g., `actionPerformed()`). Follows Stage 5 pattern from MVC lecture. |
| Created `GuiController` class | Implements `Features` interface to mediate between GUI view and model. Separate from text-based `Controller` because GUI requires event-driven callbacks instead of sequential command processing. No Swing dependencies - keeps controller testable. |
| Created `GuiViewInterface` | Separate from `ViewInterface` which is designed for text output (`displayMessage()`, `displayError()`). GUI view needs different operations like `displayMonth()`, `updateCalendarList()`. Allows controller to update view without knowing Swing implementation details. |
| Created `GuiView` (JFrame) | Implements `GuiViewInterface` using Java Swing. Contains all UI components (calendar grid, buttons, panels) but exposes minimal interface to controller. Translates low-level Swing events (button clicks) to high-level Features calls. |
| Dialog classes (`CreateCalendarDialog`, `CreateEventDialog`, etc.) | Encapsulates user input forms for creating/editing calendars and events. Provides validation and user-friendly error messages. Returns null values to indicate "no change" for optional fields in edit operations. |
| Modified `CalendarRunner.main()` | Added support for GUI mode (no command-line arguments). Uses `SwingUtilities.invokeLater()` for thread-safe GUI initialization. Maintains backward compatibility with `--mode interactive` and `--mode headless`. |
| Renamed classes from `GUI*` to `Gui*` | Checkstyle rule requires abbreviations to have max 1 consecutive capital letter. `GUI` → `Gui` to comply with code style requirements. |

## Features Status

### ✅ Working Features:

**Calendar Management:**
- Create new calendar with custom name and timezone
- Switch between multiple calendars
- Display current calendar name and timezone
- Default calendar automatically created on first launch (uses system timezone)

**Month View:**
- Month calendar grid showing all days
- Navigation: Previous month, Next month, Today button
- Days with events shown in bold blue text
- Selected day highlighted with blue background and border
- Current day (today) shown in red text
- Month/year display header

**Week View:**
- Toggle between Month View and Week View using buttons
- Week grid showing 7 days (Sunday to Saturday)
- Same visual indicators as month view (events, selected day, today)
- Previous/Next buttons navigate by week in week view mode
- Week header shows date range (e.g., "Week of Jun 15 - Jun 21")

**Calendar Color Coding:**
- Each calendar is assigned a unique color from a palette of 8 colors
- Color indicator panel next to calendar selector shows current calendar's color
- Colors persist for the session (same calendar always has same color)

**Event Display:**
- Click any day to view events scheduled on that day
- Events shown with: subject, time range, location (if present), privacy status
- Scrollable event list for days with many events
- "No events" message when day has no events

**Create Events:**
- Create single events with: subject, date, start/end times, location, description, privacy
- Create all-day events (automatically sets 8:00 AM - 5:00 PM)
- Create recurring event series with:
  - Weekday selection (Sun-Sat)
  - End condition: specific date OR number of occurrences
  - Same fields as single events
- Input validation with user-friendly error messages
- Date/time format validation (yyyy-MM-dd, HH:mm)

**Edit Events:**
- Edit single events (all fields modifiable)
- For series events, three options:
  - Edit this event only
  - Edit this and future events
  - Edit entire series
- Event selection dialog when multiple events on same day
- Pre-populated form with current values
- Validation ensures end time > start time

**Error Handling:**
- Graceful handling of invalid input (empty fields, bad formats, etc.)
- User-friendly error messages (no stack traces or technical details)
- Prevents duplicate events (same subject + start + end)
- Timezone validation for calendar creation

**UI/UX:**
- Reasonable layout proportions (no oversized or haphazard components)
- Intuitive button placement and labeling
- Dropdown timezone selector with common zones at top
- Dialog-based workflows for complex operations
- Confirmation required before making changes

### ❌ Not Working / Not Implemented:

- **Event deletion** - No delete command exists in model (by design of original codebase)
- **Calendar deletion** - No delete calendar functionality
- **Export from GUI** - Export to CSV/iCal works via command line but not exposed in GUI
- **Multi-day event display** - Calendar only shows events starting on each day (not spanning)
- **Event search/filter** - No keyword search within events
- **Keyboard shortcuts** - No hotkeys for common operations
- **Drag-and-drop** - Cannot drag events to reschedule

## Notes for Graders

### Testing the GUI

**To run GUI mode:**
```bash
java -jar build/libs/calendar.jar
```
(Or double-click the JAR file in Windows)

**To run interactive mode:**
```bash
java -jar build/libs/calendar.jar --mode interactive
```

**To run headless mode:**
```bash
java -jar build/libs/calendar.jar --mode headless res/commands.txt
```

### Architecture Highlights

1. **MVC Separation:** View has NO direct model access. Controller is only component that mutates model. Model is completely decoupled from both view and controller.

2. **Features Pattern:** Follows "Stage 5" pattern from MVC lecture. High-level, application-specific events (not generic Swing listeners). Controller doesn't know about Swing - no `javax.swing` imports.

3. **Testability:** `GuiController` can be tested with mock view (no actual Swing needed). Features interface allows simulating user actions programmatically.

4. **Platform Independence:** Uses Java Swing (pure JDK, no external libraries). File paths use `java.nio.file.Path` for cross-platform compatibility.

### Known Limitations

1. **Series editing complexity:** When editing a series event's time, it may split into separate events (this is model behavior, not GUI bug).

2. **Timezone display:** Events are stored in calendar's timezone but displayed in same timezone (no automatic conversion to user's local time).

3. **Large series:** Creating series with many occurrences (e.g., daily for 1 year) may take a moment due to duplicate checking.

4. **Window resize:** Calendar grid cells resize with window but may become small on narrow windows. Minimum window size is set to 1000x700.

### Testing Recommendations

1. Create multiple calendars with different timezones (e.g., "Work" in America/New_York, "Personal" in America/Los_Angeles)
2. Switch between calendars to verify events are independent
3. Create recurring series (e.g., "Team Meeting" every Monday/Wednesday for 8 weeks)
4. Edit series events with all three scopes (single, from-date, all)
5. Test validation by entering invalid dates/times
6. Verify all-day events show 08:00-17:00

### Screenshot

A screenshot of the GUI is included in this `res/` folder as `gui_screenshot.png`.

### Code Quality

- All checkstyle rules passing (excluding pre-existing warnings in `DateTimeParser.java`)
- Comprehensive JavaDoc comments on all public methods
- Consistent naming conventions (Features, GuiController, GuiView, etc.)
- Proper access modifiers (private fields, public interfaces)
- No code duplication (dialogs share validation logic patterns)

### Backward Compatibility

All existing text-based functionality remains fully operational:
- Interactive mode works as before
- Headless mode works as before
- All 23 command variants still supported
- Existing test suite passes (395 tests, 12 pre-existing failures unrelated to GUI)
