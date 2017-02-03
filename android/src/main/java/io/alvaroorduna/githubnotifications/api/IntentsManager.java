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

import android.content.Intent;
import android.net.Uri;


public class IntentsManager {

    private static Uri normalizeUri(Uri uri) {
        if (uri != null && uri.getAuthority() != null) {
            if (uri.getPath().contains("/api/v3/")) {
                String authority = uri.getPath().replace("/api/v3/", "/");
                uri = uri.buildUpon().path(authority).build();
            } else if (uri.getAuthority().contains("api.")) {
                String authority = uri.getAuthority().replace("api.", "");
                uri = uri.buildUpon().authority(authority).build();
            }

            if (uri.getPath().contains("repos/")) {
                String path = uri.getPath().replace("repos/", "");
                uri = uri.buildUpon().path(path).build();
            }
            if (uri.getPath().contains("commits/")) {
                String path = uri.getPath().replace("commits/", "commit/");
                uri = uri.buildUpon().path(path).build();
            }
            if (uri.getPath().contains("pulls/")) {
                String path = uri.getPath().replace("pulls/", "pull/");
                uri = uri.buildUpon().path(path).build();
            }
        }
        return uri;
    }

    public static Intent checkUri(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, normalizeUri(uri));
    }
}
