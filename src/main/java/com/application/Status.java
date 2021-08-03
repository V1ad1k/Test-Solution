package com.application;

import java.util.Date;

@SuppressWarnings("unused")
public class Status {

    private int requests;
    private Date activeFrom;

    public Status(int requests, Date activeFrom) {
        this.requests = requests;
        this.activeFrom = activeFrom;
    }

    public int getRequests() {
        return requests;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }
}
