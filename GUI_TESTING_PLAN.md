# GUI Testing Plan

## Overview

This document outlines a comprehensive testing plan for the Calendar Application's Graphical User Interface (GUI). The plan covers functional testing, UI/UX testing, error handling, and integration testing.

---

## Test Environment Setup

### Prerequisites
- Java JDK 11 or higher
- Built JAR file: `build/libs/calendar.jar`
- Test data files in `res/` directory

### Launching GUI for Testing
```bash
# Standard GUI mode
java -jar build/libs/calendar.jar

# Headless-to-GUI mode (with pre-loaded data)
java -jar build/libs/calendar.jar --mode headless-gui res/commands.txt
```

---

## 1. Calendar Management Testing

### 1.1 Create Calendar
**Test Cases:**
- [ ] **TC-CAL-001**: Create calendar with valid name and timezone
  - Steps: Click "New Calendar" → Enter "Work" → Select "America/New_York" → Click "Create"
  - Expected: Calendar appears in dropdown, becomes active, timezone displayed correctly

- [ ] **TC-CAL-002**: Create calendar with duplicate name
  - Steps: Create "Work" calendar → Try to create another "Work" calendar
  - Expected: Error message "Calendar already exists"

- [ ] **TC-CAL-003**: Create calendar with empty name
  - Steps: Click "New Calendar" → Leave name empty → Click "Create"
  - Expected: Error message "Calendar name cannot be empty"

- [ ] **TC-CAL-004**: Create calendar with special characters in name
  - Steps: Create calendar with name "Work & Personal"
  - Expected: Calendar created successfully, name displayed correctly

- [ ] **TC-CAL-005**: Create multiple calendars with different timezones
  - Steps: Create "Work" (EST), "Personal" (PST), "Travel" (UTC)
  - Expected: All calendars appear in dropdown, timezone conversion works correctly

### 1.2 Switch Calendar
**Test Cases:**
- [ ] **TC-CAL-006**: Switch between calendars
  - Steps: Create 2 calendars → Use dropdown to switch
  - Expected: Events update, current calendar name displayed, month view refreshes

- [ ] **TC-CAL-007**: Verify calendar persistence during session
  - Steps: Create calendar → Create events → Switch away → Switch back
  - Expected: Events still present, calendar state maintained

### 1.3 Default Calendar
**Test Cases:**
- [ ] **TC-CAL-008**: Default calendar created on first launch
  - Steps: Launch GUI without any commands
  - Expected: "My Calendar" appears in dropdown, system timezone used

---

## 2. Month View Navigation Testing

### 2.1 Month Navigation
**Test Cases:**
- [ ] **TC-NAV-001**: Navigate to previous month
  - Steps: Click "< Previous" button
  - Expected: Month decrements, calendar grid updates, events for new month displayed

- [ ] **TC-NAV-002**: Navigate to next month
  - Steps: Click "Next >" button
  - Expected: Month increments, calendar grid updates

- [ ] **TC-NAV-003**: Navigate to today
  - Steps: Navigate to different month → Click "Today"
  - Expected: Returns to current month, today's date selected

- [ ] **TC-NAV-004**: Navigate across year boundary
  - Steps: Start in December → Click "Next >"
  - Expected: January of next year displayed, year updates correctly

- [ ] **TC-NAV-005**: Navigate multiple months quickly
  - Steps: Click "Next >" 6 times rapidly
  - Expected: UI remains responsive, all months display correctly

### 2.2 Day Selection
**Test Cases:**
- [ ] **TC-NAV-006**: Select a day with events
  - Steps: Click a day that has events
  - Expected: Day highlighted, events displayed in right panel

- [ ] **TC-NAV-007**: Select a day without events
  - Steps: Click an empty day
  - Expected: Day highlighted, "No events on this day" message shown

- [ ] **TC-NAV-008**: Select different days rapidly
  - Steps: Click multiple days in quick succession
  - Expected: Selection updates correctly, events refresh for each day

