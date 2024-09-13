# Web Crawler Email Extractor

## Overview

This is a Java-based web crawler that extracts email addresses ending with `@ksu.edu.sa` from web pages of King Saud University (KSU). It uses the **Jsoup** library to fetch and parse HTML documents, and regular expressions to match KSU emails. The results (extracted emails) are saved into a text file named `KSU_Emails.txt`.

## Features

- **Recursively crawls web pages**: Starts at a given URL and follows links containing `ksu.edu.sa`.
- **Email extraction**: Uses a regular expression to extract email addresses from the HTML.
- **Prevents duplicates**: Stores URLs and emails in sets to avoid processing duplicates.
- **Depth control**: Limits the recursion depth to two levels.
- **Performance tracking**: Tracks the time taken to crawl the website and displays statistics on completion.

## Technologies Used

- **Java**: Programming language used to implement the web crawler.
- **Jsoup**: A Java library used to parse HTML, fetch web pages, and extract data.
- **Regular Expressions**: To match and extract email addresses from the web pages.

## Prerequisites

- **Java 8** or higher.
- **Jsoup Library**: You can download Jsoup from [jsoup.org](https://jsoup.org/) or include it in your project using Maven:
  
  ```xml
  <dependency>
      <groupId>org.jsoup</groupId>
      <artifactId>jsoup</artifactId>
      <version>1.13.1</version>
  </dependency>

## How to Run

1. Clone or download this repository to your local machine.
2. Ensure that the **Jsoup** library is added to your project (if you're using an IDE like Eclipse or IntelliJ, you can add it through Maven or manually include the `.jar` file).
3. Compile and run the program from your IDE or command line.

   Example (using `javac` and `java` from the terminal):

   ```bash
   javac -cp .:jsoup-1.13.1.jar Crawler.java
   java -cp .:jsoup-1.13.1.jar Crawler
   ```
4. The program will crawl the specified KSU website and its subpages, extract emails, and save them to `KSU_Emails.txt`.

 ## Program Output Summary

- **Number of websites visited**: [Number]
- **Number of emails extracted**: [Number]
- **Time taken to complete the crawling process**: [Time]

Emails are saved to a text file: `KSU_Emails.txt`.

## Example Output

```bash
Crawling this page: https://www.ksu.edu.sa/en
Crawling this page: https://www.ksu.edu.sa/en/privacy
Crawling this page: https://www.ksu.edu.sa/en/terms

-------------------------------------------------------
Emails have been saved successfully in KSU_Emails.txt
-------------------------------------------------------
Crawling Summary:
-------------------------------------------------------
Number of websites visited:                   68
Number of emails crawled:                     408
Time taken to crawl:                          12.831 s
-------------------------------------------------------
```