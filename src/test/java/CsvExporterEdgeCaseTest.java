import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.util.CsvExporter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

/**
 * Comprehensive edge case tests for CsvExporter to achieve 100% mutation
 * coverage.
 */
public class CsvExporterEdgeCaseTest {

  @Test
  public void testEmptyEventList() {
    String csv = CsvExporter.toCsv(Collections.emptyList());
    assertTrue(csv.startsWith("Subject,Start Date,Start Time,End Date,End Time,"));
    assertTrue(csv.contains("All Day Event,Description,Location,Private"));

    String[] lines = csv.split("\n");
    assertEquals(1, lines.length);
  }

  @Test
  public void testEventWithNullDescription() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        null, "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains(",,"));
  }

  @Test
  public void testEventWithNullLocation() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", null, false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("Desc,,"));
  }

  @Test
  public void testEventWithCommaInSubject() {
    EventInterface e = new Event("Subject, with comma",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"Subject, with comma\""));
  }

  @Test
  public void testEventWithQuoteInSubject() {
    EventInterface e = new Event("Subject \"quoted\"",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"Subject \"\"quoted\"\"\""));
  }

  @Test
  public void testEventWithNewlineInDescription() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc\nwith newline", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"Desc\nwith newline\""));
  }

  @Test
  public void testEventWithCommaAndQuoteInLocation() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Room, \"101\"", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"Room, \"\"101\"\"\""));
  }

  @Test
  public void testEventWithAllSpecialCharacters() {
    EventInterface e = new Event("Subject, \"test\"",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc\nwith\nnewlines", "Room, \"101\"", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"Subject, \"\"test\"\"\""));
    assertTrue(csv.contains("\"Desc\nwith\nnewlines\""));
    assertTrue(csv.contains("\"Room, \"\"101\"\"\""));
  }

  @Test
  public void testMultipleEvents() {
    List<EventInterface> events = new ArrayList<>();
    events.add(new Event("Event1",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc1", "Loc1", false, UUID.randomUUID(), null));
    events.add(new Event("Event2",
        LocalDateTime.of(2025, 6, 2, 14, 0),
        LocalDateTime.of(2025, 6, 2, 15, 0),
        "Desc2", "Loc2", true, UUID.randomUUID(), null));
    String csv = CsvExporter.toCsv(events);
    String[] lines = csv.split("\n");
    assertEquals(3, lines.length);
    assertTrue(csv.contains("Event1"));
    assertTrue(csv.contains("Event2"));

    assertTrue(csv.contains("True") || csv.contains("False"));
  }

  @Test
  public void testEventWithPmTime() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 14, 0),
        LocalDateTime.of(2025, 6, 1, 15, 30),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("2:00 PM"));
    assertTrue(csv.contains("3:30 PM"));
  }

  @Test
  public void testEventWithMidnightTime() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 0, 0),
        LocalDateTime.of(2025, 6, 1, 1, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("12:00 AM"));
    assertTrue(csv.contains("1:00 AM"));
  }

  @Test
  public void testEventWithNoonTime() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 12, 0),
        LocalDateTime.of(2025, 6, 1, 13, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("12:00 PM"));
    assertTrue(csv.contains("1:00 PM"));
  }

  @Test
  public void testEventWithPrivateTrue() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", true, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains(",True\n") || csv.contains(",True\r\n"));
  }

  @Test
  public void testEventWithPrivateFalse() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains(",False\n") || csv.contains(",False\r\n"));
  }

  @Test
  public void testEventWithEmptyStringFields() {
    EventInterface e = new Event("Subject",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "", "", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("Subject,"));

    assertFalse(csv.contains("\"\""));
  }

  @Test
  public void testEventWithOnlyComma() {
    EventInterface e = new Event(",",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\",\""));
  }

  @Test
  public void testEventWithOnlyQuote() {
    EventInterface e = new Event("\"",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        "Desc", "Loc", false, UUID.randomUUID(), null);
    String csv = CsvExporter.toCsv(List.of(e));
    assertTrue(csv.contains("\"\"\"\""));
  }
}
