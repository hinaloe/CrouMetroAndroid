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
    public UserEntity(String json) {
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map jsonData = parser.parseJson(json);
        this.setUserID(Long.parseLong((String) jsonData.get("id")));
        this.setName((String) jsonData.get("name"));
        this.setScreenName((String) jsonData.get("screen_name"));
        this.setProfileImage((String) jsonData.get("profile_image_url_https"));
        this.setCoverImage((String) jsonData.get("cover_image_url_https"));
        //String createdAt = (String)jsonData.get("created_at");
        //this.setCreatedAt(ConvertDateToLong(createdAt));
        this.setFollowing(Boolean.parseBoolean((String) jsonData.get("following")));
        this.setFriendsCount(Integer.parseInt((String) jsonData.get("friends_count")));
        this.setDescription((String) jsonData.get("description"));
        this.setLocation((String) jsonData.get("location"));
        this.setStatusCount(Long.parseLong((String) jsonData.get("statuses_count")));
        this.setIsProtected(Boolean.parseBoolean((String) jsonData.get("protected")));
        this.setFavoritesCount(Long.parseLong((String) jsonData.get("favorites_count")));
        this.setIsFollowRequest(Boolean.parseBoolean((String) jsonData.get("follow_request_sent")));
        this.setFollowersCount(Long.parseLong((String) jsonData.get("followers_count")));
        this.setIsFollowing(Boolean.parseBoolean((String) jsonData.get("following")));
        this.setFriendsCount(Long.parseLong((String) jsonData.get("friends_count")));
        this.setURL((String) jsonData.get("url"));
    }

    public UserEntity(JSONObject jsonData) throws JSONException {
        this.setUserID(jsonData.getInt("id"));
        this.setName(jsonData.getString("name"));
        this.setScreenName(jsonData.getString("screen_name"));
        this.setProfileImage((String) jsonData.getString("profile_image_url_https"));
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
