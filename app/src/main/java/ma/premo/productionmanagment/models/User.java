package ma.premo.productionmanagment.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;

public class User {

    @SerializedName("id")
    private String id;
    @SerializedName("nom")
    private String nom;
    @SerializedName("prenom")
    private String prenom;
    @SerializedName("tele")
    private String tele;
    @SerializedName("fonction")
    private String fonction;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("matricule")
    private int matricule;
    @SerializedName("line")
    private String line;



    private Boolean selected =false ;

    public User(String nom, String prenom, String tele, String fonction, int matricule,Boolean selected, String password, String email ) {
        this.nom = nom;
        this.prenom = prenom;
        this.tele = tele;
        this.fonction = fonction;
        this.password = password;
        this.email = email;
        this.matricule = matricule;
        this.selected = selected;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTele() {
        return tele;
    }

    public String getFonction() {
        return fonction;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getMatricule() {
        return matricule;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    public String getUsername() {
        return username;
    }


    public String toString2() {
        return "User{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tele='" + tele + '\'' +
                ", fonction='" + fonction + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", matricule=" + matricule +
                ", line='" + line + '\'' +
                ", selected=" + selected +
                '}';
    }



    @Override
    public String toString() {
        return this.getNom() + " " + getPrenom();
    }


}
