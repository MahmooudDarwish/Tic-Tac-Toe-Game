package utils.game_file_manager;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameFileManager {

    private File file;
    private BufferedWriter writer;
    private List<String> recordedEvents;

    public GameFileManager() {
        this.recordedEvents = new ArrayList<>();
    }

    public void startRecordingGame() {
        // Generate file name with current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedNow = now.format(formatter);
        String filePath = "Records/Time_Stamp_" + formattedNow + ".txt";

        this.file = new File(filePath);

        try {
            System.out.println("Recording Started..... ");
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordEvent(int row, int col, String player) {
        String event = row + "," + col + "," + player;
        System.out.println("Record This Event: " + event);
        recordedEvents.add(event);
        if (writer != null) {
            try {
                writer.write(event);
                writer.newLine();
                writer.flush();  // Ensure data is written to the file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void endRecordingGame() {
        System.out.println("Recording End..... ");
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer = null;  // Ensure writer is nullified
            }
        }
    }

    public void deleteRecordedFile() {
        if (file != null && file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    public List<String> loadRecordedEvents(String fileName) {
        List<String> events = new ArrayList<>();
        String filePath = "Records/" + fileName + ".txt";
        File file = new File(filePath); // Create File object using filePath

        if (file.exists()) { // Check if the file exists
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    events.add(line); // Read each line and add to the list
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace in case of an error
            }
        } else {
            System.out.println("File does not exist: " + filePath); // Inform that file does not exist
        }

        return events; // Return the list of events
    }


    public static List<String> listRecordsFile(String directoryPath) {
        try (Stream<Path> files = Files.list(Paths.get(directoryPath))) {
            return files
                    .filter(Files::isRegularFile) // Ensure it's a file, not a directory
                    .map(Path::getFileName) // Get the file name
                    .map(Path::toString) // Convert Path to String
                    .map(filename -> {
                        int lastDotIndex = filename.lastIndexOf('.');
                        return (lastDotIndex == -1) ? filename : filename.substring(0, lastDotIndex);
                    }) // Remove file extension
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

/*
\package utils.game_file_manager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.shape.Path;

public class GameFileManager {

    private File file;
    private BufferedWriter writer;
    private List<String> recordedEvents;
    LocalDateTime now;
    public GameFileManager() {
        this.recordedEvents = new ArrayList<>();
    }

    public void startRecordingGame() {
        // Generate file name with current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedNow = now.format(formatter);
        String filePath = "Records/Time_Stamp_" + formattedNow + ".txt";

        this.file = new File(filePath);

        try {
            System.out.println("Recording Started..... ");
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordEvent(int row, int col, String player) {
        String event = row + "," + col + "," + player;
        System.out.println("Record This Event: " + event);
        recordedEvents.add(event);
        if (writer != null) {
            try {
                writer.write(event);
                writer.newLine();
                writer.flush();  // Ensure data is written to the file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void endRecordingGame() {
        System.out.println("Recording End..... ");
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer = null;  // Ensure writer is nullified
            }
        }
    }
    
    public void deleteRecordedFile() {
            file.delete();
        }
    
    public List<String> loadRecordedEvents() {
        List<String> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                events.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }
    
     public static List<String> listFileNames(String directoryPath) {
        try (Stream<Path> files = Files.list(Paths.get(directoryPath))) {
            return files
                    .filter(Files::isRegularFile) // Ensure it's a file, not a directory
                    .map(Path::getFileName)       // Get the file name
                    .map(Path::toString)          // Convert Path to String
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    

}
*/
