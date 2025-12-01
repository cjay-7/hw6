package calendar.view;

import java.time.DayOfWeek;

/**
 * Centralized UI messages for the calendar application.
 * Provides consistent error messages, success messages, and labels across the application.
 * This eliminates magic strings and makes internationalization easier in the future.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public final class UIMessages {

  

  public static final String APP_TITLE = "Calendar Application";
  public static final String CREATE_EVENT_TITLE = "Create New Event";
  public static final String CREATE_SERIES_TITLE = "Create Recurring Series";
  public static final String EDIT_EVENT_TITLE = "Edit Event";
  public static final String NEW_CALENDAR_TITLE = "Create New Calendar";

  

  public static final String ERROR_NO_CALENDAR = "Please create or select a calendar first.";
  public static final String ERROR_CALENDAR_NAME_EMPTY = "Calendar name cannot be empty.";
  public static final String ERROR_INVALID_TIMEZONE = "Please select a valid timezone.";
  public static final String ERROR_CALENDAR_EXISTS = "A calendar named '%s' already exists.\n"
      + "Please choose a different name.";
  public static final String ERROR_CALENDAR_NOT_FOUND = "Calendar '%s' not found.";
  public static final String ERROR_INVALID_CALENDAR_NAME = "Invalid calendar name.";

  

  public static final String ERROR_DUPLICATE_EVENT =
      "An event with the same subject, start time, and end time already exists.";
  public static final String ERROR_INVALID_SUBJECT = "Event subject cannot be empty.";
  public static final String ERROR_INVALID_TIMES = "Event start and end times must be specified.";
  public static final String ERROR_END_BEFORE_START = "Event end time must be after start time.";
  public static final String ERROR_NO_EVENT_SELECTED = "No event selected to edit.";
  public static final String ERROR_INVALID_DATE = "Invalid date selected.";
  public static final String ERROR_INVALID_MONTH = "Invalid month: %d";

  

  public static final String ERROR_NO_WEEKDAYS =
      "Please select at least one weekday for the recurring series.";
  public static final String ERROR_START_DAY_NOT_SELECTED =
      "The start date is a %s, but you haven't selected %s in the recurring days.";
  public static final String ERROR_BOTH_END_CONDITIONS =
      "Please specify either an end date OR number of occurrences, not both.";
  public static final String ERROR_NO_END_CONDITION =
      "Please specify either an end date or number of occurrences.";
  public static final String ERROR_END_BEFORE_START_DATE =
      "Series end date must be after the start date.";
  public static final String ERROR_INVALID_OCCURRENCES =
      "Number of occurrences must be at least 1.";
  public static final String ERROR_SERIES_SAME_DAY =
      "Series events must start and end on the same day.";
  public static final String ERROR_SERIES_CONFLICT =
      "Failed to create series. One or more events conflict with existing events.";

  

  public static final String ERROR_EDIT_DUPLICATE =
      "Failed to update event. The changes would create a duplicate event.";
  public static final String ERROR_EDIT_FAILED = "Failed to edit event: %s";
  public static final String ERROR_EDIT_SERIES_FAILED = "Failed to edit series: %s";
  public static final String ERROR_EDIT_SERIES_GENERAL = "Failed to update series.";

  

  public static final String SUCCESS_CALENDAR_CREATED = "Calendar '%s' created successfully!";
  public static final String SUCCESS_EVENT_CREATED = "Event '%s' created successfully!";
  public static final String SUCCESS_SERIES_CREATED =
      "Recurring series '%s' created successfully!";
  public static final String SUCCESS_EVENT_UPDATED = "Event updated successfully!";
  public static final String SUCCESS_SERIES_UPDATED =
      "All events in the series updated successfully!";
  public static final String SUCCESS_SERIES_FROM_DATE_UPDATED =
      "Series events updated successfully!";

  

  public static final String INFO_NO_EVENTS = "No events scheduled for this day.";
  public static final String INFO_SELECT_EVENT = "Select event to edit:";
  public static final String INFO_NO_EVENTS_TO_EDIT =
      "No events to edit on the selected day.\nPlease select a day with events first.";

  

  public static final String LABEL_SUBJECT_REQUIRED = "Subject (required):";
  public static final String LABEL_DATE_FORMAT = "Date (yyyy-MM-dd):";
  public static final String LABEL_START_TIME = "Start Time (HH:mm):";
  public static final String LABEL_END_TIME = "End Time (HH:mm):";
  public static final String LABEL_LOCATION = "Location (optional):";
  public static final String LABEL_DESCRIPTION = "Description (optional):";
  public static final String LABEL_ALL_DAY = "All-day event (8:00 AM - 5:00 PM)";
  public static final String LABEL_PRIVATE = "Mark as private";
  public static final String LABEL_CALENDAR_NAME = "Calendar Name:";
  public static final String LABEL_TIMEZONE = "Timezone:";

  

  public static final String ERROR_INVALID_DATE_FORMAT =
      "Invalid date or time format.\n\nDate: yyyy-MM-dd\nTime: HH:mm";

  

  /**
   * Formats the calendar created success message.
   *
   * @param name the calendar name
   * @return formatted message
   */
  public static String formatCalendarCreated(String name) {
    return String.format(SUCCESS_CALENDAR_CREATED, name);
  }

  /**
   * Formats the event created success message.
   *
   * @param subject the event subject
   * @return formatted message
   */
  public static String formatEventCreated(String subject) {
    return String.format(SUCCESS_EVENT_CREATED, subject);
  }

  /**
   * Formats the series created success message.
   *
   * @param subject the series subject
   * @return formatted message
   */
  public static String formatSeriesCreated(String subject) {
    return String.format(SUCCESS_SERIES_CREATED, subject);
  }

  /**
   * Formats the calendar exists error message.
   *
   * @param name the calendar name
   * @return formatted message
   */
  public static String formatCalendarExists(String name) {
    return String.format(ERROR_CALENDAR_EXISTS, name);
  }

  /**
   * Formats the calendar not found error message.
   *
   * @param name the calendar name
   * @return formatted message
   */
  public static String formatCalendarNotFound(String name) {
    return String.format(ERROR_CALENDAR_NOT_FOUND, name);
  }

  /**
   * Formats the start day not selected error message.
   *
   * @param day the day of week
   * @return formatted message
   */
  public static String formatStartDayNotSelected(DayOfWeek day) {
    return String.format(ERROR_START_DAY_NOT_SELECTED, day, day);
  }

  /**
   * Formats the invalid month error message.
   *
   * @param month the invalid month value
   * @return formatted message
   */
  public static String formatInvalidMonth(int month) {
    return String.format(ERROR_INVALID_MONTH, month);
  }

  /**
   * Formats the edit failed error message.
   *
   * @param reason the reason for failure
   * @return formatted message
   */
  public static String formatEditFailed(String reason) {
    return String.format(ERROR_EDIT_FAILED, reason);
  }

  /**
   * Formats the edit series failed error message.
   *
   * @param reason the reason for failure
   * @return formatted message
   */
  public static String formatEditSeriesFailed(String reason) {
    return String.format(ERROR_EDIT_SERIES_FAILED, reason);
  }

  private UIMessages() {
    
  }
}



