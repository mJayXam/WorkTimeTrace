package com.worktimetrace.frontend.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlManager {
    private String userUrl;
    private String timeUrl;
    private String pdfUrl;

    UrlManager(@Value("${usermanagement.url}") String user, @Value("${timemanagement.url}") String time, @Value("${export.url}") String pdf){
        userUrl = user;
        timeUrl = time;
        pdfUrl = pdf;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getTimeUrl() {
        return timeUrl;
    }

    public void setTimeUrl(String timeUrl) {
        this.timeUrl = timeUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    
}
