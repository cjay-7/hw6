import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.controller.Features;
import calendar.controller.GuiController;
import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.EventInterface;
import calendar.view.GuiViewInterface;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive tests for GuiController.
 */
public class GuiControllerTest {

  private CalendarManager manager;
  private MockGuiView mockView;
  private GuiController controller;

  /**
   * Sets up the test environment before each test.
   */
  @Before
  public void setUp() {
    manager = new CalendarManager();
    mockView = new MockGuiView();
    controller = new GuiController(manager, mockView);
  }

  @Test
  public void testDefaultCalendarCreatedOnInit() {
    assertTrue(mockView.displayCalled);
    assertFalse(manager.getAllCalendars().isEmpty());

  }

  @Test
  public void testConstructorCallsAddFeatures() {

    CalendarManager newManager = new CalendarManager();
    MockGuiView newView = new MockGuiView();
    GuiController newController = new GuiController(newManager, newView);

    assertTrue(newView.displayCalled);
    assertFalse(newManager.getAllCalendars().isEmpty());
  }

  @Test
  public void testCreateCalendarSuccess() {

    mockView.displayMonthCalled = false;
    mockView.updateCalendarListCalled = false;
    mockView.displayEventsForDayCalled = false;
    mockView.lastCalendarList.clear();
    mockView.lastSelectedDate = null;

    controller.createCalendar("Work Calendar", ZoneId.of("America/Los_Angeles"));
    assertTrue(mockView.lastMessage.contains("created successfully"));
    assertEquals(2, manager.getAllCalendars().size());

    assertTrue("refreshCalendarList should be called", mockView.updateCalendarListCalled);
    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateCalendarDuplicate() {
    controller.createCalendar("My Calendar", ZoneId.of("America/New_York"));
    assertTrue(mockView.lastError.contains("already exists"));
  }

  @Test
  public void testCreateCalendarEmptyName() {
    controller.createCalendar("", ZoneId.of("America/New_York"));
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateCalendarNullName() {
    controller.createCalendar(null, ZoneId.of("America/New_York"));
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateCalendarNullTimezone() {
    controller.createCalendar("Test Calendar", null);
    assertTrue(mockView.lastError.contains("valid timezone"));
  }

  @Test
  public void testSwitchCalendarSuccess() {
    controller.createCalendar("Second Calendar", ZoneId.of("Europe/London"));
    mockView.lastError = null;
    controller.switchCalendar("Second Calendar");
    assertEquals("Second Calendar", manager.getCurrentCalendar().getName());
  }

  @Test
  public void testSwitchCalendarNotFound() {
    controller.switchCalendar("Nonexistent");
    assertTrue(mockView.lastError.contains("not found"));
  }

  @Test
  public void testSwitchCalendarEmptyName() {
    controller.switchCalendar("");
    assertTrue(mockView.lastError.contains("Invalid calendar name"));
  }

  @Test
  public void testSwitchCalendarNullName() {
    controller.switchCalendar(null);
    assertTrue(mockView.lastError.contains("Invalid calendar name"));
  }

  @Test
  public void testNavigateToMonth() {
    controller.navigateToMonth(2025, 6);
    assertTrue(mockView.displayMonthCalled);
    assertEquals(2025, mockView.lastDisplayedYear);
    assertEquals(6, mockView.lastDisplayedMonth);
  }

  @Test
  public void testNavigateToMonthInvalidMonth() {
    controller.navigateToMonth(2025, 13);
    assertTrue(mockView.lastError.contains("Invalid month"));
  }

  @Test
  public void testNavigateToMonthZeroMonth() {
    controller.navigateToMonth(2025, 0);
    assertTrue(mockView.lastError.contains("Invalid month"));
  }

  @Test
  public void testNavigateToPreviousMonth() {
    controller.navigateToMonth(2025, 6);
    controller.navigateToPreviousMonth();
    assertEquals(5, mockView.lastDisplayedMonth);
  }

  @Test
  public void testNavigateToPreviousMonthYearWrap() {
    controller.navigateToMonth(2025, 1);
    controller.navigateToPreviousMonth();
    assertEquals(12, mockView.lastDisplayedMonth);
    assertEquals(2024, mockView.lastDisplayedYear);
  }

  @Test
  public void testNavigateToNextMonth() {
    controller.navigateToMonth(2025, 6);
    controller.navigateToNextMonth();
    assertEquals(7, mockView.lastDisplayedMonth);
  }

  @Test
  public void testNavigateToNextMonthYearWrap() {
    controller.navigateToMonth(2025, 12);
    controller.navigateToNextMonth();
    assertEquals(1, mockView.lastDisplayedMonth);
    assertEquals(2026, mockView.lastDisplayedYear);
  }

  @Test
  public void testNavigateToToday() {
    controller.navigateToMonth(2020, 1);
    controller.navigateToToday();
    LocalDate today = LocalDate.now();
    assertEquals(today.getYear(), mockView.lastDisplayedYear);
    assertEquals(today.getMonthValue(), mockView.lastDisplayedMonth);
  }

  @Test
  public void testSelectDay() {
    LocalDate date = LocalDate.of(2025, 6, 15);
    controller.selectDay(date);
    assertEquals(date, mockView.lastSelectedDate);
  }

  @Test
  public void testSelectDayNull() {
    controller.selectDay(null);
    assertTrue(mockView.lastError.contains("Invalid date"));
  }

  @Test
  public void testSwitchToWeekView() {
    controller.switchToWeekView();
    assertTrue(mockView.displayWeekCalled);
  }

  @Test
  public void testSwitchToMonthView() {
    controller.switchToWeekView();
    controller.switchToMonthView();
    assertTrue(mockView.displayMonthCalled);
  }

  @Test
  public void testNavigatePreviousInWeekView() {
    controller.switchToWeekView();
    mockView.displayWeekCalled = false;
    controller.navigateToPreviousMonth();
    assertTrue(mockView.displayWeekCalled);
  }

  @Test
  public void testNavigateNextInWeekView() {
    controller.switchToWeekView();
    mockView.displayWeekCalled = false;
    controller.navigateToNextMonth();
    assertTrue(mockView.displayWeekCalled);
  }

  @Test
  public void testCreateEventSuccess() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);

    mockView.displayMonthCalled = false;
    mockView.lastSelectedDate = null;

    controller.createEvent("Meeting", start, end, "Room 101", "Team meeting", false);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertNotNull("refreshEventsForSelectedDay should be called", mockView.lastSelectedDate);
  }

  @Test
  public void testCreateEventNullSubject() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent(null, start, end, null, null, false);
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateEventEmptySubject() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("  ", start, end, null, null, false);
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateEventNullTimes() {
    controller.createEvent("Meeting", null, null, null, null, false);
    assertTrue(mockView.lastError.contains("must be specified"));
  }

  @Test
  public void testCreateEventNullStart() {
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Meeting", null, end, null, null, false);
    assertTrue(mockView.lastError.contains("must be specified"));
  }

  @Test
  public void testCreateEventNullEnd() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    controller.createEvent("Meeting", start, null, null, null, false);
    assertTrue(mockView.lastError.contains("must be specified"));
  }

