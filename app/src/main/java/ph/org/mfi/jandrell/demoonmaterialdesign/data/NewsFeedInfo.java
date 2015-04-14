package ph.org.mfi.jandrell.demoonmaterialdesign.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jandrell on 2/14/2015.
 */
public class NewsFeedInfo implements Parcelable {
    private String mId;
    private String mTitle;
    private String mBody;
    private String mPublishDate;
    private String mPosterId;

    public NewsFeedInfo(String mId, String mTitle, String mBody, String mPublishDate, String mPosterId) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mBody = mBody;
        this.mPublishDate = mPublishDate;
        this.mPosterId = mPosterId;
    }

    public NewsFeedInfo(Parcel source) {
        mId = source.readString();
        mTitle = source.readString();
        mBody = source.readString();
        mPosterId = source.readString();
        mPublishDate = source.readString();
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmPublishDate() {
        return mPublishDate;
    }

    public void setmPublishDate(String mPublishDate) {
        this.mPublishDate = mPublishDate;
    }

    public String getmPosterId() {
        return mPosterId;
    }

    public void setmPosterId(String mPosterId) {
        this.mPosterId = mPosterId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeString(mPosterId);
        dest.writeString(mPublishDate);
    }

    public static final Creator<NewsFeedInfo> CREATOR
            = new Creator<NewsFeedInfo>() {

        @Override
        public NewsFeedInfo createFromParcel(Parcel source) {
            Log.d("JD", "create from parcel");
            return new NewsFeedInfo(source);
        }

        @Override
        public NewsFeedInfo[] newArray(int size) {
            return new NewsFeedInfo[size];
        }
    };
}
