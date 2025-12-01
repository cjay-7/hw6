# USEME.md - Calendar Application User Guide

## Running the Application

### Prerequisites
- Java JDK 11 or higher installed
- The calendar JAR file built (located in `build/libs/`)

### Building the Application

From the project root directory:

```bash
./gradlew jar
```

This creates `build/libs/calendar.jar`

### Running in Different Modes

#### 1. GUI Mode (Default)

**Command:**
```bash
java -jar build/libs/calendar.jar
```

Or simply double-click `build/libs/calendar.jar` in your file explorer.

**Description:** Launches the graphical user interface with a month view calendar, event management, and multiple calendar support.

---

#### 2. Interactive Mode

**Command:**
```bash
java -jar build/libs/calendar.jar --mode interactive
```

**Description:** Runs the application in text-based interactive mode where you can type commands one at a time and see results immediately.

**Example Session:**
```
> create calendar "Work" "America/New_York"
Calendar created: Work

> use calendar "Work"
Now using calendar: Work

> create event "Meeting" on 2025-01-20 from 09:00 to 10:00 public
Event created successfully

> print events on 2025-01-20
Events on 2025-01-20:
  Meeting (09:00 - 10:00)

> exit
```

---

#### 3. Headless Mode

**Command:**
```bash
java -jar build/libs/calendar.jar --mode headless res/commands.txt
```

**Description:** Executes commands from a text file and exits. Useful for batch processing or automation.

**Example:**
```bash
# Run the provided example commands
java -jar build/libs/calendar.jar --mode headless res/commands.txt

# Run invalid commands to see error handling
java -jar build/libs/calendar.jar --mode headless res/invalid.txt
```

---

## GUI Features and How to Use Them

### 1. Creating a New Calendar

**Steps:**
1. Click the **"New Calendar"** button at the top of the window
2. In the dialog that appears:
   - Enter a unique calendar name (e.g., "Work", "Personal")
   - Select a timezone from the dropdown
   - Common timezones are listed at the top for convenience
3. Click **"Create"**
4. The new calendar becomes the active calendar automatically

**Notes:**
- Calendar names must be unique (case-insensitive)
- A default calendar is created automatically on first launch
- The current calendar and timezone are displayed below the selector

---

### 2. Switching Between Calendars

**Steps:**
1. Use the **calendar dropdown** at the top-left
2. Select the calendar you want to work with
3. The month view and events update automatically

**Visual Indicator:**
- The current calendar name and timezone appear below the dropdown
- Format: "Current: [Calendar Name] ([Timezone])"

---

### 3. Navigating Months

**Available Controls:**
- **"< Previous"** button: Go to previous month
- **"Next >"** button: Go to next month
- **"Today"** button: Jump to current month and select today's date

**Visual Feedback:**
- Current month and year displayed in bold at top of calendar grid
- Today's date shown in red text
- Selected date highlighted with blue background

---

### 4. Viewing Events on a Specific Day

**Steps:**
1. Click any day in the calendar grid
2. Events for that day appear in the right panel
3. The panel header shows the selected date

**Event Display Format:**
```
[1] Meeting Name
    09:00 - 10:00
    Location: Conference Room
    (Private)    ← shown only if event is private

[2] Another Event
    14:00 - 15:00
```

**Notes:**
- Numbers [1], [2], etc. identify events for editing
- All-day events show as 08:00 - 17:00
- "No events on this day" message if day is empty

---

### 5. Creating a Single Event

**Steps:**
1. Click **"Create Event"** button at the bottom
2. In the menu that appears, select **"Single Event"**
3. In the dialog, fill in the fields:
   - **Subject** (required): Event name
   - **Date** (required): Format yyyy-MM-dd (e.g., 2025-01-20)
   - **All-day event** (checkbox): Check for 8am-5pm event
   - **Start Time** (required): Format HH:mm (e.g., 09:00)
   - **End Time** (required): Format HH:mm (e.g., 10:00)
   - **Location** (optional): Where the event takes place
   - **Description** (optional): Additional details
   - **Private event** (checkbox): Mark as private
