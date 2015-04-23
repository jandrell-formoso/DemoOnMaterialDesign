package ph.org.mfi.jandrell.demoonmaterialdesign.data;

import java.util.Date;

/**
 * Created by ITD11 on 3/31/2015.
 */
public class DTRData {
    private int dtrId;
    private int ojtId;
    private int infoId;
    private Date date;
    private Long in;
    private Long out;
    private Long hours;
    private String remarks;

    public DTRData() {
    }

    public DTRData(String remarks, Long hours, Long out, Long in, Date date, int infoId, int ojtId, int dtrId) {
        this.remarks = remarks;
        this.hours = hours;
        this.out = out;
        this.in = in;
        this.date = date;
        this.infoId = infoId;
        this.ojtId = ojtId;
        this.dtrId = dtrId;
    }

    public int getDtrId() {
        return dtrId;
    }

    public void setDtrId(int dtrId) {
        this.dtrId = dtrId;
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

    public Long getIn() {
        return in;
    }

    public void setIn(Long in) {
        this.in = in;
    }

    public Long getOut() {
        return out;
    }

    public void setOut(Long out) {
        this.out = out;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
