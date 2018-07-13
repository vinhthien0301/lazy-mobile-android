package com.lazymining.mobile.Object;

import java.util.ArrayList;

/**
 * Created by doanngocduc on 1/23/18.
 */

public class MinerObject {
    private String speedAmout;
    private String share;
    private String reject;
    private String workTime;
    private String mine_hole;
    private String name;
    private String ip;
    private ArrayList<CardTempObject> vgaArray;
    private String workStatus;
    private String warningMessage;
    private String machineId;
    private String email;
    private String mineCoinHr;

    public MinerObject(){
        setSpeedAmout("");
        setShare("");
        setReject("");
        setWorkTime("");
        setMine_hole("");
        setName("");
        setIp("");
        setVgaArray(new ArrayList<CardTempObject>());
        setWorkStatus("");
        setWarningMessage("");
        setMachineId("");
        setEmail("");
        setMineCoinHr("");
    }
    public MinerObject(String name, String ip,String speedAmout, String share, String reject, String workTime,
                       String mine_hole, ArrayList<CardTempObject> vgaArray,String workStatus,String warningMessage,String machineId,
                       String email,String mineCoinHr){
        setSpeedAmout(speedAmout);
        setShare(share);
        setReject(reject);
        setWorkTime(workTime);
        setMine_hole(mine_hole);
        setName(name);
        setIp(ip);
        setVgaArray(vgaArray);
        setWorkStatus(workStatus);
        setWarningMessage(warningMessage);
        setMachineId(machineId);
        setEmail(email);
        setMineCoinHr(mineCoinHr);
    }


    public String getSpeedAmout() {
        return speedAmout;
    }

    public void setSpeedAmout(String speedAmout) {
        this.speedAmout = speedAmout;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getMine_hole() {
        return mine_hole;
    }

    public void setMine_hole(String mine_hole) {
        this.mine_hole = mine_hole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<CardTempObject> getVgaArray() {
        return vgaArray;
    }

    public void setVgaArray(ArrayList<CardTempObject> vgaArray) {
        this.vgaArray = vgaArray;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMineCoinHr() {
        return mineCoinHr;
    }

    public void setMineCoinHr(String mineCoinHr) {
        this.mineCoinHr = mineCoinHr;
    }
}