4. Click **"Create Event"**

**Validation:**
- Subject cannot be empty
- Date must be valid (yyyy-MM-dd format)
- Times must be valid (HH:mm format in 24-hour)
- End time must be after start time
- If all-day is checked, times are automatically set to 08:00-17:00

**Notes:**
- The date field is pre-filled with the currently selected day
- Private events are marked with "(Private)" in the event list

---

### 6. Creating a Recurring Event Series

**Steps:**
1. Click **"Create Event"** button
2. Select **"Recurring Series"**
3. Fill in basic event details (same as single event)
4. **Select weekdays** for recurrence:
   - Check one or more days: Sun, Mon, Tue, Wed, Thu, Fri, Sat
   - At least one day must be selected
5. **Choose series end condition** (select one):
   - **On date**: Enter end date (yyyy-MM-dd)
   - **After # occurrences**: Enter number (e.g., 10)
6. Click **"Create Series"**

**Examples:**
- Weekly team meeting every Monday for 8 weeks:
  - Check "Mon"
  - Select "After # occurrences": 8

- Gym sessions Mon/Wed/Fri until end of March:
  - Check "Mon", "Wed", "Fri"
  - Select "On date": 2025-03-31

**Notes:**
- Series generates events only on selected weekdays
- If start date is not a selected weekday, first occurrence starts on next matching day
- Maximum series length: events generated up to 10 years in future

---

### 7. Editing Events

**Steps:**
1. **Select a day** with events by clicking it in the calendar
2. Click **"Edit Event"** button
3. If multiple events on that day:
   - A selection dialog appears
   - Choose the event to edit
4. In the edit dialog:
   - All fields show current values
   - Modify any fields you want to change
   - For **series events**, choose edit scope:
     - **This event only**: Edit just this one occurrence
     - **This and future events**: Edit from this date forward
     - **All events in series**: Edit entire recurring series
5. Click **"Save Changes"**

**Series Editing Behavior:**
- **"This event only"**: Creates independent event, splits from series
- **"This and future"**: Modifies all future occurrences
- **"All events in series"**: Updates every occurrence including past ones

**Notes:**
- Changing a series event's time significantly may split it from the series
- You cannot edit events on days with no events (button shows error)
- Empty location/description fields remove those values from the event

---

### 8. Visual Indicators in the Calendar Grid

**Day Cell Colors and Styles:**

| Indicator | Meaning |
|-----------|---------|
| **Bold blue text** | Day has one or more events |
| **Red text** | Today's date |
| **Blue background + border** | Currently selected day |
| **Gray background** | Day header (Sun, Mon, etc.) |
| **Light gray cells** | Empty padding cells |

**Example:** If January 20 is today, has events, and is selected:
- Text is red (today) and bold (has events)
- Background is light blue (selected)

---

### 9. Error Handling

The GUI provides user-friendly error messages for common mistakes:

**Common Errors:**
- "Subject is required" → Tried to create event with empty name
- "End time must be after start time" → Invalid time range
- "Invalid date or time format" → Wrong format (use yyyy-MM-dd and HH:mm)
- "Calendar already exists" → Duplicate calendar name
- "Event already exists" → Duplicate event (same subject + start + end)
- "Please select at least one weekday" → Series without weekdays
- "No events to edit" → Edit button clicked with no events on selected day

**Error Dialog:**
- Appears as a popup with error icon
- Explains what went wrong
- Suggests how to fix it
- Click "OK" to dismiss and return to form

---

## Command-Line Mode Commands

For interactive and headless modes, see `res/commands.txt` for valid command examples.

### Command Categories

**Calendar Management:**
```
create calendar "Name" "Timezone"
use calendar "Name"
edit calendar "Name" name "NewName"
edit calendar "Name" timezone "NewTimezone"
```

**Single Events:**
```
create event "Subject" on yyyy-MM-dd from HH:mm to HH:mm [at "Location"] [described as "Description"] [public|private]
create all-day event "Subject" on yyyy-MM-dd [at "Location"] [described as "Description"] [public|private]
```

