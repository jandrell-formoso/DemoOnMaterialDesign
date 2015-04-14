package ph.org.mfi.jandrell.demoonmaterialdesign.data;

import java.util.Date;

/**
 * Created by ITD11 on 3/20/2015.
 */
public class SummaryOfAlt {
    private int altId;
    private int ojtId;
    private int infoId;
    private Date date;
    private String status;
    private String reason;
    private String notedBy;

    public SummaryOfAlt() {

    }

    public SummaryOfAlt(int altId, int ojtId, int infoId, Date date, String status, String reason, String notedBy) {
        this.altId = altId;
        this.ojtId = ojtId;
        this.infoId = infoId;
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.notedBy = notedBy;
    }

    public int getAltId() {
        return altId;
    }

    public void setAltId(int altId) {
        this.altId = altId;
    }

    public int getOjtId() {
        return ojtId;
    }

    public void setOjtId(int ojtId) {
        this.ojtId = ojtId;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotedBy() {
        return notedBy;
    }

    public void setNotedBy(String notedBy) {
        this.notedBy = notedBy;
    }
}
