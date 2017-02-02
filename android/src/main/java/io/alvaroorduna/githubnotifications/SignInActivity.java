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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import io.alvaroorduna.githubnotifications.api.APIError;
import io.alvaroorduna.githubnotifications.api.APIHelper;
import io.alvaroorduna.githubnotifications.api.ServiceGenerator;
import io.alvaroorduna.githubnotifications.api.models.AccessToken;
import io.alvaroorduna.githubnotifications.api.services.AuthService;
import io.alvaroorduna.githubnotifications.utils.LogUtils;
import io.alvaroorduna.githubnotifications.utils.PreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private static final String LOG_TAG = LogUtils.makeLogTag(SignInActivity.class);

    private RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mMainLayout = (RelativeLayout) findViewById(R.id.sign_in_layout);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, APIHelper.getGitHubAuthorizeUri());
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(APIHelper.CALLBACK_URL)) {
            String code = uri.getQueryParameter(APIHelper.QUERY_PARAM_CODE);
            if (code != null) {
                AuthService authService = ServiceGenerator.createService(AuthService.class, this);
                Call<AccessToken> call = authService.getAccessToken(code,
                        APIHelper.CLIENT_ID,
                        APIHelper.CLIENT_SECRET,
                        APIHelper.CALLBACK_URL);
                call.enqueue(new AccessTokenCallback());
            } else {
                String error = uri.getQueryParameter(APIHelper.QUERY_PARAM_ERROR);
                Log.e(LOG_TAG, "Couldn't get SigIn code: " +
                        ((error != null) ? error : "Unknown error"));
                onSignInFailure();
            }
        }
    }

    private void onSignInSuccessful(AccessToken accessToken) {
        PreferencesUtils.setAccessToken(this, accessToken);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onSignInFailure() {
        Snackbar.make(mMainLayout, getString(R.string.sign_in_failure_msg), Snackbar.LENGTH_LONG)
                .show();
    }

    private class AccessTokenCallback implements Callback<AccessToken> {

        private final String LOG_TAG = LogUtils.makeLogTag(AccessTokenCallback.class);

        @Override
        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
            if (response.isSuccessful()) {
                onSignInSuccessful(response.body());
            } else {
                APIError error = new APIError(response);
                LogUtils.LOGE(LOG_TAG, "Couldn't get AccessToken: " + error.toString());
                onSignInFailure();
            }
        }

        @Override
        public void onFailure(Call<AccessToken> call, Throwable t) {
            LogUtils.LOGE(LOG_TAG, "Couldn't get AccessToken: " + t.getMessage(), t);
            onSignInFailure();
        }
    }
}
