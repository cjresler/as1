package ca.ualberta.cs.FuelTrack;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by cjresler on 2016-01-30.
 */
public class LogTest extends ActivityInstrumentationTestCase2 {
    public LogTest(){
        super(FuelTrackActivity.class);
    }

    public void testUpdateCost(){
        Log log = new Log();
        log.setAmount(10);
        log.setUnit_cost(90);
        log.updateCost();
        assertTrue(log.getCost() == 9);
    }

    public void testUpdateLog(){
        Log log = new Log();
        Log log2 = new Log();

        log2.setStation("NEW");
        log2.setAmount(9);

        log.updateLog(log2);
        assertTrue(log.getStation().equalsIgnoreCase(log2.getStation()));
        assertTrue(log.getAmount() == log2.getAmount());
    }
}
