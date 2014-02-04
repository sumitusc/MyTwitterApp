package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.activities.ProfileActivity;
import com.codepath.apps.mytwitterapp.adapters.TweetsAdapter;
import com.codepath.apps.mytwitterapp.constants.Constants;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TweetsListFragment extends Fragment {
	
	TweetsAdapter adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
		return view;
	}
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 
		ArrayList<Tweet>  tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		
		ListView lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Log.d("DEBUG"," ITem clicked....ROW = "+position+" ID="+id);
				Tweet tweet = getAdapter().getItem(position);
				//Log.d("DEBUG"," ITem clicked....Name = "+tweet.getUser().getName()+" Screen Name="+tweet.getUser().getScreenName());
				
				String screenName = tweet.getUser().getName();
				long userId = tweet.getUser().getId();
				Intent i = new Intent(getActivity(), ProfileActivity.class);
				i.putExtra("screen_name", screenName);
				i.putExtra("user_id", userId);
				startActivity(i);
			}
			
		});
		
		
	}
	
	
	public TweetsAdapter getAdapter(){
		return adapter;
	}
}
