package calendar.controller;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.model.EditSpec;
import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.model.EventSeries;
import calendar.model.EventStatus;
import calendar.view.GuiViewInterface;
import calendar.view.UIMessages;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * GUI controller for calendar application.
 */
public class GuiController implements Features {
  private final CalendarManager manager;
  private final GuiViewInterface view;

  private int currentYear;
  private int currentMonth;
  private LocalDate selectedDate;
  
  /**
   * Tracks whether the current view is week view (true) or month view (false).
   */
  private boolean isWeekView;

  /**
   * Constructor.
   *
   * <p>manager the calendar manager
   * view the GUI view
   */
  public GuiController(CalendarManager manager, GuiViewInterface view) {
    this.manager = manager;
    this.view = view;
    this.isWeekView = false;

    LocalDate today = LocalDate.now();
    this.currentYear = today.getYear();
    this.currentMonth = today.getMonthValue();
    this.selectedDate = today;

    view.addFeatures(this);
    initializeView();
  }

  private void initializeView() {
    if (manager.getAllCalendars().isEmpty()) {
      createDefaultCalendar();
    }

    refreshCalendarList();
    refreshMonthView();

    if (manager.getCurrentCalendar() != null) {
      refreshEventsForSelectedDay();
    }

    view.display();
  }

  /**
   * Creates a default calendar if none exists.
   */
  private void createDefaultCalendar() {
    ZoneId systemZone = ZoneId.systemDefault();
    manager.createCalendar("My Calendar", systemZone);
    manager.setCurrentCalendar("My Calendar");
  }

  

  @Override
  public void switchToWeekView() {
    this.isWeekView = true;
    refreshView();
  }

  @Override
  public void switchToMonthView() {
    this.isWeekView = false;
    refreshView();
  }

  /**
   * Refreshes the current view (month or week) based on view mode.
   */
  private void refreshView() {
    if (isWeekView) {
      refreshWeekView();
    } else {
      refreshMonthView();
    }
  }

  /**
   * Refreshes the week view display.
   */
  private void refreshWeekView() {
    CalendarModelInterface model = getCurrentModel();
    
    
    LocalDate weekStart = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() % 7);
    LocalDate weekEnd = weekStart.plusDays(6);
    
