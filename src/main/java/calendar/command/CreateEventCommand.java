package calendar.command;

import calendar.model.Calendar;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.util.DateTimeParser;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Command to create a single event.
 */
public class CreateEventCommand extends BaseCommand {
  private final String subject;
  private final String from;
  private final String to;

  /**
   * Creates a CreateEventCommand.
   *
   * @param subject the event subject
   * @param from    the start datetime string
   * @param to      the end datetime string
   */
  public CreateEventCommand(String subject, String from, String to) {
    this.subject = subject;
    this.from = from;
    this.to = to;
  }

  @Override
  protected String getOperationName() {
    return "create event";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarModelInterface model = CommandHelper.getCurrentModel(manager, view);
    if (model == null) {
      return false;
    }

    LocalDateTime start = DateTimeParser.parseDateTime(from);
    LocalDateTime end = DateTimeParser.parseDateTime(to);
    EventInterface event =
        new Event(subject, start, end, null, null, false, UUID.randomUUID(), null);
    boolean ok = model.createEvent(event);
    if (ok) {
      view.displayMessage("Created event: " + subject);
    } else {
      view.displayError("Duplicate event: " + subject);
    }
    return ok;
  }
}
