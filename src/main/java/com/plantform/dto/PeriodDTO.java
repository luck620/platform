package com.plantform.dto;

import java.util.List;

public class PeriodDTO {
    private String weekST;
    private List<String> periodList;
    public String getWeekST() {
        return weekST;
    }

    public void setWeekST(String weekST) {
        this.weekST = weekST;
    }

    public List<String> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<String> periodList) {
        this.periodList = periodList;
    }
}
