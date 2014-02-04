package com.codepath.apps.mytwitterapp.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.R.layout;
import com.codepath.apps.mytwitterapp.R.menu;
import com.codepath.apps.mytwitterapp.adapters.TweetsAdapter;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.constants.Constants;
import com.codepath.apps.mytwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitterapp.fragments.MentionsFragment;
import com.codepath.apps.mytwitterapp.fragments.TweetsListFragment;


import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends FragmentActivity implements TabListener {
	
	private final int REQUEST_CODE = 20;
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetsAdapter adapter = null;
	TweetsListFragment fragmentTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		setupNavigationTabs();
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home")
						.setTag("HomeTimelineFragment")
							.setIcon(R.drawable.ic_home).setTabListener(this);
		
		Tab tabMentions = actionBar.newTab().setText("Mentions")
							.setTag("MentionsFragment")
								.setIcon(R.drawable.ic_mentions).setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeAction(MenuItem mi){
		//Toast.makeText(this, "Compose a Tweet", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this,ComposeTweetActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	public void onProfileView(MenuItem mi){
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
		//startActivityForResult(i, REQUEST_CODE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
						
			if(data.hasExtra("tweettext")){
				String tweetText =  data.getExtras().getString("tweettext");
				//Log.d("DEBUG", "tweetText="+tweetText);
				
				MyTwitterApp.getRestClient().postTweet(new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject jsonResponse) {
						
						Tweet tweet = Tweet.fromJson(jsonResponse);
						//Log.d("DEBUG","tweet="+tweet.getBody()+", User="+tweet.getUser().getScreenName());
						
						//Remove last tweet
						int size = tweets.size();
						if(size > 0){
							tweets.remove(size-1);
						}
						
						//Add tweet on top
						tweets.add(0, tweet);
						//update adapter for change
						if(adapter != null){
							adapter.notifyDataSetChanged();
						}else{
							//Log.d("DEBUG", "......adapter is NULL");
						}
						
						//Log.d("DEBUG", "tweets size = "+tweets.size());
					}
					
				}, tweetText);
				
			}
			
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//Log.d("DEBUG", tab.getTag()+" TAB is REselected...");
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//Log.d("DEBUG", tab.getTag()+" TAB is selected...");
		
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
			
		if(tab.getTag() == "HomeTimelineFragment"){
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		}else{
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//Log.d("DEBUG", tab.getTag()+" TAB is UNselected...");
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		//Log.d("DEBUG", "Attaching to "+fragment.getId());
	}

}
