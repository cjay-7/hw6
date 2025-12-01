import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.model.CalendarModel;
import calendar.model.CalendarModelInterface;
import calendar.model.EditSpec;
import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.model.EventSeries;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;

/**
 * Example test showing how to create, query, and test events.
 */
public class CalendarModelTest {

  @Test
  public void testCreateSingleEvent() {

    CalendarModelInterface model = new CalendarModel();

    LocalDateTime start = LocalDateTime.of(2025, 5, 5, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 5, 5, 11, 0);

    EventInterface event = new Event("Team Meeting", start, end,
        "Discuss project progress", "Conference Room A", false,
        UUID.randomUUID(), null);

    boolean success = model.createEvent(event);
    assertTrue("Event should be created", success);

    List<EventInterface> eventsOnDate = model.getEventsOnDate(LocalDate.of(2025, 5, 5));
    assertEquals("Should have one event", 1, eventsOnDate.size());
    assertEquals("Event subject should match", "Team Meeting", eventsOnDate.get(0).getSubject());
  }

  @Test
  public void testCreateEventSeries() {
    CalendarModelInterface model = new CalendarModel();

    LocalDateTime start = LocalDateTime.of(2025, 5, 5, 14, 0);
    LocalDateTime end = LocalDateTime.of(2025, 5, 5, 15, 0);

    EventInterface template = new Event("Weekly Standup", start, end,
        "Team standup meeting", "Zoom", false, UUID.randomUUID(), null);

    UUID seriesId = UUID.randomUUID();
    Set<java.time.DayOfWeek> weekdays = new HashSet<>();
    weekdays.add(java.time.DayOfWeek.MONDAY);
    weekdays.add(java.time.DayOfWeek.WEDNESDAY);

    EventSeries series = new EventSeries(seriesId, template, weekdays, null,

        6,
        false);

    boolean success = model.createEventSeries(series);
    assertTrue("Series should be created", success);

    List<EventInterface> allEvents = model.getEventsInRange(LocalDateTime.of(2025, 5, 1, 0, 0),
        LocalDateTime.of(2025, 6, 1, 0, 0));

    long count = allEvents.stream().filter(e -> e.getSubject().equals("Weekly Standup")).count();

    assertEquals("Should have 6 occurrences", 6, count);
  }

  @Test
  public void testQueryEventsOnDate() {
    CalendarModelInterface model = new CalendarModel();

    LocalDate testDate = LocalDate.of(2025, 5, 10);

    EventInterface event1 = new Event("Morning Meeting",
        LocalDateTime.of(testDate, java.time.LocalTime.of(9, 0)),
        LocalDateTime.of(testDate, java.time.LocalTime.of(10, 0)),
        null, null, false, UUID.randomUUID(), null);

    EventInterface event2 = new Event("Lunch Break",
        LocalDateTime.of(testDate, java.time.LocalTime.of(12, 0)),
        LocalDateTime.of(testDate, java.time.LocalTime.of(13, 0)),
        null, null, false, UUID.randomUUID(), null);

    model.createEvent(event1);
    model.createEvent(event2);

    List<EventInterface> events = model.getEventsOnDate(testDate);
    assertEquals("Should have 2 events", 2, events.size());

    assertTrue(events.get(0).getStartDateTime().isBefore(events.get(1).getStartDateTime()));
  }

  @Test
  public void testQueryEventsInRange() {
    CalendarModelInterface model = new CalendarModel();

    LocalDateTime day1 = LocalDateTime.of(2025, 5, 10, 10, 0);
    LocalDateTime day2 = LocalDateTime.of(2025, 5, 12, 10, 0);
    LocalDateTime day3 = LocalDateTime.of(2025, 5, 15, 10, 0);

    model.createEvent(
        new Event("Event 1", day1, day1.plusHours(1), null, null, false, UUID.randomUUID(), null));
    model.createEvent(
        new Event("Event 2", day2, day2.plusHours(1), null, null, false, UUID.randomUUID(), null));
    model.createEvent(
        new Event("Event 3", day3, day3.plusHours(1), null, null, false, UUID.randomUUID(), null));

    List<EventInterface> events = model.getEventsInRange(LocalDateTime.of(2025, 5, 11, 0, 0),
        LocalDateTime.of(2025, 5, 14, 23, 59));

    assertEquals("Should have 1 event in range", 1, events.size());
    assertEquals("Should be Event 2", "Event 2", events.get(0).getSubject());
  }

