package com.innerlogic.croumetro.post;

import com.innerlogic.croumetro.user.UserEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Timothy on 14/02/08.
 */
public class PostEntity {

    public PostEntity(JSONObject jsonObject) throws JSONException {
        this.setPost(jsonObject.getString("text"));
        JSONObject object;

        if (jsonObject.has("entities")) {
            object = jsonObject.getJSONObject("entities");
            if (object.has("media")) {
                object = object.getJSONObject("media");
                this.setImageUrl(object.getString("media_url_https"));
                this.setHasMedia(true);
            }
        }

        if (jsonObject.has("spread_status")) {
            object = jsonObject.getJSONObject("spread_status");
            this.setIsSpreaded(true);
            this.set_spreadStatus(new PostEntity(object));
        }
        object = jsonObject.getJSONObject("user");
        this.setUser(new UserEntity(object));
        this.setStatusID(jsonObject.getLong("id"));
    }

    private long _createdAt;

    public final long getCreatedAt() {
        return _createdAt;
    }

    private void setCreatedAt(long value) {
        _createdAt = value;
    }

    private String _createdDate;

    public final String getCreatedDate() {
        return _createdDate;
    }

    private void setCreatedDate(String value) {
        _createdDate = value;
    }

    private boolean _isFavorited;

    public final boolean getIsFavorited() {
        return _isFavorited;
    }

    private void setIsFavorited(boolean value) {
        _isFavorited = value;
    }

    private long _favoritedCount;

    public final long getFavoritedCount() {
        return _favoritedCount;
    }

    private void setFavoritedCount(long value) {
        _favoritedCount = value;
    }

    private long _statusID;

    public final long getStatusID() {
        return _statusID;
    }

    private void setStatusID(long value) {
        _statusID = value;
    }

    private String _inReplyToScreenName;

    public final String getInReplyToScreenName() {
        return _inReplyToScreenName;
    }

    private void setInReplyToScreenName(String value) {
        _inReplyToScreenName = value;
    }

    private long _inReplyToStatusID;

    public final long getInReplyToStatusID() {
        return _inReplyToStatusID;
    }

    private void setInReplyToStatusID(long value) {
        _inReplyToStatusID = value;
    }

    private String _hasMediaString;

    public final String getHasMediaString() {
        return _hasMediaString;
    }

    private void setHasMediaString(String value) {
        _hasMediaString = value;
    }

    private boolean _hasMedia;

    public final boolean getHasMedia() {
        return _hasMedia;
    }

    private void setHasMedia(boolean value) {
        _hasMedia = value;
    }

    private long _inReplyToUserID;

    public final long getInReplyToUserID() {
        return _inReplyToUserID;
    }

    private void setInReplyToUserID(long value) {
        _inReplyToUserID = value;
    }

    private long _spreadCount;

    public final long getSpreadCount() {
        return _spreadCount;
    }

    private void setSpreadCount(long value) {
        _spreadCount = value;
    }

    private boolean _isSpreaded;

    public final boolean getIsSpreaded() {
        return _isSpreaded;
    }

    private void setIsSpreaded(boolean value) {
        _isSpreaded = value;
    }

    private PostEntity _spreadStatus;

    public final PostEntity get_spreadStatus() {
        return _spreadStatus;
    }

    private void set_spreadStatus(PostEntity value) {
        _spreadStatus = value;
    }

    private PostEntity _replyStatus;

    public final PostEntity getReplyStatus() {
        return _replyStatus;
    }

    private void setReplyStatus(PostEntity value) {
        _replyStatus = value;
    }

    private String _post;

    public final String getPost() {
        return _post;
    }

    private void setPost(String value) {
        _post = value;
    }

    private UserEntity _user;

    public final UserEntity getUser() {
        return _user;
    }

    private void setUser(UserEntity value) {
        _user = value;
    }

    private String _sourceName;

    public final String getSourceName() {
        return _sourceName;
    }

    private void setSourceName(String value) {
        _sourceName = value;
    }

    private String _sourceUrl;

    public final String getSourceUrl() {
        return _sourceUrl;
    }

    private void setSourceUrl(String value) {
        _sourceUrl = value;
    }

    private String _imageUrl;

    public final String getImageUrl() {
        return _imageUrl;
    }

    private void setImageUrl(String value) {
        _imageUrl = value;
    }

    private String _spreadBy;

    public final String getSpreadBy() {
        return _spreadBy;
    }

    private void setSpreadBy(String value) {
        _spreadBy = value;
    }
}