---

## 3. Event Creation Testing

### 3.1 Single Event Creation
**Test Cases:**
- [ ] **TC-EVT-001**: Create event with all fields
  - Steps: Select day → "Create Event" → "Single Event" → Fill all fields → "Create Event"
  - Expected: Event appears on calendar, shows in event list

- [ ] **TC-EVT-002**: Create event with minimal fields (subject, date, times only)
  - Steps: Create event with only required fields
  - Expected: Event created successfully

- [ ] **TC-EVT-003**: Create all-day event
  - Steps: Check "All-day event" checkbox → Create
  - Expected: Event shows as 08:00-17:00, marked appropriately

- [ ] **TC-EVT-004**: Create private event
  - Steps: Check "Private event" checkbox → Create
  - Expected: Event shows "(Private)" label in event list

- [ ] **TC-EVT-005**: Create event with invalid date format
  - Steps: Enter date as "1/20/2025" instead of "2025-01-20"
  - Expected: Error message "Invalid date or time format"

- [ ] **TC-EVT-006**: Create event with invalid time format
  - Steps: Enter time as "9:00 AM" instead of "09:00"
  - Expected: Error message "Invalid date or time format"

- [ ] **TC-EVT-007**: Create event with end time before start time
  - Steps: Start: 10:00, End: 09:00
  - Expected: Error message "End time must be after start time"

- [ ] **TC-EVT-008**: Create event with empty subject
  - Steps: Leave subject field empty → Create
  - Expected: Error message "Subject is required"

- [ ] **TC-EVT-009**: Create duplicate event
  - Steps: Create event → Try to create same event again (same subject, date, times)
  - Expected: Error message "Event already exists"

- [ ] **TC-EVT-010**: Create event on different months
  - Steps: Navigate to different month → Create event
  - Expected: Event created, appears when navigating back to that month

### 3.2 Recurring Event Series Creation
**Test Cases:**
- [ ] **TC-SER-001**: Create series with "FOR N occurrences"
  - Steps: "Create Event" → "Recurring Series" → Fill fields → Check weekdays → "After # occurrences": 10
  - Expected: 10 events created on selected weekdays

- [ ] **TC-SER-002**: Create series with "UNTIL date"
  - Steps: Create series → "On date": 2025-03-31
  - Expected: Events created until end date on selected weekdays

- [ ] **TC-SER-003**: Create series with multiple weekdays
  - Steps: Check Mon, Wed, Fri → Create series
  - Expected: Events created only on Mon/Wed/Fri

- [ ] **TC-SER-004**: Create series without selecting weekdays
  - Steps: Create series without checking any weekday
  - Expected: Error message "Please select at least one weekday"

- [ ] **TC-SER-005**: Create series with start date not on selected weekday
  - Steps: Start on Tuesday, select only Monday
  - Expected: First event occurs on next Monday

- [ ] **TC-SER-006**: Create series with invalid end date (before start)
  - Steps: Start: 2025-03-01, End: 2025-02-28
  - Expected: Error message about invalid date range

- [ ] **TC-SER-007**: Create series with zero occurrences
  - Steps: Enter 0 for occurrences
  - Expected: Error message about invalid occurrences

- [ ] **TC-SER-008**: Verify series events appear on calendar
  - Steps: Create series → Navigate through months
  - Expected: All series events visible on correct days

---

## 4. Event Editing Testing

### 4.1 Edit Single Event
**Test Cases:**
- [ ] **TC-EDT-001**: Edit event subject
  - Steps: Select day with event → "Edit Event" → Change subject → "Save Changes"
  - Expected: Event name updated, displayed correctly

- [ ] **TC-EDT-002**: Edit event times
  - Steps: Edit event → Change start/end times → Save
  - Expected: Times updated, event appears at new time

- [ ] **TC-EDT-003**: Edit event location
  - Steps: Edit event → Change location → Save
  - Expected: Location updated in event display

