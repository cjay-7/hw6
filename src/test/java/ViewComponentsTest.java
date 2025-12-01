import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.controller.Features;
import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.Event;
import calendar.model.EventInterface;
import calendar.view.GuiViewInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for view components through interfaces and mock implementations.
 */
public class ViewComponentsTest {

  private CalendarManager manager;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    manager = new CalendarManager();
    manager.createCalendar("TestCalendar", ZoneId.of("America/New_York"));
    manager.setCurrentCalendar("TestCalendar");
  }

  @Test
  public void testMockViewDisplayMonth() {
    TestGuiView view = new TestGuiView();
    LocalDate today = LocalDate.now();
    List<LocalDate> daysWithEvents = new ArrayList<>();
    daysWithEvents.add(today);

    view.displayMonth(2025, 6, daysWithEvents, today);

    assertEquals(2025, view.lastYear);
    assertEquals(6, view.lastMonth);
    assertEquals(today, view.lastSelectedDate);
    assertEquals(1, view.lastDaysWithEvents.size());
  }

  @Test
  public void testMockViewDisplayWeek() {
    TestGuiView view = new TestGuiView();
    LocalDate weekStart = LocalDate.of(2025, 6, 15);
    List<LocalDate> daysWithEvents = new ArrayList<>();
    daysWithEvents.add(weekStart.plusDays(1));
    daysWithEvents.add(weekStart.plusDays(3));

    view.displayWeek(weekStart, daysWithEvents, weekStart);

    assertEquals(weekStart, view.lastWeekStart);
    assertEquals(2, view.lastDaysWithEvents.size());
  }

  @Test
  public void testMockViewDisplayEventsForDay() {
    TestGuiView view = new TestGuiView();
    LocalDate date = LocalDate.of(2025, 6, 15);

    List<EventInterface> events = new ArrayList<>();
    events.add(new Event("Event1",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, false, UUID.randomUUID(), null));
    events.add(new Event("Event2",
        LocalDateTime.of(2025, 6, 15, 14, 0),
        LocalDateTime.of(2025, 6, 15, 15, 0),
        "Description", "Location", true, UUID.randomUUID(), UUID.randomUUID()));

    view.displayEventsForDay(date, events);

    assertEquals(date, view.lastEventDate);
    assertEquals(2, view.lastEvents.size());
  }

  @Test
  public void testMockViewDisplayEventsForDayEmpty() {
    TestGuiView view = new TestGuiView();
    LocalDate date = LocalDate.of(2025, 6, 15);

    view.displayEventsForDay(date, new ArrayList<>());

    assertEquals(date, view.lastEventDate);
    assertTrue(view.lastEvents.isEmpty());
  }

  @Test
  public void testMockViewDisplayEventsForDayNull() {
    TestGuiView view = new TestGuiView();

    view.displayEventsForDay(null, new ArrayList<>());

    assertNull(view.lastEventDate);
  }

  @Test
  public void testMockViewShowError() {
    TestGuiView view = new TestGuiView();

    view.showError("Test error message");

    assertEquals("Test error message", view.lastError);
  }

  @Test
  public void testMockViewShowMessage() {
    TestGuiView view = new TestGuiView();

    view.showMessage("Test success message");

    assertEquals("Test success message", view.lastMessage);
  }

  @Test
  public void testMockViewUpdateCalendarList() {
    TestGuiView view = new TestGuiView();

    List<CalendarInterface> calendars = manager.getAllCalendars();
    view.updateCalendarList(calendars, "TestCalendar");

    assertEquals(1, view.lastCalendars.size());
    assertEquals("TestCalendar", view.lastCurrentCalendarName);
  }

  @Test
  public void testMockViewUpdateCalendarListMultiple() {
    TestGuiView view = new TestGuiView();

    manager.createCalendar("Calendar2", ZoneId.of("Europe/London"));
    manager.createCalendar("Calendar3", ZoneId.of("Asia/Tokyo"));

    List<CalendarInterface> calendars = manager.getAllCalendars();
    view.updateCalendarList(calendars, "Calendar2");

    assertEquals(3, view.lastCalendars.size());
    assertEquals("Calendar2", view.lastCurrentCalendarName);
  }

  @Test
  public void testMockViewDisplay() {
    TestGuiView view = new TestGuiView();

    view.display();

    assertTrue(view.displayCalled);
  }

  @Test
  public void testMockViewGetCurrentMonth() {
    TestGuiView view = new TestGuiView();
    view.displayMonth(2025, 8, new ArrayList<>(), LocalDate.now());

    int[] currentMonth = view.getCurrentMonth();

    assertEquals(2025, currentMonth[0]);
    assertEquals(8, currentMonth[1]);
  }

  @Test
  public void testMockViewGetSelectedDate() {
    TestGuiView view = new TestGuiView();
    LocalDate date = LocalDate.of(2025, 7, 20);
    view.displayMonth(2025, 7, new ArrayList<>(), date);

    LocalDate selected = view.getSelectedDate();

    assertEquals(date, selected);
  }

  @Test
  public void testMockViewAddFeatures() {
    TestGuiView view = new TestGuiView();
    MockFeatures features = new MockFeatures();

    view.addFeatures(features);

    assertEquals(features, view.lastFeatures);
  }

  @Test
  public void testEventWithSeriesId() {
    UUID seriesId = UUID.randomUUID();
    Event event = new Event("Series Event",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        "Part of series", null, false, UUID.randomUUID(), seriesId);

    assertTrue(event.getSeriesId().isPresent());
    assertEquals(seriesId, event.getSeriesId().get());
  }

  @Test
  public void testEventWithPrivateStatus() {
    Event event = new Event("Private Event",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, true, UUID.randomUUID(), null);

    assertTrue(event.isPrivate());
  }

  @Test
  public void testEventWithPublicStatus() {
    Event event = new Event("Public Event",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        null, null, false, UUID.randomUUID(), null);

    assertFalse(event.isPrivate());
  }

  @Test
  public void testEventWithLongDescription() {
    String longDescription = "This is a very long description that exceeds fifty characters "
        + "and should be truncated when displayed in certain views.";
    Event event = new Event("Event",
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 11, 0),
        longDescription, null, false, UUID.randomUUID(), null);

    assertTrue(event.getDescription().get().length() > 50);
  }

  @Test
  public void testCalendarListWithDifferentTimezones() {
    manager.createCalendar("Pacific", ZoneId.of("America/Los_Angeles"));
    manager.createCalendar("Europe", ZoneId.of("Europe/Paris"));
    manager.createCalendar("Asia", ZoneId.of("Asia/Tokyo"));

    List<CalendarInterface> calendars = manager.getAllCalendars();
    assertEquals(4, calendars.size());

    TestGuiView view = new TestGuiView();
    view.updateCalendarList(calendars, "Pacific");

    assertEquals("Pacific", view.lastCurrentCalendarName);
  }

  /**
   * Test implementation of GuiViewInterface.
   */
  private static class TestGuiView implements GuiViewInterface {
    int lastYear = 0;
    int lastMonth = 0;
    LocalDate lastSelectedDate = null;
    LocalDate lastWeekStart = null;
    List<LocalDate> lastDaysWithEvents = new ArrayList<>();
    LocalDate lastEventDate = null;
    List<EventInterface> lastEvents = new ArrayList<>();
    String lastError = null;
    String lastMessage = null;
    List<CalendarInterface> lastCalendars = new ArrayList<>();
    String lastCurrentCalendarName = null;
    boolean displayCalled = false;
    Features lastFeatures = null;

    @Override
    public void addFeatures(Features features) {
      this.lastFeatures = features;
    }

    @Override
    public void updateCalendarList(List<CalendarInterface> calendars, String currentCalendarName) {
      this.lastCalendars = new ArrayList<>(calendars);
      this.lastCurrentCalendarName = currentCalendarName;
    }

    @Override
    public void displayMonth(int year, int month, List<LocalDate> daysWithEvents,
        LocalDate selectedDate) {
      this.lastYear = year;
      this.lastMonth = month;
      this.lastDaysWithEvents = new ArrayList<>(daysWithEvents);
      this.lastSelectedDate = selectedDate;
    }

    @Override
    public void displayWeek(LocalDate weekStart, List<LocalDate> daysWithEvents,
        LocalDate selectedDate) {
      this.lastWeekStart = weekStart;
      this.lastDaysWithEvents = new ArrayList<>(daysWithEvents);
      this.lastSelectedDate = selectedDate;
    }

    @Override
    public void displayEventsForDay(LocalDate date, List<EventInterface> events) {
      this.lastEventDate = date;
      this.lastEvents = new ArrayList<>(events);
    }

    @Override
    public void showError(String message) {
      this.lastError = message;
    }

    @Override
    public void showMessage(String message) {
      this.lastMessage = message;
    }

    @Override
    public void display() {
      this.displayCalled = true;
    }

    @Override
    public int[] getCurrentMonth() {
      return new int[] { lastYear, lastMonth };
    }

    @Override
    public LocalDate getSelectedDate() {
      return lastSelectedDate;
    }
  }

  /**
   * Mock implementation of Features for testing.
   */
  private static class MockFeatures implements Features {
    @Override
    public void createCalendar(String name, ZoneId timezone) {
    }

    @Override
    public void switchCalendar(String calendarName) {
    }

    @Override
    public void navigateToMonth(int year, int month) {
    }

    @Override
    public void navigateToPreviousMonth() {
    }

    @Override
    public void navigateToNextMonth() {
    }

    @Override
    public void navigateToToday() {
    }

    @Override
    public void selectDay(LocalDate date) {
    }

    @Override
    public void switchToWeekView() {
    }

    @Override
    public void switchToMonthView() {
    }

    @Override
    public void createEvent(String subject, LocalDateTime start, LocalDateTime end,
        String location, String description, boolean isPrivate) {
    }

    @Override
    public void createEventSeries(String subject, LocalDateTime start, LocalDateTime end,
        String location, String description, boolean isPrivate,
        java.util.Set<java.time.DayOfWeek> weekdays,
        LocalDate endDate, Integer occurrences) {
    }

    @Override
    public void editEvent(EventInterface event, String newSubject, LocalDateTime newStart,
        LocalDateTime newEnd, String newLocation, String newDescription,
        Boolean newIsPrivate) {
    }

    @Override
    public void editSeries(String seriesId, String newSubject, LocalDateTime newStart,
        LocalDateTime newEnd, String newLocation, String newDescription,
        Boolean newIsPrivate) {
    }

    @Override
    public void editSeriesFromDate(String seriesId, LocalDate fromDate, String newSubject,
        LocalDateTime newStart, LocalDateTime newEnd,
        String newLocation, String newDescription,
        Boolean newIsPrivate) {
    }
  }
}
