package org.szelc.financial.report;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private Company company;
    private ReportType type;
    private String period;
    private List<ReportData> reportDataList = new ArrayList<>();

    public Report(Company company, ReportType type, String period) {
        this.company = company;
        this.type = type;
        this.period = period;
    }

    public void addReportData(ReportData reportData){
        reportDataList.add(reportData);
    }

    public List<ReportData> getReportDataList() {
        return reportDataList;
    }

    @Override
    public String toString() {
        return "Report{" +
                "company=" + company +
                ", type=" + type +
                ", period='" + period + '\'' +
                ", reportDataList=" + reportDataList +
                '}';
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setReportDataList(List<ReportData> reportDataList) {
        this.reportDataList = reportDataList;
    }

    public String getCompanyName(){
        return company.getName();
    }
}
