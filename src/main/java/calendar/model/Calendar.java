package calendar.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents a calendar with a unique name, timezone, and associated calendar
 * model.
 * Each calendar manages its own set of events through its CalendarModel.
 *
 * <p>DESIGN RATIONALE:
 * - Calendar acts as a container that associates a name and timezone with a
 * CalendarModel
 * - This separation allows multiple calendars to exist independently
 * - ZoneId from java.time provides robust timezone handling with IANA database
 * support
 * - Validation ensures calendar integrity (non-null, non-empty names)
 *
 * <p>REPRESENTATION INVARIANTS:
 * - name must not be null or empty
 * - timezone must not be null
 * - model must not be null
 */
public class Calendar implements CalendarInterface {
  private String name;
  private ZoneId timezone;
  private final CalendarModelInterface model;

  /**
   * Creates a new Calendar with the specified name, timezone, and model.
   *
   * @param name     the unique name for this calendar
   * @param timezone the timezone for this calendar (IANA format)
   * @param model    the calendar model that manages events
   * @throws IllegalArgumentException if name is null/empty, or timezone/model is
   *                                  null
   */
  public Calendar(String name, ZoneId timezone, CalendarModelInterface model) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Calendar name cannot be null or empty");
    }
    if (timezone == null) {
      throw new IllegalArgumentException("Calendar timezone cannot be null");
    }
    if (model == null) {
      throw new IllegalArgumentException("Calendar model cannot be null");
    }

    this.name = name;
    this.timezone = timezone;
    this.model = model;
  }

  /**
   * Gets the name of this calendar.
   *
   * @return the calendar name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this calendar.
   *
   * @param name the new name for this calendar
   * @throws IllegalArgumentException if name is null or empty
   */
  @Override
  public void setName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Calendar name cannot be null or empty");
    }
    this.name = name;
  }

  /**
   * Gets the timezone of this calendar.
   *
   * @return the calendar timezone
   */
  @Override
  public ZoneId getTimezone() {
    return timezone;
  }

  /**
   * Sets the timezone of this calendar and converts all event times.
   * Event times are adjusted so they represent the same instant in the new
   * timezone.
   *
   * <p>For example, if an event is at 10:00 AM in America/New_York and the
   * timezone is changed to Europe/Paris, the event will be at 4:00 PM Paris time
   * (the same instant in time).
   *
   * @param timezone the new timezone for this calendar
   * @throws IllegalArgumentException if timezone is null
   */
  @Override
  public void setTimezone(ZoneId timezone) {
    if (timezone == null) {
      throw new IllegalArgumentException("Calendar timezone cannot be null");
    }
    if (this.timezone.equals(timezone)) {
      return;
    }

    ZoneId oldTimezone = this.timezone;
    convertEventTimes(oldTimezone, timezone);

    this.timezone = timezone;
  }

  /**
   * Converts all event times from one timezone to another.
   *
   * @param fromZone the original timezone
   * @param toZone   the new timezone
   */
  private void convertEventTimes(ZoneId fromZone, ZoneId toZone) {
    List<EventInterface> events = model.getAllEvents();
    for (EventInterface event : events) {
      LocalDateTime oldStart = event.getStartDateTime();
      LocalDateTime oldEnd = event.getEndDateTime();

      ZonedDateTime zonedStart = oldStart.atZone(fromZone);
      LocalDateTime newStart = zonedStart.withZoneSameInstant(toZone).toLocalDateTime();

      ZonedDateTime zonedEnd = oldEnd.atZone(fromZone);
      LocalDateTime newEnd = zonedEnd.withZoneSameInstant(toZone).toLocalDateTime();

      EditSpec spec = new EditSpec(null, newStart, newEnd, null, null, null);
      model.editEvent(event.getId(), spec);
    }
  }

  /**
   * Gets the calendar model that manages events for this calendar.
   *
   * @return the calendar model
   */
  @Override
  public CalendarModelInterface getModel() {
    return model;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Calendar calendar = (Calendar) o;
    return Objects.equals(name, calendar.name)
        && Objects.equals(timezone, calendar.timezone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, timezone);
  }

  @Override
  public String toString() {
    return "Calendar{name='" + name + "', timezone=" + timezone + "}";
  }
}
