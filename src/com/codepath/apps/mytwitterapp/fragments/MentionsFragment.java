package com.codepath.apps.mytwitterapp.fragments;

import org.json.JSONArray;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.constants.Constants;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;

public class MentionsFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MyTwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJason(jsonTweets));
				//Log.d("DEBUG", "Mentions: "+jsonTweets.toString());
			}
		}, Constants.MAX_COUNT_TWEETS_MENTIONS);
	}
}
