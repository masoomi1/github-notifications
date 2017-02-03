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


public class Plan implements Parcelable {

    private String name;
    private int space;
    private int privateRepos;
    private int collaborators;

    public final static Parcelable.Creator<Plan> CREATOR = new Creator<Plan>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Plan createFromParcel(Parcel in) {
            Plan instance = new Plan();
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.space = ((int) in.readValue((int.class.getClassLoader())));
            instance.privateRepos = ((int) in.readValue((int.class.getClassLoader())));
            instance.collaborators = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public Plan[] newArray(int size) {
            return (new Plan[size]);
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getPrivateRepos() {
        return privateRepos;
    }

    public void setPrivateRepos(int privateRepos) {
        this.privateRepos = privateRepos;
    }

    public int getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(int collaborators) {
        this.collaborators = collaborators;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(space);
        dest.writeValue(privateRepos);
        dest.writeValue(collaborators);
    }

    public int describeContents() {
        return 0;
    }
}
