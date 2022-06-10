package ma.premo.productionmanagment.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line implements Comparable<Line> {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("center")
    @Expose
    private int center;

    public Line(String designation, int center) {
        this.designation = designation;
        this.center = center;
    }

    public Line() {
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public int getCenter() {
        return center;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCenter(int center) {
        this.center = center;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", designation='" + designation + '\'' +
                ", center=" + center +
                '}';
    }

    @Override
    public int compareTo(Line line) {
        return this.designation.compareTo(line.designation);
    }
}
