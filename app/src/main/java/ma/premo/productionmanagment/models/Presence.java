package ma.premo.productionmanagment.models;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Presence implements Comparable<Presence> {

    @SerializedName("id")
    private String id;

    @SerializedName("idPerson")
    private String idPerson;

    @SerializedName("nomPerson")
    private String nomPerson;

    @SerializedName("prenomPerson")
    private String prenomPerson;

    @SerializedName("matriculePerson")
    private int matriculePerson;

    @SerializedName("functionPerson")
    private String functionPerson;

    @SerializedName("etat")
    private String etat;

    @SerializedName("nbrHeurs")
    private int nbrHeurs = 0;

    @SerializedName("line")
    private String line;

    @SerializedName("date")
    private String date;

    @SerializedName("createdAt")
    private Date createdAt;

    public Presence() {
    }

    public Presence(String idPerson, String nomPerson, String prenomPerson, int matriculePerson, String functionPerson, String etat, int nbrHeurs, String line) {
        this.idPerson = idPerson;
        this.nomPerson = nomPerson;
        this.prenomPerson = prenomPerson;
        this.matriculePerson = matriculePerson;
        this.functionPerson = functionPerson;
        this.etat = etat;
        this.nbrHeurs = nbrHeurs;
        this.line = line;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public String getNomPerson() {
        return nomPerson;
    }

    public String getPrenomPerson() {
        return prenomPerson;
    }

    public int getMatriculePerson() {
        return matriculePerson;
    }

    public String getFunctionPerson() {
        return functionPerson;
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

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public void setNomPerson(String nomPerson) {
        this.nomPerson = nomPerson;
    }

    public void setPrenomPerson(String prenomPerson) {
        this.prenomPerson = prenomPerson;
    }

    public void setMatriculePerson(int matriculePerson) {
        this.matriculePerson = matriculePerson;
    }

    public void setFunctionPerson(String functionPerson) {
        this.functionPerson = functionPerson;
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

    @Override
    public String toString() {
        return "Presence{" +
                "id='" + id + '\'' +
                ", idPerson='" + idPerson + '\'' +
                ", nomPerson='" + nomPerson + '\'' +
                ", prenomPerson='" + prenomPerson + '\'' +
                ", matriculePerson=" + matriculePerson +
                ", functionPerson='" + functionPerson + '\'' +
                ", etat='" + etat + '\'' +
                ", nbrHeurs=" + nbrHeurs +
                ", line='" + line + '\'' +
                '}';
    }

    @Override
    public int compareTo(Presence presence) {
        return this.getLine().compareTo(presence.line);
    }
}
