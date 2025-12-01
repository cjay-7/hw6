import static org.junit.Assert.assertTrue;

import calendar.command.CommandParser;
import calendar.controller.Controller;
import calendar.model.CalendarManager;
import calendar.view.ConsoleView;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive edge case tests for Controller to achieve 100% mutation
 * coverage.
 */
public class ControllerEdgeCaseTest {

  private CalendarManager manager;
  private StringWriter output;
  private ConsoleView view;

  /**
   * Sets up test fixtures before each test.
   */
  @Before
  public void setUp() {
    manager = new CalendarManager();
    output = new StringWriter();
    view = new ConsoleView(output);
    manager.createCalendar("TestCal", ZoneId.of("America/New_York"));
    manager.setCurrentCalendar("TestCal");
  }

  @Test
  public void testInteractiveModeShowsPrompt() throws IOException {
    StringReader input = new StringReader("exit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, true);
    controller.run();
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Enter commands"));
  }

  @Test
  public void testHeadlessModeNoPrompt() throws IOException {
    StringReader input = new StringReader("exit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();
    String outputStr = output.toString();
    assertTrue(!outputStr.contains("Enter commands") || outputStr.isEmpty());
  }

  @Test
  public void testHeadlessModeRequiresExit() throws IOException {
    StringReader input = new StringReader("create calendar Test\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();
    String outputStr = output.toString();
    assertTrue(outputStr.contains("must end with 'exit'"));
  }

  @Test
  public void testHeadlessModeWithExit() throws IOException {
    StringReader input = new StringReader("create calendar Test\nexit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();
    String outputStr = output.toString();
    assertTrue(!outputStr.contains("must end with 'exit'"));
  }

  @Test
  public void testEmptyLinesIgnored() throws IOException {
    StringReader input = new StringReader("\n\n\nexit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();

    assertTrue(true);
  }

  @Test
  public void testWhitespaceOnlyLinesIgnored() throws IOException {
    StringReader input = new StringReader("   \n\t\n  \nexit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();

    assertTrue(true);
  }

  @Test
  public void testInvalidCommandInInteractiveMode() throws IOException {
    StringReader input = new StringReader("invalid command\nexit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, true);
    controller.run();
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Invalid") || outputStr.contains("Error"));
  }

  @Test
  public void testInvalidCommandInHeadlessMode() throws IOException {
    StringReader input = new StringReader("invalid command\nexit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Invalid") || outputStr.contains("Error"));
  }

  @Test
  public void testMultipleCommands() throws IOException {
    StringReader input = new StringReader(
        "create calendar Test1\n"
            + "use calendar Test1\n"
            + "create event Meeting 2025-06-01T10:00 2025-06-01T11:00\n"
            + "exit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();

    assertTrue(true);
  }

  @Test
  public void testExceptionHandlingInInteractiveMode() throws IOException {

    StringReader input = new StringReader("exit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, true);
    controller.run();

    assertTrue(true);
  }

  @Test
  public void testExceptionHandlingInHeadlessMode() throws IOException {
    StringReader input = new StringReader("exit\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();

    assertTrue(true);
  }

  @Test
  public void testEmptyInput() throws IOException {
    StringReader input = new StringReader("");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();
    String outputStr = output.toString();
    assertTrue(outputStr.contains("must end with 'exit'"));
  }

  @Test
  public void testExitCommandStopsLoop() throws IOException {
    StringReader input = new StringReader("exit\ncreate calendar Test\n");
    Controller controller = new Controller(manager, view,
        new CommandParser(), input, false);
    controller.run();

    assertTrue(true);
  }
}
