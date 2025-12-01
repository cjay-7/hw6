package calendar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialog for creating a new calendar.
 */
public class CreateCalendarDialog extends JDialog {
  private JTextField nameField;
  private JComboBox<String> timezoneCombo;
  private boolean confirmed;

  private String calendarName;
  private ZoneId timezone;

  /**
   * Constructor.
   *
   * <p>parent the parent frame
   */
  public CreateCalendarDialog(JFrame parent) {
    super(parent, "Create New Calendar", true);
    this.confirmed = false;

    setSize(500, 300);
    setLayout(new BorderLayout(15, 15));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    
    JLabel titleLabel = new JLabel("Create New Calendar");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createVerticalStrut(20));

    
    JLabel nameLabel = new JLabel("Calendar Name:");
    nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    nameLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(nameLabel);
    mainPanel.add(Box.createVerticalStrut(5));

    nameField = new JTextField(30);
    nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    nameField.setMaximumSize(new Dimension(450, 30));
    nameField.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(nameField);
    mainPanel.add(Box.createVerticalStrut(15));

    
    JLabel timezoneLabel = new JLabel("Timezone:");
    timezoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    timezoneLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(timezoneLabel);
    mainPanel.add(Box.createVerticalStrut(5));

    timezoneCombo = new JComboBox<>(getCommonTimezones());
    timezoneCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    timezoneCombo.setSelectedItem(ZoneId.systemDefault().getId());
    timezoneCombo.setMaximumSize(new Dimension(450, 30));
    timezoneCombo.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(timezoneCombo);
    mainPanel.add(Box.createVerticalStrut(25));

    
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

    JButton createButton = ButtonFactory.createSaveButton("Create", () -> {
      String name = nameField.getText().trim();
      if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Calendar name is required", 
            UIConstants.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        return;
      }

      try {
        this.calendarName = name;
        this.timezone = ZoneId.of((String) timezoneCombo.getSelectedItem());
        this.confirmed = true;
        dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Invalid timezone: " + ex.getMessage(),
            UIConstants.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
      }
    });
    buttonPanel.add(createButton);

    JButton cancelButton = ButtonFactory.createCancelButton("Cancel", () -> {
      this.confirmed = false;
      dispose();
    });
    buttonPanel.add(cancelButton);

    mainPanel.add(buttonPanel);
    add(mainPanel, BorderLayout.CENTER);

    setLocationRelativeTo(parent);
    setResizable(false);
  }

  /**
   * Shows the dialog and returns whether user confirmed.
   *
   * <p>true if confirmed, false otherwise
   */
  public boolean showDialog() {
    setVisible(true);
    return confirmed;
  }

  public String getCalendarName() {
    return calendarName;
  }

  public ZoneId getTimezone() {
    return timezone;
  }

  private String[] getCommonTimezones() {
    List<String> zones = new ArrayList<>(ZoneId.getAvailableZoneIds());
    Collections.sort(zones);

    List<String> common = new ArrayList<>();
    common.add("America/New_York");
    common.add("America/Chicago");
    common.add("America/Denver");
    common.add("America/Los_Angeles");
    common.add("America/Phoenix");
    common.add("America/Anchorage");
    common.add("Pacific/Honolulu");
    common.add("Europe/London");
    common.add("Europe/Paris");
    common.add("Europe/Berlin");
    common.add("Asia/Tokyo");
    common.add("Asia/Shanghai");
    common.add("Asia/Dubai");
    common.add("Australia/Sydney");
    common.add("--------- All Timezones ---------");

    common.addAll(zones);
    return common.toArray(new String[0]);
  }
}
