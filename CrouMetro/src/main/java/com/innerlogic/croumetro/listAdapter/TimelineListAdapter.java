package com.innerlogic.croumetro.listAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.innerlogic.croumetro.R;
import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.net.OauthRequest;
import com.innerlogic.croumetro.net.PostRequest;
import com.innerlogic.croumetro.post.PostEntity;
import com.innerlogic.croumetro.tools.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Timothy on 14/02/08.
 */
public class TimelineListAdapter extends ArrayAdapter {

    private ArrayList<PostEntity> posts;

    static ImageLoader imageLoader = ImageLoader.getInstance();

    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private AsyncTask<Void, Void, String> listviewTask = null;
    public TimelineListAdapter(Context context,
                               int textViewResourceId,
                               ArrayList<PostEntity> items) {
        super(context, textViewResourceId, items);
        this.posts = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.timeline_listview_layout, null);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        oAuth2Helper = new OAuth2Helper(prefs);
        PostEntity o = posts.get(position);
        TextView post = (TextView) v.findViewById(R.id.post);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView username = (TextView) v.findViewById(R.id.username);
        TextView spreadBy = (TextView) v.findViewById(R.id.spreadBy);
        TextView time = (TextView) v.findViewById(R.id.time);
        ImageView avatar = (ImageView) v.findViewById(R.id.avatar);
        ImageView mediaImage = (ImageView) v.findViewById(R.id.mediaImage);
        ImageButton likeButton = (ImageButton) v.findViewById(R.id.likeButton);
        ImageButton replyButton = (ImageButton) v.findViewById(R.id.replyButton);
        ImageButton addFollowerButton = (ImageButton) v.findViewById(R.id.addFollower);
        ImageButton favoriteButton = (ImageButton) v.findViewById(R.id.favoriteButton);

        spreadBy.setVisibility(ImageView.GONE);
        mediaImage.setVisibility(ImageView.GONE);
        addFollowerButton.setVisibility(ImageView.GONE);
        SimpleDateFormat dateFmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        Date tempDate = new Date();
        try {
            tempDate = dateFmt.parse(o.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time.setText(DateUtils.getRelativeTimeSpanString(tempDate.getTime()));
        String userName = o.getUser().getName();
        if (o.getIsSpreaded()) {
            spreadBy.setText(String.format("%sさんがイイネ！しました。", userName));
            spreadBy.setVisibility(ImageView.VISIBLE);
            o = o.get_spreadStatus();
            userName = o.getUser().getName();
        }
        String screenName = String.format("@%s", o.getUser().getScreenName());
        name.setText(userName);
        username.setText(screenName);
        post.setText(o.getPost());
        if (o.getHasMedia()) {
            mediaImage.setVisibility(ImageView.VISIBLE);
            imageLoader.displayImage(o.getImageUrl(), mediaImage);
        }
        imageLoader.displayImage(o.getUser().getProfileImage(), avatar);

        if(!o.getUser().getIsFollowing())
        {
            addFollowerButton.setVisibility(ImageView.VISIBLE);
        }

        final PostEntity finalO = o;
        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Favorite Button Clicked", "ListView");

                OauthRequest oauthRequest = new OauthRequest(prefs, oAuth2Helper);
                String favoritesUrl = String.format(Constants.FAVORITE_CREATE, finalO.getStatusID());
                listviewTask = new PostRequest(favoritesUrl, null, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String json) {
                        // TODO: Replace Favorite Icon with "Unfavorite" icon/command.
                        Log.i(Constants.TAG, "Favorite: " + json);
                    }
                }.execute();

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Like Button Clicked", "ListView");

                OauthRequest oauthRequest = new OauthRequest(prefs, oAuth2Helper);
                String favoritesUrl = String.format(Constants.LIKE_CREATE, finalO.getStatusID());
                listviewTask = new PostRequest(favoritesUrl, null, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String json) {
                        // TODO: Send notification or other messaging saying the command succeeded.
                        Log.i(Constants.TAG, "Favorite: " + json);
                    }
                }.execute();
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Reply Button Clicked", "ListView");

            }
        });

        addFollowerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Add Follower Button Clicked", "ListView");
            }
        });

        return v;
    }


}
