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

package io.alvaroorduna.githubnotifications.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import io.alvaroorduna.githubnotifications.BuildConfig;
import io.alvaroorduna.githubnotifications.api.models.AccessToken;

public class PreferencesUtils {

    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_TOKEN_TYPE = "token_type";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    public static void setAccessToken(@NonNull final Context context,
                                      @NonNull AccessToken AccessToken) {
        getSharedPreferences(context).edit()
                .putString(PREF_ACCESS_TOKEN, AccessToken.getAccessToken())
                .putString(PREF_TOKEN_TYPE, AccessToken.getTokenType())
                .apply();
    }

    public static String getAccessToken(@NonNull final Context context) {
        return getSharedPreferences(context).getString(PREF_ACCESS_TOKEN, null);
    }

    public static String getTokenType(@NonNull final Context context) {
        return getSharedPreferences(context).getString(PREF_TOKEN_TYPE, null);
    }

    public static void removeAccessToken(@NonNull final Context context) {
        getSharedPreferences(context).edit()
                .remove(PREF_ACCESS_TOKEN)
                .remove(PREF_TOKEN_TYPE)
                .apply();
    }
}
