package ma.premo.productionmanagment.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PresenceGroup {

    @SerializedName("id")
    private String id;
    @SerializedName("group")
    private String group;
    @SerializedName("shift")
    private String shift;
    @SerializedName("date")
    private String date;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("leaderId")
    private String leaderId;
    @SerializedName("leaderName")
    private String leaderName;
    @SerializedName("engineer")
    private String engineer;
    @SerializedName("totalOperators")
    private int totalOperators;
    @SerializedName("sumHours")
    private Double sumHours;
    @SerializedName("listPresence")
    private List<Presence> listPresence = new ArrayList<>();
    @SerializedName("status")
    private String status;

    public PresenceGroup() {
    }

    public PresenceGroup(String group, String shift, String date, String leaderId, String leaderName, String engineer, int totalOperators, Double sumHours, List<Presence> listPresence) {
        this.group = group;
        this.shift = shift;
        this.date = date;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.engineer = engineer;
        this.totalOperators = totalOperators;
        this.sumHours = sumHours;
        this.listPresence = listPresence;
    }


    public void addPresence(Presence presence){
        this.listPresence.add(presence);
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getShift() {
        return shift;
    }

    public String getDate() {
        return date;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public String getEngineer() {
        return engineer;
    }

    public int getTotalOperators() {
        return totalOperators;
    }

    public Double getSumHours() {
        return sumHours;
    }

    public List<Presence> getListPresence() {
        return listPresence;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public void setTotalOperators(int totalOperators) {
        this.totalOperators = totalOperators;
    }

    public void setSumHours(Double sumHours) {
        this.sumHours = sumHours;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setListPresence(List<Presence> listPresence) {
        this.listPresence = listPresence;
    }

    @Override
    public String toString() {
        return "PresenceGroup{" +
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
                ", shift='" + shift + '\'' +
                ", date='" + date + '\'' +
                ", leaderId='" + leaderId + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", engineer='" + engineer + '\'' +
                ", totalOperators=" + totalOperators +
                ", sumHours=" + sumHours +
                ", status=" + status +
                ", listPresence=" + listPresence +
                '}';
    }



}
