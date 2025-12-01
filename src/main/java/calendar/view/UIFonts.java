package calendar.view;

import java.awt.Font;

/**
 * Centralized font constants for calendar UI.
 * Provides consistent typography across the application.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public final class UIFonts {

  /**
   * Standard font family.
   */
  public static final String FONT_FAMILY = "Arial";

  /**
   * Font for day header labels (Sun, Mon, etc.).
   */
  public static final Font DAY_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 12);

  /**
   * Font for normal day numbers in calendar.
   */
  public static final Font NORMAL_DAY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);

  /**
   * Font for days with events (bold).
   */
  public static final Font DAY_WITH_EVENTS_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);

  /**
   * Font for today's date.
   */
  public static final Font TODAY_FONT = new Font(FONT_FAMILY, Font.BOLD, 16);

  /**
   * Font for button text.
   */
  public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

  /**
   * Font for action buttons (larger, bold).
   */
  public static final Font ACTION_BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

  /**
   * Font for labels.
   */
  public static final Font LABEL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

  /**
   * Font for titles and headers.
   */
  public static final Font HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);

  /**
   * Font for dialog titles.
   */
  public static final Font DIALOG_TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 16);

  /**
   * Font for section headers.
   */
  public static final Font SECTION_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);

  /**
   * Font for event list text.
   */
  public static final Font EVENT_LIST_FONT = new Font("Monospaced", Font.PLAIN, 12);

  /**
   * Font for calendar selector.
   */
  public static final Font CALENDAR_SELECTOR_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);

  /**
   * Font for month/year display.
   */
  public static final Font MONTH_YEAR_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);

  private UIFonts() {

  }
}
