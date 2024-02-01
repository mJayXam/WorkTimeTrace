package com.worktimetrace.frontend.Models;

public class PDFExport {
    private double hourRate;

    public PDFExport() {
    }

    public PDFExport(int hourRate) {
        this.hourRate = hourRate;
    }

    public double getHourRate() {
        return hourRate;
    }

    public void setHourRate(double hourRate) {
        this.hourRate = hourRate;
    }

    @Override
    public String toString() {
        return "PDFExport [hourRate=" + hourRate + "]";
    }
    
}
