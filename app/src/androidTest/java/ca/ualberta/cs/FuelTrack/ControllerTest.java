package ca.ualberta.cs.FuelTrack;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by cjresler on 2016-01-30.
 */
public class ControllerTest extends ActivityInstrumentationTestCase2 {
    public ControllerTest(){
        super(FuelTrackActivity.class);
    }

    public void testUpdateTotalCost(){
        Controller controller = new Controller();
        ArrayList<Log> logs = new ArrayList<Log>();
        Log log = new Log();
        Log log2 = new Log();
        log.setAmount(10);
        log.setUnit_cost(90);
        log2.setAmount(10);
        log2.setUnit_cost(100);
        logs.add(log);
        logs.add(log2);

        controller.updateTotalCost(logs);
        assertTrue(controller.getTotalCost() == 19);
    }
}
