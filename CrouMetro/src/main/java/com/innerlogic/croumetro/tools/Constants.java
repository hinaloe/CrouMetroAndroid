package com.innerlogic.croumetro.tools;

/**
 * Created by Timothy on 14/02/05.
 */
public class Constants {
    public static final String CROUDIA = "https://api.croudia.com/";

    //OAuth
    public static final String RESPONSE_URL = "https://sites.google.com/site/slowbeeflogin";

    public static final String OAUTH_TOKEN = CROUDIA + "oauth/token";

    public static final String AUTH_URL = CROUDIA + "oauth/authorize";

    public static final String VERIFY = CROUDIA + "account/verify_credentials.json";

    public static final String CONSUMER_KEY = "2203c7d744b0907aa27d5bc7011b995bfda36373a2ca9fb51b2e890cc3e167c4";

    public static final String CONSUMER_SECRET = "faeb15f3d359a01007872c9f298dfc6b8879a3e2ec4eb3cac964043a0838238f";

    //Timeline
    public static final String PUBLIC_TIMELINE = CROUDIA + "statuses/public_timeline.json";

    public static final String HOME_TIMELINE = CROUDIA + "statuses/home_timeline.json";

    public static final String MENTIONS_TIMELINE = CROUDIA + "statuses/mentions.json";

    public static final String USER_TIMELINE = CROUDIA + "statuses/user_timeline.json";

    //API Posts
    public static final String MESSAGE_UPDATE = CROUDIA + "statuses/update.json";

    public static final String FAVORITE_CREATE = CROUDIA + "favorites/create/%d.json";

    public static final String FAVORITE_DESTROY = CROUDIA + "favorites/destroy/%d.json";

    public static final String LIKE_CREATE = CROUDIA + "statuses/spread/%d.json";

    //Debug
    public static final String TAG = "CrouMetro";
}