- [ ] **TC-EDT-004**: Edit event description
  - Steps: Edit event → Change description → Save
  - Expected: Description updated

- [ ] **TC-EDT-005**: Edit event privacy status
  - Steps: Edit public event → Check "Private" → Save
  - Expected: Event shows "(Private)" label

- [ ] **TC-EDT-006**: Edit event on day with multiple events
  - Steps: Day with 3 events → "Edit Event" → Select event from list → Edit
  - Expected: Correct event selected, only that event modified

- [ ] **TC-EDT-007**: Edit event on day with no events
  - Steps: Select empty day → Click "Edit Event"
  - Expected: Error message "No events to edit"

- [ ] **TC-EDT-008**: Cancel edit dialog
  - Steps: Open edit dialog → Click "Cancel" or close
  - Expected: No changes saved, dialog closes

### 4.2 Edit Series Events
**Test Cases:**
- [ ] **TC-SED-001**: Edit "This event only"
  - Steps: Select series event → Edit → "This event only" → Change time → Save
  - Expected: Only selected event changed, split from series

- [ ] **TC-SED-002**: Edit "This and future events"
  - Steps: Select series event → Edit → "This and future" → Change subject → Save
  - Expected: All future occurrences updated, past events unchanged

- [ ] **TC-SED-003**: Edit "All events in series"
  - Steps: Select series event → Edit → "All events in series" → Change location → Save
  - Expected: All occurrences (past and future) updated

- [ ] **TC-SED-004**: Verify series split behavior
  - Steps: Edit series event "This event only" → Verify other events unchanged
  - Expected: Series continues for other events, edited event independent

---

## 5. Visual Indicators Testing

### 5.1 Calendar Grid Display
**Test Cases:**
- [ ] **TC-VIS-001**: Days with events shown in bold
  - Steps: Create events on various days
  - Expected: Days with events display in bold text

- [ ] **TC-VIS-002**: Today's date shown in red
  - Steps: View current month
  - Expected: Today's date displayed in red text

- [ ] **TC-VIS-003**: Selected day highlighted
  - Steps: Click a day
  - Expected: Day has blue background and border

- [ ] **TC-VIS-004**: Today with events and selected
  - Steps: Select today (which has events)
  - Expected: Red text (today) + bold (events) + blue background (selected)

- [ ] **TC-VIS-005**: Month header displays correctly
  - Steps: Navigate through months
  - Expected: Month name and year displayed correctly (e.g., "January 2025")

- [ ] **TC-VIS-006**: Weekday headers displayed
  - Steps: View any month
  - Expected: Sun, Mon, Tue, Wed, Thu, Fri, Sat headers visible

- [ ] **TC-VIS-007**: Padding cells for month boundaries
  - Steps: View months that don't start on Sunday
  - Expected: Empty cells shown in light gray

### 5.2 Event List Display
**Test Cases:**
- [ ] **TC-VIS-008**: Events listed with numbers
  - Steps: Day with multiple events
  - Expected: Events numbered [1], [2], [3], etc.

- [ ] **TC-VIS-009**: Event times displayed correctly
  - Steps: View events with various times
  - Expected: Times in HH:mm format (24-hour)

- [ ] **TC-VIS-010**: Private events marked
  - Steps: View day with private events
  - Expected: "(Private)" label shown

- [ ] **TC-VIS-011**: Location displayed when present
  - Steps: View event with location
  - Expected: "Location: [location]" shown

- [ ] **TC-VIS-012**: Description displayed when present
  - Steps: View event with description
  - Expected: Description text shown

---

## 6. Error Handling Testing

### 6.1 Input Validation
**Test Cases:**
- [ ] **TC-ERR-001**: Invalid date format
  - Steps: Enter "1/20/25" in date field
  - Expected: Clear error message, field highlighted

- [ ] **TC-ERR-002**: Invalid time format
  - Steps: Enter "9am" in time field
  - Expected: Clear error message

