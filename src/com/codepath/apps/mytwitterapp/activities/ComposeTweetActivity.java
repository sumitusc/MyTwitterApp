package com.codepath.apps.mytwitterapp.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.R.id;
import com.codepath.apps.mytwitterapp.R.layout;
import com.codepath.apps.mytwitterapp.R.menu;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ComposeTweetActivity extends FragmentActivity {
	
	EditText etTweet;
	Button btnTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		addListenerOnButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	
	public void addListenerOnButton(){
		etTweet = (EditText) findViewById(R.id.etTweet);
		btnTweet = (Button) findViewById(R.id.btnTweet);
		
		btnTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tweetText = etTweet.getText().toString();
				
				Intent result = new Intent();
				result.putExtra("tweettext", tweetText);
				setResult(RESULT_OK, result);
				finish();
			}
		});
	}

}
