package calendar.controller;

import java.io.IOException;

/**
 * Interface defining the contract for calendar application controllers.
 *
 * <p>This interface follows the Controller component of the Model-View-Controller (MVC)
 * architecture pattern. Implementations are responsible for:
 * <ul>
 *   <li>Coordinating interactions between the Model (CalendarManager/CalendarModel)
 *       and View components</li>
 *   <li>Processing user input and translating it into model operations</li>
 *   <li>Managing the application lifecycle (startup, command processing, shutdown)</li>
 *   <li>Handling I/O operations and delegating display updates to the View</li>
 * </ul>
 *
 * <p>DESIGN RATIONALE:
 * <ul>
 *   <li>Single run() method allows different controller implementations
 *       (interactive, headless, GUI) to have their own execution strategies</li>
 *   <li>Controllers own the main application loop and determine when to exit</li>
 *   <li>IOException declaration allows controllers to handle I/O errors appropriately</li>
 * </ul>
 *
 * <p>Known implementations:
 * <ul>
 *   <li>{@code InteractiveController} - processes commands from stdin interactively</li>
 *   <li>{@code HeadlessController} - processes commands from a file</li>
 *   <li>{@code GuiController} - handles GUI-based calendar interactions</li>
 * </ul>
 *
 * @see calendar.model.CalendarManager
 * @see calendar.view.ViewInterface
 */
public interface ControllerInterface {

  /**
   * Starts the controller and begins processing user commands or interactions.
   *
   * <p>This method serves as the main entry point for the controller. Once called,
   * the controller takes control of the application flow and processes user input
   * until an exit condition is met (e.g., user types 'exit', EOF reached, or
   * window closed for GUI).
   *
   * <p>For interactive controllers, this method typically enters a read-eval-print
   * loop. For headless controllers, it processes all commands from the input source.
   * For GUI controllers, it initializes the event-driven interface.
   *
   * @throws IOException if an I/O error occurs while reading input or writing output,
   *                     such as when the input stream is closed unexpectedly or
   *                     when file operations fail
   */
  void run() throws IOException;
}

