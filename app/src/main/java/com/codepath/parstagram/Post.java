package com.codepath.parstagram;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel(analyze = {Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "Likes";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String desc) {
        put(KEY_DESCRIPTION, desc);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile pf) {
        put(KEY_IMAGE, pf);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ArrayList<String> getLikes() {
        JSONArray jsonLikes = getJSONArray(KEY_LIKES);
        if (jsonLikes == null) {
            return null;
        }
        ArrayList<String> likes = new ArrayList<>();
        for (int i = 0; i < jsonLikes.length(); i++) {
            try {
                likes.add(jsonLikes.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return likes;
    }

    public int getNumLikes() {
        List<String> likes = getLikes();
        if (likes != null) {
            return likes.size();
        } else {
            return 0;
        }
    }

    public void addLike(String userId) {
        ArrayList<String> likes = getLikes();
        if (likes == null || !likes.contains(userId)) {
            add(KEY_LIKES, userId);
            saveInBackground();
        }
    }

    public String getTimeAgo() {
        Date createdAt = getCreatedAt();
        return Post.calculateTimeAgo(createdAt);
    }

    private static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "1 minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 120 * MINUTE_MILLIS) {
                return "1 hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "1 day ago";
            } else if (diff < 7 * DAY_MILLIS){
                return diff / DAY_MILLIS + " days ago";
            } else {
                String pattern = "MMMM d";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(createdAt);
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}
