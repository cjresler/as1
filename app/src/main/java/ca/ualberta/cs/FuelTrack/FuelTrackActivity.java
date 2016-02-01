package ca.ualberta.cs.FuelTrack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FuelTrackActivity extends Activity {

	private static final String FILENAME = "file.sav";
	private EditText bodyText;
	private ListView logsList;
	private Controller controller;

	private ArrayList<Log> logs = new ArrayList<Log>();
	private ArrayAdapter<Log> adapter;
	private Log tempLog = new Log();
	private TextView costText;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Main edit text box
		bodyText = (EditText) findViewById(R.id.body);
		//Displays total cost of all logs
		costText = (TextView) findViewById(R.id.totalCost);
		controller = new Controller();

		final Button cancelButton = (Button) findViewById(R.id.cancel);
		final Button addButton = (Button) findViewById(R.id.add);
		final Button okButton = (Button) findViewById(R.id.ok);
		final TextView textBoxLabel = (TextView) findViewById(R.id.textBoxLabel);
		logsList = (ListView) findViewById(R.id.oldTweetsList);

		//Handle Add button click
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				tempLog = new Log();
				//Set mode to Adding
				controller.setMode(Controller.MODE_ADDING);
				//Enable the OK and Cancel buttons
				controller.enableOkButton(bodyText, addButton, cancelButton, okButton, textBoxLabel);
			}
		});

		//Handle OK button click
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				controller.okButtonClicked(tempLog, bodyText, textBoxLabel, logs, adapter, costText,
						addButton, cancelButton, okButton);
				//Automatically scroll to bottom to show most recent entries
				logsList.setSelection(adapter.getCount() - 1);
				saveInFile();
			}
		});

		//Handle Cancel button click
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				controller.cancelButtonClicked(bodyText, addButton, cancelButton, okButton, textBoxLabel);
				bodyText.setHint("");
			}
		});

		//Format of this taken from http://stackoverflow.com/questions/2468100/android-listview-click-howto
		//Handle logs on list being clicked
		logsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				//Only allow user to click logs in list if they are not already currently editing/adding
				//a log
				if(controller.getCurrentMode() == Controller.MODE_IDLE) {
					textBoxLabel.setText(R.string.date);
					controller.enableOkButton(bodyText, addButton, cancelButton, okButton, textBoxLabel);
					bodyText.append(logs.get(position).dateToString());
					controller.setOkCounter(0);
					tempLog = logs.get(position);
					//Set mode to Editing
					controller.setMode(Controller.MODE_EDITING);
					controller.setPosition(position);
				}
			}
		});
	}

	//Mostly taken from lonelytwitter code
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadFromFile();
		controller.updateTotalCost(logs);
		costText.setText(String.format("$%.2f", controller.getTotalCost()));
		adapter = new ArrayAdapter<Log>(this,
				R.layout.list_item, logs);
		logsList.setAdapter(adapter);
	}

	//Taken from lonelytwitter code
	private void loadFromFile() {
		try {
			FileInputStream fis = openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			Gson gson = new Gson();

			// Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html Jan-20-2016
			Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
			logs = gson.fromJson(in, listType);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logs = new ArrayList<Log>(); //tweets didn't load so must create
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	//Taken from lonelytwitter code
	private void saveInFile() {
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE); //Can put 0 instead of Context.MODE_PRIVATE
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
			Gson gson = new Gson();
			gson.toJson(logs, out);
			out.flush();

			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
}