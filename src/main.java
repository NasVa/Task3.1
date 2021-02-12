import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class main {

    public static Scanner scan;

    public static boolean CheckCorrect(String[] argv){
        boolean isCorrect = true;
        if(argv.length % 2 == 0 || argv.length < 3) {
            System.out.println("Please, enter odd number of lines(3 and more)");
            isCorrect = false;
        }
        else
            for (int i = 0; i < argv.length; i++) {
                for (int j = i+1; j < argv.length; j++) {
                    if (argv[i].equals(argv[j])) {
                        System.out.println("All words must be unique. Try again!");
                        isCorrect = false;
                    }
                }
            }
        return isCorrect;
    }

    public static void getHMac(byte[] key, String word) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha256 = Mac.getInstance("HMacSHA256");
        SecretKeySpec sKey = new SecretKeySpec(key, "HMacSHA256");
        sha256.init(sKey);
        System.out.println("HMac:\n" + Base64.encodeBase64String(sha256.doFinal(word.getBytes("UTF-8"))));
    }

    public static int inputResult(String[] argv, int step, byte[] key) {
        System.out.println("Available moves:");
        for (int i = 0; i < argv.length; i++)
            System.out.println((i + 1) + " - " + argv[i]);
        System.out.println("0 - exit\nEnter your move: ");
        int move = scan.nextInt();
        if (move == 0)
            return 0;
        else {
            if (step == move - 1)
                System.out.println("Draw! Try again!");
            else
                for (int i = 1; i < argv.length / 2 + 1; i++) {
                    if (move + i - 1 == step) {
                        System.out.println("Your move:" + argv[move - 1] + "\nComputer move:" +
                                argv[step] + "\nYou lose\nHMac key: " + key);
                        return 1;
                    } else if (i == argv.length / 2) {
                        System.out.println("Your move:" + argv[move - 1] + "\nComputer move:" +
                                argv[step] + "\nYou win!\nHMac key: " + key);
                        return 1;
                    }
                }
            return 1;
        }
    }

    public static void main(String[] argv) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        int a = 1;
        boolean isCorrect = CheckCorrect(argv);
        while (isCorrect && (a != 0)) {
            scan = new Scanner(System.in);
            if (CheckCorrect(argv)) {
                SecureRandom ran = new SecureRandom();
                byte key[] = new byte[16];
                ran.nextBytes(key);
                int step = ran.nextInt(argv.length);
                String word = argv[step];
                getHMac(key, word);
                a = inputResult(argv, step, key);
            }
        }
    }
}
