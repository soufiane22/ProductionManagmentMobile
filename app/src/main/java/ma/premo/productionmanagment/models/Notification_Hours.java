package ma.premo.productionmanagment.models;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification_Hours implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("of")
    @Expose
    private int OF;
    @SerializedName("ligne")
    @Expose
    private Line ligne;
    @SerializedName("produit")
    @Expose
    private Produit produit;

    @SerializedName("idLeader")
    @Expose
    private String idLeader;

    @SerializedName("leaderName")
    @Expose
    private String leaderName;

    @SerializedName("shift")
    @Expose
    private String shift;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("nbr_operateurs")
    @Expose
    private int nbr_operateurs;

    @SerializedName("total_h")
    @Expose
    private int total_h;

    @SerializedName("h_sup")
    @Expose
    private int h_sup;

    @SerializedName("h_devolution")
    @Expose
    private int h_devolution;

    @SerializedName("h_nouvau_projet")
    @Expose
    private int h_nouvau_projet;

    @SerializedName("h_arrete")
    @Expose
    private int h_arrete;

    @SerializedName("h_normal")
    @Expose
    private int h_normal;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalOutput")
    @Expose
    private int totalOutput;

    @SerializedName("totalScrap")
    @Expose
    private int totalScrap;

    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("standar_hours")
    private double standar_hours;

    @SerializedName("productivity")
    private double productivity;

    @SerializedName("scrapRatio")
    private double scrapRatio;

    public double getScrapRatio() {
        return scrapRatio;
    }

    public void setScrapRatio(float scrapRatio) {
        this.scrapRatio = scrapRatio;
    }

    public double getStandar_hours() {
        return standar_hours;
    }

    public double getProductivity() {
        return productivity;
    }

    public void setLigne(Line ligne) {
        this.ligne = ligne;
    }

    public void setStandar_hours(double standar_hours) {
        this.standar_hours = standar_hours;
    }

    public void setProductivity(double productivity) {
        this.productivity = productivity;
    }

    public int getTotalOutput() {
        return totalOutput;
    }

    public int getTotalScrap() {
        return totalScrap;
    }

    public void setTotalOutput(int totalOutput) {
        this.totalOutput = totalOutput;
    }

    public void setTotalScrap(int totalScrap) {
        this.totalScrap = totalScrap;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getH_normal() {
        return h_normal;
    }

    public void setH_normal(int h_normal) {
        this.h_normal = h_normal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOF(int OF) {
        this.OF = OF;
    }

    public void setLine(Line line) {
        this.ligne = line;
    }

    public void setIdLeader(String idLeader) {
        this.idLeader = idLeader;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNbr_operateurs(int nbr_operateurs) {
        this.nbr_operateurs = nbr_operateurs;
    }

    public void setTotal_h(int total_h) {
        this.total_h = total_h;
    }

    public void setH_sup(int h_sup) {
        this.h_sup = h_sup;
    }

    public void setH_devolution(int h_devolution) {
        this.h_devolution = h_devolution;
    }

    public void setH_nouvau_projet(int h_nouvau_projet) {
        this.h_nouvau_projet = h_nouvau_projet;
    }

    public void setH_arrete(int h_arrete) {
        this.h_arrete = h_arrete;
    }

    public int getOF() {
        return OF;
    }

    public Line getLigne() {
        return ligne;
    }

    public String getIdLeader() {
        return idLeader;
    }

    public String getShift() {
        return shift;
    }

    public String getDate() {
        return date;
    }

    public int getNbr_operateurs() {
        return nbr_operateurs;
    }

    public int getTotal_h() {
        return total_h;
    }

    public int getH_sup() {
        return h_sup;
    }

    public int getH_devolution() {
        return h_devolution;
    }

    public int getH_nouvau_projet() {
        return h_nouvau_projet;
    }
    public int getH_arrete() {
        return h_arrete;
    }

    public Produit getProduit() {
        return produit;
    }

    public String getRemark() {
        return remark;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Notification_Hours(int OF, Line ligne, Produit p, String chefEquipe, String shift, String date, int nbr_operateurs, int total_h, int h_sup,
                              int h_devolution, int h_nouvau_projet, int h_arrete , int hNormal) {
        this.OF = OF;
        this.ligne = ligne;
        this.produit= p;
        this.idLeader = chefEquipe;
        this.shift = shift;
        this.date = date;
        this.nbr_operateurs = nbr_operateurs;
        this.total_h = total_h;
        this.h_sup = h_sup;
        this.h_devolution = h_devolution;
        this.h_nouvau_projet = h_nouvau_projet;
        this.h_arrete = h_arrete;
        this.h_normal = hNormal;
    }
    public  Notification_Hours(){};

    @Override
    public String toString() {
        return "Notification_Hours{" +
                "id='" + id + '\'' +
                ", OF=" + OF +
                ", ligne=" + ligne +
                ", produit=" + produit +
                ", idLeader='" + idLeader + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", shift='" + shift + '\'' +
                ", date='" + date + '\'' +
                ", remark='" + remark + '\'' +
                ", nbr_operateurs=" + nbr_operateurs +
                ", total_h=" + total_h +
                ", h_sup=" + h_sup +
                ", h_devolution=" + h_devolution +
                ", h_nouvau_projet=" + h_nouvau_projet +
                ", h_arrete=" + h_arrete +
                ", h_normal=" + h_normal +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", standar_hours=" + standar_hours +
                ", productivity=" + productivity +
                ", ScrapRation=" + scrapRatio +
                '}';
    }
}
