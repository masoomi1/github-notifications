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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.alvaroorduna.githubnotifications.BuildConfig;
import io.alvaroorduna.githubnotifications.api.models.AccessToken;
import io.alvaroorduna.githubnotifications.utils.LogUtils;
import io.alvaroorduna.githubnotifications.utils.NetworkUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String LOG_TAG = LogUtils.makeLogTag(ServiceGenerator.class);

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String CACHE_FILE_NAME = "http-cache";
    private static final long CACHE_FILE_MAX_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final int CACHE_MAX_AGE = 2;
    private static final TimeUnit CACHE_MAX_AGE_UNITS = TimeUnit.MINUTES;
    private static final int OFFLINE_CACHE_MAX_AGE = 7;
    private static final TimeUnit OFFLINE_CACHE_MAX_AGE_UNITS = TimeUnit.DAYS;

    public static <S> S createService(@NonNull final Class<S> serviceClass,
                                      @NonNull final Context context) {
        return createService(serviceClass, context, null);
    }

    public static <S> S createService(@NonNull final Class<S> serviceClass,
                                      @NonNull final Context context,
                                      @Nullable final AccessToken accessToken) {
        return provideRetrofit(context, accessToken).create(serviceClass);
    }

    private static Retrofit provideRetrofit(@NonNull final Context context,
                                            @Nullable final AccessToken accessToken) {
        return new Retrofit.Builder()
                .baseUrl(APIHelper.API_BASE_URL)
                .client(provideOkHttpClient(context, accessToken))
                .addConverterFactory(provideConverterFactory())
                .build();
    }

    private static OkHttpClient provideOkHttpClient(@NonNull final Context context,
                                                    @Nullable final AccessToken accessToken) {
        return new OkHttpClient.Builder()
                .addInterceptor(provideGitHubInterceptor(accessToken))
                .addInterceptor(provideLogInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor(context))
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache(context))
                .build();
    }

    private static Converter.Factory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    private static Cache provideCache(@NonNull final Context context) {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), CACHE_FILE_NAME), CACHE_FILE_MAX_SIZE);
        } catch (Exception e) {
            LogUtils.LOGE(LOG_TAG, "Could not create Cache!", e);
        }
        return cache;
    }

    private static Interceptor provideGitHubInterceptor(@Nullable final AccessToken token) {
        return chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json");

            if (token != null && token.isValid()) {
                requestBuilder = requestBuilder.header("Authorization", token.toString());
            }

            return chain.proceed(requestBuilder.build());
        };
    }

    private static Interceptor provideLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.HEADERS
                : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(CACHE_MAX_AGE, CACHE_MAX_AGE_UNITS)
                    .build();

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    private static Interceptor provideOfflineCacheInterceptor(@NonNull final Context context) {
        return chain -> {
            Request request = chain.request();

            if (!NetworkUtils.hasNetwork(context)) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(OFFLINE_CACHE_MAX_AGE, OFFLINE_CACHE_MAX_AGE_UNITS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }
}
