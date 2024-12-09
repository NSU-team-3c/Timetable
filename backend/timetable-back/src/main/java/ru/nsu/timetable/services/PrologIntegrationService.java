package ru.nsu.timetable.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PrologIntegrationService {
    public String generateTimetable(String inputRequirementsPath, String queryType) throws IOException, InterruptedException {

        String prologExecutable = "./scryer-prolog";
        String prologScript = "generation.pl";
        String queryFile = "query.txt";
        String outputFilePath = "timetable.xml";
        writeQueryFile(queryType);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(prologExecutable, prologScript);
            processBuilder.redirectInput(new File(queryFile));
            processBuilder.redirectErrorStream(true);

            System.out.println("Starting process...");
            Process process = processBuilder.start();

            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("OUTPUT: " + line);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading output: " + e.getMessage());
                }
            });

            outputThread.start();

            int exitCode = process.waitFor();
            outputThread.join();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException e) {
            System.err.println("Error starting process: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Process interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Timetable file created successfully: " + outputFilePath);
        return outputFilePath;
    }

    private void writeQueryFile(String queryType) throws IOException {
        String queryContent = switch (queryType) {
            case "run" -> "run.";
            case "create_timetable" -> "create_timetable.";
            default -> "requirements_variables(Rs, Vs, Rooms), labeling([ff], Vs), print_groups(Rs).";
        };

        Files.writeString(Path.of("query1.txt"), queryContent);
    }
}
