package calendar.command;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.view.ViewInterface;
import java.io.IOException;

/**
 * Helper class providing common functionality for command implementations.
 * Eliminates boilerplate code that appears repeatedly across multiple commands.
 *
 * <p>DESIGN RATIONALE:
 * - Reduces code duplication across 12+ command classes
 * - Centralizes error messaging for consistency
 * - Simplifies command implementation
 * - Makes it easier to maintain and update common behavior
 */
public class CommandHelper {

  /**
   * Private constructor to prevent instantiation of utility class.
   */
  private CommandHelper() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Retrieves the current calendar's model, displaying an error if no calendar is selected.
   * This eliminates the repetitive null-checking pattern found in many commands.
   *
   * @param manager the calendar manager
   * @param view the view for displaying error messages
   * @return the current calendar's model, or null if no calendar is currently in use
   * @throws IOException if I/O fails
   */
  public static CalendarModelInterface getCurrentModel(CalendarManager manager,
                                                         ViewInterface view) throws IOException {
    CalendarInterface currentCal = manager.getCurrentCalendar();
    if (currentCal == null) {
      view.displayError("No calendar is currently in use. "
          + "Use 'use calendar --name <name>' first.");
      return null;
    }
    return currentCal.getModel();
  }

  /**
   * Retrieves the current calendar, displaying an error if no calendar is selected.
   *
   * @param manager the calendar manager
   * @param view the view for displaying error messages
   * @return the current calendar, or null if no calendar is currently in use
   * @throws IOException if I/O fails
   */
  public static CalendarInterface getCurrentCalendar(CalendarManager manager, ViewInterface view)
      throws IOException {
    CalendarInterface currentCal = manager.getCurrentCalendar();
    if (currentCal == null) {
      view.displayError("No calendar is currently in use. "
          + "Use 'use calendar --name <name>' first.");
      return null;
    }
    return currentCal;
  }
}
