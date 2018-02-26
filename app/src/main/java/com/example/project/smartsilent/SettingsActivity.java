package com.example.project.smartsilent;

import android.os.Bundle;


public class SettingsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		getLayoutInflater().inflate(R.layout.settings, frameLayout);
		

		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);

	}
}
