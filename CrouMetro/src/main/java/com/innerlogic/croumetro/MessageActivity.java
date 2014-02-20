package com.innerlogic.croumetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.net.OauthRequest;
import com.innerlogic.croumetro.net.PostRequest;
import com.innerlogic.croumetro.tools.Constants;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MessageActivity extends ActionBarActivity {
    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    public EditText mEdit;
    public ImageView targetImage;
    private AsyncTask<Void, Void, String> postTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void sendPost(View view) throws ExecutionException, InterruptedException {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oAuth2Helper = new OAuth2Helper(this.prefs);
        OauthRequest oauthRequest = new OauthRequest(prefs, oAuth2Helper);
        Map<String, String> paramsMap = new HashMap<String, String>();
        mEdit = (EditText) findViewById(R.id.postMessage);
        targetImage = (ImageView) findViewById(R.id.targetImage);
        paramsMap.put("status", mEdit.getText().toString());
        postTest = new PostRequest(Constants.MESSAGE_UPDATE, paramsMap, oAuth2Helper, prefs) {
            @Override
            protected void onPostExecute(String json) {
                // TODO: Return to previous view.
                Log.i(Constants.TAG, "Message Returned : " + json);
            }
        }.execute();
    }

    public void openGallery(View view) throws ExecutionException, InterruptedException {
        targetImage = (ImageView) findViewById(R.id.targetImage);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_message, container, false);
            return rootView;
        }
    }

}
