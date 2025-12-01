package calendar.view;

import calendar.model.EventInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Dialog for editing an event.
 */
public class EditEventDialog extends JDialog {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Scope of editing for a recurring event.
   */
  public enum EditScope {
    SINGLE, FROM_DATE, ALL
  }

  private JTextField subjectField;
  private JTextField dateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField locationField;
  private JTextArea descriptionArea;
  private JCheckBox privateCheckbox;

  private JRadioButton singleRadio;
  private JRadioButton fromDateRadio;
  private JRadioButton allRadio;

  private boolean confirmed;
  private EditScope editScope;
  private String newSubject;
  private LocalDateTime newStartDateTime;
  private LocalDateTime newEndDateTime;
  private String newLocation;
  private String newDescription;
  private Boolean newIsPrivate;

  private final EventInterface originalEvent;

  /**
   * Constructor.
   *
   * @param parent the parent frame
   * @param event the event to edit
   */
  public EditEventDialog(JFrame parent, EventInterface event) {
    super(parent, "Edit Event", true);
    this.confirmed = false;
    this.originalEvent = event;

    setSize(600, 750);
    setMinimumSize(new Dimension(600, 750));
    setLayout(new BorderLayout(15, 15));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    
    JLabel titleLabel = new JLabel("Edit Event");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createVerticalStrut(20));

    
    if (event.getSeriesId().isPresent()) {
      JLabel seriesLabel = new JLabel("This event is part of a recurring series");
      seriesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      seriesLabel.setForeground(CalendarTheme.SERIES_INDICATOR);
      seriesLabel.setAlignmentX(LEFT_ALIGNMENT);
      mainPanel.add(seriesLabel);
      mainPanel.add(Box.createVerticalStrut(10));

      JLabel scopeLabel = new JLabel("Which events do you want to edit?");
      scopeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
      scopeLabel.setAlignmentX(LEFT_ALIGNMENT);
      mainPanel.add(scopeLabel);
      mainPanel.add(Box.createVerticalStrut(10));

      JPanel seriesScopePanel = new JPanel();
      seriesScopePanel.setLayout(new BoxLayout(seriesScopePanel, BoxLayout.Y_AXIS));
      seriesScopePanel.setAlignmentX(LEFT_ALIGNMENT);
      seriesScopePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      seriesScopePanel.setBackground(CalendarTheme.PANEL_BG_LIGHT);

      final ButtonGroup scopeGroup = new ButtonGroup();
      Font radioFont = new Font("SansSerif", Font.PLAIN, 14);

      singleRadio = new JRadioButton(
          "Only this event (" + event.getStartDateTime().toLocalDate() + ")");
      singleRadio.setFont(radioFont);
      singleRadio.setBackground(CalendarTheme.PANEL_BG_LIGHT);
      
      fromDateRadio = new JRadioButton("This and all future events");
      fromDateRadio.setFont(radioFont);
      fromDateRadio.setBackground(CalendarTheme.PANEL_BG_LIGHT);
      
      allRadio = new JRadioButton("All events in this series");
      allRadio.setFont(radioFont);
      allRadio.setBackground(CalendarTheme.PANEL_BG_LIGHT);

      scopeGroup.add(singleRadio);
      scopeGroup.add(fromDateRadio);
      scopeGroup.add(allRadio);
      singleRadio.setSelected(true);

      seriesScopePanel.add(Box.createVerticalStrut(10));
      seriesScopePanel.add(singleRadio);
      seriesScopePanel.add(Box.createVerticalStrut(8));
      seriesScopePanel.add(fromDateRadio);
      seriesScopePanel.add(Box.createVerticalStrut(8));
      seriesScopePanel.add(allRadio);
      seriesScopePanel.add(Box.createVerticalStrut(10));

      mainPanel.add(seriesScopePanel);
      mainPanel.add(Box.createVerticalStrut(20));
    }

    
    JLabel fieldsLabel = new JLabel("Edit Event Details:");
    fieldsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    fieldsLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(fieldsLabel);
    mainPanel.add(Box.createVerticalStrut(15));

    
    mainPanel.add(createFieldPanel("Subject:", 
        subjectField = new JTextField(30), event.getSubject()));
    mainPanel.add(Box.createVerticalStrut(10));

    
    dateField = new JTextField(15);
    dateField.setText(event.getStartDateTime().toLocalDate().format(DATE_FORMAT));
    mainPanel.add(createFieldPanel("Date (yyyy-MM-dd):", dateField, null));
    mainPanel.add(Box.createVerticalStrut(10));

    
    startTimeField = new JTextField(10);
    startTimeField.setText(event.getStartDateTime().toLocalTime().format(TIME_FORMAT));
    mainPanel.add(createFieldPanel("Start Time (HH:mm):", startTimeField, null));
    mainPanel.add(Box.createVerticalStrut(10));

    
    endTimeField = new JTextField(10);
    endTimeField.setText(event.getEndDateTime().toLocalTime().format(TIME_FORMAT));
    mainPanel.add(createFieldPanel("End Time (HH:mm):", endTimeField, null));
    mainPanel.add(Box.createVerticalStrut(10));

    
    locationField = new JTextField(30);
    locationField.setText(event.getLocation().orElse(""));
    mainPanel.add(createFieldPanel("Location:", locationField, null));
    mainPanel.add(Box.createVerticalStrut(10));

    
    JLabel descLabel = new JLabel("Description:");
    descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    descLabel.setAlignmentX(LEFT_ALIGNMENT);
    mainPanel.add(descLabel);
    mainPanel.add(Box.createVerticalStrut(5));
    
