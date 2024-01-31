package com.worktimetrace.pdfexport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class urlkeeper {
    private String url;

    urlkeeper(@Value("${timemanagement.url}") String url){
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
