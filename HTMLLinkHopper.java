/*
Alan Lai
HTTPLinkHopper Project
9/10/2021 - 10/10/2021
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.charset.MalformedInputException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class HTTPLinkHopper {
    public static Set<String> uniqueURLs = new HashSet<String>();
    public static void main(String[] args) throws IOException {
        // BREAK CASE: If argument is not two arguments, has an invalid URL, or second argument isn't an integer, throw an error
        if(args.length != 2 || !checkValidURL(args[0]) || !isInteger(args[1])) {
            System.out.println("Invalid Input, please use as: java WebCrawler <url:String> <# of hops:int>");
            System.exit(0);
        }
        // If Valid URL 
        runHopper(args);
    }


    public static void runHopper(String[] args) throws IOException {
        // Command line arguments: java WebCrawler (arg[0] is the URL) (arg[1] is the number of hops)
        String stringURL = args[0];
        int num_hops = Integer.parseInt(args[1]);

        // Decrement hop for every page visited.
        num_hops--; 

        // EDGE CASE: If URL ends with a backslash, remove the backslash. Still a valid link.
        //System.out.println(stringURL);
        if(stringURL.charAt(stringURL.length() - 1) == '/') {
            stringURL = stringURL.substring(0, stringURL.length() - 1);
        }
        
        // Establish URL object visiting link
        URL url = new URL(stringURL);
        // Establish connection using URL object
        URLConnection connection = new URL(stringURL).openConnection();
        // Open HTTP URL Connection for status code 
        HttpURLConnection HTTPconnection = (HttpURLConnection)url.openConnection();
        // Get x00 status code 
        int statusCode = HTTPconnection.getResponseCode();

        // BREAK CASE: If invalid status code occurs, stop the program and output error.
        if(statusCode>399 && statusCode<500) {
            System.out.println("ERROR!: 400 Code error");
            System.exit(0);
        // EDGE CASE: If the code is 300-499, connect to the URL and get the next link through InputStream. 
        } else if(statusCode>299 && statusCode<500) {
            connection.connect();
            InputStream stream = HTTPconnection.getInputStream();
            stringURL = connection.getURL().toString();
            stream.close();
        }

        // System.out.println(statusCode);

        // This URL should be added to our URL set to not be visited in the future.
        uniqueURLs.add(stringURL);
        // Output visited string to console.
        System.out.println("Hopped to " + stringURL);

        // Buffered reader will find get link stream from HTTP Connection
        BufferedReader reader = new BufferedReader(new InputStreamReader(HTTPconnection.getInputStream()));
        String line;
        StringBuilder currOutputString = new StringBuilder();
        while((line = reader.readLine()) != null) {
            //System.out.println(line);
            currOutputString.append(line);
        }
        // Find next URL to hop and output.
        String nextHoppedURL = getNextNewURL(currOutputString.toString());
        if(num_hops > 0) {
            // Run the hopper with the new URL
            runHopper(new String[]{nextHoppedURL, Integer.toString(num_hops)});
        } else {
            System.out.println("Finished.");
        }
    }
    

    private static String getNextNewURL(String current) throws IOException, MalformedURLException {
        // Pattern Object which with regex to help us find next URL
        Pattern linkPattern = Pattern.compile("<a[^>]*href=\"http", Pattern.CASE_INSENSITIVE);
        // Matcher Object starting from our current URL
        Matcher matchHelper = linkPattern.matcher(current);

        // Matcher Object will find next new URL.
        while(matchHelper.find()) {
            int index = current.indexOf("\"", current.indexOf("href", matchHelper.start() + 1));
            String nextURL = current.substring(index + 1, current.indexOf("\"", index + 5));

            // Establish connections (like earlier)
            URL url = new URL(nextURL);
            HttpURLConnection HTTPconnection =(HttpURLConnection)url.openConnection();
            int statusCode= HTTPconnection.getResponseCode();
            // If already visited or status code is 400, no need to add to Unique URLs.
            if(uniqueURLs.contains(nextURL) || statusCode > 399 && statusCode < 500) {
                continue;
            } else {
                uniqueURLs.add(nextURL);
                return nextURL;
            }
        }
        // In cases we don't find anymore, program stops.
        System.out.println("No more unique URLs to visit. Stopping program");
        System.exit(0);
        return "";
    }
    

    // checkValidURL will return true if a link is valid otherwise, returns false.
    private static boolean checkValidURL(String link) {
        try {
            new URL(link).toURI(); // If link doesn't throw an error, it is a valid URL. Return true in this case.
            URL url = new URL(link);
            HttpURLConnection HTTPconnection = (HttpURLConnection)url.openConnection();
            int statusCode = HTTPconnection.getResponseCode(); 
            return true;
        } catch(Exception err) {   // Otherwise, an error represents the link is invalid, and returns false.
            System.out.println(link + " is an invalid URL.");
            return false;
        }
    }

    // isInteger checks if a string is a valid integer which will be the number of hops.
    private static boolean isInteger(String toCheck) {
        if(toCheck == null || toCheck.equals("")) { // If string is invalid, return false.
            System.out.println("ERROR: Second argument is empty. Please put a number.");
            return false;
        }
        try {   // If the string can be parsed into an integer without error, it's a valid integer. 
            int val = Integer.parseInt(toCheck);
            if(val < 0) {
                System.out.println(toCheck + " is not a valid number. Please input a positive integer.");
                return false;
            }
            return true;
        } catch (NumberFormatException err) {
            System.out.println(toCheck + " is not a valid number.");
        }
        return false;
    }
}


// Build:
// javac HTTPLinkHopper.java

// Compile command:
// java HTTPLinkHopper <link>

/*
When program takes in invalid string, tell user how to properly use the program in command line

Fully qualified names/links only

If no more hrefs and hops left: Stop and say a message 

Try to catch Time Out errors

Initial link is counted as 1 hop

*/
