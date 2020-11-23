package stockalerts.webclient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ArgosRetriever {

    public boolean getAddToTrolley() {
        try {
            Document doc = Jsoup.connect("https://www.argos.co.uk/browse/technology/video-games-and-consoles/ps5/ps5-consoles/c:812421/").get();
            Elements btnCta = doc.getElementsMatchingText(" to Trolley");
            return (btnCta.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
