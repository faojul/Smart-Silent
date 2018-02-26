package com.example.project.smartsilent;

import android.os.Bundle;


public class AboutUs extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		getLayoutInflater().inflate(R.layout.about_us, frameLayout);
		

		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);

	}
}