  @Test
  public void testIsBusy() {
    CalendarModelInterface model = new CalendarModel();

    LocalDateTime eventStart = LocalDateTime.of(2025, 5, 10, 10, 0);
    LocalDateTime eventEnd = LocalDateTime.of(2025, 5, 10, 11, 0);

    model.createEvent(
        new Event("Meeting", eventStart, eventEnd, null, null, false, UUID.randomUUID(), null));

    assertTrue("Should be busy at 10:30", model.isBusy(LocalDateTime.of(2025, 5, 10, 10, 30)));

    assertFalse("Should not be busy at 9:00", model.isBusy(LocalDateTime.of(2025, 5, 10, 9, 0)));

    assertFalse("Should not be busy at 12:00", model.isBusy(LocalDateTime.of(2025, 5, 10, 12, 0)));
  }

  @Test
  public void testEditEvent() {
    CalendarModelInterface model = new CalendarModel();

    EventInterface event = new Event("Old Subject", LocalDateTime.of(2025, 5, 10, 10, 0),
        LocalDateTime.of(2025, 5, 10, 11, 0), "Old description", "Old location", false,
        UUID.randomUUID(), null);

    model.createEvent(event);
    UUID eventId = event.getId();

    EditSpec spec = new EditSpec("New Subject",
        null,
        null,
        "New description",
        null,
        null);

    boolean success = model.editEvent(eventId, spec);
    assertTrue("Edit should succeed", success);

    EventInterface edited = model.findEventById(eventId);
    assertNotNull("Event should exist", edited);
    assertEquals("Subject should be updated", "New Subject", edited.getSubject());
    assertEquals("Description should be updated", "New description",
        edited.getDescription().orElse(""));
  }

  @Test
  public void testFindEventByProperties() {
    CalendarModelInterface model = new CalendarModel();

    EventInterface event = new Event("Search Test", LocalDateTime.of(2025, 5, 10, 10, 0),
        LocalDateTime.of(2025, 5, 10, 11, 0), null, null, false, UUID.randomUUID(), null);

    model.createEvent(event);

    EventInterface found = model.findEventByProperties("Search Test",
        LocalDateTime.of(2025, 5, 10, 10, 0),
        LocalDateTime.of(2025, 5, 10, 11, 0));

    assertNotNull("Event should be found", found);
    assertEquals("Should match", event.getId(), found.getId());
  }

  @Test
  public void testFindEventByPropertiesNotFound() {
    CalendarModelInterface model = new CalendarModel();
    EventInterface result = model.findEventByProperties("NonExistent",
        LocalDateTime.of(2025, 1, 1, 10, 0),
        LocalDateTime.of(2025, 1, 1, 11, 0));
    assertNull("Should return null for non-existent event", result);
  }

  @Test
  public void testFindEventByIdNotFound() {
    CalendarModelInterface model = new CalendarModel();
    EventInterface result = model.findEventById(UUID.randomUUID());
    assertNull("Should return null for non-existent event ID", result);
  }

  @Test
  public void testGetEventsInRangeEmpty() {
    CalendarModelInterface model = new CalendarModel();
    List<EventInterface> events = model.getEventsInRange(
        LocalDateTime.of(2025, 1, 1, 0, 0),
        LocalDateTime.of(2025, 1, 31, 23, 59));
    assertTrue("Empty model should return empty list", events.isEmpty());
  }

  @Test
  public void testIsBusyFalseWhenNoEvents() {
    CalendarModelInterface model = new CalendarModel();
    boolean busy = model.isBusy(LocalDateTime.of(2025, 1, 15, 10, 0));
    assertFalse("Should not be busy when no events", busy);
  }

  @Test
  public void testEditNonExistentEvent() {
    CalendarModelInterface model = new CalendarModel();
    EditSpec spec = new EditSpec("New Subject", null, null, null, null, null);
    boolean result = model.editEvent(UUID.randomUUID(), spec);
    assertFalse("Should fail to edit non-existent event", result);
  }

  @Test
  public void testEditNonExistentSeries() {
    CalendarModelInterface model = new CalendarModel();
    EditSpec spec = new EditSpec("New Subject", null, null, null, null, null);
    boolean result = model.editEntireSeries(UUID.randomUUID(), spec);
    assertFalse("Should fail to edit non-existent series", result);
  }

