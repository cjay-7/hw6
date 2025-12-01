package calendar.controller;

import java.time.LocalDateTime;

/**
 * Parameter object for event creation.
 * Reduces parameter list clutter and provides better encapsulation.
 */
public class EventCreationParameters {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String location;
  private final String description;
  private final boolean isPrivate;

  /**
   * Creates event creation parameters.
   *
   * @param subject     the event subject
   * @param start       the start date-time
   * @param end         the end date-time
   * @param location    the location (optional)
   * @param description the description (optional)
   * @param isPrivate   whether the event is private
   */
  public EventCreationParameters(String subject, LocalDateTime start, LocalDateTime end,
                                 String location, String description, boolean isPrivate) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.location = location;
    this.description = description;
    this.isPrivate = isPrivate;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public String getLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public boolean isPrivate() {
    return isPrivate;
  }
}
