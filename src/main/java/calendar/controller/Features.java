package calendar.controller;

import calendar.model.EventInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

/**
 * Features interface for calendar controller operations.
 */
public interface Features {

  /**
   * Creates a new calendar.
   *
   * @param name     the calendar name
   * @param timezone the timezone for the calendar
   */
  void createCalendar(String name, ZoneId timezone);

  /**
   * Switches to a different calendar.
   *
   * @param calendarName the name of the calendar to switch to
   */
  void switchCalendar(String calendarName);

  /**
   * Navigates to a specific month.
   *
   * @param year  the year
   * @param month the month
   */
  void navigateToMonth(int year, int month);

  /**
   * Navigates to the previous month.
   */
  void navigateToPreviousMonth();

  /**
   * Navigates to the next month.
   */
  void navigateToNextMonth();

  /**
   * Navigates to today's date.
   */
  void navigateToToday();

  /**
   * Selects a specific day.
   *
   * @param date the date to select
   */
  void selectDay(LocalDate date);

  /**
   * Switches to week view.
   */
  void switchToWeekView();

  /**
   * Switches to month view.
   */
  void switchToMonthView();

  /**
   * Creates a new event.
   *
   * @param subject     the event subject
   * @param start       the start date/time
   * @param end         the end date/time
   * @param location    the event location
   * @param description the event description
   * @param isPrivate   whether the event is private
   */
  void createEvent(String subject, LocalDateTime start, LocalDateTime end,
      String location, String description, boolean isPrivate);

  /**
   * Creates a recurring event series.
   *
   * @param subject     the event subject
   * @param start       the start date/time
   * @param end         the end date/time
   * @param location    the event location
   * @param description the event description
   * @param isPrivate   whether the event is private
   * @param weekdays    the weekdays to repeat on
   * @param endDate     the series end date
   * @param occurrences the number of occurrences
   */
  void createEventSeries(String subject, LocalDateTime start, LocalDateTime end,
      String location, String description, boolean isPrivate,
      Set<java.time.DayOfWeek> weekdays,
      LocalDate endDate, Integer occurrences);

  /**
   * Edits an existing event.
   *
   * @param event          the event to edit
   * @param newSubject     the new subject
   * @param newStart       the new start date/time
   * @param newEnd         the new end date/time
   * @param newLocation    the new location
   * @param newDescription the new description
   * @param newIsPrivate   the new private status
   */
  void editEvent(EventInterface event,
      String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
      String newLocation, String newDescription, Boolean newIsPrivate);

  /**
   * Edits an entire event series.
   *
   * @param seriesId       the series identifier
   * @param newSubject     the new subject
   * @param newStart       the new start date/time
   * @param newEnd         the new end date/time
   * @param newLocation    the new location
   * @param newDescription the new description
   * @param newIsPrivate   the new private status
   */
  void editSeries(String seriesId,
      String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
      String newLocation, String newDescription, Boolean newIsPrivate);

  /**
   * Edits an event series from a specific date onwards.
   *
   * @param seriesId       the series identifier
   * @param fromDate       the date to start editing from
   * @param newSubject     the new subject
   * @param newStart       the new start date/time
   * @param newEnd         the new end date/time
   * @param newLocation    the new location
   * @param newDescription the new description
   * @param newIsPrivate   the new private status
   */
  void editSeriesFromDate(String seriesId, LocalDate fromDate,
      String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
      String newLocation, String newDescription, Boolean newIsPrivate);
}
