package ma.premo.productionmanagment.models;

public class DeclarationPresence {
    private String group;
    private String date;
    private String leader;
    private String engineer;
    private int sumOperators;
    private int totalHours ;

    public DeclarationPresence(String group, String date, String leader, String engineer, int sumOperators, int totalHours) {
        this.group = group;
        this.date = date;
        this.leader = leader;
        this.engineer = engineer;
        this.sumOperators = sumOperators;
        this.totalHours = totalHours;
    }

    public DeclarationPresence() {
    }

    public String getGroup() {
        return group;
    }

    public String getDate() {
        return date;
    }

    public String getLeader() {
        return leader;
    }

    public String getEngineer() {
        return engineer;
    }

    public int getSumOperators() {
        return sumOperators;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public void setSumOperators(int sumOperators) {
        this.sumOperators = sumOperators;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public String toString() {
        return "DeclarationPresence{" +
                "group='" + group + '\'' +
                ", date='" + date + '\'' +
                ", leader='" + leader + '\'' +
                ", engineer='" + engineer + '\'' +
                ", sumOperators=" + sumOperators +
                ", totalHours=" + totalHours +
                '}';
    }
}
