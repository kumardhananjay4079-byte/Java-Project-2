import java.io.*;
import java.util.*;

public class Main {
    private static LinkedList<String> document = new LinkedList<>();
    private static Stack<Edit> undoStack = new Stack<>();
    private static Stack<Edit> redoStack = new Stack<>();
    private static final String FILE_NAME = "document.txt";
    private static final Scanner sc = new Scanner(System.in);

    // Stores the type of edit and the affected line.
    static class Edit {
        String type;       // ADD or DELETE
        String line;

        Edit(String type, String line) {
            this.type = type;
            this.line = line;
        }
    }

    private static void addLine() {
        System.out.print("Enter text: ");
        String line = sc.nextLine();
        if (line.trim().isEmpty()) {
            System.out.println("Line cannot be empty.");
            return;
        }
        document.addLast(line);
        undoStack.push(new Edit("ADD", line));
        redoStack.clear();
        System.out.println("Line added.");
    }

    private static void deleteLastLine() {
        if (document.isEmpty()) {
            System.out.println("Document is empty. Nothing to delete.");
            return;
        }
        String removed = document.removeLast();
        undoStack.push(new Edit("DELETE", removed));
        redoStack.clear();
        System.out.println("Last line deleted.");
    }

    private static void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Edit edit = undoStack.pop();
        if (edit.type.equals("ADD")) {
            if (!document.isEmpty()) document.removeLast();
        } else {
            document.addLast(edit.line);
        }
        redoStack.push(edit);
        System.out.println("Undo done.");
    }

    private static void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }
        Edit edit = redoStack.pop();
        if (edit.type.equals("ADD")) {
            document.addLast(edit.line);
        } else {
            if (!document.isEmpty()) document.removeLast();
        }
        undoStack.push(edit);
        System.out.println("Redo done.");
    }

    private static void display() {
        if (document.isEmpty()) {
            System.out.println("Document is empty.");
            return;
        }
        System.out.println("Document:");
        int number = 1;
        for (String line : document) {
            System.out.println(number++ + ". " + line);
        }
    }

    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                document.addLast(line);
            }
            System.out.println("Document loaded.");
        } catch (IOException e) {
            System.out.println("Error loading document: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : document) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Document saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving document: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        loadFromFile();
        while (true) {
            System.out.println("\n===== UNDO / REDO TEXT EDITOR =====");
            System.out.println("1. Add line");
            System.out.println("2. Delete last line");
            System.out.println("3. Undo");
            System.out.println("4. Redo");
            System.out.println("5. Display");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Choose 1 to 6.");
                continue;
            }

            switch (choice) {
                case 1: addLine(); break;
                case 2: deleteLastLine(); break;
                case 3: undo(); break;
                case 4: redo(); break;
                case 5: display(); break;
                case 6:
                    saveToFile();
                    System.out.println("Exiting editor.");
                    return;
                default:
                    System.out.println("Invalid option. Choose 1 to 6.");
            }
        }
    }
}