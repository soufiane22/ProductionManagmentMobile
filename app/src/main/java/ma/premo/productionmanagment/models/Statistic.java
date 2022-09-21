package ma.premo.productionmanagment.models;


public class Statistic {
    String leaderName;
    int sumOutput;
    float productivity;
    float tauxScrap;

    public Statistic() {
    }

    public String getLeaderName() {
        return leaderName;
    }

    public int getSumOutput() {
        return sumOutput;
    }

    public float getProductivity() {
        return productivity;
    }

    public float getTauxScrap() {
       //tauxScrap =  Float.parseFloat(String.format("%.2f",tauxScrap));
       System.out.println("taux scrap "+tauxScrap);
        return tauxScrap;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setSumOutput(int sumOutput) {
        this.sumOutput = sumOutput;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    public void setTauxScrap(float tauxScrap) {
        this.tauxScrap = tauxScrap;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "leaderName='" + leaderName + '\'' +
                ", sumOutput=" + sumOutput +
                ", productivity=" + productivity +
                ", tauxScrap=" + tauxScrap +
                '}';
    }
}


