package stockalerts.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import stockalerts.email.EmailClient;
import stockalerts.utils.Recipients;
import stockalerts.webclient.ArgosRetriever;
import stockalerts.webclient.CurrysRetriever;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Configuration
@EnableScheduling
public class RefreshScheduler {

    private ArgosRetriever argosRetriever;
    private CurrysRetriever currysRetriever;
    private EmailClient emailClient;

    @Autowired
    public RefreshScheduler(ArgosRetriever argosRetriever, CurrysRetriever currysRetriever, EmailClient emailClient) {
        this.argosRetriever = argosRetriever;
        this.currysRetriever = currysRetriever;
        this.emailClient = emailClient;
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public void scheduleArgosScrape() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("Checking Argos - " + formatter.format(calendar.getTime()));

        if (argosRetriever.getAddToTrolley()) {
            emailClient.sendEmail("Johnny's PS5 update - Argos - on sale!", "https://www.argos.co.uk/browse/technology/video-games-and-consoles/ps5/ps5-consoles/c:812421/", Recipients.ALL_RECIPIENTS);
            System.out.println("*********** On sale!! - Argos *********** " + formatter.format(calendar.getTime()));
            Thread.sleep(900000);
        }

        Thread.sleep(2000);
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public void scheduleCurrysScrape() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("Checking Currys - " + formatter.format(calendar.getTime()));

        if (currysRetriever.get825GB()) {
            System.out.println("*********** On sale!! - Currys *********** " + formatter.format(calendar.getTime()));
            emailClient.sendEmail("Johnny's PS5 update - Currys - Search results contains PS5", "https://www.currys.co.uk/gbuk/search-keywords/xx_xx_xx_xx_xx/ps5/xx-criteria.html", Recipients.ALL_RECIPIENTS);
            Thread.sleep(5000000);
        }

        Thread.sleep(2000);
    }
}