    List<LocalDate> daysWithEvents = new ArrayList<>();
    if (model != null) {
      for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
        List<EventInterface> events = model.getEventsOnDate(date);
        if (!events.isEmpty()) {
          daysWithEvents.add(date);
        }
      }
    }
    
    view.displayWeek(weekStart, daysWithEvents, selectedDate);
  }

  

  @Override
  public void createCalendar(String name, ZoneId timezone) {
    if (name == null || name.trim().isEmpty()) {
      view.showError(UIMessages.ERROR_CALENDAR_NAME_EMPTY);
      return;
    }

    if (timezone == null) {
      view.showError(UIMessages.ERROR_INVALID_TIMEZONE);
      return;
    }

    try {
      boolean success = manager.createCalendar(name, timezone);
      if (success) {
        manager.setCurrentCalendar(name);
        refreshCalendarList();
        refreshView();
        refreshEventsForSelectedDay();
        view.showMessage(UIMessages.formatCalendarCreated(name));
      } else {
        view.showError(UIMessages.formatCalendarExists(name));
      }
    } catch (IllegalArgumentException e) {
      view.showError("Failed to create calendar: " + e.getMessage());
    }
  }

  @Override
  public void switchCalendar(String calendarName) {
    if (calendarName == null || calendarName.trim().isEmpty()) {
      view.showError(UIMessages.ERROR_INVALID_CALENDAR_NAME);
      return;
    }

    boolean success = manager.setCurrentCalendar(calendarName);
    if (success) {
      refreshCalendarList();
      refreshView();
      refreshEventsForSelectedDay();
    } else {
      view.showError(UIMessages.formatCalendarNotFound(calendarName));
    }
  }

  

  @Override
  public void navigateToMonth(int year, int month) {
    if (month < 1 || month > 12) {
      view.showError(UIMessages.formatInvalidMonth(month));
      return;
    }

    this.currentYear = year;
    this.currentMonth = month;
    refreshView();
  }

  @Override
  public void navigateToPreviousMonth() {
    if (isWeekView) {
      selectedDate = selectedDate.minusWeeks(1);
      refreshView();
    } else {
      currentMonth--;
      if (currentMonth < 1) {
        currentMonth = 12;
        currentYear--;
      }
      refreshView();
    }
  }

  @Override
  public void navigateToNextMonth() {
    if (isWeekView) {
      selectedDate = selectedDate.plusWeeks(1);
      refreshView();
    } else {
      currentMonth++;
      if (currentMonth > 12) {
        currentMonth = 1;
        currentYear++;
      }
      refreshView();
    }
  }

  @Override
  public void navigateToToday() {
    LocalDate today = LocalDate.now();
    this.currentYear = today.getYear();
    this.currentMonth = today.getMonthValue();
    this.selectedDate = today;
    refreshView();
    refreshEventsForSelectedDay();
  }

  @Override
  public void selectDay(LocalDate date) {
    if (date == null) {
      view.showError(UIMessages.ERROR_INVALID_DATE);
      return;
    }

    this.selectedDate = date;
    refreshEventsForSelectedDay();
    refreshView();
  }

  

  @Override
  public void createEvent(String subject, LocalDateTime start, LocalDateTime end,
                          String location, String description, boolean isPrivate) {
    CalendarModelInterface model = getCurrentModel();
    if (model == null) {
      view.showError(UIMessages.ERROR_NO_CALENDAR);
      return;
    }

    try {
      validateSubject(subject);
      validateEventTimes(start, end);

      EventStatus status = isPrivate ? EventStatus.PRIVATE : EventStatus.PUBLIC;
      EventInterface event = new Event(
          subject.trim(), start, end,
          description != null && !description.trim().isEmpty() ? description.trim() : null,
          location != null && !location.trim().isEmpty() ? location.trim() : null,
          status.isPrivate(),
          UUID.randomUUID(),
          null
      );

      boolean success = model.createEvent(event);
      if (success) {
        view.showMessage(UIMessages.formatEventCreated(subject));
        refreshView();
        refreshEventsForSelectedDay();
      } else {
        view.showError(UIMessages.ERROR_DUPLICATE_EVENT);
      }
    } catch (IllegalArgumentException e) {
      view.showError("Failed to create event: " + e.getMessage());
    }
  }

  @Override
  public void createEventSeries(String subject, LocalDateTime start, LocalDateTime end,
                                String location, String description, boolean isPrivate,
                                Set<java.time.DayOfWeek> weekdays,
                                LocalDate endDate, Integer occurrences) {
    CalendarModelInterface model = getCurrentModel();
    if (model == null) {
      view.showError(UIMessages.ERROR_NO_CALENDAR);
      return;
    }

    try {
      
      validateSubject(subject);
      validateEventTimes(start, end);
      validateSeriesSameDay(start, end);

      
      validateSeriesWeekdays(weekdays, start.getDayOfWeek());
      boolean usesEndDate = validateAndParseSeriesEndCondition(
          endDate, occurrences, start.toLocalDate());

      
      EventSeries series = buildEventSeries(
          subject, start, end, location, description, isPrivate,
          weekdays, endDate, occurrences, usesEndDate);

      boolean success = model.createEventSeries(series);
      if (success) {
        view.showMessage(UIMessages.formatSeriesCreated(subject));
        refreshView();
        refreshEventsForSelectedDay();
      } else {
        view.showError(UIMessages.ERROR_SERIES_CONFLICT);
      }
    } catch (IllegalArgumentException e) {
      view.showError(e.getMessage());
    }
  }

  

  @Override
  public void editEvent(EventInterface event,
                        String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                        String newLocation, String newDescription, Boolean newIsPrivate) {
    CalendarModelInterface model = getCurrentModel();
    if (model == null) {
      view.showError(UIMessages.ERROR_NO_CALENDAR);
      return;
    }

    if (event == null) {
      view.showError(UIMessages.ERROR_NO_EVENT_SELECTED);
      return;
    }

    if (newStart != null && newEnd != null && !newEnd.isAfter(newStart)) {
      view.showError(UIMessages.ERROR_END_BEFORE_START);
      return;
    }

    try {
      EventStatus newStatus = null;
      if (newIsPrivate != null) {
        newStatus = newIsPrivate ? EventStatus.PRIVATE : EventStatus.PUBLIC;
      }

      EditSpec spec = new EditSpec(
          newSubject, newStart, newEnd, newDescription, newLocation, newStatus
      );

      boolean success = model.editEvent(event.getId(), spec);
      if (success) {
        view.showMessage(UIMessages.SUCCESS_EVENT_UPDATED);
        refreshView();
        refreshEventsForSelectedDay();
      } else {
        view.showError(UIMessages.ERROR_EDIT_DUPLICATE);
      }
    } catch (IllegalArgumentException e) {
      view.showError(UIMessages.formatEditFailed(e.getMessage()));
    }
  }

  @Override
  public void editSeries(String seriesId,
                         String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                         String newLocation, String newDescription, Boolean newIsPrivate) {
    CalendarModelInterface model = getCurrentModel();
    if (model == null) {
      view.showError(UIMessages.ERROR_NO_CALENDAR);
      return;
    }

    try {
      EventStatus newStatus = null;
      if (newIsPrivate != null) {
        newStatus = newIsPrivate ? EventStatus.PRIVATE : EventStatus.PUBLIC;
      }

      EditSpec spec = new EditSpec(
          newSubject, newStart, newEnd, newDescription, newLocation, newStatus
      );

      UUID seriesUuid = UUID.fromString(seriesId);
      boolean success = model.editEntireSeries(seriesUuid, spec);
      if (success) {
        view.showMessage(UIMessages.SUCCESS_SERIES_UPDATED);
        refreshView();
        refreshEventsForSelectedDay();
      } else {
        view.showError(UIMessages.ERROR_EDIT_SERIES_GENERAL);
      }
    } catch (IllegalArgumentException e) {
      view.showError(UIMessages.formatEditSeriesFailed(e.getMessage()));
    }
  }

  @Override
  public void editSeriesFromDate(String seriesId, LocalDate fromDate,
                                 String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                                 String newLocation, String newDescription, Boolean newIsPrivate) {
    CalendarModelInterface model = getCurrentModel();
    if (model == null) {
      view.showError(UIMessages.ERROR_NO_CALENDAR);
      return;
    }

    try {
      EventStatus newStatus = null;
      if (newIsPrivate != null) {
        newStatus = newIsPrivate ? EventStatus.PRIVATE : EventStatus.PUBLIC;
      }

      EditSpec spec = new EditSpec(
          newSubject, newStart, newEnd, newDescription, newLocation, newStatus
      );

      UUID seriesUuid = UUID.fromString(seriesId);
      boolean success = model.editSeriesFrom(seriesUuid, fromDate, spec);
      if (success) {
        view.showMessage(UIMessages.SUCCESS_SERIES_FROM_DATE_UPDATED);
        refreshView();
        refreshEventsForSelectedDay();
      } else {
        view.showError(UIMessages.ERROR_EDIT_SERIES_GENERAL);
      }
    } catch (IllegalArgumentException e) {
      view.showError(UIMessages.formatEditSeriesFailed(e.getMessage()));
    }
  }

  

  /**
   * Gets the current calendar's model.
   *
   * @return the current model, or null if no calendar is selected
   */
  private CalendarModelInterface getCurrentModel() {
    CalendarInterface current = manager.getCurrentCalendar();
    return current != null ? current.getModel() : null;
  }

  /**
   * Refreshes the calendar list in the view.
   */
  private void refreshCalendarList() {
    List<CalendarInterface> calendars = manager.getAllCalendars();
    String currentName = manager.getCurrentCalendar() != null
        ? manager.getCurrentCalendar().getName()
        : null;
    view.updateCalendarList(calendars, currentName);
  }

  /**
   * Refreshes the month view display.
   */
  private void refreshMonthView() {
    CalendarModelInterface model = getCurrentModel();

    List<LocalDate> daysWithEvents = new ArrayList<>();
    if (model != null) {
      LocalDate firstDay = LocalDate.of(currentYear, currentMonth, 1);
      LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);

      for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
        List<EventInterface> events = model.getEventsOnDate(date);
        if (!events.isEmpty()) {
          daysWithEvents.add(date);
        }
      }
    }

    view.displayMonth(currentYear, currentMonth, daysWithEvents, selectedDate);
  }

  /**
   * Refreshes the events display for the currently selected day.
   */
  private void refreshEventsForSelectedDay() {
    CalendarModelInterface model = getCurrentModel();

    if (model != null && selectedDate != null) {
      List<EventInterface> events = model.getEventsOnDate(selectedDate);
      view.displayEventsForDay(selectedDate, events);
    } else {
      view.displayEventsForDay(selectedDate, new ArrayList<>());
    }
  }

  /**
   * Validates event subject is not null or empty.
   *
   * @param subject the subject to validate
   * @throws IllegalArgumentException if subject is null or empty
   */
  private void validateSubject(String subject) {
    if (subject == null || subject.trim().isEmpty()) {
      throw new IllegalArgumentException("Event subject cannot be empty.");
    }
  }

  /**
   * Validates event start and end times are not null and end is after start.
   *
   * @param start the start time
   * @param end the end time
   * @throws IllegalArgumentException if times are null or invalid
   */
  private void validateEventTimes(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Event start and end times must be specified.");
    }
    if (!end.isAfter(start)) {
      throw new IllegalArgumentException("Event end time must be after start time.");
    }
  }

  /**
   * Validates series weekdays selection.
   *
   * @param weekdays the selected weekdays
   * @param startDay the start day of week
   * @throws IllegalArgumentException if validation fails
   */
  private void validateSeriesWeekdays(Set<java.time.DayOfWeek> weekdays,
                                      java.time.DayOfWeek startDay) {
    if (weekdays == null || weekdays.isEmpty()) {
      throw new IllegalArgumentException(UIMessages.ERROR_NO_WEEKDAYS);
    }
    if (!weekdays.contains(startDay)) {
      throw new IllegalArgumentException(UIMessages.formatStartDayNotSelected(startDay));
    }
  }

  /**
   * Validates that series events start and end on the same day.
   *
   * @param start the start date-time
   * @param end the end date-time
   * @throws IllegalArgumentException if not on same day
   */
  private void validateSeriesSameDay(LocalDateTime start, LocalDateTime end) {
    if (!start.toLocalDate().equals(end.toLocalDate())) {
      throw new IllegalArgumentException(UIMessages.ERROR_SERIES_SAME_DAY);
    }
  }

  /**
   * Validates and parses the series end condition.
   *
   * @param endDate the end date (optional)
   * @param occurrences the number of occurrences (optional)
   * @param startDate the series start date
   * @return true if using end date, false if using occurrences
   * @throws IllegalArgumentException if validation fails
   */
  private boolean validateAndParseSeriesEndCondition(LocalDate endDate, Integer occurrences,
                                                      LocalDate startDate) {
    if (endDate != null && occurrences != null) {
      throw new IllegalArgumentException(UIMessages.ERROR_BOTH_END_CONDITIONS);
    }
    if (endDate == null && occurrences == null) {
      throw new IllegalArgumentException(UIMessages.ERROR_NO_END_CONDITION);
    }

    if (endDate != null) {
      if (!endDate.isAfter(startDate)) {
        throw new IllegalArgumentException(UIMessages.ERROR_END_BEFORE_START_DATE);
      }
      return true;
    } else {
      if (occurrences <= 0) {
        throw new IllegalArgumentException(UIMessages.ERROR_INVALID_OCCURRENCES);
      }
      return false;
    }
  }

  /**
   * Builds an event series object.
   *
   * @param subject the event subject
   * @param start the start date-time
   * @param end the end date-time
   * @param location the location
   * @param description the description
   * @param isPrivate whether the event is private
   * @param weekdays the recurring weekdays
   * @param endDate the end date (optional)
   * @param occurrences the number of occurrences (optional)
   * @param usesEndDate whether using end date vs occurrences
   * @return the event series
   */
  private EventSeries buildEventSeries(String subject, LocalDateTime start, LocalDateTime end,
                                       String location, String description, boolean isPrivate,
                                       Set<java.time.DayOfWeek> weekdays,
                                       LocalDate endDate, Integer occurrences,
                                       boolean usesEndDate) {
    EventStatus status = isPrivate ? EventStatus.PRIVATE : EventStatus.PUBLIC;
    UUID seriesId = UUID.randomUUID();

    EventInterface template = new Event(
        subject.trim(), start, end,
        description != null && !description.trim().isEmpty() ? description.trim() : null,
        location != null && !location.trim().isEmpty() ? location.trim() : null,
        status.isPrivate(),
        UUID.randomUUID(),
        seriesId
    );

    return new EventSeries(seriesId, template, weekdays, endDate, occurrences, usesEndDate);
  }
}
