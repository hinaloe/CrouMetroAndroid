package com.innerlogic.croumetro;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.innerlogic.croumetro.listAdapter.TimelineListAdapter;
import com.innerlogic.croumetro.net.GetRequest;
import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.net.OauthRequest;
import com.innerlogic.croumetro.post.PostEntity;
import com.innerlogic.croumetro.tools.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Timothy on 14/02/08.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimelineFragment extends ListFragment implements
        OnRefreshListener {
    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private AsyncTask<Void, Void, String> timeline = null;
    private ArrayList<PostEntity> postList = new ArrayList<PostEntity>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PullToRefreshLayout mPullToRefreshLayout;
    private TimelineListAdapter timelineListAdapter;
    private Boolean refreshList = false;
    private Boolean isLoading = false;
    private OauthRequest oauthRequest;
    static ImageLoader imageLoader = ImageLoader.getInstance();

    public static TimelineFragment newInstance(int sectionNumber) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        boolean pauseOnScroll = true; // or true
        boolean pauseOnFling = true; // or false
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
        getListView().setOnScrollListener(listener);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    addMoreItems();
                }
            }
        });
        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())
                .listener(this)
                .setup(mPullToRefreshLayout);
    }

    private void addMoreItems() {
        if (!isLoading) {
            isLoading = true;
            refreshList = false;
            new AsyncTask<Void, Void, Void>() {
                String url;

                @Override
                protected Void doInBackground(Void... params) {
                    refreshList = true;
                    PostEntity post = postList.get(postList.size() - 1);
                    ;
                    url = Constants.PUBLIC_TIMELINE;
                    url += "&max_id=" + post.getStatusID();
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    timeline = new GetRequest(url, oAuth2Helper, prefs) {
                        @Override
                        protected void onPostExecute(String result) {
                            json = result;
                            new CreateTimeline().execute();
                            isLoading = false;
                        }
                    }.execute();
                }
            }.execute();
        }
        return;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle b = getArguments();
            String url = "";
            //TODO: Always set arguments.
            url = Constants.PUBLIC_TIMELINE;
            prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            oAuth2Helper = new OAuth2Helper(this.prefs);
            oauthRequest = new OauthRequest(prefs, oAuth2Helper);
            GetTimeline(url);
        }
    }

    private String json;

    private void GetTimeline(String url) {
        timeline = new GetRequest(url, oAuth2Helper, prefs) {
            @Override
            protected void onPostExecute(String result) {
                json = result;
                new CreateTimeline().execute();
            }
        }.execute();
    }


    private void setTimelineList() {
        if (timelineListAdapter == null) {
            timelineListAdapter = new TimelineListAdapter(this.getActivity(), R.layout.timeline_listview_layout, postList);

        } else {
            timelineListAdapter.notifyDataSetChanged();
        }
        setListAdapter(timelineListAdapter);
    }

    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {
            String url;

            @Override
            protected Void doInBackground(Void... params) {
                refreshList = true;
                PostEntity post = postList.get(0);
                url = Constants.PUBLIC_TIMELINE;
                url += "&since_id=" + post.getStatusID();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                timeline = new GetRequest(url, oAuth2Helper, prefs) {
                    @Override
                    protected void onPostExecute(String result) {
                        json = result;
                        new CreateTimeline().execute();
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                }.execute();
                // Notify PullToRefreshLayout that the refresh has finished

            }
        }.execute();
    }


    private class CreateTimeline extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                JSONArray jsonRoot = new JSONArray(json);
                for (int i = 0; i < jsonRoot.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonRoot.get(i);
                    PostEntity post = new PostEntity(jsonObject);
                    if (refreshList) {
                        postList.add(0, post);
                    } else {
                        postList.add(post);
                    }

                }
                Log.i(Constants.TAG, "Got Post List");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setTimelineList();
        }
    }
}
