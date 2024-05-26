import java.io.Serializable;

public class Participant implements Serializable {
    private String name;
    private String lastName;
    private String workplace;
    private String reportTitle;
    private String email;

    public Participant(String name, String lastName, String workplace, String reportTitle, String email) {
        this.name = name;
        this.lastName = lastName;
        this.workplace = workplace;
        this.reportTitle = reportTitle;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}