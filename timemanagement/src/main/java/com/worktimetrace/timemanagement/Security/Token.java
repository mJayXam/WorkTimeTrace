package com.worktimetrace.timemanagement.Security;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Token {

    @JsonAlias("username")
    private String unsername;

    @JsonAlias("token")
    private String token;

    public Token(){

    }

    public String getUnsername() {
        return unsername;
    }
    public Token setUnsername(String unsername) {
        this.unsername = unsername;
        return this;
    }
    public String getToken() {
        return token;
    }
    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public static Token make(){
        return new Token();
    }

    @Override
    public String toString() {
        return String.format("""
                {
                    "username": "%s",
                    "token": "%s"
                }
                """, unsername, token);
    }
    
}
