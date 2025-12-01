package calendar.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 * Factory class for creating consistently styled buttons.
 * Reduces code duplication across dialog and view classes.
 */
public final class ButtonFactory {

  private ButtonFactory() {
    
  }

  /**
   * Creates a styled action button with consistent appearance.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createActionButton(String text, Color bgColor, Runnable action) {
    JButton button = new JButton(text);
    button.setFont(UIFonts.ACTION_BUTTON_FONT);
    button.setPreferredSize(UIConstants.ACTION_BUTTON_SIZE);
    button.setBackground(bgColor);
    button.setForeground(CalendarTheme.BUTTON_TEXT);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(false);
    if (action != null) {
      button.addActionListener(e -> action.run());
    }
    return button;
  }

  /**
   * Creates a styled dialog button (save/create).
   *
   * @param text the button text
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createSaveButton(String text, Runnable action) {
    return createDialogButton(text, CalendarTheme.BUTTON_SAVE_BG, action);
  }

  /**
   * Creates a styled dialog button (cancel).
   *
   * @param text the button text
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createCancelButton(String text, Runnable action) {
    return createDialogButton(text, CalendarTheme.BUTTON_CANCEL_BG, action);
  }

  /**
   * Creates a styled dialog button with specified color.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param action the action to perform on click
   * @return the configured button
   */
  private static JButton createDialogButton(String text, Color bgColor, Runnable action) {
    JButton button = new JButton(text);
    button.setFont(UIFonts.ACTION_BUTTON_FONT);
    button.setPreferredSize(UIConstants.DIALOG_BUTTON_SIZE);
    button.setBackground(bgColor);
    button.setForeground(CalendarTheme.BUTTON_TEXT);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(false);
    if (action != null) {
      button.addActionListener(e -> action.run());
    }
    return button;
  }
}


