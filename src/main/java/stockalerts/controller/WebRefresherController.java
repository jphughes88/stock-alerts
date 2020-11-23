package stockalerts.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stockalerts.scheduler.amazon.AmazonClient;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WebRefresherController {

    private AmazonClient amazonClient;

    @Autowired
    public WebRefresherController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping("/amazon")
    public String getAmazonPage() {
        Optional<Document> doc = amazonClient.getAmazonDocument();

        if (doc.isPresent()) {
            Elements elementsContainingText = doc.get().getElementsContainingText("Experience lightning-fast loading with an ultra-high-speed SSD, deeper immersion with support for haptic feedback, adaptive triggers and 3D Audio, and an all-new generation of incredible PlayStation games");
            return elementsContainingText.get(0).toString();
        } else {
            return null;
        }
    }

    @GetMapping("/argos")
    public String getArgosPage() throws IOException {

        Document doc = Jsoup.connect("https://www.argos.co.uk/browse/technology/video-games-and-consoles/ps5/ps5-consoles/c:812421/").get();
        return doc.toString();
    }

    @GetMapping("/currys")
    public String getCurrysPage() throws IOException {

        Document doc = Jsoup.connect("https://www.currys.co.uk/gbuk/search-keywords/xx_xx_xx_xx_xx/ps5/xx-criteria.html").get();
        return doc.toString();
    }
}