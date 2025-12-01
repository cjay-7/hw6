package calendar.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Parameter object for event series creation.
 * Reduces the 9-parameter method signature to a single object,
 * improving code readability and maintainability.
 */
public class SeriesCreationParameters {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String location;
  private final String description;
  private final boolean isPrivate;
  private final Set<DayOfWeek> weekdays;
  private final LocalDate endDate;
  private final Integer occurrences;

  /**
   * Creates series creation parameters.
   *
   * @param subject      the event subject
   * @param start        the start date-time
   * @param end          the end date-time
   * @param location     the location (optional)
   * @param description  the description (optional)
   * @param isPrivate    whether the event is private
   * @param weekdays     the days of week for recurrence
   * @param endDate      the series end date (optional if using occurrences)
   * @param occurrences  the number of occurrences (optional if using endDate)
   */
  public SeriesCreationParameters(String subject, LocalDateTime start, LocalDateTime end,
                                  String location, String description, boolean isPrivate,
                                  Set<DayOfWeek> weekdays,
                                  LocalDate endDate, Integer occurrences) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.location = location;
    this.description = description;
    this.isPrivate = isPrivate;
    this.weekdays = weekdays;
    this.endDate = endDate;
    this.occurrences = occurrences;
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

  public Set<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public Integer getOccurrences() {
    return occurrences;
  }
}
