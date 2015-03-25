package ph.org.mfi.jandrell.demoonmaterialdesign.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jandrell on 2/14/2015.
 */
public class NewsFeedInfo {
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



}
