package com.tournoux.ws.btsocket.model;

import javax.persistence.*;

@Entity
@Table( name = "rcvrSignals")
public class RcvrSignal {

    private long id;
    private String ssi = "";
    private String snr = "";
    private String sqi = "";
    private String per = "";
    private boolean ready = false;
    private long timestamp = 0l;

    public RcvrSignal() {

    }

    public RcvrSignal(String ssi, String snr, String sqi, String per){
        this.per = per;
        this.ssi = ssi;
        this.snr = snr;
        this.sqi = sqi;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Column(name = "ssi", nullable = true)
    public String getSsi() {
        return ssi;
    }

    public void setSsi(String ssi) {
        this.ssi = ssi;
    }

    @Column(name = "snr", nullable=true)
    public String getSnr() {
        return snr;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    @Column(name = "sqi", nullable = true)
    public String getSqi() {
        return sqi;
    }

    public void setSqi(String sqi) {
        this.sqi = sqi;
    }

    @Column(name="per", nullable = true)
    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    @Column(name="ready",nullable=true)
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Column(name="timestamp",nullable = true)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
