
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


public class Notification implements Parcelable {

    private String id;
    private Repository repository;
    private Subject subject;
    private String reason;
    private boolean unread;
    private String updatedAt;
    private String lastReadAt;
    private String url;

    public final static Creator<Notification> CREATOR = new Creator<Notification>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Notification createFromParcel(Parcel in) {
            Notification instance = new Notification();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.repository = ((Repository) in.readValue((Repository.class.getClassLoader())));
            instance.subject = ((Subject) in.readValue((Subject.class.getClassLoader())));
            instance.reason = ((String) in.readValue((String.class.getClassLoader())));
            instance.unread = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastReadAt = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Notification[] newArray(int size) {
            return (new Notification[size]);
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(repository);
        dest.writeValue(subject);
        dest.writeValue(reason);
        dest.writeValue(unread);
        dest.writeValue(updatedAt);
        dest.writeValue(lastReadAt);
        dest.writeValue(url);
    }

    public int describeContents() {
        return 0;
    }
}
