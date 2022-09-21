package ma.premo.productionmanagment.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Groupe {

    @SerializedName("id")
    private String id;
    @SerializedName("designation")
    private String designation;
    @SerializedName("shift")
    private String shift;

    @SerializedName("chefEquipe")
    private String chefEquipe;

    @SerializedName("ingenieur")
    private User ingenieur;

    @SerializedName("technicalExpert")
    private User technicalExpert;

    @SerializedName("zone")
    private String zone;

    @SerializedName("passwordGroup")
    private String passwordGroup;

    @SerializedName("listLine")
    private List<Line> listLine;
    @SerializedName("listOperateurs")
    private List<User> listOperateurs;

    @SerializedName("leaderName")
    private String leaderName;

    public Groupe(String designation, String shift, String chefEquipe, List<Line> listLine, List<User> listOperateurs) {
        this.designation = designation;
        this.shift = shift;
        this.chefEquipe = chefEquipe;
        this.listLine = listLine;
        this.listOperateurs = listOperateurs;
    }

    public Groupe() {
    }

    public String getPasswordGroup() {
        return passwordGroup;
    }



    public User getTechnicalExpert() {
        return technicalExpert;
    }

    public String getZone() {
        return zone;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setTechnicalExpert(User technicalExpert) {
        this.technicalExpert = technicalExpert;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getShift() {
        return shift;
    }

    public String getChefEquipe() {
        return chefEquipe;
    }

    public List<Line> getListLine() {
        return listLine;
    }

    public List<User> getListOperateurs() {
        return listOperateurs;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setChefEquipe(String chefEquipe) {
        this.chefEquipe = chefEquipe;
    }

    public void setListLine(List<Line> listLine) {
        this.listLine = listLine;
    }

    public void setListOperateurs(List<User> listOperateurs) {
        this.listOperateurs = listOperateurs;
    }

    public User getIngenieur() {
        return ingenieur;
    }

    public void setIngenieur(User ingenieur) {
        this.ingenieur = ingenieur;
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "id='" + id + '\'' +
                ", designation='" + designation + '\'' +
                ", shift='" + shift + '\'' +
                ", chefEquipe='" + chefEquipe + '\'' +
                ", ingenieur=" + ingenieur.toString() +
                ", listLine=" + listLine +
                ", listOperateurs=" + listOperateurs +
                '}';
    }
}