- [ ] **TC-ERR-003**: Missing required fields
  - Steps: Try to create event without subject
  - Expected: Error message, creation prevented

- [ ] **TC-ERR-004**: Invalid time range
  - Steps: End time before start time
  - Expected: Error message explaining the issue

- [ ] **TC-ERR-005**: Calendar name validation
  - Steps: Try to create calendar with empty name
  - Expected: Error message, creation prevented

### 6.2 Business Logic Errors
**Test Cases:**
- [ ] **TC-ERR-006**: Duplicate calendar name
  - Steps: Create calendar → Try to create with same name
  - Expected: Error message "Calendar already exists"

- [ ] **TC-ERR-007**: Duplicate event
  - Steps: Create event → Try to create identical event
  - Expected: Error message "Event already exists"

- [ ] **TC-ERR-008**: Edit without selection
  - Steps: Click "Edit Event" without selecting day with events
  - Expected: Error message "No events to edit"

- [ ] **TC-ERR-009**: Series validation errors
  - Steps: Create series without weekdays
  - Expected: Appropriate error message

---

## 7. Integration Testing

### 7.1 Headless-to-GUI Integration
**Test Cases:**
- [ ] **TC-INT-001**: Load data from headless mode
  - Steps: `java -jar calendar.jar --mode headless-gui res/commands.txt`
  - Expected: GUI opens with calendars and events from commands file

- [ ] **TC-INT-002**: Verify loaded calendars
  - Steps: After headless-gui, check calendar dropdown
  - Expected: All calendars from commands file present

- [ ] **TC-INT-003**: Verify loaded events
  - Steps: After headless-gui, navigate to dates with events
  - Expected: All events from commands file visible

- [ ] **TC-INT-004**: Create new events after loading
  - Steps: After headless-gui, create new event via GUI
  - Expected: New event appears, doesn't conflict with loaded events

### 7.2 Multi-Calendar Integration
**Test Cases:**
- [ ] **TC-INT-005**: Events isolated per calendar
  - Steps: Create events in "Work" → Switch to "Personal"
  - Expected: Personal calendar empty, Work events not visible

- [ ] **TC-INT-006**: Timezone conversion
  - Steps: Create event in EST calendar → Switch to PST calendar
  - Expected: Times displayed correctly for each timezone

---

## 8. UI/UX Testing

### 8.1 Layout and Appearance
**Test Cases:**
- [ ] **TC-UX-001**: Window resizing
  - Steps: Resize GUI window
  - Expected: Components scale appropriately, no clipping

- [ ] **TC-UX-002**: Button sizes appropriate
  - Steps: View all buttons
  - Expected: Buttons not oversized, text readable

- [ ] **TC-UX-003**: Text field sizes appropriate
  - Steps: View all input fields
  - Expected: Fields appropriately sized, not too large/small

- [ ] **TC-UX-004**: Color scheme consistent
  - Steps: View entire interface
  - Expected: Colors used consistently (blue for selection, red for today, etc.)

- [ ] **TC-UX-005**: Spacing and alignment
  - Steps: View interface
  - Expected: Components properly spaced, aligned, not haphazard

### 8.2 User-Friendliness
**Test Cases:**
- [ ] **TC-UX-006**: Date pre-filled in create dialog
  - Steps: Select day → "Create Event"
  - Expected: Date field pre-filled with selected day

- [ ] **TC-UX-007**: Clear error messages
  - Steps: Trigger various errors
  - Expected: Messages explain what went wrong and how to fix

- [ ] **TC-UX-008**: Intuitive button labels
  - Steps: Review all button text
  - Expected: Labels clearly describe function

- [ ] **TC-UX-009**: Calendar identification
  - Steps: View interface with multiple calendars
  - Expected: Current calendar clearly indicated (name + timezone shown)

---

## 9. Performance Testing

