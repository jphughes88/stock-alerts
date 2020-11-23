package stockalerts.utils;

import org.springframework.stereotype.Component;

@Component
public class ThreadSleeper {

    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
