package com.example.project.smartsilent;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.adapters.NavigationDrawerListAdapter;
import com.example.project.models.Items;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {

	private ActionBar actionBar;

	private Toast toast;
	private long lastBackPressTime = 0;

	protected FrameLayout frameLayout;




	protected ListView mDrawerList;

	protected String[] listArray = {"Smart Silent", "Select Place", "Select Time", /*"Settings",*/ "Help", "About Us"};
	protected ArrayList<Items> _items;

	protected static int position;

	private static boolean isLaunch = true;

	private DrawerLayout mDrawerLayout;

	private ActionBarDrawerToggle actionBarDrawerToggle;

	private ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#009A9A"));


	private GoogleApiClient client;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_base_layout);

		actionBar = getSupportActionBar();


		frameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);



		_items = new ArrayList<Items>();
		_items.add(new Items("Home", "", R.drawable.home));
		_items.add(new Items("Select Place", "", R.drawable.place));
		_items.add(new Items("Select Time", "", R.drawable.timepick));
//		_items.add(new Items("Settings", "", R.drawable.settings));
		_items.add(new Items("Help", "", R.drawable.help));
		_items.add(new Items("About Us", "", R.drawable.aboutus));


		mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, _items));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				openActivity(position);
			}
		});


		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(colorDrawable);



		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				///R.drawable.settings,
				//toolbar,
				R.string.open_drawer,
				R.string.close_drawer)   {
			@Override
			public void onDrawerClosed(View drawerView) {
				actionBarDrawerToggle.syncState();
				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBarDrawerToggle.syncState();
				invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
			}
		};

		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

		if (isLaunch) {

			isLaunch = false;
			openActivity(0);
		}

		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}


	protected void openActivity(int position) {



		mDrawerLayout.closeDrawer(mDrawerList);
		BaseActivity.position = position;

		switch (position) {
			case 0:
				startActivity(new Intent(this, HomeActivity.class));
				break;
			case 1:
				startActivity(new Intent(this, PlaceCurrent.class));
				break;
			case 2:
				startActivity(new Intent(this, TimeActivity.class));
				break;
			case 3:
				startActivity(new Intent(this, HelpActivity.class));
				break;
			case 4:
				startActivity(new Intent(this, AboutUs.class));
				break;

			default:
				break;
		}


	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item != null && item.getItemId() == android.R.id.home) {
			toggle();

		}
		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
//		return super.onOptionsItemSelected(item);

	}
	private void toggle() {
		if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			mDrawerLayout.openDrawer(GravityCompat.START);
		}
	}




	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast,
						(ViewGroup) findViewById(R.id.custom_toast_container));

				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText("Press back again to close this app");

				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
				this.lastBackPressTime = System.currentTimeMillis();
			} else {
				if (toast != null) {
					toast.cancel();
				}
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				super.onBackPressed();
			}
		}
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	/*public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("Base Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}*/
}
