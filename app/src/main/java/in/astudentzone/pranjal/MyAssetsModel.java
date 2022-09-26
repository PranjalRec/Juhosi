package in.astudentzone.pranjal;

public class MyAssetsModel {
    String policyName, policyNumber, dueDate, premium;

    public MyAssetsModel(String policyName, String policyNumber, String dueDate, String premium) {
        this.policyName = policyName;
        this.policyNumber = policyNumber;
        this.dueDate = dueDate;
        this.premium = premium;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
}
