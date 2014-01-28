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


import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends Activity {
	
	private final int REQUEST_CODE = 20;
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetsAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		//this.setTitle("New Rotten");
		
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				//ArrayList<Tweet> tweets = Tweet.fromJason(jsonTweets);
				tweets = Tweet.fromJason(jsonTweets);
				ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
				
				//Log.d("DEBUG", "Tweets: "+jsonTweets.toString());
			}
		}, Constants.MAX_COUNT_TWEETS_HOME_TIMELINE);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			
			/*
			Tweet tweet = (Tweet) data.getSerializableExtra("tweetResponse");
			Log.d("DEBUG","tweet="+tweet.getBody()+", User="+tweet.getUser().getScreenName());
			int size = tweets.size();
			tweets.remove(size-1);
			tweets.add(0, tweet);
			adapter.notifyDataSetChanged();
			Log.d("DEBUG", "tweets size = "+tweets.size());
			*/
			
			
			if(data.hasExtra("tweettext")){
				String tweetText =  data.getExtras().getString("tweettext");
				
				MyTwitterApp.getRestClient().postTweet(new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject jsonResponse) {
						
						Tweet tweet = Tweet.fromJson(jsonResponse);
						//Log.d("DEBUG","tweet="+tweet.getBody()+", User="+tweet.getUser().getScreenName());
						
						//Remove last tweet
						int size = tweets.size();
						tweets.remove(size-1);
						
						//Add tweet on top
						tweets.add(0, tweet);
						//update adapter for change
						adapter.notifyDataSetChanged();
						
						//Log.d("DEBUG", "tweets size = "+tweets.size());
					}
					
				}, tweetText);
			}
			
		}
	}

}
