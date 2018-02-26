package com.example.project.smartsilent;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class TimeActivity extends BaseActivity {
	private TimeDate timeDate;

	private TextView startTimeDisplay;
	private TextView endTimeDisplay;
	private Button startPickTime;
	private Button endPickTime;
	private Button showlist;


	private static final int TIME_PICKER_END = 0;
	private static final int TIME_PICKER_START = 1;

	int silentHour, silentMinute, unsilentHour, unsilentMinute;
	int silentHourDis, unsilentHourDis;
	String timeSet1, timeSet2, minutes1, minutes2 = " ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getLayoutInflater().inflate(R.layout.time, frameLayout);

		startTimeDisplay = (TextView) findViewById(R.id.silentTimeDisplay);
		startPickTime = (Button) findViewById(R.id.silentPickTime);
		showlist = (Button) findViewById(R.id.showlist);

    /* add a click listener to the button   */
		startPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_PICKER_START);
			}
		});

		showlist.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TimeActivity.this,TimeList.class);
				startActivity(intent);
			}
		});


		final Calendar c = Calendar.getInstance();
		silentHour = c.get(Calendar.HOUR_OF_DAY);
		silentMinute = c.get(Calendar.MINUTE);
		//from_day = c.get(Calendar.DAY_OF_MONTH);


		updateStartDisplay();



		endTimeDisplay = (TextView) findViewById(R.id.unsilentTimeDisplay);
		endPickTime = (Button) findViewById(R.id.unsilentPickTime);


		endPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_PICKER_END);
			}
		});


		final Calendar c1 = Calendar.getInstance();
		unsilentHour = c1.get(Calendar.HOUR_OF_DAY);
		unsilentMinute = c1.get(Calendar.MINUTE);
		//to_day = c1.get(Calendar.DAY_OF_MONTH);

		updateEndDisplay();



		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);


	}


	private void updateEndDisplay() {

		unsilentHourDis = unsilentHour;

		if (unsilentHourDis > 12) {
			unsilentHourDis -= 12;
			timeSet2 = "PM";
		} else if (unsilentHourDis == 0) {
			unsilentHourDis += 12;
			timeSet2 = "AM";
		} else if (unsilentHourDis == 12)
			timeSet2 = "PM";
		else
			timeSet2 = "AM";
		String minutes = " ";
		if (unsilentMinute < 10) {
			minutes = "0" + unsilentMinute;
		}
		endTimeDisplay.setText(
				new StringBuilder()

						.append(unsilentHourDis).append("-")
						.append(unsilentMinute).append("-")
						.append(timeSet2).append(" "));
	}


	private void updateStartDisplay() {

		silentHourDis = silentHour;

		if (silentHourDis > 12) {
			silentHourDis -= 12;
			timeSet1 = "PM";
		} else if (silentHourDis == 0) {
			silentHourDis += 12;
			timeSet1 = "AM";
		} else if (silentHourDis == 12)
			timeSet1 = "PM";
		else
			timeSet1 = "AM";


		String minutes = " ";
		if (silentMinute < 10) {
			minutes = "0" + silentMinute;
		}
		startTimeDisplay.setText(
				new StringBuilder()

						.append(silentHourDis).append("-")
						.append(silentMinute).append("-")
						.append(timeSet1).append(" "));
	}


	private TimePickerDialog.OnTimeSetListener silentTimeListener =
			new TimePickerDialog.OnTimeSetListener() {

				public void onTimeSet(TimePicker view, int hour,
									  int minute) {

					silentHour = hour;
					silentMinute = minute;


					updateStartDisplay();


					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,
							(ViewGroup) findViewById(R.id.custom_toast_container));

					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText( "Selected Silent Time : " + silentHourDis + "-" + silentMinute);

					Toast toast = new Toast(getApplicationContext());
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(layout);
					toast.show();



					Calendar midnightCalendar = Calendar.getInstance();

					AlarmManager am = (AlarmManager) (TimeActivity.this.getSystemService(ALARM_SERVICE));

					midnightCalendar.set(Calendar.HOUR_OF_DAY, silentHour);
					midnightCalendar.set(Calendar.MINUTE, silentMinute);


					PendingIntent midnightPI = PendingIntent.getBroadcast(TimeActivity.this, 0, new Intent("net.accella.sheduleexample.SilenceBroadcastReceiver"), PendingIntent.FLAG_UPDATE_CURRENT);

					am.setRepeating(AlarmManager.RTC_WAKEUP, midnightCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, midnightPI);



				}
			};



	private TimePickerDialog.OnTimeSetListener unsilentTimeListener =
			new TimePickerDialog.OnTimeSetListener() {

				public void onTimeSet(TimePicker view, int hour,
									  int minute) {
					unsilentHour = hour;
					unsilentMinute = minute;

					updateEndDisplay();



					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,
							(ViewGroup) findViewById(R.id.custom_toast_container));

					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("Selected Unsilent Time : " + unsilentHourDis + "-" + unsilentMinute);

					Toast toast = new Toast(getApplicationContext());
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(layout);
					toast.show();



					Calendar sixCalendar = Calendar.getInstance();

					//set the time

					AlarmManager am = (AlarmManager) (TimeActivity.this.getSystemService(ALARM_SERVICE));
					sixCalendar.set(Calendar.HOUR_OF_DAY, unsilentHour);
					sixCalendar.set(Calendar.MINUTE, unsilentMinute);
					//sixCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);


					PendingIntent sixPI = PendingIntent.getBroadcast(TimeActivity.this, 0, new Intent("net.accella.sheduleexample.UnsilenceBroadcastReceiver"), PendingIntent.FLAG_UPDATE_CURRENT);

					am.setRepeating(AlarmManager.RTC_WAKEUP, sixCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sixPI);

					TimeDateProvider.settimevalue(silentHour,silentMinute,unsilentHour,unsilentMinute);

				}
			};

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
			case TIME_PICKER_START:
				return new TimePickerDialog(this, silentTimeListener, silentHour, silentMinute, false);
			case TIME_PICKER_END:
				return new TimePickerDialog(this, unsilentTimeListener, unsilentHour, unsilentMinute, false);
		}
		return null;
	}
}
