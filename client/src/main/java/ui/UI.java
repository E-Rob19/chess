package ui;

import java.util.Scanner;

public class UI {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.printf("Type your numbers%n>>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var words = line.split(" ");

            var equation = String.join(" ", words);
            System.out.printf("%s %n", equation);
        }
    }

}
