
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

package io.alvaroorduna.githubnotifications.api.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Subject implements Parcelable {

    private String title;
    private String url;
    private String latestCommentUrl;
    private String type;

    public final static Creator<Subject> CREATOR = new Creator<Subject>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Subject createFromParcel(Parcel in) {
            Subject instance = new Subject();
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.latestCommentUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Subject[] newArray(int size) {
            return (new Subject[size]);
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLatestCommentUrl() {
        return latestCommentUrl;
    }

    public void setLatestCommentUrl(String latestCommentUrl) {
        this.latestCommentUrl = latestCommentUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(url);
        dest.writeValue(latestCommentUrl);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }
}
