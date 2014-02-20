package com.innerlogic.croumetro.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.innerlogic.croumetro.R;
import com.innerlogic.croumetro.user.UserEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Timothy on 14/02/15.
 */
public class DrawerHeaderAdapter extends ArrayAdapter {
    private UserEntity userEntity;

    static ImageLoader imageLoader = ImageLoader.getInstance();

    public DrawerHeaderAdapter(Context context, int resource, UserEntity userEntity) {
        super(context, resource);
        this.userEntity = userEntity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.drawer_header_layout, null);
        }
        TextView screenName = (TextView) v.findViewById(R.id.screenName);
        TextView name = (TextView) v.findViewById(R.id.name);
        ImageView avatar = (ImageView) v.findViewById(R.id.userAvatar);

        screenName.setText(userEntity.getScreenName());
        name.setText(userEntity.getName());
        imageLoader.displayImage(userEntity.getProfileImage(), avatar);
        return v;
    }
}
