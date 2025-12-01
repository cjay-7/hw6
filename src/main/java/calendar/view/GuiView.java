package calendar.view;

import calendar.controller.Features;
import calendar.model.CalendarInterface;
import calendar.model.EventInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * GUI view implementation for calendar application.
 * Refactored to use extracted component classes for better separation of concerns.
 */
public class GuiView extends JFrame implements GuiViewInterface {
  private Features features;

  private CalendarHeader calendarHeader;
  private NavigationPanel navigationPanel;
  private CalendarGridPanel calendarGridPanel;
  private EventDisplayPanel eventDisplayPanel;
  private DialogManager dialogManager;

  private int currentYear;
  private int currentMonth;
  private LocalDate selectedDate;
  private boolean isWeekView = false;

  private final List<EventInterface> currentDayEvents;

  /**
   * Constructor.
   */
  public GuiView() {
    super(UIConstants.WINDOW_TITLE);

    currentDayEvents = new ArrayList<>();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(UIConstants.STANDARD_SPACING, UIConstants.STANDARD_SPACING));
    setMinimumSize(UIConstants.MAIN_WINDOW_MIN_SIZE);

    initializeComponents();
    buildLayout();

    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(
        UIConstants.STANDARD_BORDER_PADDING, UIConstants.STANDARD_BORDER_PADDING,
        UIConstants.STANDARD_BORDER_PADDING, UIConstants.STANDARD_BORDER_PADDING));

    pack();
    setLocationRelativeTo(null);
  }

  private void initializeComponents() {
    calendarHeader = new CalendarHeader();
    navigationPanel = new NavigationPanel();
    calendarGridPanel = new CalendarGridPanel();
    eventDisplayPanel = new EventDisplayPanel();
    dialogManager = new DialogManager(this);
  }

  private void buildLayout() {
    
    add(calendarHeader, BorderLayout.NORTH);

    
    JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
    centerPanel.add(navigationPanel, BorderLayout.NORTH);
    centerPanel.add(calendarGridPanel, BorderLayout.CENTER);
    add(centerPanel, BorderLayout.CENTER);

    
    add(eventDisplayPanel, BorderLayout.EAST);

    
    buildBottomPanel();
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
    calendarHeader.setFeatures(features);
    calendarHeader.setNewCalendarCallback(() -> dialogManager.showNewCalendarDialog());
    navigationPanel.setFeatures(features);
    calendarGridPanel.setFeatures(features);
    dialogManager.setFeatures(features);
  }

  @Override
  public void updateCalendarList(List<CalendarInterface> calendars, String currentCalendarName) {
    calendarHeader.updateCalendarList(calendars, currentCalendarName);
  }

  @Override
  public void displayMonth(int year, int month, List<LocalDate> daysWithEvents,
                           LocalDate selectedDate) {
    this.currentYear = year;
    this.currentMonth = month;
    this.selectedDate = selectedDate;
    this.isWeekView = false;

    navigationPanel.updateMonthLabel(year, month);
    navigationPanel.updateViewToggle(false);
    calendarGridPanel.buildMonthGrid(year, month, daysWithEvents, selectedDate);
  }

  @Override
  public void displayWeek(LocalDate weekStart, List<LocalDate> daysWithEvents,
                          LocalDate selectedDate) {
    this.selectedDate = selectedDate;
    this.isWeekView = true;

    navigationPanel.updateWeekLabel(weekStart);
    navigationPanel.updateViewToggle(true);
    calendarGridPanel.buildWeekGrid(weekStart, daysWithEvents, selectedDate);
  }

  @Override
  public void displayEventsForDay(LocalDate date, List<EventInterface> events) {
    this.currentDayEvents.clear();
    this.currentDayEvents.addAll(events);
    dialogManager.setCurrentDayEvents(events);
    dialogManager.setSelectedDate(date);
    eventDisplayPanel.displayEventsForDay(date, events);
  }

  @Override
  public void showError(String message) {
    JOptionPane.showMessageDialog(
          this, message, UIConstants.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void display() {
    setVisible(true);
  }

  @Override
  public int[] getCurrentMonth() {
    return new int[]{currentYear, currentMonth};
  }

  @Override
  public LocalDate getSelectedDate() {
    return selectedDate;
  }

  

  /**
   * Builds the bottom panel with action buttons.
   */
  private void buildBottomPanel() {
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
        UIConstants.BOTTOM_PANEL_SPACING, UIConstants.BOTTOM_PANEL_SPACING));
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(
        UIConstants.STANDARD_BORDER_PADDING, 0, UIConstants.STANDARD_BORDER_PADDING, 0));

    JButton createEventButton = createActionButton(
        UIConstants.BTN_NEW_EVENT,
        CalendarTheme.BUTTON_CREATE_EVENT_BG,
        () -> dialogManager.showCreateEventDialog());
    bottomPanel.add(createEventButton);

    JButton createSeriesButton = createActionButton(
        UIConstants.BTN_NEW_SERIES,
        CalendarTheme.BUTTON_CREATE_SERIES_BG,
        () -> dialogManager.showCreateSeriesDialog());
    bottomPanel.add(createSeriesButton);

    JButton editEventButton = createActionButton(
        "Edit Event",
        CalendarTheme.BUTTON_EDIT_EVENT_BG,
        () -> dialogManager.showEditEventDialog());
    bottomPanel.add(editEventButton);

    add(bottomPanel, BorderLayout.SOUTH);
  }

  /**
   * Creates a styled action button with consistent appearance.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param action the action to perform on click
   * @return the configured button
   */
  private JButton createActionButton(String text, Color bgColor, Runnable action) {
    JButton button = new JButton(text);
    button.setFont(UIFonts.ACTION_BUTTON_FONT);
    button.setPreferredSize(UIConstants.ACTION_BUTTON_SIZE);
    button.setBackground(bgColor);
    button.setForeground(CalendarTheme.BUTTON_TEXT);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.addActionListener(e -> action.run());
    return button;
  }
}
