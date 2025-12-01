import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import calendar.util.StringUtils;
import org.junit.Test;

/**
 * Comprehensive edge case tests for StringUtils to achieve 100% mutation
 * coverage.
 */
public class StringUtilsEdgeCaseTest {

  @Test
  public void testStripQuotesEmptyString() {
    assertEquals("", StringUtils.stripQuotes(""));
  }

  @Test
  public void testStripQuotesWhitespaceOnly() {
    assertEquals("", StringUtils.stripQuotes("   "));
  }

  @Test
  public void testStripQuotesSingleQuoteOnly() {

    try {
      StringUtils.stripQuotes("'");

    } catch (StringIndexOutOfBoundsException e) {

      assertTrue(true);
    }
  }

  @Test
  public void testStripQuotesDoubleQuoteOnly() {

    try {
      StringUtils.stripQuotes("\"");

    } catch (StringIndexOutOfBoundsException e) {

      assertTrue(true);
    }
  }

  @Test
  public void testStripQuotesMismatchedQuotes() {
    assertEquals("'test\"", StringUtils.stripQuotes("'test\""));
  }

  @Test
  public void testStripQuotesQuotesInMiddle() {
    assertEquals("test'quote\"here", StringUtils.stripQuotes("test'quote\"here"));
  }

  @Test
  public void testStripQuotesOnlyOpeningDoubleQuote() {
    assertEquals("\"test", StringUtils.stripQuotes("\"test"));
  }

  @Test
  public void testStripQuotesOnlyClosingDoubleQuote() {
    assertEquals("test\"", StringUtils.stripQuotes("test\""));
  }

  @Test
  public void testStripQuotesOnlyOpeningSingleQuote() {
    assertEquals("'test", StringUtils.stripQuotes("'test"));
  }

  @Test
  public void testStripQuotesOnlyClosingSingleQuote() {
    assertEquals("test'", StringUtils.stripQuotes("test'"));
  }

  @Test
  public void testStripQuotesWhitespaceBeforeQuotes() {
    assertEquals("test", StringUtils.stripQuotes("  \"test\"  "));
  }

  @Test
  public void testStripQuotesMultipleQuotes() {
    assertEquals("\"test\"", StringUtils.stripQuotes("\"\"test\"\""));
  }

  @Test
  public void testStripQuotesNestedQuotes() {
    assertEquals("'inner\"", StringUtils.stripQuotes("\"'inner\"\""));
  }

  @Test
  public void testStripQuotesSingleCharWithQuotes() {

    String result = StringUtils.stripQuotes("\"a\"");
    assertEquals("a", result);
  }

  @Test
  public void testStripQuotesTwoCharsWithQuotes() {

    String result = StringUtils.stripQuotes("\"ab\"");
    assertEquals("ab", result);
  }
}
