package helpers;
import io.github.cdimascio.dotenv.Dotenv;

public class BaseClass {

    public static String timestamp() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMalformed() //
                .ignoreIfMissing()
                .load();
        String timestamp = dotenv.get("TIMESTAMP");
        return timestamp;
    }

    public static String publicKey() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMalformed() //
                .ignoreIfMissing()
                .load();
        final String publicKey = dotenv.get("PUBLIC_KEY");
        return publicKey;
    }

    public static String hash() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMalformed() //
                .ignoreIfMissing()
                .load();
        final String hash = dotenv.get("HASH");
        return hash;
    }
}