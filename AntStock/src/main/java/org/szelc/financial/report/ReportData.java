package org.szelc.financial.report;

public class ReportData {
    Integer id;
    ReportField field;
    Float value;

    public ReportData(ReportField field, Float value) {
        this.field = field;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ReportField getField() {
        return field;
    }

    public void setField(ReportField field) {
        this.field = field;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "id=" + id +
                ", field=" + field +
                ", value=" + value +
                '}';
    }
}
