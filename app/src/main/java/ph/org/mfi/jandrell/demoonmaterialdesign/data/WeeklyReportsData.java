package ph.org.mfi.jandrell.demoonmaterialdesign.data;

import java.util.Date;

/**
 * Created by ITD11 on 3/31/2015.
 */
public class WeeklyReportsData {

    private int wwrId;
    private int ojtId;
    private int infoId;
    private int weekNo;
    private Date startDate;
    private Date endDate;
    private String accomplishments;
    private String difficulties;

    public WeeklyReportsData(int wwrId, int ojtId, int infoId, int weekNo, Date startDate, Date endDate, String accomplishments, String difficulties) {
        this.wwrId = wwrId;
        this.ojtId = ojtId;
        this.infoId = infoId;
        this.weekNo = weekNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accomplishments = accomplishments;
        this.difficulties = difficulties;
    }

    public int getWwrId() {
        return wwrId;
    }

    public void setWwrId(int wwrId) {
        this.wwrId = wwrId;
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

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAccomplishments() {
        return accomplishments;
    }

    public void setAccomplishments(String accomplishments) {
        this.accomplishments = accomplishments;
    }

    public String getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }

    @Override
    public String toString() {
        return "WeeklyReportsData{" +
                "wwrId=" + wwrId +
                ", ojtId=" + ojtId +
                ", infoId=" + infoId +
                ", weekNo=" + weekNo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", accomplishments='" + accomplishments + '\'' +
                ", difficulties='" + difficulties + '\'' +
                '}';
    }
}
