package com.innerlogic.croumetro.listAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.innerlogic.croumetro.R;
import com.innerlogic.croumetro.post.PostEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Timothy on 14/02/08.
 */
public class TimelineListAdapter extends ArrayAdapter {

    private ArrayList<PostEntity> posts;

    static ImageLoader imageLoader = ImageLoader.getInstance();

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
        PostEntity o = posts.get(position);
        TextView post = (TextView) v.findViewById(R.id.post);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView username = (TextView) v.findViewById(R.id.username);
        TextView spreadBy = (TextView) v.findViewById(R.id.spreadBy);
        ImageView avatar = (ImageView) v.findViewById(R.id.avatar);
        ImageView mediaImage = (ImageView) v.findViewById(R.id.mediaImage);
        ImageButton likeButton = (ImageButton) v.findViewById(R.id.likeButton);
        ImageButton replyButton = (ImageButton) v.findViewById(R.id.replyButton);
        ImageButton addFollowerButton = (ImageButton) v.findViewById(R.id.addFollower);
        ImageButton favoriteButton = (ImageButton) v.findViewById(R.id.favoriteButton);

        spreadBy.setVisibility(ImageView.GONE);
        mediaImage.setVisibility(ImageView.GONE);

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

        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Favorite Button Clicked", "ListView");

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Like Button Clicked", "ListView");

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
