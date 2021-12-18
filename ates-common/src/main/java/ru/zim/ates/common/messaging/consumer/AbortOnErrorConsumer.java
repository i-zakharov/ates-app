package ru.zim.ates.common.messaging.consumer;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public abstract class AbortOnErrorConsumer implements AbstractConsumer {

    protected static final int MAX_ERRORS_COUNT = 5;

    protected AtomicInteger errorsCounter = new AtomicInteger(0);
    protected ApplicationContext applicationContext;

    public AbortOnErrorConsumer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    public void receiveMessage(String message) {
        log.info("Received: {}", message);
        try {
            processMessage(message);
            errorsCounter.set(0);
        } catch (Throwable e) {
            onError(e);
        }
    }

    protected abstract void processMessage(String message);

    @SneakyThrows
    protected void onError(Throwable e) {
        if (errorsCounter.incrementAndGet() > MAX_ERRORS_COUNT) {
            abort();
        }
        throw e;
    }

    private void abort() {
        log.error("Too many errors aborting application");
        ((ConfigurableApplicationContext) applicationContext).close();
    }

}
