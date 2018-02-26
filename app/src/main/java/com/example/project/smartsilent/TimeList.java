package com.example.project.smartsilent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;

import java.util.List;

public class TimeList extends BaseActivity {

    List<TimeDate> mTimeDataList;






    final Context context = this;
    private RadioButton buttonPlacePick;
    private RadioButton buttonCurrentPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.time_list);
        getLayoutInflater().inflate(R.layout.time_list, frameLayout);


//        buttonPlacePick = (RadioButton) findViewById(R.id.btn1);
//
//        buttonPlacePick.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(TimeList.this, PickPlace.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//        });
//
//        buttonCurrentPlace = (RadioButton) findViewById(R.id.btn2);
//
//        buttonCurrentPlace.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(TimeList.this, PlaceCurrent.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//        });

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
        TimeController.getInstance().init(this);
        mTimeDataList = TimeController.getInstance().mTimeData;
//        Collections.sort(mTimeDataList, new Comparator<GeofenceData>() {
//            @Override
//            public int compare(GeofenceData o1, GeofenceData o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });


        TimeDateAdapter adapter = new TimeDateAdapter(this, mTimeDataList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvtimeItems);
        recyclerView.setAdapter(adapter);
    }
}
