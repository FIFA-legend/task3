package by.bsuir;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        checkIsOdd(args);
        checkIsEqual(args);
        SecureRandom secureRandom = new SecureRandom();
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            byte[] generatedKey = secureRandom.generateSeed(16);
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(generatedKey, "HmacSHA256");
            sha256.init(secretKey);
            int computerChoice = random.nextInt(args.length);
            byte[] hash = sha256.doFinal(args[computerChoice].getBytes(StandardCharsets.UTF_8));
            System.out.print("HMAC: ");
            System.out.println(new String(Hex.encodeHex(hash, false)));
            printMenu(args);
            int userChoice = makeChoice(args.length, scanner);
            if (userChoice == 0) break;
            System.out.println("Your move: " + args[userChoice - 1]);
            System.out.println("Computer move: " + args[computerChoice]);
            determineWinner(userChoice, computerChoice, args.length / 2);
            System.out.print("Key: ");
            System.out.println(new String(Hex.encodeHex(generatedKey, false)));
            System.out.println();
        }
    }

    private static void checkIsOdd(String[] args) {
        if (args.length < 3 || args.length % 2 != 1) {
            System.out.println("The amount of arguments must be greater than or equal to 3");
            System.out.println("The number of arguments must be odd");
            System.exit(0);
        }
    }

    private static void checkIsEqual(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            for (int j = i + 1; j < args.length; j++) {
                if (args[i].equals(args[j])) {
                    System.out.println("Arguments must not be equal");
                    System.out.println("Argument " + (i + 1) + " equals to argument " + (j + 1));
                    System.exit(0);
                }
            }
        }
    }

    private static void printMenu(String[] args) {
        System.out.println("Available moves:");
        for (int i = 0; i < args.length; i++) {
            System.out.println((i + 1) + " - " + args[i]);
        }
        System.out.println("0 - exit");
    }

    private static int makeChoice(int maxValue, Scanner scanner) {
        int choice = -1;
        while (choice < 0 || choice > maxValue) {
            try {
                System.out.print("Your move (from 0 to " + maxValue + "): ");
                choice = scanner.nextInt();
            } catch (RuntimeException e) {
                System.out.println("You answer must be integer");
            }
        }
        return choice;
    }

    private static void determineWinner(int userChoice, int computerChoice, int amountOfChoices) {
        if (userChoice - 1 == computerChoice) {
            System.out.println("Draw");
        } else if ((userChoice - 1 > computerChoice && userChoice - 1 <= computerChoice + amountOfChoices)
                || (computerChoice + amountOfChoices >= amountOfChoices && userChoice - 1 <= (computerChoice + amountOfChoices) % amountOfChoices)) {
            System.out.println("You win!!!");
        } else {
            System.out.println("You lose :(");
        }
    }
}
