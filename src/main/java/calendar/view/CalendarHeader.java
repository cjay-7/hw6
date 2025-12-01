package calendar.view;

import calendar.controller.Features;
import calendar.model.CalendarInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Header panel component for calendar selection and display.
 * Handles calendar selector, new calendar button, and calendar indicator.
 */
public class CalendarHeader extends JPanel {
  private static final Color[] CALENDAR_COLORS = {
      new Color(66, 133, 244), new Color(234, 67, 53), new Color(251, 188, 5),
      new Color(52, 168, 83), new Color(156, 39, 176), new Color(255, 109, 0),
      new Color(0, 172, 193), new Color(121, 134, 203)
  };

  private JComboBox<String> calendarSelector;
  private JLabel currentCalendarLabel;
  private JButton newCalendarButton;
  private JPanel calendarIndicatorPanel;

  private Features features;
  private Map<String, Color> calendarColors;
  private int colorIndex;
  private Runnable newCalendarCallback;

  /**
   * Constructor.
   */
  public CalendarHeader() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    calendarColors = new HashMap<>();
    colorIndex = 0;
    buildTopPanel();
  }

  /**
   * Sets the features interface for calendar management callbacks.
   *
   * @param features the features interface
   */
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Sets the callback for creating a new calendar.
   *
   * @param callback the callback to execute when new calendar button is clicked
   */
  public void setNewCalendarCallback(Runnable callback) {
    this.newCalendarCallback = callback;
  }

  /**
   * Updates the calendar list and current calendar display.
   *
   * @param calendars the list of all calendars
   * @param currentCalendarName the name of the current calendar
   */
  public void updateCalendarList(List<CalendarInterface> calendars, String currentCalendarName) {
    
    for (CalendarInterface cal : calendars) {
      if (!calendarColors.containsKey(cal.getName())) {
        calendarColors.put(cal.getName(), CALENDAR_COLORS[colorIndex % CALENDAR_COLORS.length]);
        colorIndex++;
      }
    }

    
    calendarSelector.removeAllItems();
    for (CalendarInterface cal : calendars) {
      calendarSelector.addItem(cal.getName());
    }

    if (currentCalendarName != null) {
      calendarSelector.setSelectedItem(currentCalendarName);
      CalendarInterface current = calendars.stream()
          .filter(c -> c.getName().equals(currentCalendarName))
          .findFirst()
          .orElse(null);

      if (current != null) {
        Color calColor = calendarColors.get(current.getName());
        currentCalendarLabel.setText("<html><b>Current Calendar:</b> " + current.getName()
            + " <span style='color: gray;'>(" + current.getTimezone().getId() + ")</span></html>");
        updateCalendarIndicator(current.getName(), calColor);
      }
    }
  }

  private void buildTopPanel() {
    JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    selectorPanel.add(new JLabel("Calendar:"));

    calendarSelector = new JComboBox<>();
    calendarSelector.setPreferredSize(new Dimension(200, 30));
    calendarSelector.setFont(new Font("SansSerif", Font.BOLD, 12));
    calendarSelector.addActionListener(e -> {
      if (features != null && calendarSelector.getSelectedItem() != null) {
        features.switchCalendar((String) calendarSelector.getSelectedItem());
      }
    });
    selectorPanel.add(calendarSelector);

    newCalendarButton = new JButton("+ New Calendar");
    newCalendarButton.setFont(new Font("SansSerif", Font.BOLD, 11));
    newCalendarButton.addActionListener(e -> {
      if (newCalendarCallback != null) {
        newCalendarCallback.run();
      }
    });
    selectorPanel.add(newCalendarButton);

    calendarIndicatorPanel = new JPanel();
    calendarIndicatorPanel.setPreferredSize(new Dimension(30, 30));
    calendarIndicatorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
        Color.DARK_GRAY, 2));
    selectorPanel.add(calendarIndicatorPanel);

    add(selectorPanel);

    currentCalendarLabel = new JLabel("No calendar selected");
    currentCalendarLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    infoPanel.add(currentCalendarLabel);
    add(infoPanel);
  }

  private void updateCalendarIndicator(String calendarName, Color color) {
    calendarIndicatorPanel.setBackground(color);
    calendarIndicatorPanel.setToolTipText(calendarName + " indicator");
  }
}



