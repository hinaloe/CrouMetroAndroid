package com.innerlogic.croumetro.user;


import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Timothy on 14/02/08.
 */
public class UserEntity implements Serializable {
    public UserEntity(JSONObject jsonData) throws JSONException {
        this.setUserID(jsonData.getInt("id"));
        this.setName(jsonData.getString("name").replaceAll("\\r|\\n", ""));
        this.setScreenName(jsonData.getString("screen_name").replaceAll("\\r|\\n", ""));
        this.setProfileImage((String) jsonData.getString("profile_image_url_https"));
        if(jsonData.has("cover_image_url_https"))
        {
            this.setCoverImage(jsonData.getString("cover_image_url_https"));
        }
        this.setFollowing(jsonData.getBoolean("following"));
        this.setFriendsCount(jsonData.getLong("friends_count"));
        this.setDescription(jsonData.getString("description"));
        this.setLocation(jsonData.getString("location"));
        this.setStatusCount(jsonData.getLong("statuses_count"));
        this.setIsFollowRequest(Boolean.parseBoolean((String) jsonData.getString("follow_request_sent")));
        this.setIsFollowing(Boolean.parseBoolean((String) jsonData.getString("following")));
        this.setIsProtected(Boolean.parseBoolean((String) jsonData.getString("protected")));
        this.setFollowersCount(jsonData.getLong("followers_count"));
        this.setURL(jsonData.getString("url"));
        this.setFavoritesCount(jsonData.getLong("favorites_count"));
        this.setIsCurrentUser(false);
    }

    private long ConvertDateToLong(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(dateString);
        return date.getTime();
    }

    private Boolean _following;

    public final Boolean getFollowing() {
        return _following;
    }

    public void setFollowing(Boolean value) {
        _following = value;
    }

    private long _createdAt;

    public final long getCreatedAt() {
        return _createdAt;
    }

    private void setCreatedAt(long value) {
        _createdAt = value;
    }

    private String _description;

    public final String getDescription() {
        return _description;
    }

    private void setDescription(String value) {
        _description = value;
    }

    private String _coverImage;

    public final String getCoverImage() {
        return _coverImage;
    }

    private void setCoverImage(String value) {
        _coverImage = value;
    }

    private long _favoritesCount;

    public final long getFavoritesCount() {
        return _favoritesCount;
    }

    private void setFavoritesCount(long value) {
        _favoritesCount = value;
    }

    private boolean _isFollowRequest;

    public final boolean getIsFollowRequest() {
        return _isFollowRequest;
    }

    private void setIsFollowRequest(boolean value) {
        _isFollowRequest = value;
    }

    private long _followersCount;

    public final long getFollowersCount() {
        return _followersCount;
    }

    private void setFollowersCount(long value) {
        _followersCount = value;
    }

    private boolean _isFollowing;

    public final boolean getIsFollowing() {
        return _isFollowing;
    }

    private void setIsFollowing(boolean value) {
        _isFollowing = value;
    }

    private long _friendsCount;

    public final long getFriendsCount() {
        return _friendsCount;
    }

    private void setFriendsCount(long value) {
        _friendsCount = value;
    }

    private long _userID;

    public final long getUserID() {
        return _userID;
    }

    private void setUserID(long value) {
        _userID = value;
    }

    private String _location;

    public final String getLocation() {
        return _location;
    }

    private void setLocation(String value) {
        _location = value;
    }

    private String _name;

    public final String getName() {
        return _name;
    }

    private void setName(String value) {
        _name = value;
    }

    private String _profileImage;

    public final String getProfileImage() {
        return _profileImage;
    }

    private void setProfileImage(String value) {
        _profileImage = value;
    }

    private boolean _isProtected;

    public final boolean getIsProtected() {
        return _isProtected;
    }

    private void setIsProtected(boolean value) {
        _isProtected = value;
    }

    private String _screenName;

    public final String getScreenName() {
        return _screenName;
    }

    private void setScreenName(String value) {
        _screenName = value;
    }

    private long _statusCount;

    public final long getStatusCount() {
        return _statusCount;
    }

    private void setStatusCount(long value) {
        _statusCount = value;
    }

    private String _URL;

    public final String getURL() {
        return _URL;
    }

    private void setURL(String value) {
        _URL = value;
    }

    private boolean _isCurrentUser;

    public final boolean getIsCurrentUser() {
        return _isCurrentUser;
    }

    public void setIsCurrentUser(boolean value) {
        _isCurrentUser = value;
    }
}
