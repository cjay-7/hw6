package calendar.view;

import calendar.controller.Features;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel component for displaying calendar grids (month and week views).
 * Handles the creation and styling of calendar day buttons.
 */
public class CalendarGridPanel extends JPanel {
  private static final Font DAY_BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 14);
  private static final Font DAY_BUTTON_BOLD_FONT = new Font("SansSerif", Font.BOLD, 15);
  private static final Font WEEK_BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 16);
  private static final Font WEEK_BUTTON_BOLD_FONT = new Font("SansSerif", Font.BOLD, 16);
  private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 13);
  private static final Font WEEK_HEADER_FONT = new Font("SansSerif", Font.BOLD, 14);

  private Features features;

  /**
   * Constructor.
   */
  public CalendarGridPanel() {
    super();
  }

  /**
   * Sets the features interface for day selection callbacks.
   *
   * @param features the features interface
   */
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Builds a month view calendar grid.
   *
   * @param year the year
   * @param month the month (1-12)
   * @param daysWithEvents list of dates that have events
   * @param selectedDate the currently selected date
   */
  public void buildMonthGrid(int year, int month, List<LocalDate> daysWithEvents,
                             LocalDate selectedDate) {
    removeAll();
    setLayout(new GridLayout(7, 7, 3, 3));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    
    addDayNameHeaders();

    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate firstOfMonth = yearMonth.atDay(1);
    int daysInMonth = yearMonth.lengthOfMonth();
    int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

    
    for (int i = 0; i < firstDayOfWeek; i++) {
      add(createEmptyCell());
    }

    
    LocalDate today = LocalDate.now();
    for (int day = 1; day <= daysInMonth; day++) {
      LocalDate date = LocalDate.of(year, month, day);
      JButton dayButton = createDayButton(date, daysWithEvents.contains(date),
          date.equals(selectedDate), date.equals(today), false);
      add(dayButton);
    }

    
    int totalCells = firstDayOfWeek + daysInMonth;
    int remainingCells = 42 - totalCells;
    for (int i = 0; i < remainingCells; i++) {
      add(createEmptyCell());
    }

    revalidate();
    repaint();
  }

  /**
   * Builds a week view calendar grid.
   *
   * @param weekStart the start date of the week (Sunday)
   * @param daysWithEvents list of dates that have events
   * @param selectedDate the currently selected date
   */
  public void buildWeekGrid(LocalDate weekStart, List<LocalDate> daysWithEvents,
                           LocalDate selectedDate) {
    removeAll();
    setLayout(new GridLayout(2, 7, 3, 3));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    
    addWeekDayNameHeaders();

    
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 7; i++) {
      LocalDate date = weekStart.plusDays(i);
      JButton dayButton = createDayButton(date, daysWithEvents.contains(date),
          date.equals(selectedDate), date.equals(today), true);
      add(dayButton);
    }

    revalidate();
    repaint();
  }

  private void addDayNameHeaders() {
    String[] dayNames = UIConstants.DAY_NAMES;
    Color[] dayColors = {
        new Color(255, 200, 200), Color.WHITE, Color.WHITE, Color.WHITE,
        Color.WHITE, Color.WHITE, new Color(200, 220, 255)
    };

    for (int i = 0; i < dayNames.length; i++) {
      JLabel label = new JLabel(dayNames[i], SwingConstants.CENTER);
      label.setFont(HEADER_FONT);
      label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
      label.setOpaque(true);
      label.setBackground(dayColors[i]);
      add(label);
    }
  }

  private void addWeekDayNameHeaders() {
    String[] dayNames = UIConstants.DAY_NAMES;

    for (String dayName : dayNames) {
      JLabel label = new JLabel(dayName, SwingConstants.CENTER);
      label.setFont(WEEK_HEADER_FONT);
      label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
      label.setOpaque(true);
      label.setBackground(Color.LIGHT_GRAY);
      add(label);
    }
  }

  private JButton createDayButton(LocalDate date, boolean hasEvents, boolean isSelected,
                                  boolean isToday, boolean isWeekView) {
    JButton dayButton;

    if (isWeekView) {
      String monthAbbr = date.getMonth().toString().substring(0, 3);
      dayButton = new JButton(
          "<html><center>" + date.getDayOfMonth()
          + "<br><small>" + monthAbbr
          + "</small></center></html>");
      dayButton.setPreferredSize(new Dimension(100, 100));
      dayButton.setFont(WEEK_BUTTON_FONT);
    } else {
      dayButton = new JButton(String.valueOf(date.getDayOfMonth()));
      dayButton.setPreferredSize(new Dimension(70, 70));
      dayButton.setFont(DAY_BUTTON_FONT);
    }

    dayButton.setFocusPainted(false);
    dayButton.setBackground(Color.WHITE);
    dayButton.setOpaque(true);

    
    if (hasEvents) {
      Font boldFont = isWeekView ? WEEK_BUTTON_BOLD_FONT : DAY_BUTTON_BOLD_FONT;
      dayButton.setFont(boldFont);
      dayButton.setForeground(CalendarTheme.EVENT_INDICATOR_COLOR);
      if (!isWeekView) {
        dayButton.setText(String.valueOf(date.getDayOfMonth()) + " •");
      }
    }

    if (isSelected) {
      dayButton.setBackground(CalendarTheme.SELECTED_DAY_BG);
      dayButton.setBorder(BorderFactory.createLineBorder(
          CalendarTheme.SELECTED_DAY_BORDER, CalendarTheme.SELECTED_BORDER_WIDTH));
    } else {
      dayButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    if (isToday) {
      dayButton.setForeground(CalendarTheme.TODAY_COLOR);
      Font boldFont = isWeekView ? WEEK_BUTTON_BOLD_FONT : DAY_BUTTON_BOLD_FONT;
      dayButton.setFont(boldFont);
    }

    
    int dayOfWeek = date.getDayOfWeek().getValue() % 7;
    if (dayOfWeek == 0) {
      dayButton.setBackground(CalendarTheme.SUNDAY_BG);
    } else if (dayOfWeek == 6) {
      dayButton.setBackground(CalendarTheme.SATURDAY_BG);
    }

    
    dayButton.addActionListener(e -> {
      if (features != null) {
        features.selectDay(date);
      }
    });

    return dayButton;
  }

  private JLabel createEmptyCell() {
    JLabel emptyLabel = new JLabel("");
    emptyLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    emptyLabel.setOpaque(true);
    emptyLabel.setBackground(CalendarTheme.EMPTY_CELL_BG);
    return emptyLabel;
  }
}



