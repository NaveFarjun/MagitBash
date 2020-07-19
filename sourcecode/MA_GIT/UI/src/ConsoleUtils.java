import MagitExceptions.ReadInputException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class ConsoleUtils {

    public static Scanner scanner = new Scanner(System.in);


    public static String ReadLine() {
        return scanner.nextLine();
    }

    public static boolean ReadBoolean() {
        boolean isLegal = false;
        String input;
        do {
            input = scanner.nextLine();
            isLegal = BooleanValidation(input);
            if (!isLegal) {
                System.out.println(input + " is invalid input, please insert boolean value (Yes/No)");
            }
        } while (!isLegal);

        return convertStringToBoolean(input);
    }

    public static int ReadInt() {
        boolean isLegal = false;
        int input = 0;

        do {
            try {
                input = Integer.parseInt(scanner.nextLine());
                isLegal = true;
            } catch (NumberFormatException e) {
                System.out.println("invalid input, insert numeric value");
            }
        } while (!isLegal);
        return input;
    }

    public static int ReadIntInRange(int start, int end) {
        int value;
        value =ReadInt();
        while (!(value>=start && value<=end)){
            System.out.println("The value have to be from: "+start+" to: " +end);
            value=ReadInt();
        }
        return value;
    }


    private static boolean convertStringToBoolean(String input) {

        return isTrue(input);
    }

    private static boolean isTrue(String input) {
        return input.equals("true") || input.equals("True") || input.equals("TRUE") || input.equals("YES") || input.equals("yes") || input.equals("Yes") || input.equals("1");
    }

    private static boolean isFalse(String input) {
        return input.equals("false") || input.equals("False") || input.equals("FALSE") || input.equals("NO") || input.equals("no") || input.equals("No") || input.equals("0");
    }

    private static boolean BooleanValidation(String input) {
        return isTrue(input) || isFalse(input);
    }
}
