package stockalerts.scheduler.amazon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import stockalerts.email.EmailClient;
import stockalerts.utils.Recipients;
import stockalerts.utils.ThreadSleeper;
import stockalerts.utils.WebPageStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Configuration
@EnableScheduling
public class AmazonScheduler {

    private final EmailClient emailClient;
    private final AmazonClient amazonClient;
    private final ThreadSleeper threadSleeper;
    private static final String ON_SALE_SUBJECT = "Johnny's PS5 update - Amazon - on sale!";
    private static final String PS5_URL = "https://www.amazon.co.uk/PlayStation-9395003-5-Console/dp/B08H95Y452/ref=sr_1_1?dchild=1&keywords=ps5&qid=1605894982&rnid=300703&s=videogames&sr=1-1";
    private static final String AMAZON_SYSTEM_MESSAGE = "*********** On sale!! - Amazon *********** ";
    private static final String UNSUCCESSFUL_AMAZON_REQUEST_SUBJECT = "Unsuccessful Amazon Requests";
    private static final String PLEASE_INVESTIGATE = "Please investigate";
    private static final String SUCCESSFUL_REQUEST_PAGE_TEXT = "Experience lightning-fast loading with an ultra-high-speed SSD, deeper immersion with support for haptic feedback, adaptive triggers and 3D Audio, and an all-new generation of incredible PlayStation games";

    private static int unsuccessfulAmazonCalls = 0;

    @Autowired
    public AmazonScheduler(EmailClient emailClient, AmazonClient amazonClient, ThreadSleeper threadSleeper) {
        this.emailClient = emailClient;
        this.amazonClient = amazonClient;
        this.threadSleeper = threadSleeper;
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public void scheduleAmazonScrape() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("Checking Amazon - " + formatter.format(calendar.getTime()));
        WebPageStatus addToCart = getAddToCart();

        if (addToCart.equals(WebPageStatus.NOT_ON_SALE)) {
            unsuccessfulAmazonCalls = 0;
        } else if (addToCart.equals(WebPageStatus.ON_SALE)) {
            emailClient.sendEmail(ON_SALE_SUBJECT, PS5_URL, Recipients.ALL_RECIPIENTS);
            System.out.println(AMAZON_SYSTEM_MESSAGE + formatter.format(calendar.getTime()));
            threadSleeper.sleep(900000);
        } else if (addToCart.equals(WebPageStatus.UNSUCCESSFUL_CALL)) {
            unsuccessfulAmazonCalls++;
            if (unsuccessfulAmazonCalls > 2) {
                emailClient.sendEmail(UNSUCCESSFUL_AMAZON_REQUEST_SUBJECT, PLEASE_INVESTIGATE, Recipients.JOHNNY);
                threadSleeper.sleep(9000000);
            }
        }
        threadSleeper.sleep(2000);
    }

    private WebPageStatus getAddToCart() {
        Optional<Document> amazonDocument = amazonClient.getAmazonDocument();

        if (amazonDocument.isEmpty()) {
            return WebPageStatus.UNSUCCESSFUL_CALL;

        } else {
            Document document = amazonDocument.get();
            Elements elementsContainingText = document.getElementsContainingText(SUCCESSFUL_REQUEST_PAGE_TEXT);

            if (elementsContainingText.size() > 0) {
                Element addToCartButton = document.getElementById("add-to-cart-button");
                Element buyNowButton = document.getElementById("buy-now-button");
                return (addToCartButton != null) || (buyNowButton != null) ? WebPageStatus.ON_SALE : WebPageStatus.NOT_ON_SALE;
            } else {
                return WebPageStatus.UNSUCCESSFUL_CALL;
            }
        }
    }
}
