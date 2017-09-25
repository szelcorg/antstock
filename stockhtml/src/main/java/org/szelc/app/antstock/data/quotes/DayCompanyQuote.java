package org.szelc.app.antstock.data.quotes;

import java.util.Date;
import org.szelc.app.antstock.settings.Settings;

/**
 *
 * @author szelc.org
 */
public class DayCompanyQuote {

    private int order;
    private Date date;
    private String companyName;
    private float high;
    private float low;
    private float open;
    private float reference;
    private float course;
    private long volume;

    public DayCompanyQuote() {
    }

    public DayCompanyQuote(String companyName, Date date, float open, float high, float low, float course, long volume) {
        this.date = date;
        this.companyName = companyName;
        this.high = high;
        this.low = low;
        this.open = open;
//        this.reference = reference;
        this.course = course;
        this.volume = volume;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getHigh() {
        return high;
    }

    public String getHighStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(high);
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public String getLowStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(low);
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getOpen() {
        return open;
    }

    public String getOpenStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(open);
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getReference() {
        return reference;
    }

    public String getReferenceStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(reference);
    }

    public void setReference(float reference) {
        this.reference = reference;
    }

    public float getCourse() {
        return course;
    }

    public String getCourseStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(course);
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getVolumeStr() {
        return Settings.DECIMAL_FORMAT_INTEGER.format(volume);
    }

    public float getChange() {
        return reference > 0 ? ((100f) * (course - reference) / reference) : 0;
    }

    public String getChangeStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getChange());
    }

    public long getVolumeMoney() {
        return ((long) ((float) volume * (float) ((high + low) / 2)));
    }

    public String getVolumeMoneyStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getVolumeMoney());
    }

    public float getHighChange() {
        return high > 0 ? ((100f) * (high - reference) / reference) : 0;
    }

    public String getHighChangeStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getHighChange());
    }

    public float getLowChange() {
        return low > 0 ? ((100f) * (low - reference) / reference) : 0;
    }

    public String getLowChangeStr() {
        return Settings.DECIMAL_FORMAT_FLOAT.format(getLowChange());
    }


   

    @Override
    public String toString() {
        return "DayCompanyQuote{" + "date=" + date + ", companyName=" + companyName + ", high=" + high + ", low=" + low + ", open=" + open + ", reference=" + reference + ", course=" + course + ", volume=" + volume + '}';
    }

}
