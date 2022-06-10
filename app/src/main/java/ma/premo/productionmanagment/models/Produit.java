package ma.premo.productionmanagment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Produit {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("idLigne")
    @Expose
    private String idLigne;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("reference")
    @Expose
    private String reference;

    public Produit(String idLigne, String designation, String reference) {
        this.idLigne = idLigne;
        this.designation = designation;
        this.reference = reference;
    }

    public Produit() {
    }

    public String getId() {
        return id;
    }

    public String getIdLigne() {
        return idLigne;
    }

    public String getDesignation() {
        return designation;
    }

    public String getReference() {
        return reference;
    }

    public void setIdLigne(String idLigne) {
        this.idLigne = idLigne;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String toString2() {
        return "Produit{" +
                "id='" + id + '\'' +
                ", idLigne='" + idLigne + '\'' +
                ", designation='" + designation + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.getDesignation();

    }
}
