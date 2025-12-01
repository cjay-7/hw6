package calendar.view;

import java.awt.Dimension;

/**
 * Centralized UI constants for sizes, spacing, and common strings.
 * Provides consistent measurements and messages across the application.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public final class UIConstants {

  

  /**
   * Default window width.
   */
  public static final int WINDOW_WIDTH = 1000;

  /**
   * Default window height.
   */
  public static final int WINDOW_HEIGHT = 700;

  /**
   * Default window size.
   */
  public static final Dimension WINDOW_SIZE = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

  

  /**
   * Preferred size for day buttons in calendar grid.
   */
  public static final Dimension DAY_BUTTON_SIZE = new Dimension(60, 50);

  /**
   * Width of text fields.
   */
  public static final int TEXT_FIELD_WIDTH = 20;

  /**
   * Width of time fields.
   */
  public static final int TIME_FIELD_WIDTH = 10;

  /**
   * Width of date fields.
   */
  public static final int DATE_FIELD_WIDTH = 12;

  /**
   * Main window minimum width.
   */
  public static final int MAIN_WINDOW_MIN_WIDTH = 1200;

  /**
   * Main window minimum height.
   */
  public static final int MAIN_WINDOW_MIN_HEIGHT = 750;

  /**
   * Main window minimum size.
   */
  public static final Dimension MAIN_WINDOW_MIN_SIZE = 
      new Dimension(MAIN_WINDOW_MIN_WIDTH, MAIN_WINDOW_MIN_HEIGHT);

  /**
   * Action button width.
   */
  public static final int ACTION_BUTTON_WIDTH = 180;

  /**
   * Action button height.
   */
  public static final int ACTION_BUTTON_HEIGHT = 45;

  /**
   * Action button size.
   */
  public static final Dimension ACTION_BUTTON_SIZE = 
      new Dimension(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);

  /**
   * Dialog button width.
   */
  public static final int DIALOG_BUTTON_WIDTH = 170;

  /**
   * Dialog button height.
   */
  public static final int DIALOG_BUTTON_HEIGHT = 40;

  /**
   * Dialog button size.
   */
  public static final Dimension DIALOG_BUTTON_SIZE = 
      new Dimension(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);

  /**
   * Standard border padding.
   */
  public static final int STANDARD_BORDER_PADDING = 10;

  /**
   * Bottom panel spacing.
   */
  public static final int BOTTOM_PANEL_SPACING = 15;

  

  /**
   * Standard spacing between components.
   */
  public static final int STANDARD_SPACING = 8;

  /**
   * Grid gap in calendar.
   */
  public static final int CALENDAR_GRID_GAP = 3;

  /**
   * Border thickness.
   */
  public static final int BORDER_THICKNESS = 1;

  /**
   * Padding for panels.
   */
  public static final int PANEL_PADDING = 10;

  

  /**
   * Number of rows in calendar grid (including header).
   */
  public static final int CALENDAR_GRID_ROWS = 7;

  /**
   * Number of columns in calendar grid.
   */
  public static final int CALENDAR_GRID_COLS = 7;

  /**
   * Maximum number of weeks to display.
   */
  public static final int MAX_CALENDAR_WEEKS = 6;

  

  /**
   * Day names for calendar header.
   */
  public static final String[] DAY_NAMES = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

  /**
   * Full day names.
   */
  public static final String[] FULL_DAY_NAMES = {
      "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

  

  /**
   * Window title.
   */
  public static final String WINDOW_TITLE = "Calendar Application";

  /**
   * New calendar button text.
   */
  public static final String BTN_NEW_CALENDAR = "New Calendar";

  /**
   * New event button text.
   */
  public static final String BTN_NEW_EVENT = "New Event";

  /**
   * New series button text.
   */
  public static final String BTN_NEW_SERIES = "New Series";

  /**
   * Previous button text.
   */
  public static final String BTN_PREVIOUS = "Previous";

  /**
   * Next button text.
   */
  public static final String BTN_NEXT = "Next";

  /**
   * Today button text.
   */
  public static final String BTN_TODAY = "Today";

  /**
   * Week view button text.
   */
  public static final String BTN_WEEK_VIEW = "Week View";

  /**
   * Month view button text.
   */
  public static final String BTN_MONTH_VIEW = "Month View";

  

  /**
   * Create event dialog title.
   */
  public static final String DIALOG_CREATE_EVENT = "Create Event";

  /**
   * Create series dialog title.
   */
  public static final String DIALOG_CREATE_SERIES = "Create Event Series";

  /**
   * Edit event dialog title.
   */
  public static final String DIALOG_EDIT_EVENT = "Edit Event";

  /**
   * New calendar dialog title.
   */
  public static final String DIALOG_NEW_CALENDAR = "New Calendar";

  

  /**
   * Subject field label.
   */
  public static final String LABEL_SUBJECT = "Subject:";

  /**
   * Start date/time label.
   */
  public static final String LABEL_START = "Start:";

  /**
   * End date/time label.
   */
  public static final String LABEL_END = "End:";

  /**
   * Location label.
   */
  public static final String LABEL_LOCATION = "Location:";

  /**
   * Description label.
   */
  public static final String LABEL_DESCRIPTION = "Description:";

  /**
   * Private checkbox label.
   */
  public static final String LABEL_PRIVATE = "Private";

  /**
   * Calendar name label.
   */
  public static final String LABEL_CALENDAR_NAME = "Calendar Name:";

  /**
   * Timezone label.
   */
  public static final String LABEL_TIMEZONE = "Timezone:";

  

  /**
   * Generic error title.
   */
  public static final String ERROR_TITLE = "Error";

  /**
   * No calendar selected error.
   */
  public static final String ERROR_NO_CALENDAR = "Please create or select a calendar first.";

  /**
   * Empty subject error.
   */
  public static final String ERROR_EMPTY_SUBJECT = "Event subject cannot be empty.";

  /**
   * Invalid date/time error.
   */
  public static final String ERROR_INVALID_DATETIME =
      "Event start and end times must be specified.";

  /**
   * End before start error.
   */
  public static final String ERROR_END_BEFORE_START = "Event end time must be after start time.";

  /**
   * Duplicate event error.
   */
  public static final String ERROR_DUPLICATE_EVENT =
      "An event with the same subject, start time, and end time already exists.";

  

  /**
   * Event created success message.
   */
  public static final String SUCCESS_EVENT_CREATED = "Event '%s' created successfully!";

  /**
   * Series created success message.
   */
  public static final String SUCCESS_SERIES_CREATED = "Recurring series '%s' created successfully!";

  /**
   * Event updated success message.
   */
  public static final String SUCCESS_EVENT_UPDATED = "Event updated successfully!";

  /**
   * Calendar created success message.
   */
  public static final String SUCCESS_CALENDAR_CREATED = "Calendar '%s' created successfully!";

  

  /**
   * Date format pattern.
   */
  public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

  /**
   * Time format pattern.
   */
  public static final String TIME_FORMAT_PATTERN = "HH:mm";

  /**
   * Date-time format pattern.
   */
  public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";

  private UIConstants() {
    
  }
}
