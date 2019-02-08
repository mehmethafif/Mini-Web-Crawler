import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawl{
  String startURL;
  Writer out;
  int MAX_DEPTH = 2; // maximum depth to crawl
  private Set<String> visitedUrls = new HashSet<>(); // set of visited urls, not to visit again


  public Crawl(String startURL, Writer out){
    this.startURL = startURL;
    this.out = out;
  }


  public void start(String URL, int depth) throws IOException {
    //Visit all not yet visited URLS to get the page.
    //Check which pages are linked there and follow them.
    //Write every URL that is successfully visited into the Writer in its own line.

    if ((!visitedUrls.contains(URL) && (depth < MAX_DEPTH))) {  // checks if url is in visitedUrls set
      System.out.println("[+] Depth: " + depth + " [" + URL + "]");
      writeln(URL); // writes url to file
      try {
        visitedUrls.add(URL); // adds url to visited urls set
        Document document = Jsoup.connect(URL).get(); // gets the page of the url
        Elements linksOnPage = document.select("a[href]"); // makes a list of urls in page
        depth++; // incremenst the depth
        for (Element page : linksOnPage) {  // recursively calls start func for every url in page
          start(page.attr("abs:href"), depth);
        }
      } catch (IOException e) { // in case of error prints it
        System.err.println("For '" + URL + "': " + e.getMessage());
      }
    }
  }



  public void writeln (String string) throws IOException { // writes to file line by line
    out.write(string);
    out.write("\n");
  }

  public void closeOut() throws IOException{ // closes the writer
    out.close();
    System.out.println("[+] Finished");
  }


  public static void main(String[] args) throws IOException {
    System.out.println("[+]Started");
    String startURL = "https://mehmethafif.github.io/";
    String fileName = "crawl_out.txt";
    Crawl crawler = new Crawl(startURL, new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
    crawler.start(startURL, 0);
    crawler.closeOut();

  }

}