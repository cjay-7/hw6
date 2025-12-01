package calendar.view;

import calendar.controller.Features;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel component for calendar navigation controls.
 * Handles month/week navigation, view switching, and today button.
 */
public class NavigationPanel extends JPanel {
  private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
  private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("MMM dd");

  private JLabel monthYearLabel;
  private JButton prevButton;
  private JButton nextButton;
  private JButton todayButton;
  private JButton monthViewButton;
  private JButton weekViewButton;

  private Features features;

  /**
   * Constructor.
   */
  public NavigationPanel() {
    super(new BorderLayout(5, 5));
    buildNavigationControls();
  }

  /**
   * Sets the features interface for navigation callbacks.
   *
   * @param features the features interface
   */
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Updates the month/year label for month view.
   *
   * @param year the year
   * @param month the month (1-12)
   */
  public void updateMonthLabel(int year, int month) {
    java.time.LocalDate firstDay = java.time.LocalDate.of(year, month, 1);
    monthYearLabel.setText(firstDay.format(MONTH_FORMATTER));
  }

  /**
   * Updates the week label for week view.
   *
   * @param weekStart the start date of the week
   */
  public void updateWeekLabel(java.time.LocalDate weekStart) {
    java.time.LocalDate weekEnd = weekStart.plusDays(6);
    monthYearLabel.setText("Week of " + weekStart.format(WEEK_FORMATTER)
        + " - " + weekEnd.format(WEEK_FORMATTER));
  }

  /**
   * Updates the view toggle button states.
   *
   * @param isWeekView true if week view is active
   */
  public void updateViewToggle(boolean isWeekView) {
    if (isWeekView) {
      weekViewButton.setBackground(new Color(150, 150, 150));
      monthViewButton.setBackground(new Color(200, 200, 200));
    } else {
      monthViewButton.setBackground(new Color(150, 150, 150));
      weekViewButton.setBackground(new Color(200, 200, 200));
    }
  }

  private void buildNavigationControls() {
    final JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

    prevButton = new JButton("< Previous");
    prevButton.setFont(new Font("SansSerif", Font.BOLD, 12));
    prevButton.addActionListener(e -> {
      if (features != null) {
        features.navigateToPreviousMonth();
      }
    });
    navPanel.add(prevButton);

    monthYearLabel = new JLabel("", SwingConstants.CENTER);
    monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    monthYearLabel.setPreferredSize(new Dimension(250, 30));
    navPanel.add(monthYearLabel);

    nextButton = new JButton("Next >");
    nextButton.setFont(new Font("SansSerif", Font.BOLD, 12));
    nextButton.addActionListener(e -> {
      if (features != null) {
        features.navigateToNextMonth();
      }
    });
    navPanel.add(nextButton);

    navPanel.add(Box.createRigidArea(new Dimension(30, 0)));

    todayButton = new JButton("Today");
    todayButton.setFont(new Font("SansSerif", Font.BOLD, 12));
    todayButton.addActionListener(e -> {
      if (features != null) {
        features.navigateToToday();
      }
    });
    navPanel.add(todayButton);

    navPanel.add(Box.createRigidArea(new Dimension(20, 0)));

    
    monthViewButton = new JButton("Month View");
    monthViewButton.setFont(new Font("SansSerif", Font.BOLD, 12));
    monthViewButton.setBackground(new Color(200, 200, 200));
    monthViewButton.setFocusPainted(false);
    monthViewButton.addActionListener(e -> {
      if (features != null) {
        features.switchToMonthView();
        updateViewToggle(false);
      }
    });
    navPanel.add(monthViewButton);

    weekViewButton = new JButton("Week View");
    weekViewButton.setFont(new Font("SansSerif", Font.BOLD, 12));
    weekViewButton.setBackground(new Color(200, 200, 200));
    weekViewButton.setFocusPainted(false);
    weekViewButton.addActionListener(e -> {
      if (features != null) {
        features.switchToWeekView();
        updateViewToggle(true);
      }
    });
    navPanel.add(weekViewButton);

    add(navPanel, BorderLayout.NORTH);
  }
}


