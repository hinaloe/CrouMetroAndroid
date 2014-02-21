package com.innerlogic.croumetro.listAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.innerlogic.croumetro.R;
import com.innerlogic.croumetro.net.GetRequest;
import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.net.OauthRequest;
import com.innerlogic.croumetro.net.PostRequest;
import com.innerlogic.croumetro.post.PostEntity;
import com.innerlogic.croumetro.tools.Constants;
import com.innerlogic.croumetro.user.UserEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

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
        TextView favoriteCount = (TextView) v.findViewById(R.id.favoriteCount);
        TextView likeCount = (TextView) v.findViewById(R.id.likeCount);
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

        if(o.getFavoritedCount() > 0)
        {
            favoriteCount.setText(String.valueOf(o.getFavoritedCount()));
            favoriteCount.setVisibility(TextView.VISIBLE);
        }

        if(o.getSpreadCount() > 0)
        {
            likeCount.setText(String.valueOf(o.getSpreadCount()));
            likeCount.setVisibility(TextView.VISIBLE);
        }


        final PostEntity finalO = o;

        avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("AvatarClicked", "ListView");
                String showUserUrl = String.format(Constants.USERS_SHOW_SCREENNAME, finalO.getUser().getScreenName());
                listviewTask = new GetRequest(showUserUrl, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String json) {
                        Log.i(Constants.TAG, "User: " + json);

                        View profileView;
                        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        profileView = vi.inflate(R.layout.activity_user_profile, null);

                        JSONObject object = null;
                        UserEntity user = null;
                        try {
                            object = new JSONObject(json);
                            user = new UserEntity(object);
                        } catch (JSONException e) {
                            throw new RuntimeException(e.toString());
                        }

                        ImageButton addFollowerButton = (ImageButton) profileView.findViewById(R.id.addFollower);
                        ImageView coverImage = (ImageView) profileView.findViewById(R.id.coverImage);
                        ImageView avatar = (ImageView) profileView.findViewById(R.id.userAvatar);
                        TextView description = (TextView) profileView.findViewById(R.id.description);
                        TextView name = (TextView) profileView.findViewById(R.id.name);
                        TextView followerCount = (TextView) profileView.findViewById(R.id.followerCount);
                        TextView followCount = (TextView) profileView.findViewById(R.id.followCount);
                        TextView messageCount = (TextView) profileView.findViewById(R.id.messageCount);
                        TextView screenName = (TextView) profileView.findViewById(R.id.screenName);

                        addFollowerButton.setVisibility(ImageView.GONE);

                        if(!user.getIsFollowing() || !user.getIsCurrentUser())
                        {
                            addFollowerButton.setVisibility(ImageView.VISIBLE);
                        }


                        screenName.setText(String.format("@%s", user.getScreenName()));
                        name.setText(user.getName());
                        description.setText(user.getDescription());
                        followCount.setText(String.valueOf(user.getFriendsCount()));
                        followerCount.setText(String.valueOf(user.getFollowersCount()));
                        messageCount.setText(String.valueOf(user.getStatusCount()));
                        imageLoader.displayImage(user.getProfileImage(), avatar);
                        imageLoader.displayImage(user.getCoverImage(), coverImage);
                        addFollowerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i("Add Follower Button Clicked", "ListView");
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(profileView)
                                .setCancelable(true).show();
                    }
                }.execute();

            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Favorite Button Clicked", "ListView");
                String favoritesUrl = String.format(Constants.FAVORITE_CREATE, finalO.getStatusID());
                listviewTask = new PostRequest(favoritesUrl, null, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String json) {
                        // TODO: Replace Favorite Icon with "Unfavorite" icon/command.
                        Log.i(Constants.TAG, "Favorite: " + json);
                        Toast toast = Toast.makeText(getContext(), "お気に入りに登録しました！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }.execute();

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Like Button Clicked", "ListView");
                String favoritesUrl = String.format(Constants.LIKE_CREATE, finalO.getStatusID());
                listviewTask = new PostRequest(favoritesUrl, null, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String json) {
                        Log.i(Constants.TAG, "Like: " + json);
                        Toast toast = Toast.makeText(getContext(), "イイネに登録しました！", Toast.LENGTH_SHORT);
                        toast.show();
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
