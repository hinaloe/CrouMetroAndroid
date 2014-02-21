package com.innerlogic.croumetro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.net.OauthRequest;
import com.innerlogic.croumetro.net.PostRequest;
import com.innerlogic.croumetro.post.PostEntity;
import com.innerlogic.croumetro.tools.Constants;
import com.innerlogic.croumetro.user.UserEntity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TimelineActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private TimelineFragment mTimelineFragment = (TimelineFragment)
            getSupportFragmentManager().findFragmentById(R.id.timeline_fragment);
    ;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private ArrayList<PostEntity> postList = new ArrayList<PostEntity>();
    private ImageView targetImage = null;
    private AsyncTask<Void, Void, String> postTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        oAuth2Helper = new OAuth2Helper(this.prefs);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        Intent intent = getIntent();
        UserEntity person = (UserEntity) intent.getSerializableExtra("currentUserEntity");

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), (View) findViewById(R.id.headerBackground), person);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position <= 0) return;
        changeTimeline(position);
    }

    private void changeTimeline(int position) {
        String url;
        switch (position) {
            case 1:
                url = Constants.HOME_TIMELINE;
                break;
            case 2:
                url = Constants.PUBLIC_TIMELINE;
                break;
            case 3:
                url = Constants.MENTIONS_TIMELINE;
                break;
            default:
                url = Constants.PUBLIC_TIMELINE;
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_homeTimeline);
                break;
            case 2:
                mTitle = getString(R.string.title_publicTimeline);
                break;
            case 3:
                mTitle = getString(R.string.title_atReplyTimeline);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.timeline, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.ADD_MESSAGE) {
            final View messageView;
            LayoutInflater vi = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            messageView = vi.inflate(R.layout.dialog_message, null);
            EditText postMessage = (EditText) messageView.findViewById(R.id.postMessage);
            final Button sendPost = (Button) messageView.findViewById(R.id.sendPost);
            Button mediaPicker = (Button) messageView.findViewById(R.id.gallery);
            final AlertDialog builder = new AlertDialog.Builder(this).create();
            sendPost.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Send Post Button Clicked", "Timeline Activity");
                    try {
                        sendPost(messageView, builder);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            mediaPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Media Button Clicked", "Timeline Activity");
                    try {
                        openGallery(messageView);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Create simple alert dialog to show post screen.
            builder.setView(messageView);
            builder.setCancelable(true);
            builder.show();

            //Intent intent = new Intent(this, MessageActivity.class);
            //this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendPost(final View messageView, final AlertDialog builder) throws ExecutionException, InterruptedException {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oAuth2Helper = new OAuth2Helper(this.prefs);
        Map<String, String> paramsMap = new HashMap<String, String>();
        EditText mEdit = (EditText) messageView.findViewById(R.id.postMessage);
        ImageView targetImage = (ImageView) messageView.findViewById(R.id.targetImage);
        paramsMap.put("status", mEdit.getText().toString());
        postTest = new PostRequest(Constants.MESSAGE_UPDATE, paramsMap, oAuth2Helper, prefs) {
            @Override
            protected void onPostExecute(String json) {
                // TODO: Return to previous view.
                Log.i(Constants.TAG, "Message Returned : " + json);
                builder.dismiss();
                Toast toast = Toast.makeText(messageView.getContext(), "あなたのささやきが送信されました！", Toast.LENGTH_SHORT);
                toast.show();
            }
        }.execute();
    }

    public void openGallery(View view) throws ExecutionException, InterruptedException {
        targetImage = (ImageView) view.findViewById(R.id.targetImage);
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Returned from gallery, if we have an image, decode it and put it into the view.
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
