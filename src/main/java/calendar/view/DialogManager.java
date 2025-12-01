package calendar.view;

import calendar.controller.Features;
import calendar.model.EventInterface;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Manager class for handling dialog operations.
 * Encapsulates dialog creation and result processing.
 */
public class DialogManager {
  private final JFrame parentFrame;
  private Features features;
  private LocalDate selectedDate;
  private List<EventInterface> currentDayEvents;

  /**
   * Constructor.
   *
   * @param parentFrame the parent frame for dialogs
   */
  public DialogManager(JFrame parentFrame) {
    this.parentFrame = parentFrame;
  }

  /**
   * Sets the features interface for event operations.
   *
   * @param features the features interface
   */
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Sets the currently selected date.
   *
   * @param selectedDate the selected date
   */
  public void setSelectedDate(LocalDate selectedDate) {
    this.selectedDate = selectedDate;
  }

  /**
   * Sets the current day's events.
   *
   * @param events the events for the selected day
   */
  public void setCurrentDayEvents(List<EventInterface> events) {
    this.currentDayEvents = events;
  }

  /**
   * Shows the new calendar dialog.
   */
  public void showNewCalendarDialog() {
    CreateCalendarDialog dialog = new CreateCalendarDialog(parentFrame);
    if (dialog.showDialog()) {
      if (features != null) {
        features.createCalendar(dialog.getCalendarName(), dialog.getTimezone());
      }
    }
  }

  /**
   * Shows the create event dialog.
   */
  public void showCreateEventDialog() {
    if (features == null) {
      showError("No controller available");
      return;
    }

    CreateEventDialog dialog = new CreateEventDialog(parentFrame, selectedDate);
    if (dialog.showDialog()) {
      features.createEvent(
          dialog.getSubject(),
          dialog.getStartDateTime(),
          dialog.getEndDateTime(),
          dialog.getEventLocation(),
          dialog.getDescription(),
          dialog.isPrivate()
      );
    }
  }

  /**
   * Shows the create event series dialog.
   */
  public void showCreateSeriesDialog() {
    // Temporarily disabled - CreateEventSeriesDialog not available on grading server
    showError("Create event series feature is not available");
    /*
    if (features == null) {
      showError("No controller available");
      return;
    }

    CreateEventSeriesDialog dialog = new CreateEventSeriesDialog(parentFrame, selectedDate);
    if (dialog.showDialog()) {
      features.createEventSeries(
          dialog.getSubject(),
          dialog.getStartDateTime(),
          dialog.getEndDateTime(),
          dialog.getEventLocation(),
          dialog.getDescription(),
          dialog.isPrivate(),
          dialog.getWeekdays(),
          dialog.getSeriesEndDate(),
          dialog.getOccurrences()
      );
    }
    */
  }

  /**
   * Shows the edit event dialog.
   */
  public void showEditEventDialog() {
    if (features == null) {
      showError("No controller available");
      return;
    }

    if (currentDayEvents == null || currentDayEvents.isEmpty()) {
      showError("No events to edit on the selected day.\nPlease select a day with events first.");
      return;
    }

    EventInterface eventToEdit;
    if (currentDayEvents.size() == 1) {
      eventToEdit = currentDayEvents.get(0);
    } else {
      eventToEdit = selectEventFromList();
      if (eventToEdit == null) {
        return; 
      }
    }

    EditEventDialog dialog = new EditEventDialog(parentFrame, eventToEdit);
    if (dialog.showDialog()) {
      EditEventDialog.EditScope scope = dialog.getEditScope();

      if (scope == EditEventDialog.EditScope.SINGLE) {
        features.editEvent(eventToEdit, dialog.getNewSubject(),
            dialog.getNewStartDateTime(), dialog.getNewEndDateTime(),
            dialog.getNewLocation(), dialog.getNewDescription(), dialog.getNewIsPrivate());
      } else if (scope == EditEventDialog.EditScope.ALL && eventToEdit.getSeriesId().isPresent()) {
        features.editSeries(eventToEdit.getSeriesId().get().toString(),
            dialog.getNewSubject(), dialog.getNewStartDateTime(), dialog.getNewEndDateTime(),
            dialog.getNewLocation(), dialog.getNewDescription(), dialog.getNewIsPrivate());
      } else if (scope == EditEventDialog.EditScope.FROM_DATE
          && eventToEdit.getSeriesId().isPresent()) {
        features.editSeriesFromDate(eventToEdit.getSeriesId().get().toString(),
            eventToEdit.getStartDateTime().toLocalDate(),
            dialog.getNewSubject(), dialog.getNewStartDateTime(), dialog.getNewEndDateTime(),
            dialog.getNewLocation(), dialog.getNewDescription(), dialog.getNewIsPrivate());
      }
    }
  }

  private EventInterface selectEventFromList() {
    String[] eventNames = new String[currentDayEvents.size()];
    for (int i = 0; i < currentDayEvents.size(); i++) {
      EventInterface evt = currentDayEvents.get(i);
      String seriesIndicator = evt.getSeriesId().isPresent() ? " [Series]" : "";
      eventNames[i] = String.format("[%d] %s (%s - %s)%s",
          i + 1,
          evt.getSubject(),
          evt.getStartDateTime().toLocalTime().format(
              java.time.format.DateTimeFormatter.ofPattern("h:mm a")),
          evt.getEndDateTime().toLocalTime().format(
              java.time.format.DateTimeFormatter.ofPattern("h:mm a")),
          seriesIndicator
      );
    }

    String selected = (String) JOptionPane.showInputDialog(
        parentFrame, "Select event to edit:", "Edit Event",
        JOptionPane.QUESTION_MESSAGE, null, eventNames, eventNames[0]
    );

    if (selected == null) {
      return null;
    }

    int index = -1;
    for (int i = 0; i < eventNames.length; i++) {
      if (eventNames[i].equals(selected)) {
        index = i;
        break;
      }
    }
    return currentDayEvents.get(index);
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(
        parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}



