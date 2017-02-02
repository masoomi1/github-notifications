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

package io.alvaroorduna.githubnotifications.api;

import android.net.Uri;

import io.alvaroorduna.githubnotifications.BuildConfig;


public class APIHelper {

    public static final String API_BASE_URL = "https://api.github.com/";
    public static final String OAUTH_BASE_URL = "https://github.com/login/oauth/";

    public static final String OAUTH_AUTHORIZE_PATH = "authorize";
    public static final String OAUTH_ACCESS_TOKEN_PATH = "access_token";

    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    public static final String CALLBACK_URL = "io.alvaroorduna.githubnotifications://oauth";
    public static final String SCOPE = "notifications";

    public static final String QUERY_PARAM_CODE = "code";
    public static final String QUERY_PARAM_ERROR = "error";

    public static Uri getGitHubAuthorizeUri() {
        return Uri.parse(OAUTH_BASE_URL + OAUTH_AUTHORIZE_PATH +
                "?client_id=" + CLIENT_ID +
                "&scope=" + SCOPE +
                "&redirect_uri=" + CALLBACK_URL);
    }
}
