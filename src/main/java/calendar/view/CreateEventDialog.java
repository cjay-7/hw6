package calendar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Dialog for creating a single event.
 */
public class CreateEventDialog extends JDialog {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  private JTextField subjectField;
  private JTextField dateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField locationField;
  private JTextArea descriptionArea;
  private JCheckBox allDayCheckbox;
  private JCheckBox privateCheckbox;

  private boolean confirmed;
  private String subject;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String location;
  private String description;
  private boolean isPrivate;

  /**
   * Constructor.
   *
   * @param parent the parent frame
   * @param defaultDate the default date
   */
  public CreateEventDialog(JFrame parent, LocalDate defaultDate) {
    super(parent, "Create New Event", true);
    this.confirmed = false;

    setSize(550, 550);
    setResizable(false);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

    
    JLabel titleLabel = new JLabel("Create New Event");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createVerticalStrut(15));

    
    addLabeledField(mainPanel, "Subject (required):", subjectField = new JTextField(30), true);

    
    dateField = new JTextField(15);
    if (defaultDate != null) {
      dateField.setText(defaultDate.format(DATE_FORMAT));
    } else {
      dateField.setText(LocalDate.now().format(DATE_FORMAT));
    }
    addLabeledField(mainPanel, "Date (yyyy-MM-dd):", dateField, false);

    
    allDayCheckbox = new JCheckBox("All-day event (8:00 AM - 5:00 PM)");
    allDayCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    allDayCheckbox.setAlignmentX(LEFT_ALIGNMENT);
    allDayCheckbox.addActionListener(e -> {
      boolean allDay = allDayCheckbox.isSelected();
      startTimeField.setEnabled(!allDay);
      endTimeField.setEnabled(!allDay);
      if (allDay) {
        startTimeField.setText("08:00");
        endTimeField.setText("17:00");
      }
    });
    mainPanel.add(allDayCheckbox);
    mainPanel.add(Box.createVerticalStrut(10));

    
    JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    timePanel.setAlignmentX(LEFT_ALIGNMENT);
    timePanel.setMaximumSize(new Dimension(500, 60));

    JPanel startPanel = new JPanel();
    startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
    JLabel startLabel = new JLabel("Start Time (HH:mm):");
    startLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    startTimeField = new JTextField(8);
    startTimeField.setText("09:00");
    startTimeField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    startPanel.add(startLabel);
    startPanel.add(Box.createVerticalStrut(3));
    startPanel.add(startTimeField);

    JPanel endPanel = new JPanel();
    endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
    JLabel endLabel = new JLabel("End Time (HH:mm):");
    endLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    endTimeField = new JTextField(8);
    endTimeField.setText("10:00");
    endTimeField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    endPanel.add(endLabel);
    endPanel.add(Box.createVerticalStrut(3));
    endPanel.add(endTimeField);

    timePanel.add(startPanel);
    timePanel.add(endPanel);
    mainPanel.add(timePanel);
    mainPanel.add(Box.createVerticalStrut(10));

    
    addLabeledField(mainPanel, "Location (optional):", locationField = new JTextField(30), false);

    
    JLabel descLabel = new JLabel("Description (optional):");
    descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(descLabel);
    mainPanel.add(Box.createVerticalStrut(3));

    descriptionArea = new JTextArea(2, 30);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descriptionArea.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)
    ));
    descriptionArea.setAlignmentX(LEFT_ALIGNMENT);
    descriptionArea.setMaximumSize(new Dimension(490, 50));
    mainPanel.add(descriptionArea);
    mainPanel.add(Box.createVerticalStrut(10));

    
    privateCheckbox = new JCheckBox("Mark as private");
    privateCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    privateCheckbox.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(privateCheckbox);
    mainPanel.add(Box.createVerticalStrut(20));

    
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

    JButton createButton = ButtonFactory.createSaveButton("Create Event", 
        () -> handleCreate());
    buttonPanel.add(createButton);

    JButton cancelButton = ButtonFactory.createCancelButton("Cancel", () -> {
      confirmed = false;
      dispose();
    });
    buttonPanel.add(cancelButton);

    mainPanel.add(buttonPanel);

    add(mainPanel, BorderLayout.CENTER);
    setLocationRelativeTo(parent);
  }

  private void addLabeledField(JPanel panel, String labelText, JTextField field, boolean required) {
    JLabel label = new JLabel(labelText);
    label.setFont(new Font("SansSerif", Font.PLAIN, 13));
    if (required) {
      label.setForeground(CalendarTheme.REQUIRED_FIELD_LABEL);
    }
    label.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(label);
    panel.add(Box.createVerticalStrut(3));

    field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    field.setMaximumSize(new Dimension(490, 28));
    field.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(field);
    panel.add(Box.createVerticalStrut(10));
  }

  private void handleCreate() {
    try {
      this.subject = subjectField.getText().trim();
      if (subject.isEmpty()) {
        showError("Subject is required");
        return;
      }

      LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMAT);
      LocalTime startTime = LocalTime.parse(startTimeField.getText().trim(), TIME_FORMAT);
      LocalTime endTime = LocalTime.parse(endTimeField.getText().trim(), TIME_FORMAT);

      this.startDateTime = LocalDateTime.of(date, startTime);
      this.endDateTime = LocalDateTime.of(date, endTime);

      if (!endDateTime.isAfter(startDateTime)) {
        showError("End time must be after start time");
        return;
      }

      String loc = locationField.getText().trim();
      this.location = loc.isEmpty() ? null : loc;

      String desc = descriptionArea.getText().trim();
      this.description = desc.isEmpty() ? null : desc;

      this.isPrivate = privateCheckbox.isSelected();
      this.confirmed = true;
      dispose();

    } catch (DateTimeParseException e) {
      showError("Invalid date or time format.\n\nDate: yyyy-MM-dd\nTime: HH:mm");
    }
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Shows the dialog.
   *
   * @return true if confirmed, false otherwise
   */
  public boolean showDialog() {
    setVisible(true);
    return confirmed;
  }


  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public String getEventLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public boolean isPrivate() {
    return isPrivate;
  }
}
