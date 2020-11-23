package stockalerts.webclient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AmazonRetriever {

    public boolean getAddToCart() {
        try {
            Document doc = Jsoup.connect("https://www.amazon.co.uk/PlayStation-9395003-5-Console/dp/B08H95Y452/ref=sr_1_1?dchild=1&keywords=ps5&qid=1605894982&rnid=300703&s=videogames&sr=1-1").get();
            Element addToCartButton = doc.getElementById("add-to-cart-button");
            Element buyNowButton = doc.getElementById("buy-now-button");
            return (addToCartButton != null) || (buyNowButton != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
