package ma.premo.productionmanagment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Produit {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("line")
    @Expose
    private Line line;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("tc")
    @Expose
    private double tc;

    public Produit(Line line, String designation, String reference) {
        this.line = line;
        this.designation = designation;
        this.reference = reference;
    }

    public Produit() {
    }

    public double getTc() {
        return tc;
    }


    public void setTc(double tc) {
        this.tc = tc;
    }

    public String getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public String getDesignation() {
        return designation;
    }

    public String getReference() {
        return reference;
    }

    public void setLine(Line line) {
        this.line = line;
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
                ", idLigne='" + line.toString() + '\'' +
                ", designation='" + designation + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.getReference();

    }
}
