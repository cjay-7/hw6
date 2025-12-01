package calendar.view;

import java.awt.Color;

/**
 * Centralized theme constants for calendar UI colors.
 * Provides a consistent color scheme across the application.
 */
public final class CalendarTheme {

  /**
   * Background color for selected day in calendar grid.
   */
  public static final Color SELECTED_DAY_BG = new Color(173, 216, 230);

  /**
   * Border color for selected day.
   */
  public static final Color SELECTED_DAY_BORDER = new Color(0, 100, 200);

  /**
   * Border width for selected day.
   */
  public static final int SELECTED_BORDER_WIDTH = 3;

  /**
   * Text color for today's date.
   */
  public static final Color TODAY_COLOR = new Color(220, 20, 60);

  /**
   * Color for event indicator dot.
   */
  public static final Color EVENT_INDICATOR_COLOR = new Color(0, 100, 200);

  /**
   * Background for Sunday cells.
   */
  public static final Color SUNDAY_BG = new Color(255, 240, 240);

  /**
   * Background for Saturday cells.
   */
  public static final Color SATURDAY_BG = new Color(240, 248, 255);

  /**
   * Background for weekday cells.
   */
  public static final Color WEEKDAY_CELL_BG = Color.WHITE;

  /**
   * Background for empty cells in calendar grid.
   */
  public static final Color EMPTY_CELL_BG = new Color(245, 245, 245);

  /**
   * Day header background colors (Sunday through Saturday).
   */
  public static final Color[] DAY_HEADER_COLORS = { new Color(255, 200, 200),
      new Color(200, 220, 255),
      new Color(200, 255, 200),
      new Color(255, 255, 200),
      new Color(255, 220, 200),
      new Color(220, 200, 255),
      new Color(200, 240, 255)
  };

  /**
   * Main calendar indicator color.
   */
  public static final Color CALENDAR_BLUE = new Color(65, 105, 225);

  /**
   * Color palette for multiple calendars.
   */
  public static final Color[] CALENDAR_PALETTE = { new Color(65, 105, 225),
      new Color(220, 20, 60),
      new Color(34, 139, 34),
      new Color(255, 140, 0),
      new Color(138, 43, 226),
      new Color(0, 128, 128),
      new Color(255, 20, 147)
  };

  /**
   * Panel background color.
   */
  public static final Color PANEL_BG = Color.WHITE;

  /**
   * Light gray background for panels and radio buttons.
   */
  public static final Color PANEL_BG_LIGHT = new Color(245, 245, 245);

  /**
   * Info panel background.
   */
  public static final Color INFO_PANEL_BG = new Color(240, 248, 255);

  /**
   * Header background.
   */
  public static final Color HEADER_BG = CALENDAR_BLUE;

  /**
   * Color for required field labels.
   */
  public static final Color REQUIRED_FIELD_LABEL = new Color(200, 0, 0);

  /**
   * Color for series indicator.
   */
  public static final Color SERIES_INDICATOR = new Color(200, 100, 0);

  /**
   * Normal text color.
   */
  public static final Color NORMAL_TEXT = Color.BLACK;

  /**
   * Disabled text color.
   */
  public static final Color DISABLED_TEXT = Color.GRAY;

  /**
   * Create Event button background color (blue).
   */
  public static final Color BUTTON_CREATE_EVENT_BG = new Color(66, 133, 244);

  /**
   * Create Series button background color (green).
   */
  public static final Color BUTTON_CREATE_SERIES_BG = new Color(52, 168, 83);

  /**
   * Edit Event button background color (yellow/orange).
   */
  public static final Color BUTTON_EDIT_EVENT_BG = new Color(251, 188, 5);

  /**
   * Save/Create button background color (green).
   */
  public static final Color BUTTON_SAVE_BG = new Color(52, 168, 83);

  /**
   * Cancel button background color (red).
   */
  public static final Color BUTTON_CANCEL_BG = new Color(234, 67, 53);

  /**
   * Button text color.
   */
  public static final Color BUTTON_TEXT = Color.BLACK;

  /**
   * Navigation button active background.
   */
  public static final Color BUTTON_NAV_ACTIVE_BG = new Color(200, 200, 200);

  /**
   * Navigation button inactive background.
   */
  public static final Color BUTTON_NAV_INACTIVE_BG = new Color(150, 150, 150);

  /**
   * Normal border color.
   */
  public static final Color NORMAL_BORDER = Color.GRAY;

  /**
   * Panel border color.
   */
  public static final Color PANEL_BORDER = Color.LIGHT_GRAY;

  /**
   * Gets the background color for a specific day of week cell.
   *
   * @param dayOfWeek 0=Sunday, 6=Saturday
   * @return background color for that day
   */
  public static Color getCellBackground(int dayOfWeek) {
    if (dayOfWeek == 0) {
      return SUNDAY_BG;
    } else if (dayOfWeek == 6) {
      return SATURDAY_BG;
    }
    return WEEKDAY_CELL_BG;
  }

  /**
   * Gets a calendar color by index.
   * Wraps around if index exceeds palette size.
   *
   * @param index the index (0-based)
   * @return color from the palette
   */
  public static Color getCalendarColor(int index) {
    return CALENDAR_PALETTE[index % CALENDAR_PALETTE.length];
  }

  private CalendarTheme() {

  }
}
