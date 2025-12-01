package calendar.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for validating and parsing date/time input from UI fields.
 * Reduces code duplication across dialog classes and controller.
 */
public final class DateTimeValidator {

  private static final DateTimeFormatter DATE_FORMATTER = 
      DateTimeFormatter.ofPattern(UIConstants.DATE_FORMAT_PATTERN);
  private static final DateTimeFormatter TIME_FORMATTER = 
      DateTimeFormatter.ofPattern(UIConstants.TIME_FORMAT_PATTERN);

  private DateTimeValidator() {
    
  }

  /**
   * Result of date/time validation.
   */
  public static class ValidationResult<T> {
    private final boolean valid;
    private final T value;
    private final String errorMessage;

    private ValidationResult(boolean valid, T value, String errorMessage) {
      this.valid = valid;
      this.value = value;
      this.errorMessage = errorMessage;
    }

    /**
     * Creates a successful validation result.
     *
     * @param <T> the type of the value
     * @param value the validated value
     * @return a successful validation result
     */
    public static <T> ValidationResult<T> success(T value) {
      return new ValidationResult<>(true, value, null);
    }

    /**
     * Creates a failed validation result.
     *
     * @param <T> the type of the value
     * @param errorMessage the error message
     * @return a failed validation result
     */
    public static <T> ValidationResult<T> failure(String errorMessage) {
      return new ValidationResult<>(false, null, errorMessage);
    }

    public boolean isValid() {
      return valid;
    }

    public T getValue() {
      return value;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
  }

  /**
   * Parses a date string.
   *
   * @param dateStr the date string to parse
   * @return validation result with parsed date or error message
   */
  public static ValidationResult<LocalDate> parseDate(String dateStr) {
    if (dateStr == null || dateStr.trim().isEmpty()) {
      return ValidationResult.failure(UIMessages.ERROR_INVALID_DATE_FORMAT);
    }

    try {
      LocalDate date = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
      return ValidationResult.success(date);
    } catch (DateTimeParseException e) {
      return ValidationResult.failure(UIMessages.ERROR_INVALID_DATE_FORMAT);
    }
  }

  /**
   * Parses a time string.
   *
   * @param timeStr the time string to parse
   * @return validation result with parsed time or error message
   */
  public static ValidationResult<LocalTime> parseTime(String timeStr) {
    if (timeStr == null || timeStr.trim().isEmpty()) {
      return ValidationResult.failure(UIMessages.ERROR_INVALID_DATE_FORMAT);
    }

    try {
      LocalTime time = LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
      return ValidationResult.success(time);
    } catch (DateTimeParseException e) {
      return ValidationResult.failure(UIMessages.ERROR_INVALID_DATE_FORMAT);
    }
  }

  /**
   * Parses date and time strings into a LocalDateTime.
   *
   * @param dateStr the date string
   * @param timeStr the time string
   * @return validation result with parsed LocalDateTime or error message
   */
  public static ValidationResult<LocalDateTime> parseDateTime(String dateStr, String timeStr) {
    ValidationResult<LocalDate> dateResult = parseDate(dateStr);
    if (!dateResult.isValid()) {
      return ValidationResult.failure(dateResult.getErrorMessage());
    }

    ValidationResult<LocalTime> timeResult = parseTime(timeStr);
    if (!timeResult.isValid()) {
      return ValidationResult.failure(timeResult.getErrorMessage());
    }

    LocalDateTime dateTime = LocalDateTime.of(dateResult.getValue(), timeResult.getValue());
    return ValidationResult.success(dateTime);
  }

  /**
   * Validates that end time is after start time.
   *
   * @param start the start date-time
   * @param end the end date-time
   * @return validation result
   */
  public static ValidationResult<LocalDateTime> validateTimeRange(LocalDateTime start, 
                                                                  LocalDateTime end) {
    if (start == null || end == null) {
      return ValidationResult.failure(UIMessages.ERROR_INVALID_TIMES);
    }

    if (!end.isAfter(start)) {
      return ValidationResult.failure(UIMessages.ERROR_END_BEFORE_START);
    }

    return ValidationResult.success(end);
  }

  /**
   * Converts empty string to null.
   *
   * @param str the string to convert
   * @return null if empty/whitespace, otherwise trimmed string
   */
  public static String emptyToNull(String str) {
    return (str == null || str.trim().isEmpty()) ? null : str.trim();
  }
}

