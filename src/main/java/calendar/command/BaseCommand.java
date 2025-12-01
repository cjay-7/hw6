package calendar.command;

import calendar.model.CalendarManager;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Abstract base class for commands that provides common error handling.
 * Eliminates duplication of try-catch blocks across all command implementations.
 * Uses the Template Method pattern to standardize error handling.
 */
public abstract class BaseCommand implements CommandInterface {

  /**
   * Template method that executes command logic with standardized error handling.
   *
   * @param manager the calendar manager
   * @param view the view for displaying messages
   * @return true if command should continue, false otherwise
   * @throws IOException if I/O error occurs
   */
  @Override
  public final boolean execute(CalendarManager manager, ViewInterface view) throws IOException {
    return executeWithErrorHandling(manager, view, getOperationName());
  }

  /**
   * Subclasses implement this method with their specific command logic.
   * No need to handle standard exceptions - they're caught by the template method.
   *
   * @param manager the calendar manager
   * @param view the view for displaying messages
   * @return true if command should continue, false otherwise
   * @throws IOException if I/O error occurs
   * @throws DateTimeParseException if date/time parsing fails
   * @throws IllegalArgumentException if validation fails
   */
  protected abstract boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException;

  /**
   * Returns the name of the operation for error messages.
   * Example: "create event", "edit series", "export calendar"
   *
   * @return operation name
   */
  protected abstract String getOperationName();

  /**
   * Executes operation with standardized error handling.
   *
   * @param manager the calendar manager
   * @param view the view for displaying errors
   * @param operationName name of operation for error messages
   * @return result of operation
   * @throws IOException if I/O error occurs
   */
  private boolean executeWithErrorHandling(CalendarManager manager,
                                          ViewInterface view,
                                          String operationName) throws IOException {
    try {
      return doExecute(manager, view);
    } catch (DateTimeParseException e) {
      view.displayError("Failed to " + operationName);
      return false;
    } catch (IllegalArgumentException e) {
      view.displayError("Failed to " + operationName + ": " + e.getMessage());
      return false;
    } catch (IOException e) {
      view.displayError("Failed to " + operationName + ": I/O error - " + e.getMessage());
      throw e;
    }
  }
}
