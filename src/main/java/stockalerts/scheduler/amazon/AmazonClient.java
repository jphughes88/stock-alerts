package stockalerts.scheduler.amazon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class AmazonClient {

    public Optional<Document> getAmazonDocument() {
        try {
            Document document = Jsoup.connect("https://www.amazon.co.uk/PlayStation-9395003-5-Console/dp/B08H95Y452/ref=sr_1_1?dchild=1&keywords=ps5&qid=1605894982&rnid=300703&s=videogames&sr=1-1")
                    .header("Host", "www.amazon.co.uk")
                    .header("Connection", "close")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Sec-Fetch-Site", "none")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                    .get();
            return Optional.of(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
