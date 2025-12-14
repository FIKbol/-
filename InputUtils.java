package utils;

import java.util.Scanner;

public class InputUtils {
    private static final Scanner sc = new Scanner(System.in);

    public static int nextInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Введите число!");
            }
        }
    }

    public static String nextLine() {
        return sc.nextLine();
    }
}
