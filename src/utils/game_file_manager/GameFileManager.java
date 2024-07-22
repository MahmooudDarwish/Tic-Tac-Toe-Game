package utils.game_file_manager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

}