**Recurring Events:**
```
create event series "Subject" from yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm on [MTWRFSU]+ repeat FOR N occurrences [public|private]
create event series "Subject" from yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm on [MTWRFSU]+ repeat UNTIL yyyy-MM-dd [public|private]
create all-day event series "Subject" on yyyy-MM-dd on [MTWRFSU]+ repeat FOR N occurrences [public|private]
```

**Viewing Events:**
```
print events on yyyy-MM-dd
print events from yyyy-MM-dd to yyyy-MM-dd
print all events
show status on yyyy-MM-dd at HH:mm
```

**Editing Events:**
```
edit event "Subject" on yyyy-MM-dd from HH:mm to HH:mm set [subject|start|end|location|description|status] "NewValue"
edit events on yyyy-MM-dd set [subject|start|end|location|description|status] "NewValue"
edit series "Subject" on yyyy-MM-dd set [property] "NewValue"
edit series "Subject" from yyyy-MM-dd set [property] "NewValue"
```

**Copying Events:**
```
copy event "Subject" on yyyy-MM-dd from HH:mm to HH:mm to yyyy-MM-dd
copy events on yyyy-MM-dd to yyyy-MM-dd
copy events from yyyy-MM-dd to yyyy-MM-dd to yyyy-MM-dd
```

**Exporting:**
```
export events to "filename.csv" as CSV
```

**Exit:**
```
exit
```

---

## Tips and Best Practices

### Calendar Organization
- Create separate calendars for different aspects of life (Work, Personal, School)
- Use descriptive calendar names
- Choose appropriate timezones for each calendar

### Event Naming
- Use clear, descriptive event names ("Team Meeting" not "mtg")
- Include location in the subject if room number is critical ("Standup - Room 301")
- Use private flag for sensitive events

### Recurring Events
- Use series for truly recurring events (daily standups, weekly meetings)
- Don't use series for one-off events or irregular patterns
- Remember: editing a series event's time may split it from the series

### Date/Time Input
- Always use 24-hour time format (HH:mm): 14:00 not 2:00 PM
- Use leading zeros: 09:00 not 9:00
- Date format is strict: 2025-01-20 not 1/20/2025 or 20-01-2025

### GUI Workflow
1. Select/create calendar first
2. Navigate to desired month
3. Click day to see events
4. Create events using "Create Event" button
5. Edit events by selecting day first, then clicking "Edit Event"

---

## Troubleshooting

