package calendar.command;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.util.DateTimeParser;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Command to show busy status at a specific date/time.
 */
public class ShowStatusCommand extends BaseCommand {
  private final String dateTimeString;

  /**
   * Creates a ShowStatusCommand.
   *
   * @param dateTimeString the datetime string
   */
  public ShowStatusCommand(String dateTimeString) {
    this.dateTimeString = dateTimeString;
  }

  @Override
  protected String getOperationName() {
    return "show status";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarInterface currentCal = CommandHelper.getCurrentCalendar(manager, view);
    if (currentCal == null) {
      return false;
    }
    CalendarModelInterface model = currentCal.getModel();

    LocalDateTime dateTime = DateTimeParser.parseDateTime(dateTimeString);
    boolean isBusy = model.isBusy(dateTime);
    String status = isBusy ? "busy" : "available";
    view.displayMessage(status);
    return true;
  }
}

