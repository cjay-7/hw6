import calendar.command.CommandParser;
import calendar.controller.Controller;
import calendar.controller.ControllerInterface;
import calendar.model.CalendarManager;
import calendar.view.ConsoleView;
import calendar.view.ViewInterface;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Main entry point for the calendar application.
 * Supports three execution modes: GUI (default), interactive, and headless.
 *
 * <p>IMPROVEMENTS:
 * - Better error messages with usage examples
 * - Proper exception handling and exit codes
 * - Thread-safe GUI initialization
 * - Clear mode selection logic
 */
public class CalendarRunner {
  /**
   * Main method to run the calendar application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {

      if (args.length == 0) {
        launchGui();
        return;
      }

      if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
        printUsageAndExit();
        return;
      }

      String mode = args[1].toLowerCase();

      switch (mode) {
        case "interactive":
          launchInteractive();
          break;
        case "headless":
          if (args.length < 3) {
            System.err.println("ERROR: Headless mode requires a commands file path.\n");
            printUsageAndExit();
            return;
          }
          launchHeadless(args[2]);
          break;
        default:
          System.err.println("ERROR: Invalid mode '" + mode + "'.\n");
          printUsageAndExit();
          break;
      }
    } catch (IOException e) {
      System.err.println("FATAL ERROR: " + e.getMessage());
      e.printStackTrace();
      System.exit(2);
    } catch (Exception e) {
      System.err.println("UNEXPECTED ERROR: " + e.getMessage());
      e.printStackTrace();
      System.exit(3);
    }
  }

  /**
   * Prints usage information and exits.
   */
  private static void printUsageAndExit() {
    System.err.println("================================================================");
    System.err.println("        Calendar Application - Usage Instructions");
    System.err.println("================================================================");
    System.err.println();
    System.err.println("USAGE:");
    System.err.println();
    System.err.println("  1. GUI Mode (Graphical Interface):");
    System.err.println("     java -jar calendar.jar");
    System.err.println("     OR double-click the JAR file");
    System.err.println();
    System.err.println("  2. Interactive Mode (Command Line):");
    System.err.println("     java -jar calendar.jar --mode interactive");
    System.err.println();
    System.err.println("  3. Headless Mode (Batch Processing):");
    System.err.println("     java -jar calendar.jar --mode headless <commands-file>");
    System.err.println();
    System.err.println("  4. Headless-to-GUI Mode (Process commands then open GUI):");
    System.err.println("     java -jar calendar.jar --mode headless-gui <commands-file>");
    System.err.println();
    System.err.println("EXAMPLES:");
    System.err.println("  java -jar calendar.jar");
    System.err.println("  java -jar calendar.jar --mode interactive");
    System.err.println("  java -jar calendar.jar --mode headless res/commands.txt");
    System.err.println("  java -jar calendar.jar --mode headless-gui res/commands.txt");
    System.err.println();
    System.exit(1);
  }

  /**
   * Launches the graphical user interface.
   */
  private static void launchGui() {
    System.out.println("Starting Calendar Application in GUI mode...");

    javax.swing.SwingUtilities.invokeLater(() -> {
      try {

        javax.swing.UIManager.setLookAndFeel(
            javax.swing.UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {

        System.err.println("Warning: Could not set system look and feel");
      }

      CalendarManager manager = new CalendarManager();
      calendar.view.GuiView view = new calendar.view.GuiView();
      calendar.controller.GuiController controller =
          new calendar.controller.GuiController(manager, view);

      System.out.println("GUI initialized successfully.");
    });
  }

  /**
   * Launches interactive mode (command line with prompts).
   *
   * @throws IOException if I/O error occurs
   */
  private static void launchInteractive() throws IOException {
    System.out.println("Starting Calendar Application in INTERACTIVE mode...");
    System.out.println("Type commands at the prompt. Type 'exit' to quit.");
    System.out.println("===========================================================");
    System.out.println();

    CalendarManager manager = new CalendarManager();
    CommandParser parser = new CommandParser();
    ViewInterface view = new ConsoleView(System.out);

    Reader input = new InputStreamReader(System.in);
    ControllerInterface controller = new Controller(manager, view, parser, input, true);
    controller.run();

    System.out.println();
    System.out.println("===========================================================");
    System.out.println("Thank you for using Calendar Application!");
  }

  /**
   * Launches headless mode (batch processing from file).
   *
   * @param commandsFilePath path to the commands file
   * @throws IOException if I/O error occurs
   */
  private static void launchHeadless(String commandsFilePath) throws IOException {
    System.out.println("Starting Calendar Application in HEADLESS mode...");
    System.out.println("Processing commands from: " + commandsFilePath);
    System.out.println("═══════════════════════════════════════════════════════════");
    System.out.println();

    CalendarManager manager = new CalendarManager();
    CommandParser parser = new CommandParser();
    ViewInterface view = new ConsoleView(System.out);

    try (Reader reader = new FileReader(commandsFilePath)) {
      ControllerInterface controller = new Controller(manager, view, parser, reader, false);
      controller.run();
    } catch (java.io.FileNotFoundException e) {
      System.err.println("ERROR: Commands file not found: " + commandsFilePath);
      System.err.println("Please check the file path and try again.");
      System.exit(1);
    }

    System.out.println();
    System.out.println("═══════════════════════════════════════════════════════════");
    System.out.println("Batch processing completed successfully.");
  }
}