**Problem:** GUI doesn't launch when running JAR
- **Solution:** Make sure you have Java 11+ installed. Run `java -version` to check.
- **Solution:** Run with: `java -jar build/libs/calendar.jar` (don't use --mode)

**Problem:** "Calendar name cannot be empty" error
- **Solution:** Enter a name in the calendar name field before clicking Create

**Problem:** Can't edit event - button does nothing
- **Solution:** Make sure you've selected a day with events first (click the day in the calendar grid)

**Problem:** "Invalid date or time format" error
- **Solution:** Use yyyy-MM-dd for dates (e.g., 2025-01-20) and HH:mm for times (e.g., 09:00)

**Problem:** Event doesn't appear on calendar
- **Solution:** Check that you've selected the correct calendar in the dropdown
- **Solution:** Navigate to the correct month using the month navigation buttons

**Problem:** Recurring series doesn't generate events
- **Solution:** Ensure at least one weekday is checked
- **Solution:** Verify end date is after start date or occurrences is positive number

---

## Example Workflows

### Workflow 1: Schedule a Weekly Team Meeting

1. Launch GUI: `java -jar build/libs/calendar.jar`
2. If needed, create a "Work" calendar (New Calendar button)
3. Navigate to the week you want the meeting to start
4. Click "Create Event" → "Recurring Series"
5. Fill in:
   - Subject: "Team Meeting"
   - Date: 2025-01-20
   - Start: 09:00
   - End: 10:00
   - Location: "Conference Room A"
   - Check "Mon" (or your preferred day)
   - Select "After # occurrences": 52 (for one year)
6. Click "Create Series"
7. The calendar now shows the meeting every Monday

### Workflow 2: Track Personal and Work Events Separately

1. Launch GUI
2. Create "Work" calendar with "America/New_York" timezone
3. Create "Personal" calendar with "America/Los_Angeles" timezone (if you're on west coast)
4. Use dropdown to switch to "Work" calendar
5. Add work events (meetings, deadlines, etc.)
6. Switch to "Personal" calendar using dropdown
7. Add personal events (gym, appointments, etc.)
8. Switch between calendars to see different event sets

### Workflow 3: Modify a Recurring Meeting Time

1. Select a day that has the recurring meeting
2. Click "Edit Event"
3. Select the meeting from the list
4. Choose edit scope:
   - "This event only" if just this one occurrence changes
   - "This and future" if schedule changes going forward
   - "All events in series" if permanent time change
5. Change the start/end times
6. Click "Save Changes"
7. Calendar updates to reflect the changes

---

## Class Diagram

The following UML class diagram illustrates the main components and their relationships:

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              CALENDAR APPLICATION                                │
│                                 MVC Architecture                                 │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│  <<interface>>      │
│ControllerInterface  │
├─────────────────────┤
│ +run()              │
└─────────┬───────────┘
          │ implements
          ▼
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│InteractiveController│    │ HeadlessController  │    │   GuiController     │
├─────────────────────┤    ├─────────────────────┤    ├─────────────────────┤
│ -manager            │    │ -manager            │    │ -manager            │
│ -view               │    │ -view               │    │ -view               │
│ -parser             │    │ -parser             │    │ -selectedDate       │
├─────────────────────┤    │ -commandFile        │    ├─────────────────────┤
│ +run()              │    ├─────────────────────┤    │ +run()              │
└─────────┬───────────┘    │ +run()              │    │ +createCalendar()   │
          │                └─────────────────────┘    │ +createEvent()      │
          │                                           │ +editEvent()        │
          │ uses                                      └──────────┬──────────┘
          ▼                                                      │
┌─────────────────────┐                                         │ uses
│   CommandParser     │                                         │
├─────────────────────┤                                         │
│ +parse(line)        │──────────────────────────┐              │
└─────────────────────┘                          │              │
          │ creates                              │              │
          ▼                                      │              │
┌─────────────────────┐                          │              │
│   <<interface>>     │                          │              │
│  CommandInterface   │                          │              │
├─────────────────────┤                          │              │
│ +execute(mgr,view)  │                          │              │
└─────────┬───────────┘                          │              │
          │ implements                           │              │
          ▼                                      │              │
┌─────────────────────────────────────────────────────────────────────────────────┐
│ CreateEventCommand  │ EditEventCommand │ PrintEventsOnCommand │ ExportCommand  │
│ CreateSeriesCommand │ EditSeriesCommand│ PrintAllEventsCommand│ CopyEventCmd   │
│ CreateCalendarCmd   │ UseCalendarCmd   │ PrintEventsRangeCmd  │ ShowStatusCmd  │
└─────────────────────────────────────────────────────────────────────────────────┘
          │
          │ uses
          ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                     MODEL                                        │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐         ┌─────────────────────┐
│  CalendarManager    │ manages │  <<interface>>      │
├─────────────────────┤ ◆─────▶ │ CalendarInterface   │
│ -calendars: Map     │    *    ├─────────────────────┤
│ -currentCalendar    │         │ +getName()          │
├─────────────────────┤         │ +setName()          │
│ +createCalendar()   │         │ +getTimezone()      │
│ +getCalendar()      │         │ +setTimezone()      │
│ +setCurrentCalendar()│         │ +getModel()         │
│ +editCalendarName() │         └─────────┬───────────┘
│ +editCalendarTZ()   │                   │ implements
│ +getAllCalendars()  │                   ▼
└─────────────────────┘         ┌─────────────────────┐
                                │     Calendar        │
                                ├─────────────────────┤
                                │ -name: String       │
                                │ -timezone: ZoneId   │
                                │ -model              │
                                └─────────┬───────────┘
                                          │ has
                                          ▼
┌─────────────────────┐         ┌─────────────────────┐
│   <<interface>>     │ ◀───────│    CalendarModel    │
│CalendarModelInterface│ impl   ├─────────────────────┤
├─────────────────────┤         │ -events: Set        │
│ +createEvent()      │         │ -seriesConfigs: Map │
│ +createEventSeries()│         ├─────────────────────┤
│ +editEvent()        │         │ +createEvent()      │
│ +editSeriesFrom()   │         │ +createEventSeries()│
│ +editEntireSeries() │         │ +editEvent()        │
│ +getEventsOnDate()  │         │ +getEventsOnDate()  │
│ +getEventsInRange() │         │ +getEventsInRange() │
│ +getAllEvents()     │         │ +getAllEvents()     │
│ +isBusy()           │         │ +isBusy()           │
│ +findEventById()    │         │ +findEventById()    │
└─────────────────────┘         └─────────────────────┘
                                          │
                                          │ contains
                                          ▼
┌─────────────────────┐         ┌─────────────────────┐
│   <<interface>>     │ ◀───────│       Event         │
│   EventInterface    │ impl    ├─────────────────────┤
├─────────────────────┤         │ -id: UUID           │
│ +getId()            │         │ -seriesId: UUID     │
│ +getSubject()       │         │ -subject: String    │
│ +getStartDateTime() │         │ -startDateTime      │
│ +getEndDateTime()   │         │ -endDateTime        │
│ +getDescription()   │         │ -description        │
│ +getLocation()      │         │ -location           │
│ +isPrivate()        │         │ -isPrivate          │
│ +getSeriesId()      │         └─────────────────────┘
│ +withModifications()│
└─────────────────────┘                   ▲
                                          │ template
┌─────────────────────┐                   │
│    EventSeries      │───────────────────┘
├─────────────────────┤
│ -seriesId: UUID     │
│ -template: Event    │
│ -weekdays: Set      │
│ -occurrences: Int   │
│ -endDate: LocalDate │
└─────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                      VIEW                                        │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐         ┌─────────────────────┐
│   <<interface>>     │         │   <<interface>>     │
│   ViewInterface     │         │  GuiViewInterface   │
├─────────────────────┤         ├─────────────────────┤
│ +displayMessage()   │         │ +addFeatures()      │
│ +displayError()     │         │ +updateCalendarList()│
│ +displayEvents()    │         │ +displayMonth()     │
└─────────┬───────────┘         │ +displayEventsForDay()│
          │ implements          │ +showError()        │
          ▼                     │ +showMessage()      │
┌─────────────────────┐         └─────────┬───────────┘
│    ConsoleView      │                   │ implements
├─────────────────────┤                   ▼
│ -output: Appendable │         ┌─────────────────────┐
├─────────────────────┤         │      GuiView        │
│ +displayMessage()   │         ├─────────────────────┤
│ +displayError()     │         │ -frame: JFrame      │
│ +displayEvents()    │         │ -calendarSelector   │
└─────────────────────┘         │ -monthGrid          │
                                │ -eventPanel         │
                                └─────────────────────┘

Legend:
─────────────────────────────────────────
│ Interface │     │ Class     │
└───────────┘     └───────────┘
    ◆───────▶  Composition (contains)
    ────────▶  Association (uses/has)
    ────────▷  Implements/Extends
```

---

## Additional Resources

- **Commands reference:** See `res/commands.txt` for complete command examples
- **Invalid commands:** See `res/invalid.txt` for examples of commands that will fail with error messages
- **Design documentation:** See `res/Misc.md` for technical details and design decisions

---

## Support

For issues or questions:
- Check this USEME.md file
- Review the error message carefully - it usually explains what went wrong
- Verify date/time formats match examples (yyyy-MM-dd, HH:mm)
- Ensure calendar is selected before creating/editing events
