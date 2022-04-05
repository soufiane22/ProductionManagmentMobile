package ma.premo.productionmanagment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Of {
    @SerializedName("id")
    @Expose
    private String id ;
    @SerializedName("reference")
    @Expose
    private String reference ;
    @SerializedName("codeProduit")
    @Expose
    private String codeProduit ;
    @SerializedName("dateDebut")
    @Expose
    private String dateDebut;
    @SerializedName("dateFin")
    @Expose
    private String dateFin;
    @SerializedName("quantiteDemande")
    @Expose
    private int quantiteDemande;
    @SerializedName("quantiteFabriqué")
    @Expose
    private int quantiteFabriqué;

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getCodeProduit() {
        return codeProduit;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public int getQuantiteDemande() {
        return quantiteDemande;
    }

    public int getQuantiteFabriqué() {
        return quantiteFabriqué;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setQuantiteDemande(int quantiteDemande) {
        this.quantiteDemande = quantiteDemande;
    }

    public void setQuantiteFabriqué(int quantiteFabriqué) {
        this.quantiteFabriqué = quantiteFabriqué;
    }

    public Of(String reference, String codeProduit, String dateDebut, String dateFin, int quantiteDemande, int quantiteFabriqué) {
        this.reference = reference;
        this.codeProduit = codeProduit;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.quantiteDemande = quantiteDemande;
        this.quantiteFabriqué = quantiteFabriqué;
    }
    public Of(){

    }

    @Override
    public String toString() {
        return "Of{" +
                "id='" + id + '\'' +
                ", reference='" + reference + '\'' +
                ", codeProduit='" + codeProduit + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", quantiteDemande=" + quantiteDemande +
                ", quantiteFabriqué=" + quantiteFabriqué +
                '}';
    }
}