  @Test
  public void testCreateEventEndBeforeStart() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 11, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 10, 0);
    controller.createEvent("Meeting", start, end, null, null, false);
    assertTrue(mockView.lastError.contains("must be after"));
  }

  @Test
  public void testCreateEventEndEqualsStart() {
    LocalDateTime time = LocalDateTime.of(2025, 6, 15, 10, 0);
    controller.createEvent("Meeting", time, time, null, null, false);
    assertTrue(mockView.lastError.contains("must be after"));
  }

  @Test
  public void testCreateEventDuplicate() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Meeting", start, end, null, null, false);
    mockView.lastError = null;

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEvent("Meeting", start, end, null, null, false);
    assertTrue(mockView.lastError.contains("already exists"));

    assertFalse("refreshView should NOT be called on duplicate", mockView.displayMonthCalled);
    assertFalse("refreshEventsForSelectedDay should NOT be called on duplicate",
        mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventPrivate() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Private Meeting", start, end, null, null, true);
    assertTrue(mockView.lastMessage.contains("created successfully"));
  }

  @Test
  public void testCreateEventWithAllFields() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEvent("Full Meeting", start, end, "Conference Room",
        "Important meeting", true);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventWithEmptyDescription() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEvent("Meeting", start, end, null, "   ", false);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventWithEmptyLocation() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEvent("Meeting", start, end, "   ", null, false);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventSeriesWithOccurrences() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Weekly Meeting", start, end, null, null, false,
        weekdays, null, 5);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventSeriesWithEndDate() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    LocalDate endDate = LocalDate.of(2025, 7, 16);
    controller.createEventSeries("Weekly Meeting", start, end, null, null, false,
        weekdays, endDate, null);
    assertTrue(mockView.lastMessage.contains("created successfully"));
  }

  @Test
  public void testCreateEventSeriesNullSubject() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries(null, start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateEventSeriesEmptySubject() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("  ", start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("cannot be empty"));
  }

  @Test
  public void testCreateEventSeriesNullTimes() {
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Meeting", null, null, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("must be specified"));
  }

  @Test
  public void testCreateEventSeriesEndBeforeStart() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 11, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 10, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("must be after"));
  }

  @Test
  public void testCreateEventSeriesSpanningDays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 17, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("same day"));
  }

  @Test
  public void testCreateEventSeriesNoWeekdays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    controller.createEventSeries("Meeting", start, end, null, null, false, null, null, 5);
    assertTrue(mockView.lastError.contains("at least one weekday"));
  }

  @Test
  public void testCreateEventSeriesEmptyWeekdays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.noneOf(DayOfWeek.class);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("at least one weekday"));
  }

  @Test
  public void testCreateEventSeriesStartDayNotInWeekdays() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.TUESDAY);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 5);
    assertTrue(mockView.lastError.contains("MONDAY"));
  }

  @Test
  public void testCreateEventSeriesBothEndDateAndOccurrences() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    LocalDate endDate = LocalDate.of(2025, 7, 16);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, endDate, 5);
    assertTrue(mockView.lastError.contains("not both"));
  }

  @Test
  public void testCreateEventSeriesNeitherEndDateNorOccurrences() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, null);
    assertTrue(mockView.lastError.contains("end date or number of occurrences"));
  }

  @Test
  public void testCreateEventSeriesEndDateBeforeStart() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    LocalDate endDate = LocalDate.of(2025, 6, 10);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, endDate, null);
    assertTrue(mockView.lastError.contains("after the start"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
    assertFalse("refreshEventsForSelectedDay should NOT be called on error",
        mockView.displayEventsForDayCalled);
  }

  @Test
  public void testCreateEventSeriesEndDateEqualsStart() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    LocalDate sameDate = LocalDate.of(2025, 6, 16);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, sameDate,
        null);
    assertTrue(mockView.lastError.contains("after the start"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
  }

  @Test
  public void testCreateEventSeriesZeroOccurrences() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 0);
    assertTrue(mockView.lastError.contains("at least 1"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
  }

  @Test
  public void testCreateEventSeriesNegativeOccurrences() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, -1);
    assertTrue(mockView.lastError.contains("at least 1"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
  }

  @Test
  public void testCreateEventSeriesOneOccurrence() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.createEventSeries("Meeting", start, end, null, null, false, weekdays, null, 1);
    assertTrue(mockView.lastMessage.contains("created successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testEditEventSuccess() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Original", start, end, null, null, false);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.editEvent(event, "Updated", null, null, null, null, null);
    assertTrue(mockView.lastMessage.contains("updated successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testEditEventNull() {
    controller.editEvent(null, "Updated", null, null, null, null, null);
    assertTrue(mockView.lastError.contains("No event selected"));
  }

  @Test
  public void testEditEventInvalidTimeRange() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Original", start, end, null, null, false);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    LocalDateTime newStart = LocalDateTime.of(2025, 6, 15, 12, 0);
    LocalDateTime newEnd = LocalDateTime.of(2025, 6, 15, 11, 0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.editEvent(event, null, newStart, newEnd, null, null, null);
    assertTrue(mockView.lastError.contains("must be after"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
    assertFalse("refreshEventsForSelectedDay should NOT be called on error",
        mockView.displayEventsForDayCalled);
  }

  @Test
  public void testEditEventWithNewStartAndEndEqual() {

    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Original", start, end, null, null, false);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    LocalDateTime sameTime = LocalDateTime.of(2025, 6, 15, 12, 0);

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.editEvent(event, null, sameTime, sameTime, null, null, null);
    assertTrue(mockView.lastError.contains("must be after"));

    assertFalse("refreshView should NOT be called on error", mockView.displayMonthCalled);
  }

  @Test
  public void testEditEventWithPrivacyChange() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 15, 11, 0);
    controller.createEvent("Original", start, end, null, null, false);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    controller.editEvent(event, null, null, null, null, null, true);
    assertTrue(mockView.lastMessage.contains("updated successfully"));
  }

  @Test
  public void testEditSeriesSuccess() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Weekly", start, end, null, null, false, weekdays, null, 3);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    String seriesId = event.getSeriesId().get().toString();

    mockView.displayMonthCalled = false;
    mockView.displayEventsForDayCalled = false;

    controller.editSeries(seriesId, "Updated Weekly", null, null, null, null, null);
    assertTrue(mockView.lastMessage.contains("updated successfully"));

    assertTrue("refreshView should be called", mockView.displayMonthCalled);
    assertTrue("refreshEventsForSelectedDay should be called", mockView.displayEventsForDayCalled);
  }

  @Test
  public void testEditSeriesWithPrivacyChange() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Weekly", start, end, null, null, false, weekdays, null, 3);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    String seriesId = event.getSeriesId().get().toString();
    controller.editSeries(seriesId, null, null, null, null, null, true);
    assertTrue(mockView.lastMessage.contains("updated successfully"));
  }

  @Test
  public void testEditSeriesFromDateSuccess() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Weekly", start, end, null, null, false, weekdays, null, 5);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    String seriesId = event.getSeriesId().get().toString();
    LocalDate fromDate = LocalDate.of(2025, 6, 23);
    controller.editSeriesFromDate(seriesId, fromDate, "Updated", null, null, null, null, null);
    assertTrue(mockView.lastMessage.contains("updated successfully"));
  }

  @Test
  public void testEditSeriesFromDateWithPrivacyChange() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 16, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 11, 0);
    Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY);
    controller.createEventSeries("Weekly", start, end, null, null, false, weekdays, null, 5);

    EventInterface event = manager.getCurrentCalendar().getModel().getAllEvents().get(0);
    String seriesId = event.getSeriesId().get().toString();
    LocalDate fromDate = LocalDate.of(2025, 6, 23);
    controller.editSeriesFromDate(seriesId, fromDate, null, null, null, null, null, true);
    assertTrue(mockView.lastMessage.contains("updated successfully"));
  }

  /**
   * Mock implementation of GuiViewInterface for testing.
   */
  private static class MockGuiView implements GuiViewInterface {
    boolean displayCalled = false;
    boolean displayMonthCalled = false;
    boolean displayWeekCalled = false;
    boolean updateCalendarListCalled = false;
    boolean displayEventsForDayCalled = false;
    String lastError = null;
    String lastMessage = null;
    int lastDisplayedYear = 0;
    int lastDisplayedMonth = 0;
    LocalDate lastSelectedDate = null;
    List<CalendarInterface> lastCalendarList = new ArrayList<>();

    @Override
    public void addFeatures(Features features) {

    }

    @Override
    public void updateCalendarList(List<CalendarInterface> calendars, String currentCalendarName) {
      updateCalendarListCalled = true;
      lastCalendarList = new ArrayList<>(calendars);
    }

    @Override
    public void displayMonth(int year, int month, List<LocalDate> daysWithEvents,
        LocalDate selectedDate) {
      displayMonthCalled = true;
      lastDisplayedYear = year;
      lastDisplayedMonth = month;
      lastSelectedDate = selectedDate;
    }

    @Override
    public void displayWeek(LocalDate weekStart, List<LocalDate> daysWithEvents,
        LocalDate selectedDate) {
      displayWeekCalled = true;
      lastSelectedDate = selectedDate;
    }

    @Override
    public void displayEventsForDay(LocalDate date, List<EventInterface> events) {
      displayEventsForDayCalled = true;
      lastSelectedDate = date;
    }

    @Override
    public void showError(String message) {
      lastError = message;
    }

    @Override
    public void showMessage(String message) {
      lastMessage = message;
    }

    @Override
    public void display() {
      displayCalled = true;
    }

    @Override
    public int[] getCurrentMonth() {
      return new int[] { lastDisplayedYear, lastDisplayedMonth };
    }

    @Override
    public LocalDate getSelectedDate() {
      return lastSelectedDate;
    }
  }

}
