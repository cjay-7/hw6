package calendar.model;

import java.time.ZoneId;

/**
 * Interface representing a calendar with a unique name, timezone, and associated model.
 *
 * <p>This interface defines the contract for calendar objects, allowing different
 * implementations such as regular calendars, shared calendars, or read-only calendars.
 *
 * <p>DESIGN RATIONALE:
 * - Follows the Dependency Inversion Principle by allowing CalendarManager to depend
 *   on this abstraction rather than the concrete Calendar class
 * - Enables polymorphism for different calendar types (e.g., SharedCalendar)
 * - Separates the calendar's identity (name, timezone) from its event management (model)
 */
public interface CalendarInterface {

  /**
   * Gets the name of this calendar.
   *
   * @return the calendar name, never null or empty
   */
  String getName();

  /**
   * Sets the name of this calendar.
   *
   * @param name the new name for this calendar
   * @throws IllegalArgumentException if name is null or empty
   */
  void setName(String name);

  /**
   * Gets the timezone of this calendar.
   *
   * @return the calendar timezone in IANA format, never null
   */
  ZoneId getTimezone();

  /**
   * Sets the timezone of this calendar.
   *
   * @param timezone the new timezone for this calendar
   * @throws IllegalArgumentException if timezone is null
   */
  void setTimezone(ZoneId timezone);

  /**
   * Gets the calendar model that manages events for this calendar.
   *
   * @return the calendar model, never null
   */
  CalendarModelInterface getModel();
}
