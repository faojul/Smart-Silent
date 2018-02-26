package com.example.project.smartsilent;

import android.os.Bundle;


public class HomeActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getLayoutInflater().inflate(R.layout.home, frameLayout);


		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);
	}

}
