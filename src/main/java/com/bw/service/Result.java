/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bw.service;

import java.util.*;

/**
 *
 * @author mr.scherr
 */
public class Result<T>{
    private boolean success = true;
    private List<String> messages = new ArrayList<>();
    private List<String> positiveResults = new ArrayList<>();
    private T payload;

    public boolean isSuccess() {
        return success;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.success = false;
        messages.add(message);
    }
    public void setPositiveResponseMessages(String message) {
        positiveResults.add(message);
    }
    public List<String> getPositiveResponseMessages() {
        return positiveResults;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }



}
