package com.example.project.smartsilent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PlaceList extends BaseActivity {



	List<GeofenceData> mGeoFenceDataList;
	private Button addbtn;
	private String name;
	protected double latitude;
	protected double longitude;
	protected String slatitude;
	protected String slongitude;
	protected String sradius;
	protected int radius;




	final Context context = this;
	private RadioButton buttonPlacePick;
	private RadioButton buttonCurrentPlace;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.place_list);
		getLayoutInflater().inflate(R.layout.place_list, frameLayout);


		buttonPlacePick = (RadioButton) findViewById(R.id.btn1);

		buttonPlacePick.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlaceList.this, PickPlace.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		buttonCurrentPlace = (RadioButton) findViewById(R.id.btn2);

		buttonCurrentPlace.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlaceList.this, PlaceCurrent.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);

		populateList();
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent smartsilent in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}


	public void populateList() {
		GeofenceController.getInstance().init(this);
		mGeoFenceDataList = GeofenceController.getInstance().mGeofenceData;
		Collections.sort(mGeoFenceDataList, new Comparator<GeofenceData>() {
			@Override
			public int compare(GeofenceData o1, GeofenceData o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});


		GeofenceDataAdapter adapter = new GeofenceDataAdapter(this, mGeoFenceDataList);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);
		recyclerView.setAdapter(adapter);
	}
}
