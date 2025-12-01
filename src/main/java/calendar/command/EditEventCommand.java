package calendar.command;

import calendar.model.Calendar;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.model.EditSpec;
import calendar.model.EventInterface;
import calendar.util.DateTimeParser;
import calendar.util.EditSpecFactory;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Command to edit a single event.
 * Format: edit event property subject from start to end with new value
 */
public class EditEventCommand extends BaseCommand {
  private final String property;
  private final String subject;
  private final String startString;
  private final String endString;
  private final String newValue;

  /**
   * Creates an EditEventCommand.
   *
   * @param property    the property to edit (subject, start, end, description, location, status)
   * @param subject     the event subject to find
   * @param startString the start datetime string
   * @param endString   the end datetime string
   * @param newValue    the new value for the property
   */
  public EditEventCommand(String property, String subject, String startString, String endString,
                          String newValue) {
    this.property = property.toLowerCase();
    this.subject = subject;
    this.startString = startString;
    this.endString = endString;
    this.newValue = newValue;
  }

  @Override
  protected String getOperationName() {
    return "edit event";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarModelInterface model = CommandHelper.getCurrentModel(manager, view);
    if (model == null) {
      return false;
    }

    LocalDateTime start = DateTimeParser.parseDateTime(startString);
    LocalDateTime end = DateTimeParser.parseDateTime(endString);
    EventInterface event = model.findEventByProperties(subject, start, end);

    if (event == null) {
      view.displayError("Event not found: " + subject);
      return false;
    }

    EditSpec spec = EditSpecFactory.createEditSpec(property, newValue);

    boolean success = model.editEvent(event.getId(), spec);
    if (success) {
      view.displayMessage("Event edited successfully");
    } else {
      view.displayError("Failed to edit: would create duplicate event");
    }
    return success;
  }
}

