package com.pmt.gateway.auth.jwt;

public class DecodedToken {

    private final String subjectId;

    private final String role;

    public DecodedToken(String subjectId, String role) {
        this.subjectId = subjectId;
        this.role = role;
    }

    public String getSubjectId() {
        return this.subjectId;
    }

    public String getRole() {
        return this.role;
    }

}