    descriptionArea = new JTextArea(3, 30);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
    descriptionArea.setText(event.getDescription().orElse(""));
    JScrollPane descScroll = new JScrollPane(descriptionArea);
    descScroll.setAlignmentX(LEFT_ALIGNMENT);
    descScroll.setMaximumSize(new Dimension(500, 80));
    mainPanel.add(descScroll);
    mainPanel.add(Box.createVerticalStrut(10));

    
    privateCheckbox = new JCheckBox("Mark as private event");
    privateCheckbox.setSelected(event.isPrivate());
    privateCheckbox.setAlignmentX(LEFT_ALIGNMENT);
    privateCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
    mainPanel.add(privateCheckbox);
    mainPanel.add(Box.createVerticalStrut(20));

    
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    
    JButton saveButton = ButtonFactory.createSaveButton("Save Changes", 
        () -> handleSave());
    buttonPanel.add(saveButton);

    JButton cancelButton = ButtonFactory.createCancelButton("Cancel", () -> {
      confirmed = false;
      dispose();
    });
    buttonPanel.add(cancelButton);

    mainPanel.add(buttonPanel);

    JScrollPane scrollPane = new JScrollPane(mainPanel);
    scrollPane.setBorder(null);
    add(scrollPane, BorderLayout.CENTER);

    setLocationRelativeTo(parent);
  }

  private JPanel createFieldPanel(String labelText, JTextField field, String prefill) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(LEFT_ALIGNMENT);

    JLabel label = new JLabel(labelText);
    label.setFont(new Font("SansSerif", Font.PLAIN, 14));
    label.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(label);
    panel.add(Box.createVerticalStrut(5));

    if (prefill != null) {
      field.setText(prefill);
    }
    field.setFont(new Font("SansSerif", Font.PLAIN, 14));
    field.setMaximumSize(new Dimension(500, 30));
    field.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(field);

    return panel;
  }

  private void handleSave() {
    try {
      if (originalEvent.getSeriesId().isPresent()) {
        if (singleRadio.isSelected()) {
          this.editScope = EditScope.SINGLE;
        } else if (fromDateRadio.isSelected()) {
          this.editScope = EditScope.FROM_DATE;
        } else {
          this.editScope = EditScope.ALL;
        }
      } else {
        this.editScope = EditScope.SINGLE;
      }

      String subject = subjectField.getText().trim();
      if (subject.isEmpty()) {
        showError("Subject cannot be empty");
        return;
      }
      this.newSubject = subject.equals(originalEvent.getSubject()) ? null : subject;

      LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMAT);
      LocalTime startTime = LocalTime.parse(startTimeField.getText().trim(), TIME_FORMAT);
      LocalTime endTime = LocalTime.parse(endTimeField.getText().trim(), TIME_FORMAT);

      LocalDateTime startDt = LocalDateTime.of(date, startTime);
      LocalDateTime endDt = LocalDateTime.of(date, endTime);

      if (!endDt.isAfter(startDt)) {
        showError("End time must be after start time");
        return;
      }

      this.newStartDateTime = startDt.equals(originalEvent.getStartDateTime()) ? null : startDt;
      this.newEndDateTime = endDt.equals(originalEvent.getEndDateTime()) ? null : endDt;

      String loc = locationField.getText().trim();
      String originalLoc = originalEvent.getLocation().orElse("");
      this.newLocation = loc.equals(originalLoc) ? null : (loc.isEmpty() ? "" : loc);

      String desc = descriptionArea.getText().trim();
      String originalDesc = originalEvent.getDescription().orElse("");
      this.newDescription = desc.equals(originalDesc) ? null : (desc.isEmpty() ? "" : desc);

      boolean isPriv = privateCheckbox.isSelected();
      this.newIsPrivate = (isPriv == originalEvent.isPrivate()) ? null : isPriv;

      if (newSubject == null && newStartDateTime == null && newEndDateTime == null
          && newLocation == null && newDescription == null && newIsPrivate == null) {
        int response = JOptionPane.showConfirmDialog(
            this,
            "No changes detected. Close without saving?",
            "No Changes",
            JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
          confirmed = false;
          dispose();
        }
        return;
      }

      this.confirmed = true;
      dispose();

    } catch (DateTimeParseException e) {
      showError("Invalid date or time format.\n\nDate: yyyy-MM-dd\nTime: HH:mm");
    }
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Validation Error",
        JOptionPane.ERROR_MESSAGE);
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

  public EditScope getEditScope() {
    return editScope;
  }

  public String getNewSubject() {
    return newSubject;
  }

  public LocalDateTime getNewStartDateTime() {
    return newStartDateTime;
  }

  public LocalDateTime getNewEndDateTime() {
    return newEndDateTime;
  }

  public String getNewLocation() {
    return newLocation;
  }

  public String getNewDescription() {
    return newDescription;
  }

  public Boolean getNewIsPrivate() {
    return newIsPrivate;
  }
}
