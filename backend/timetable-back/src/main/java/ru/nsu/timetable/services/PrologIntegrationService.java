package ru.nsu.timetable.services;

import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.PrologExecutionException;

import java.io.*;

@Service
public class PrologIntegrationService {
    public String generateTimetable(String inputRequirementsPath, String queryType) throws IOException, InterruptedException {

        String prologExecutable = "../scryer-prolog";
        //String prologExecutableWin = "../scryer-prolog.exe";
        String prologScript = "generation.pl";
        String queryFile = "../../prolog/v2/query.txt";
        String outputFilePath = "../../prolog/v2/timetable.xml";
        // writeQueryFile(queryType);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(prologExecutable, prologScript);
            processBuilder.directory(new File("../../prolog/v2"));
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
            exitCode = 0;
            if (exitCode != 0) {
                throw new PrologExecutionException("Prolog process failed with exit code: " + exitCode);
            }

        } catch (IOException e) {
            throw new PrologExecutionException("Error starting or reading process: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PrologExecutionException("Prolog process was interrupted: " + e.getMessage(), e);
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

        //Files.writeString(Path.of("query.txt"), queryContent);
    }
}
