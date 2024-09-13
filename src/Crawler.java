import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Crawler class that crawls a website to extract email addresses ending with
 * "ksu.edu.sa".
 *
 * <p>
 * This class uses the Jsoup library to parse HTML from web pages and extracts
 * email addresses using regular expressions. The emails and visited URLs are
 * stored in sets to avoid duplication. The results are written to a file after
 * the crawl is complete.
 * 
 * @author BADER ALMUTLAQ
 * @since 2024/09/14
 */
public class Crawler {

    /**
     * A set to store the URLs of the visited websites.
     */
    private static HashSet<String> visited = new HashSet<>();

    /**
     * A set to store the unique email addresses found during crawling.
     */
    private static HashSet<String> foundEmails = new HashSet<>();

    /**
     * Main method to start the web crawling process.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String url = "https://ccis.ksu.edu.sa/en"; // Starting URL
        String fileName = "KSU_Emails.txt"; // File to save extracted emails

        // Start timer to measure the execution time
        long startTime = System.currentTimeMillis();

        int level = 0; // Crawl depth level

        // Start crawling from the given URL
        crawlWebsite(url, level);

        // Write the extracted emails to a file
        try {
            FileWriter writer = new FileWriter(fileName);
            for (String email : foundEmails) {
                writer.write(email + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not open the file: " + e.getMessage());
        }

        // End timer and calculate the execution time
        long endTime = System.currentTimeMillis();

        // Display the summary of the crawl
        System.out.println("\n\n-------------------------------------------------------");
        System.out.println("\nEmails have been saved successfully in " + fileName);
        System.out.println("\n-------------------------------------------------------");
        System.out.println("Crawling Summary:");
        System.out.println("-------------------------------------------------------\n");
        System.out.printf("%-45s %d%n%n", "Number of websites visited:", visited.size());
        System.out.printf("%-45s %d%n%n", "Number of emails crawled:", foundEmails.size());
        System.out.printf("%-45s %.3f s%n%n", "Time taken to crawl:", (endTime - startTime) / 1000.0);
        System.out.println("-------------------------------------------------------\n\n");
    }

    /**
     * Recursively crawls the website, starting from the given URL.
     *
     * <p>
     * This method crawls the website up to a certain depth (controlled by `level`)
     * and extracts email addresses found on each page. It avoids revisiting URLs
     * that have already been crawled and only follows links that contain
     * "ksu.edu.sa".
     *
     * @param url   The URL of the page to crawl
     * @param level The current level of recursion (depth)
     */
    public static void crawlWebsite(String url, int level) {
        if (level == 2) // Limit recursion depth to 2 levels
            return;

        if (visited.contains(url)) // Avoid revisiting URLs
            return;

        // Fetch the document from the URL
        Document doc = connection(url);
        if (doc == null) // If the document couldn't be fetched, return
            return;

        // Get the HTML content of the page
        String html = doc.html();

        // Extract email addresses from the HTML
        emailMatcher(html);

        // Get all links on the page and follow those containing "ksu.edu.sa"
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String nextPage = link.absUrl("href"); // Get absolute URL of the link
            if (nextPage.contains("ksu.edu.sa")) {
                // Recursively crawl the next page
                crawlWebsite(nextPage, level + 1);
            }
        }
    }

    /**
     * Establishes a connection to the given URL and retrieves its HTML document.
     *
     * <p>
     * If the connection is successful (HTTP status code 200), the URL is added
     * to the set of visited websites and the document is returned. Otherwise,
     * it logs the failure and returns null.
     *
     * @param url The URL of the page to connect to
     * @return The HTML document of the page, or null if the connection fails
     */
    public static Document connection(String url) {
        try {
            Connection con = Jsoup.connect(url); // Connect to the URL
            Document doc = con.get(); // Fetch the HTML document
            visited.add(url); // Add the URL to the visited set
            if (con.response().statusCode() == 200) { // Check if the connection was successful
                System.out.println("Crawling this page: " + url);
                return doc;
            } else {
                System.out.println("Could not connect to this website: " + url + " the status code is:"
                        + con.response().statusCode());
                return null;
            }
        } catch (IOException e) {
            // Log the error if connection fails
            System.err.println("Failed to crawl this URL: " + url + " due to: " + e.getMessage());
            return null;
        }
    }

    /**
     * Uses a regular expression to find and extract email addresses from the given
     * HTML content.
     *
     * <p>
     * The method looks for email addresses in the format of "ksu.edu.sa" and adds
     * them to the set of found emails to avoid duplicates.
     *
     * @param html The HTML content of the page to search for email addresses
     */
    public static void emailMatcher(String html) {
        // Regular expression to match KSU email addresses
        String rgx = "[a-zA-Z0-9_.%+-]+@ksu\\.edu\\.sa";
        Pattern pattern = Pattern.compile(rgx, Pattern.CASE_INSENSITIVE); // Compile the pattern
        Matcher matcher = pattern.matcher(html); // Match the pattern against the HTML content

        // Find all matches and add them to the foundEmails set
        while (matcher.find()) {
            String email = matcher.group(); // Extract the email address
            foundEmails.add(email); // Add to the set of emails
        }
    }
}