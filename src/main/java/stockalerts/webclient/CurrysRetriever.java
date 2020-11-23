package stockalerts.webclient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CurrysRetriever {

    public boolean get825GB() {
        try {
            Document doc = Jsoup.connect("https://www.currys.co.uk/gbuk/search-keywords/xx_xx_xx_xx_xx/ps5/xx-criteria.html").get();
            Elements elements = doc.getElementsContainingText("825 GB");
            return (elements.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
