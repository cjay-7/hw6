package calendar.command;

import calendar.model.CalendarInterface;
import calendar.model.CalendarManager;
import calendar.model.CalendarModelInterface;
import calendar.util.CsvExporter;
import calendar.util.IcalExporter;
import calendar.view.ViewInterface;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

/**
 * Command to export calendar to CSV or iCal file.
 * Format: export cal fileName.csv or export cal fileName.ical
 *
 * <p>The export format is automatically detected by the file extension:
 * - .csv: Exports to CSV format
 * - .ical or .ics: Exports to iCalendar format (RFC 5545)
 *
 * <p>DESIGN RATIONALE:
 * - Auto-detection by extension provides better user experience
 * - Supports multiple export formats without changing command syntax
 * - Displays absolute path so user knows where file is saved
 * - Platform-independent path handling
 */
public class ExportCommand extends BaseCommand {
  private final String fileName;

  /**
   * Creates an ExportCommand.
   *
   * @param fileName the name of the file to create (.csv or .ical)
   */
  public ExportCommand(String fileName) {
    this.fileName = fileName;
  }

  @Override
  protected String getOperationName() {
    return "export calendar";
  }

  @Override
  protected boolean doExecute(CalendarManager manager, ViewInterface view)
      throws IOException, DateTimeParseException, IllegalArgumentException {
    CalendarInterface currentCal = CommandHelper.getCurrentCalendar(manager, view);
    if (currentCal == null) {
      return false;
    }

    CalendarModelInterface model = currentCal.getModel();

    Path filePath = Paths.get(fileName).toAbsolutePath().normalize();

    
    Path currentDir = Paths.get("").toAbsolutePath();
    if (!filePath.startsWith(currentDir)) {
      view.displayError("Invalid file path: cannot write outside current directory");
      return false;
    }

    String lowerFileName = fileName.toLowerCase();

    if (lowerFileName.endsWith(".csv")) {
      String csvContent = CsvExporter.toCsv(model.getAllEvents());
      Files.writeString(filePath, csvContent);
      view.displayMessage("Calendar exported to: " + filePath);
      return true;
    } else if (lowerFileName.endsWith(".ical") || lowerFileName.endsWith(".ics")) {
      String icalContent = IcalExporter.toIcal(
          model.getAllEvents(),
          currentCal.getName(),
          currentCal.getTimezone()
      );
      Files.writeString(filePath, icalContent);
      view.displayMessage("Calendar exported to: " + filePath);
      return true;
    } else {
      view.displayError("Unsupported file format. Use .csv or .ical extension.");
      return false;
    }
  }
}

