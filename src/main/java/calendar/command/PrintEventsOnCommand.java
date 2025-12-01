package calendar.command;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.model.EventInterface;
import calendar.util.DateTimeParser;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Command to print events on a specific date.
 */
public class PrintEventsOnCommand extends BaseCommand {
  private final String dateString;

  /**
   * Creates a PrintEventsOnCommand.
   *
   * @param dateString the date string in format YYYY-MM-DD
   */
  public PrintEventsOnCommand(String dateString) {
    this.dateString = dateString;
  }

  @Override
  protected String getOperationName() {
    return "print events";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarInterface currentCal = CommandHelper.getCurrentCalendar(manager, view);
    if (currentCal == null) {
      return false;
    }
    CalendarModelInterface model = currentCal.getModel();

    LocalDate date = DateTimeParser.parseDate(dateString);
    List<EventInterface> events = model.getEventsOnDate(date);
    view.displayEvents(events);
    return true;
  }
}

