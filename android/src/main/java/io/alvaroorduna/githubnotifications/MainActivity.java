/*
 * MIT License
 *
 * Copyright (c) 2017 Álvaro Orduna León
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the
 * Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS
 * IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.alvaroorduna.githubnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import io.alvaroorduna.githubnotifications.adapters.NotificationsAdapter;
import io.alvaroorduna.githubnotifications.api.APIError;
import io.alvaroorduna.githubnotifications.api.ServiceGenerator;
import io.alvaroorduna.githubnotifications.api.models.AccessToken;
import io.alvaroorduna.githubnotifications.api.models.Notification;
import io.alvaroorduna.githubnotifications.api.services.NotificationService;
import io.alvaroorduna.githubnotifications.utils.LogUtils;
import io.alvaroorduna.githubnotifications.utils.PreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = LogUtils.makeLogTag(MainActivity.class);

    private RelativeLayout mMainLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // TODO: avoid downloading data on onConfigChange
        AccessToken accessToken = AccessToken.newInstance(this);
        if (!accessToken.isValid()) {
            requestSignIn();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            NotificationService notificationService =
                    ServiceGenerator.createService(NotificationService.class, this, accessToken);
            Call<List<Notification>> call = notificationService.getUnread(false);
            call.enqueue(new NotificationsCallback());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                PreferencesUtils.removeAccessToken(this);
                requestSignIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void onNotificationsSuccessful(List<Notification> notifications) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(new NotificationsAdapter(this, notifications));
    }

    private void onNotificationsFailure() {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mMainLayout, getString(R.string.notifications_failure_msg), Snackbar.LENGTH_LONG)
                .show();
    }

    private class NotificationsCallback implements Callback<List<Notification>> {

        @Override
        public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
            if (response.isSuccessful()) {
                onNotificationsSuccessful(response.body());
            } else {
                APIError error = new APIError(response);
                LogUtils.LOGE(LOG_TAG, "Couldn't get user notifications: " + error.toString());
                onNotificationsFailure();
            }
        }

        @Override
        public void onFailure(Call<List<Notification>> call, Throwable t) {
            LogUtils.LOGE(LOG_TAG, "Couldn't get user notifications: " + t.getMessage(), t);
            onNotificationsFailure();
        }
    }
}