  @Test
  public void testEditSeriesFromNonExistentSeries() {
    CalendarModelInterface model = new CalendarModel();
    EditSpec spec = new EditSpec("New Subject", null, null, null, null, null);
    boolean result = model.editSeriesFrom(UUID.randomUUID(), LocalDate.now(), spec);
    assertFalse("Should fail to edit non-existent series", result);
  }

  @Test
  public void testGetEventsOnDateWithMultipleEvents() {
    CalendarModelInterface model = new CalendarModel();
    LocalDate testDate = LocalDate.of(2025, 6, 15);

    model.createEvent(new Event("Event1",
        LocalDateTime.of(2025, 6, 15, 9, 0),
        LocalDateTime.of(2025, 6, 15, 10, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("Event2",
        LocalDateTime.of(2025, 6, 15, 11, 0),
        LocalDateTime.of(2025, 6, 15, 12, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("Event3",
        LocalDateTime.of(2025, 6, 15, 14, 0),
        LocalDateTime.of(2025, 6, 15, 15, 0),
        null, null, false, UUID.randomUUID(), null));

    List<EventInterface> events = model.getEventsOnDate(testDate);
    assertEquals("Should have 3 events", 3, events.size());
  }

  @Test
  public void testGetAllEventsEmpty() {
    CalendarModelInterface model = new CalendarModel();
    List<EventInterface> events = model.getAllEvents();
    assertTrue("Empty model should return empty list", events.isEmpty());
  }

  @Test
  public void testCreateDuplicateEvent() {
    CalendarModelInterface model = new CalendarModel();
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);

    Event event1 = new Event("Meeting", start, end, null, null, false, UUID.randomUUID(), null);
    Event event2 = new Event("Meeting", start, end, null, null, false, UUID.randomUUID(), null);

    assertTrue("First event should be created", model.createEvent(event1));
    assertFalse("Duplicate event should be rejected", model.createEvent(event2));
  }

  @Test
  public void testGetEventsInRangeWithMultipleEvents() {
    CalendarModelInterface model = new CalendarModel();

    model.createEvent(new Event("Event1",
        LocalDateTime.of(2025, 6, 10, 10, 0),
        LocalDateTime.of(2025, 6, 10, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("Event2",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("Event3",
        LocalDateTime.of(2025, 6, 20, 10, 0),
        LocalDateTime.of(2025, 6, 20, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    List<EventInterface> events = model.getEventsInRange(
        LocalDateTime.of(2025, 6, 1, 0, 0),
        LocalDateTime.of(2025, 6, 30, 23, 59));

    assertEquals("Should have 3 events in range", 3, events.size());
  }

  @Test
  public void testGetEventsInRangePartialOverlap() {
    CalendarModelInterface model = new CalendarModel();

    model.createEvent(new Event("Before",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("Inside",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    model.createEvent(new Event("After",
        LocalDateTime.of(2025, 6, 30, 10, 0),
        LocalDateTime.of(2025, 6, 30, 11, 0),
        null, null, false, UUID.randomUUID(), null));

    List<EventInterface> events = model.getEventsInRange(
        LocalDateTime.of(2025, 6, 10, 0, 0),
        LocalDateTime.of(2025, 6, 20, 23, 59));

    assertEquals("Should have 1 event in range", 1, events.size());
    assertEquals("Should be 'Inside' event", "Inside", events.get(0).getSubject());
  }

  @Test
  public void testEditEventAllFields() {
    CalendarModelInterface model = new CalendarModel();

    Event event = new Event("Original",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, false, UUID.randomUUID(), null);
    model.createEvent(event);

    LocalDateTime newStart = LocalDateTime.of(2025, 6, 15, 14, 0);
    LocalDateTime newEnd = LocalDateTime.of(2025, 6, 15, 15, 0);

    EditSpec spec = EditSpec.builder()
        .subject("Updated")
        .start(newStart)
        .end(newEnd)
        .description("New Desc")
        .location("New Loc")
        .status(calendar.model.EventStatus.PRIVATE)
        .build();

    boolean result = model.editEvent(event.getId(), spec);
    assertTrue("Edit should succeed", result);

    EventInterface updated = model.findEventById(event.getId());
    assertEquals("Subject should be updated", "Updated", updated.getSubject());
    assertEquals("Start should be updated", newStart, updated.getStartDateTime());
    assertEquals("End should be updated", newEnd, updated.getEndDateTime());
    assertEquals("Description should be updated", "New Desc", updated.getDescription().get());
    assertEquals("Location should be updated", "New Loc", updated.getLocation().get());
    assertTrue("Should be private", updated.isPrivate());
  }
}