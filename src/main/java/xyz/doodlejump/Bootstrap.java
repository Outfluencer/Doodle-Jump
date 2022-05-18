package xyz.doodlejump;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class Bootstrap {

    public static void main(final String[] args) throws Exception {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            System.err.println("This Game requires Java 8 or above to function! Please download and install it!");
            System.out.println("You can check your Java version with the command: java -version");
            return;
        }

        DoodleJump.startWindow();
    }

}
