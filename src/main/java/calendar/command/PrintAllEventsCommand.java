package calendar.command;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.model.EventInterface;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Command to print all events in the calendar.
 */
public class PrintAllEventsCommand extends BaseCommand {

  /**
   * Creates a PrintAllEventsCommand.
   */
  public PrintAllEventsCommand() {
  }

  @Override
  protected String getOperationName() {
    return "print all events";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarInterface currentCal = CommandHelper.getCurrentCalendar(manager, view);
    if (currentCal == null) {
      return false;
    }
    CalendarModelInterface model = currentCal.getModel();

    List<EventInterface> events = model.getAllEvents();
    view.displayEvents(events);
    return true;
  }
}

