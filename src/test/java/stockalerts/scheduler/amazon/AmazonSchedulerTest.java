package stockalerts.scheduler.amazon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import stockalerts.email.EmailClient;
import stockalerts.utils.Recipients;
import stockalerts.utils.ThreadSleeper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AmazonSchedulerTest {
    @Mock
    EmailClient emailClient;
    @Mock
    AmazonClient amazonClient;
    @Mock
    ThreadSleeper threadSleeper;
    @Mock
    Document document;
    @Mock
    Elements elementsContainingText;
    @Mock
    Element element;
    @InjectMocks
    AmazonScheduler amazonScheduler;

    private static final String UNSUCCESSFUL_AMAZON_REQUEST_SUBJECT = "Unsuccessful Amazon Requests";
    private static final String PLEASE_INVESTIGATE = "Please investigate";
    private static final String ON_SALE_SUBJECT = "Johnny's PS5 update - Amazon - on sale!";
    private static final String PS5_URL = "https://www.amazon.co.uk/PlayStation-9395003-5-Console/dp/B08H95Y452/ref=sr_1_1?dchild=1&keywords=ps5&qid=1605894982&rnid=300703&s=videogames&sr=1-1";
    private static final String SUCCESSFUL_REQUEST_PAGE_TEXT = "Experience lightning-fast loading with an ultra-high-speed SSD, deeper immersion with support for haptic feedback, adaptive triggers and 3D Audio, and an all-new generation of incredible PlayStation games";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenAmazonClientReturnsOptionalEmpty3TimesThenSendUnsuccessfulAmazonRequestEmailToJohnny() throws InterruptedException {
        when(amazonClient.getAmazonDocument()).thenReturn(Optional.empty());
        doNothing().when(threadSleeper).sleep(9000000);

        amazonScheduler.scheduleAmazonScrape();
        amazonScheduler.scheduleAmazonScrape();
        amazonScheduler.scheduleAmazonScrape();

        verify(emailClient, times(1)).sendEmail(UNSUCCESSFUL_AMAZON_REQUEST_SUBJECT, PLEASE_INVESTIGATE, Recipients.JOHNNY);
    }

    @Test
    void givenAmazonClientReturnsDocumentContainingAddToCartButtonThenSendEmailsToEveryone() throws InterruptedException {
        when(amazonClient.getAmazonDocument()).thenReturn(Optional.of(document));
        when(document.getElementsContainingText(SUCCESSFUL_REQUEST_PAGE_TEXT)).thenReturn(elementsContainingText);
        when(elementsContainingText.size()).thenReturn(1);
        when(document.getElementById("add-to-cart-button")).thenReturn(element);
        when(document.getElementById("buy-now-button")).thenReturn(null);
        amazonScheduler.scheduleAmazonScrape();

        doNothing().when(threadSleeper).sleep(900000);

        verify(emailClient, times(1)).sendEmail(ON_SALE_SUBJECT, PS5_URL, Recipients.ALL_RECIPIENTS);
    }

    @Test
    void givenAmazonClientReturnsDocumentContainingBuyNowButtonThenSendEmailsToEveryone() throws InterruptedException {
        when(amazonClient.getAmazonDocument()).thenReturn(Optional.of(document));
        when(document.getElementsContainingText(SUCCESSFUL_REQUEST_PAGE_TEXT)).thenReturn(elementsContainingText);
        when(elementsContainingText.size()).thenReturn(1);
        when(document.getElementById("add-to-cart-button")).thenReturn(null);
        when(document.getElementById("buy-now-button")).thenReturn(element);
        amazonScheduler.scheduleAmazonScrape();

        doNothing().when(threadSleeper).sleep(900000);

        verify(emailClient, times(1)).sendEmail(ON_SALE_SUBJECT, PS5_URL, Recipients.ALL_RECIPIENTS);
    }

    @Test
    void givenAmazonClientReturnsDocumentNotContainingBuyNowOrAddToCartButtonThenSendNoEmails() throws InterruptedException {
        when(amazonClient.getAmazonDocument()).thenReturn(Optional.of(document));
        when(document.getElementsContainingText(SUCCESSFUL_REQUEST_PAGE_TEXT)).thenReturn(elementsContainingText);
        when(elementsContainingText.size()).thenReturn(1);
        when(document.getElementById("add-to-cart-button")).thenReturn(null);
        when(document.getElementById("buy-now-button")).thenReturn(null);
        amazonScheduler.scheduleAmazonScrape();

        doNothing().when(threadSleeper).sleep(900000);

        verify(emailClient, times(0)).sendEmail(any(), any(), any());
    }
}
