package com.plantform.dto;

import java.util.List;

public class PeriodDTO {
    private String weekST;
    private List<PeriodSTDTO> periodList;
    public String getWeekST() {
        return weekST;
    }

    public void setWeekST(String weekST) {
        this.weekST = weekST;
    }

    public List<PeriodSTDTO> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<PeriodSTDTO> periodList) {
        this.periodList = periodList;
    }
}
