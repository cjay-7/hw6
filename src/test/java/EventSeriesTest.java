import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.model.EventSeries;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;

/**
 * Tests for EventSeries class.
 */
public class EventSeriesTest {

  @Test
  public void testEventSeriesCreationWithOccurrences() {
    EventInterface template = new Event("Standup",
        LocalDateTime.of(2025, 6, 2, 9, 0),
        LocalDateTime.of(2025, 6, 2, 9, 30),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(DayOfWeek.MONDAY);
    weekdays.add(DayOfWeek.WEDNESDAY);

    EventSeries series = new EventSeries(seriesId, template, weekdays, null, 5, false);

    assertEquals(seriesId, series.getSeriesId());
    assertEquals(template, series.getTemplate());
    assertEquals(weekdays, series.getWeekdays());
    assertNull(series.getEndDate());
    assertEquals(Integer.valueOf(5), series.getOccurrences());
    assertFalse(series.usesEndDate());
  }

  @Test
  public void testEventSeriesCreationWithEndDate() {
    EventInterface template = new Event("Sync",
        LocalDateTime.of(2025, 6, 3, 14, 0),
        LocalDateTime.of(2025, 6, 3, 15, 0),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(DayOfWeek.TUESDAY);
    LocalDate endDate = LocalDate.of(2025, 6, 30);

    EventSeries series = new EventSeries(seriesId, template, weekdays, endDate, null, true);

    assertEquals(seriesId, series.getSeriesId());
    assertEquals(endDate, series.getEndDate());
    assertNull(series.getOccurrences());
    assertTrue(series.usesEndDate());
  }

  @Test
  public void testEventSeriesGetters() {
    EventInterface template = new Event("Test",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(DayOfWeek.FRIDAY);

    EventSeries series = new EventSeries(seriesId, template, weekdays, null, 3, false);

    assertNotNull(series.getSeriesId());
    assertNotNull(series.getTemplate());
    assertNotNull(series.getWeekdays());
    assertEquals(1, series.getWeekdays().size());
    assertTrue(series.getWeekdays().contains(DayOfWeek.FRIDAY));
  }

  @Test
  public void testEventSeriesWithMultipleWeekdays() {
    EventInterface template = new Event("Multi Day",
        LocalDateTime.of(2025, 6, 16, 10, 0),
        LocalDateTime.of(2025, 6, 16, 11, 0),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(DayOfWeek.MONDAY);
    weekdays.add(DayOfWeek.WEDNESDAY);
    weekdays.add(DayOfWeek.FRIDAY);

    EventSeries series = new EventSeries(seriesId, template, weekdays, null, 6, false);

    assertEquals(3, series.getWeekdays().size());
    assertTrue(series.getWeekdays().contains(DayOfWeek.MONDAY));
    assertTrue(series.getWeekdays().contains(DayOfWeek.WEDNESDAY));
    assertTrue(series.getWeekdays().contains(DayOfWeek.FRIDAY));
  }

  @Test
  public void testEventSeriesWithAllWeekdays() {
    EventInterface template = new Event("Daily",
        LocalDateTime.of(2025, 6, 16, 10, 0),
        LocalDateTime.of(2025, 6, 16, 11, 0),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      weekdays.add(day);
    }

    EventSeries series = new EventSeries(seriesId, template, weekdays, null, 7, false);

    assertEquals(7, series.getWeekdays().size());
  }

  @Test
  public void testEventSeriesWithEndDateAndNoOccurrences() {
    EventInterface template = new Event("Until End",
        LocalDateTime.of(2025, 6, 16, 10, 0),
        LocalDateTime.of(2025, 6, 16, 11, 0),
        null, null, false, UUID.randomUUID(), null);
    UUID seriesId = UUID.randomUUID();
    Set<DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(DayOfWeek.MONDAY);
    LocalDate endDate = LocalDate.of(2025, 12, 31);

    EventSeries series = new EventSeries(seriesId, template, weekdays, endDate, null, true);

    assertEquals(endDate, series.getEndDate());
    assertNull(series.getOccurrences());
    assertTrue(series.usesEndDate());
  }
}
