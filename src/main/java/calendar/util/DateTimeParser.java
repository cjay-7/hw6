package calendar.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Date/time parsing helpers per README formats.
 */
public final class DateTimeParser {

  /** Date format: yyyy-MM-dd. */
  public static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /** Time format: HH:mm. */
  public static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("HH:mm");

  /** DateTime format: yyyy-MM-ddTHH:mm. */
  public static final DateTimeFormatter DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  private DateTimeParser() {
  }

  /**
   * Parses a date string in format yyyy-MM-dd.
   *
   * @param dateString the date string
   * @return parsed LocalDate
   */
  public static LocalDate parseDate(String dateString) {
    return LocalDate.parse(dateString, DATE_FORMATTER);
  }

  /**
   * Parses a time string in format HH:mm.
   *
   * @param timeString the time string
   * @return parsed LocalTime
   */
  public static LocalTime parseTime(String timeString) {
    return LocalTime.parse(timeString, TIME_FORMATTER);
  }

  /**
   * Parses a datetime string in format yyyy-MM-ddTHH:mm.
   *
   * @param dateTime the datetime string
   * @return parsed LocalDateTime
   */
  public static LocalDateTime parseDateTime(String dateTime) {
    return LocalDateTime.parse(dateTime, DATETIME_FORMATTER);
  }
}
