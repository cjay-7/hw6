package calendar.view;

import calendar.controller.Features;
import calendar.model.CalendarInterface;
import calendar.model.EventInterface;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for GUI view operations.
 */
public interface GuiViewInterface {

  /**
   * Adds features/controller callbacks to the view.
   *
   * @param features the features interface
   */
  void addFeatures(Features features);

  /**
   * Updates the calendar list display.
   *
   * @param calendars the list of calendars
   * @param currentCalendarName the current calendar name
   */
  void updateCalendarList(List<CalendarInterface> calendars, String currentCalendarName);

  /**
   * Displays a month view.
   *
   * @param year the year
   * @param month the month
   * @param daysWithEvents days that have events
   * @param selectedDate the currently selected date
   */
  void displayMonth(int year, int month, List<LocalDate> daysWithEvents,
                    LocalDate selectedDate);

  /**
   * Displays a week view.
   *
   * @param weekStart the start of the week
   * @param daysWithEvents days that have events
   * @param selectedDate the currently selected date
   */
  void displayWeek(LocalDate weekStart, List<LocalDate> daysWithEvents,
                   LocalDate selectedDate);

  /**
   * Displays events for a specific day.
   *
   * @param date the date
   * @param events the events for that day
   */
  void displayEventsForDay(LocalDate date, List<EventInterface> events);

  /**
   * Shows an error message.
   *
   * @param message the error message
   */
  void showError(String message);

  /**
   * Shows an informational message.
   *
   * @param message the message
   */
  void showMessage(String message);

  /**
   * Displays the view.
   */
  void display();

  /**
   * Gets the current month being displayed.
   *
   * @return array with year and month
   */
  int[] getCurrentMonth();

  /**
   * Gets the currently selected date.
   *
   * @return the selected date
   */
  LocalDate getSelectedDate();
}
