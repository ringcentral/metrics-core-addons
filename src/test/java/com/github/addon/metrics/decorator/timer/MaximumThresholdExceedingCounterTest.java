package com.github.addon.metrics.decorator.timer;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.github.addon.metrics.decorator.Decorators;
import junit.framework.TestCase;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MaximumThresholdExceedingCounterTest extends TestCase {

    private static final Duration THRESHOLD = Duration.ofMillis(100);

    private final Counter exceedingCounter = new Counter();
    private final Timer targetTimer = new Timer();
    private final Timer decorator = Decorators.forTimer(targetTimer).withMaximumExceedingThresholdCounter(exceedingCounter, THRESHOLD).build();

    @Test
    public void test() {
        decorator.update(THRESHOLD.toMillis() + 1, TimeUnit.MILLISECONDS);
        assertEquals(1, exceedingCounter.getCount());

        decorator.update(THRESHOLD.toMillis() - 1, TimeUnit.MILLISECONDS);
        assertEquals(1, exceedingCounter.getCount());

        decorator.update(THRESHOLD.toMillis(), TimeUnit.MILLISECONDS);
        assertEquals(1, exceedingCounter.getCount());

        decorator.update(THRESHOLD.toMillis() + 100500, TimeUnit.MILLISECONDS);
        assertEquals(2, exceedingCounter.getCount());

        assertEquals(4, targetTimer.getCount());
    }

}