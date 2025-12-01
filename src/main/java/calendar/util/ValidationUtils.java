package calendar.util;

/**
 * Utility class for common validation operations.
 * Eliminates code duplication across validation logic.
 */
public final class ValidationUtils {

  /**
   * Checks if a string is null or empty (after trimming).
   *
   * @param str the string to check
   * @return true if null or empty, false otherwise
   */
  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Checks if a string is not null and not empty (after trimming).
   *
   * @param str the string to check
   * @return true if has content, false if null or empty
   */
  public static boolean hasContent(String str) {
    return !isNullOrEmpty(str);
  }

  /**
   * Validates that a string is not null or empty, throwing exception if invalid.
   *
   * @param str the string to validate
   * @param fieldName the name of the field for error messages
   * @throws IllegalArgumentException if string is null or empty
   */
  public static void requireNonEmpty(String str, String fieldName) {
    if (isNullOrEmpty(str)) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  /**
   * Validates that a string is not null or empty, returning error message if invalid.
   *
   * @param str the string to validate
   * @param fieldName the name of the field for error messages
   * @return null if valid, error message if invalid
   */
  public static String validateNonEmpty(String str, String fieldName) {
    if (isNullOrEmpty(str)) {
      return fieldName + " cannot be empty.";
    }
    return null;
  }

  private ValidationUtils() {
    
  }
}




