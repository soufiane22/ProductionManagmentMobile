package ma.premo.productionmanagment.models;


import com.google.gson.annotations.SerializedName;

public class Presence {

    @SerializedName("id")
    private String id;
    @SerializedName("date")
    private String date;
    @SerializedName("shift")
    private String shift;
    @SerializedName("etat")
    private String etat;
    @SerializedName("nbrHeurs")
    private int nbrHeurs = 0;

    @SerializedName("line")
    private String line;

    @SerializedName("chefEquipe")
    private String chefEquipe;

    @SerializedName("operateur")
    private User operateur;

    @SerializedName("ingenieur")
    private String ingenieur;

    public Presence(String date, String shift, String etat, int nbrHeurs, String line, String chefEquipe, User operateur, String ingenieur) {
        this.date = date;
        this.shift = shift;
        this.etat = etat;
        this.nbrHeurs = nbrHeurs;
        this.line = line;
        this.chefEquipe = chefEquipe;
        this.operateur = operateur;
        this.ingenieur = ingenieur;
    }

    public Presence() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setNbrHeurs(int nbrHeurs) {
        this.nbrHeurs = nbrHeurs;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setChefEquipe(String chefEquipe) {
        this.chefEquipe = chefEquipe;
    }

    public void setOperateur(User operateur) {
        this.operateur = operateur;
    }

    public void setIngenieur(String ingenieur) {
        this.ingenieur = ingenieur;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getShift() {
        return shift;
    }

    public String getEtat() {
        return etat;
    }

    public int getNbrHeurs() {
        return nbrHeurs;
    }

    public String getLine() {
        return line;
    }

    public String getChefEquipe() {
        return chefEquipe;
    }

    public User getOperateur() {
        return operateur;
    }

    public String getIngenieur() {
        return ingenieur;
    }

    @Override
    public String toString() {
        return "Presence{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", shift='" + shift + '\'' +
                ", etat='" + etat + '\'' +
                ", nbrHeurs=" + nbrHeurs +
                ", line=" + line +
                ", chefEquipe=" + chefEquipe +
                ", operateur=" + operateur.toString() +
                ", ingenieur=" + ingenieur +
                '}';
    }
}
