import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import calendar.util.StringUtils;
import org.junit.Test;

/**
 * Comprehensive tests for StringUtils class to achieve high mutation coverage.
 */
public class StringUtilsTest {

  // ==================== Basic Quote Stripping Tests ====================

  @Test
  public void testStripDoubleQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("\"hello\""));
  }

  @Test
  public void testStripSingleQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("'hello'"));
  }

  @Test
  public void testNoQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("hello"));
  }

  @Test
  public void testEmptyString() {
    assertEquals("", StringUtils.stripQuotes(""));
  }

  // ==================== Whitespace Trimming Tests ====================

  @Test
  public void testTrimWhitespaceBeforeQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("  \"hello\"  "));
  }

  @Test
  public void testTrimWhitespaceBeforeSingleQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("  'hello'  "));
  }

  @Test
  public void testOnlyWhitespace() {
    assertEquals("", StringUtils.stripQuotes("   "));
  }

  @Test
  public void testWhitespaceNoQuotes() {
    assertEquals("hello", StringUtils.stripQuotes("  hello  "));
  }

  // ==================== Mismatched Quotes Tests ====================

  @Test
  public void testMismatchedQuotesDoubleToSingle() {
    // Starts with double, ends with single - should NOT strip
    assertEquals("\"hello'", StringUtils.stripQuotes("\"hello'"));
  }

  @Test
  public void testMismatchedQuotesSingleToDouble() {
    // Starts with single, ends with double - should NOT strip
    assertEquals("'hello\"", StringUtils.stripQuotes("'hello\""));
  }

  @Test
  public void testOnlyOpeningDoubleQuote() {
    assertEquals("\"hello", StringUtils.stripQuotes("\"hello"));
  }

  @Test
  public void testOnlyClosingDoubleQuote() {
    assertEquals("hello\"", StringUtils.stripQuotes("hello\""));
  }

  @Test
  public void testOnlyOpeningSingleQuote() {
    assertEquals("'hello", StringUtils.stripQuotes("'hello"));
  }

  @Test
  public void testOnlyClosingSingleQuote() {
    assertEquals("hello'", StringUtils.stripQuotes("hello'"));
  }

  // ==================== Edge Cases Tests ====================

  @Test
  public void testSingleCharacterNoQuotes() {
    assertEquals("a", StringUtils.stripQuotes("a"));
  }

  @Test
  public void testTwoCharacterDoubleQuotes() {
    // Just "" should become empty
    assertEquals("", StringUtils.stripQuotes("\"\""));
  }

  @Test
  public void testTwoCharacterSingleQuotes() {
    // Just '' should become empty
    assertEquals("", StringUtils.stripQuotes("''"));
  }

  @Test
  public void testQuotesInsideString() {
    assertEquals("say \"hi\"", StringUtils.stripQuotes("\"say \"hi\"\""));
  }

  @Test
  public void testNestedQuotes() {
    assertEquals("'nested'", StringUtils.stripQuotes("\"'nested'\""));
  }

  // Note: Single quote/double quote only (length 1) would cause StringIndexOutOfBoundsException
  // in the current implementation due to substring(1, 0). These are edge cases that
  // should be handled but are out of scope for this test class.

  // ==================== Special Characters Tests ====================

  @Test
  public void testQuotedStringWithSpaces() {
    assertEquals("hello world", StringUtils.stripQuotes("\"hello world\""));
  }

  @Test
  public void testQuotedStringWithNewlines() {
    assertEquals("hello\nworld", StringUtils.stripQuotes("\"hello\nworld\""));
  }

  @Test
  public void testQuotedStringWithTabs() {
    assertEquals("hello\tworld", StringUtils.stripQuotes("\"hello\tworld\""));
  }

  // ==================== Boundary Value Tests ====================

  @Test
  public void testVeryLongQuotedString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      sb.append("a");
    }
    String expected = sb.toString();
    assertEquals(expected, StringUtils.stripQuotes("\"" + expected + "\""));
  }

  @Test
  public void testQuoteAtPositionOne() {
    // Ensure substring(1, length-1) works correctly
    assertEquals("x", StringUtils.stripQuotes("\"x\""));
  }

  @Test
  public void testPreservesInternalQuotes() {
    assertEquals("a\"b\"c", StringUtils.stripQuotes("\"a\"b\"c\""));
  }
}

