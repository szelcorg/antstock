package org.szelc.financial.report;

public enum ReportType {

    QUARTAL(1, "quartal"),
    SEMI(2, "semi"),
    YEAR(3, "year");


    private final Integer id;
    private final String name;

    ReportType(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public static ReportType fromStr(String type) {
        if("kwartalny".equals(type)){
            return QUARTAL;
        }
        if("polroczny".equals(type)){
            return SEMI;
        }
        if("roczny".equals(type)){
            return YEAR;
        }
        return null;

    }
}
