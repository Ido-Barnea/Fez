package fez;

import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import fez.main.Objects.ResultObjects.InterpreterResult;
import fez.main.Subjects.List;
import fez.main.Runner;

public class Shell {

    public static Scanner scanner;

    /**
     * reads a fez program file.
     * note - the filePath has to be an absolute path
     * @param filePath the path of the fez program
     * @return the content of the fez program
     */
    private static String readProgram(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            StringBuilder text = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                text.append(line).append("\n");
                line = reader.readLine();
            }

            reader.close();
            return text.toString();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) System.out.printf("Error: Couldn't find file \"%s\"", filePath);
            else e.printStackTrace();
            return "";
        }
    }

    private static void runTerminal() {
        Runner runner = new Runner();
        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (!Objects.equals(input, "")) {
                if (input.equals("exit")) break;
                else if (input.split("\\s+")[0].equals("run")) { // If trying to run a Fez program
                    String[] splitInput = input.split("\\s+");
                    if (splitInput.length < 2) System.out.println("Error: File path is required.");
                    else runProgram(runner, splitInput[1]);
                } else {
                    InterpreterResult result = runner.run("<Program>", input, false);
                    if (result.exception() != null) System.out.println(result.exception());
                    else if (result.result() != null) {
                        List statements = (List) result.result();
                        if (statements.size() == 1) System.out.println(statements.get(0));
                        else if (statements.size() > 1) System.out.println(statements);
                    }
                }

                System.out.println();
            }
        }

        scanner.close();
    }

    private static void runProgram(Runner runner, String filePath) {
        String input = readProgram(filePath);

        if (!input.equals("")) {
            InterpreterResult result = runner.run(filePath, input, true);
            if (result.exception() != null) System.out.println(result.exception());
            else if (result.result() != null) {
                List statements = (List) result.result();
                if (statements.size() == 1) System.out.println(statements.get(0));
                else if (statements.size() > 1) System.out.println(statements);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        runTerminal();
    }
}