### 9.1 Responsiveness
**Test Cases:**
- [ ] **TC-PERF-001**: Rapid navigation
  - Steps: Click "Next >" rapidly 10 times
  - Expected: UI remains responsive, no freezing

- [ ] **TC-PERF-002**: Large number of events
  - Steps: Create 100+ events in a month
  - Expected: Calendar displays correctly, selection works

- [ ] **TC-PERF-003**: Long series
  - Steps: Create series with 100+ occurrences
  - Expected: All events created, calendar navigable

- [ ] **TC-PERF-004**: Multiple calendars
  - Steps: Create 10+ calendars
  - Expected: Dropdown works, switching responsive

---

## 10. Edge Cases and Boundary Testing

### 10.1 Date Boundaries
**Test Cases:**
- [ ] **TC-EDG-001**: Events on month boundaries
  - Steps: Create event on last day of month → Navigate to next month
  - Expected: Event still visible when navigating back

- [ ] **TC-EDG-002**: Events on year boundaries
  - Steps: Create event on Dec 31 → Navigate to Jan 1
  - Expected: Navigation works correctly across years

- [ ] **TC-EDG-003**: Leap year handling
  - Steps: Create event on Feb 29 in leap year
  - Expected: Event displays correctly

### 10.2 Time Boundaries
**Test Cases:**
- [ ] **TC-EDG-004**: Events at midnight (00:00)
  - Steps: Create event from 23:00 to 00:00
  - Expected: Handled correctly (end time after start)

- [ ] **TC-EDG-005**: Events spanning midnight
  - Steps: Create event from 23:00 to 01:00
  - Expected: Event created, displays correctly

- [ ] **TC-EDG-006**: Very short events (1 minute)
  - Steps: Create event 09:00 to 09:01
  - Expected: Event created successfully

---

## 11. Regression Testing

### 11.1 Previously Fixed Issues
**Test Cases:**
- [ ] **TC-REG-001**: Verify all previously reported bugs remain fixed
  - Steps: Test each bug fix from issue tracker
  - Expected: Bugs do not reappear

---

## 12. Test Execution Checklist

### Pre-Testing Setup
- [ ] Build application: `./gradlew jar`
- [ ] Verify JAR exists: `build/libs/calendar.jar`
- [ ] Prepare test data files
- [ ] Clear any previous test data

### Test Execution
- [ ] Execute each test case systematically
- [ ] Document results (Pass/Fail/Blocked)
- [ ] Capture screenshots for failures
- [ ] Note any unexpected behavior

### Post-Testing
- [ ] Compile test results
- [ ] Document bugs found
- [ ] Create bug reports with steps to reproduce
- [ ] Verify fixes for any bugs found

---

## Test Data Files

### Recommended Test Commands File
Create `res/test-commands.txt`:
```
create calendar --name Work --timezone America/New_York
use calendar --name Work
create event "Team Meeting" from 2025-06-15T14:00 to 2025-06-15T15:30
create event "Daily Standup" from 2025-06-01T09:00 to 2025-06-01T09:15 repeats MTWRF for 10 times
create event "All Day Event" on 2025-06-20
create calendar --name Personal --timezone America/Los_Angeles
use calendar --name Personal
create event "Gym" from 2025-06-10T18:00 to 2025-06-10T19:00 repeats MWF for 20 times
exit
```

---

## Bug Reporting Template

When a bug is found, document it with:

1. **Test Case ID**: TC-XXX-XXX
2. **Severity**: Critical / High / Medium / Low
3. **Steps to Reproduce**: Detailed steps
4. **Expected Result**: What should happen
5. **Actual Result**: What actually happens
6. **Screenshots**: If applicable
7. **Environment**: OS, Java version, etc.

---

## Notes

- All tests should be performed by someone unfamiliar with the codebase
- Tests should be performed without reading technical documentation
- Focus on user-friendliness and intuitive operation
- Document any confusing or unclear UI elements
- Note any missing error messages or unclear feedback


