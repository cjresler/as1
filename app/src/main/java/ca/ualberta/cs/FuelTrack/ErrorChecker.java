package ca.ualberta.cs.FuelTrack;

import android.graphics.Color;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cjresler on 2016-01-28.
 *
 * This class is used to check for valid user input
 */
public class ErrorChecker{
    private SimpleDateFormat sdf;

    //Set up date format to be used by application
    public ErrorChecker() {
        this.sdf = new SimpleDateFormat("yyyy-mm-dd");
    }

    //Checks if a given string can be converted into a valid date using proper format
    //Help writing this taken from
    // http://stackoverflow.com/questions/20231539/java-check-the-date-format-of-current-string-is-according-to-required-format-or
    public boolean isValidDate(String value) {
        Date date = null;
        try {
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    //Check if a given string is < 21 characters
    public boolean isStringValid(String value){
        return (value.length() < 21);
    }

    //Display error message if given string is too long
    public void stringTooLong(EditText bodyText){
        bodyText.setError("");
        bodyText.getText().clear();
        bodyText.setHint("Too many characters, maximum is 20.");
        bodyText.setHintTextColor(Color.RED);
    }

    //Display error message if date is invalid
    public void invalidDate(EditText bodyText){
        bodyText.setError("");
        bodyText.getText().clear();
        bodyText.setHint("Please use yyyy-mm-dd format.");
        bodyText.setHintTextColor(Color.RED);
    }

    //Display error message if number is invalid
    public void invalidNumber(EditText bodyText){
        bodyText.setError("");
        bodyText.getText().clear();
        bodyText.setHint("Invalid number. Please enter digits.");
        bodyText.setHintTextColor(Color.RED);
    }
}
