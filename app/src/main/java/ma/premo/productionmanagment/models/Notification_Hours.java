package ma.premo.productionmanagment.models;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification_Hours {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("of")
    @Expose
    private String OF;
    @SerializedName("ligne")
    @Expose
    private String ligne;
    @SerializedName("chefEquipe")
    @Expose
    private String chefEquipe;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOF(String OF) {
        this.OF = OF;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public void setChefEquipe(String chefEquipe) {
        this.chefEquipe = chefEquipe;
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

    public String getOF() {
        return OF;
    }

    public String getLigne() {
        return ligne;
    }

    public String getChefEquipe() {
        return chefEquipe;
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



    @Override
    public String toString() {
        return "Notification_Hours{" +
                "id='" + id + '\'' +
                ", OF='" + OF + '\'' +
                ", ligne='" + ligne + '\'' +
                ", chefEquipe='" + chefEquipe + '\'' +
                ", shift='" + shift + '\'' +
                ", date=" + date +
                ", nbr_operateurs=" + nbr_operateurs +
                ", total_h=" + total_h +
                ", h_sup=" + h_sup +
                ", h_devolution=" + h_devolution +
                ", h_nouvau_projet=" + h_nouvau_projet +
                ", h_arrete=" + h_arrete +
                '}';
    }



    public Notification_Hours(String OF, String ligne, String chefEquipe, String shift, String date, int nbr_operateurs, int total_h, int h_sup,
                              int h_devolution, int h_nouvau_projet, int h_arrete) {
        this.OF = OF;
        this.ligne = ligne;
        this.chefEquipe = chefEquipe;
        this.shift = shift;
        this.date = date;
        this.nbr_operateurs = nbr_operateurs;
        this.total_h = total_h;
        this.h_sup = h_sup;
        this.h_devolution = h_devolution;
        this.h_nouvau_projet = h_nouvau_projet;
        this.h_arrete = h_arrete;
    }
    public  Notification_Hours(){};


}
