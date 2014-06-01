package com.example.floatinglabelpattern;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;

import com.example.floatinglabelpattern.widget.FloatingLabelEditText;
import com.example.floatinglabelpattern.widget.FloatingLabelSpinner;

public class MainActivity extends Activity {

    private static final String[] DAYS_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private FloatingLabelEditText mEditText1;
    private FloatingLabelEditText mEditText2;
    private FloatingLabelSpinner mSpinner1;
    private Button mButton;
    private int mCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mEditText1 = (FloatingLabelEditText) findViewById(R.id.view4);
        mEditText2 = (FloatingLabelEditText) findViewById(R.id.view5);
        mSpinner1 = (FloatingLabelSpinner) findViewById(R.id.field_spinner);
        mButton = (Button) findViewById(R.id.button);
        mEditText2.setText("Another dummy text");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCounter) {
                    case 0:
                        mEditText1.setText("Some dummy text");
                        break;
                    case 1:
                        mEditText1.setFloatingLabelColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
                        break;
                    default:
                        break;
                }
                mCounter++;
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_item, DAYS_OF_WEEK); //selected item will look like a spinner set from XML
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter);


        //Log.i("MainActivity", "ShortAnimTime: " + getResources().getInteger(android.R.integer.config_shortAnimTime));
		//Log.i("MainActivity", "MediumAnimTime: " + getResources().getInteger(android.R.integer.config_mediumAnimTime));
		//Log.i("MainActivity", "LongAnimTime: " + getResources().getInteger(android.R.integer.config_longAnimTime));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
