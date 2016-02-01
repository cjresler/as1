package ca.ualberta.cs.FuelTrack;

import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cjresler on 2016-01-30.
 *
 * This class controls the buttons and flow of the program.
 */
public class Controller {
    //Variables for keeping track of the state
    public static final int MODE_IDLE = 0;
    public static final int MODE_EDITING = 1;
    public static final int MODE_ADDING = 2;

    private int okCounter;
    private ErrorChecker check;
    private int currentMode;
    private int position;
    private double totalCost;

    public Controller() {
        this.okCounter = 0;
        this.check = new ErrorChecker();
        this.currentMode = 0;
        this.position = 0;
        this.totalCost = 0;
    }

    //okCounter
    public void setOkCounter(int okCounter) {
        this.okCounter = okCounter;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setMode(int newMode) {
        this.currentMode = newMode;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //Updates total cost by adding up costs of all logs
    public void updateTotalCost(ArrayList<Log> logs){
        totalCost = 0;
        for(int i = 0; i < logs.size(); i++){
            totalCost += logs.get(i).getCost();
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    //Handle Cancel button clicks
    public void cancelButtonClicked(EditText bodyText, Button add, Button cancel, Button ok, TextView label){
        disableOkButton(bodyText, add, cancel, ok, label);
        okCounter = 0;
        currentMode = MODE_IDLE;
        bodyText.setError(null);
    }

    //Handle OK button being clicked. Behavior changes based on okCounter variable, which
    //keeps track of which field is being entered at any given time.
    public void okButtonClicked(Log tempLog, EditText bodyText, TextView label, ArrayList<Log> logs, ArrayAdapter<Log> adapter,
                                TextView costText, Button add, Button cancel, Button ok) {
        //Reset hint to nothing
        bodyText.setHint("");
        //Handle Date entry
        if (okCounter == 0) {
            //Check for valid date format, display hint if incorrect
            if (!check.isValidDate(bodyText.getText().toString())) {
                check.invalidDate(bodyText);
            } else {
                bodyText.setHint("");
                String dateStr = bodyText.getText().toString();

                tempLog.setDate(dateStr);
                bodyText.getText().clear();

                //If in edit mode, set up current station in edit box
                if(currentMode == MODE_EDITING){
                    bodyText.append(logs.get(position).getStation());
                }
                label.setText(R.string.station);
                okCounter++;
            }
        }
        //Handle Station entry
        else if (okCounter == 1) {
            //Check string validity -- 20 character limit
            if(check.isStringValid(bodyText.getText().toString())){
                tempLog.setStation(bodyText.getText().toString());
                bodyText.getText().clear();
                //If in edit mode, set up current odometer for editing
                if(currentMode == MODE_EDITING){
                    bodyText.append(String.format("%.1f", logs.get(position).getOdometer()));
                }
                label.setText(R.string.odometer);
                okCounter++;
            }
            else{
                check.stringTooLong(bodyText);
            }
        }
        //Handle Odometer entry
        else if (okCounter == 2) {
            float odometer = 0;
            //Check to make sure input is numerical
            try {
                odometer = Float.parseFloat(bodyText.getText().toString());
                tempLog.setOdometer(odometer);
                bodyText.getText().clear();
                //If in edit mode, set up current fuel grade for editing
                if(currentMode == MODE_EDITING){
                    bodyText.append(logs.get(position).getFuel_grade());
                }
                label.setText(R.string.grade);
                okCounter++;
                //If not numerical, tell user
            } catch (NumberFormatException e) {
                check.invalidNumber(bodyText);
            }
        }
        //Handle Fuel Grade entry
        else if(okCounter == 3) {
            //Check string validity -- 20 character limit
            if(check.isStringValid(bodyText.getText().toString())){
                tempLog.setFuel_grade(bodyText.getText().toString());
                bodyText.getText().clear();
                //If in edit mode, set up current amount for editing
                if(currentMode == MODE_EDITING){
                    bodyText.append(String.format("%.3f", logs.get(position).getAmount()));
                }
                label.setText(R.string.amount);
                okCounter++;
            }
            else{
                check.stringTooLong(bodyText);
            }
        }
        //Handle Amount entry
        else if (okCounter == 4) {
            float amount = 0;
            //Ensure that input is numerical
            try {
                amount = Float.parseFloat(bodyText.getText().toString());
                tempLog.setAmount(amount);
                bodyText.getText().clear();
                //If in edit mode, set up current unit cost for editing
                if(currentMode == MODE_EDITING){
                    bodyText.append(String.format("%.1f", logs.get(position).getUnit_cost()));
                }
                label.setText(R.string.unitCost);
                okCounter++;
                //If not numerical, let user know
            } catch (NumberFormatException e) {
                check.invalidNumber(bodyText);
            }
        }
        //Handle Unit Cost entry
        else if (okCounter == 5) {
            float unit_cost = 0;
            //Ensure input is numerical
            try {
                unit_cost = Float.parseFloat(bodyText.getText().toString());
                tempLog.setUnit_cost(unit_cost);
                bodyText.getText().clear();
                label.setText(R.string.unitCost);
                okCounter = 0;
                tempLog.updateCost();
                //If in Adding mode, add log to list of logs
                 if(currentMode == MODE_ADDING){
                     logs.add(tempLog);
                     adapter.notifyDataSetChanged();
                     label.setVisibility(View.INVISIBLE);
                 }
                 //If in edit mode, set info of current log to info of new log
                else{
                     logs.get(position).updateLog(tempLog);
                     adapter.notifyDataSetChanged();
                 }
                //Update total cost
                updateTotalCost(logs);
                costText.setText(String.format("$%.2f", totalCost));
                currentMode = MODE_IDLE;
                disableOkButton(bodyText, add, cancel, ok, label);
            } catch (NumberFormatException e) {
                check.invalidNumber(bodyText);
            }
        }
    }

    //Enable the OK button and cancel button. Disables add button, sets up label for text box
    //Allows text box to be clicked on and typed into
    public void enableOkButton(EditText text, Button add, Button cancel, Button ok, TextView label){
        text.setFocusable(true);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        text.setEnabled(true);
        text.setClickable(true);
        text.setHint("");
        add.setVisibility(View.GONE);
        add.setClickable(false);
        cancel.setVisibility(View.VISIBLE);
        cancel.setClickable(true);
        ok.setVisibility(View.VISIBLE);
        ok.setClickable(true);
        label.setVisibility(View.VISIBLE);
        label.setText(R.string.date);
    }

    //Disables OK button and cancel button. Enables Add button. Makes text box unclickable,
    //hides text box label
    public void disableOkButton(EditText bodyText, Button add, Button cancel, Button ok, TextView label) {
        add.setVisibility(View.VISIBLE);
        add.setClickable(true);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);
        ok.setVisibility(View.GONE);
        ok.setClickable(false);
        bodyText.setEnabled(false);
        bodyText.getText().clear();
        label.setVisibility(View.INVISIBLE);
        label.setText(R.string.date);
    }
}
