package com.codepath.apps.mytwitterapp.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.TwitterClient;
import com.codepath.apps.mytwitterapp.R.layout;
import com.codepath.apps.mytwitterapp.R.menu;
import com.codepath.apps.mytwitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		
		Intent myIntent = getIntent();
		String screenName = myIntent.getStringExtra("screen_name");
		long userId = myIntent.getLongExtra("user_id", 0);
		//Log.d("DEBUG", "Got screen_name="+screenName+", userid="+userId);
		
		if(screenName != null && !screenName.isEmpty() && userId != 0){
			loadProfileInfo(screenName,userId);
		}else{
			loadProfileInfo();
		}
		
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		UserTimelineFragment fragment = new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putString("screen_name", screenName);
		args.putLong("user_id", userId);
		fragment.setArguments(args);
		fts.replace(R.id.fcUserTimeline, fragment);
		fts.commit();
		
	}

	private void loadProfileInfo(String screenName, long userId) {
		
		MyTwitterApp.getRestClient().getUserProfiles(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonProfiles) {
				JSONObject jsonUser = null;
				try{
					jsonUser = jsonProfiles.getJSONObject(0);
				}catch(Exception e){
					Log.e("ERROR", "Could not get user from jsonProfiles.");
				}
				if(jsonUser != null){
					User  u = User.fromJson(jsonUser);
					//Log.d("DEBUG","Got a user profile for "+u.getScreenName());
					getActionBar().setTitle("@"+u.getScreenName());
					populateProfileHeader(u);
				}
			}
		},screenName,userId);
	}
	
	
	private void loadProfileInfo() {
		MyTwitterApp.getRestClient().getMyInfo(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONObject json) {
				User u = User.fromJson(json);
				getActionBar().setTitle("@"+u.getScreenName());
				populateProfileHeader(u);
				
				//Log.d("DEBUG","Got a user profile for "+u.getScreenName());
				
			}
		});
		
	}
	
	private void populateProfileHeader(User u){
		
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFolowers = (TextView) findViewById(R.id.tvFolowers);
		TextView tvFolowing = (TextView) findViewById(R.id.tvFolowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFolowers.setText(u.getFollowersCount()+" followers");
		tvFolowing.setText(u.getFriendsCount() + " following");
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		//u.get
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
