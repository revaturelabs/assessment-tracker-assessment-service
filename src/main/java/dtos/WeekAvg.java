package dtos;

public class WeekAvg {
    private int assessmentId;
    private double averageScore;

    public WeekAvg(int assessmentId, double averageScore) {
        this.assessmentId = assessmentId;
        this.averageScore = averageScore;
    }

    public WeekAvg() {
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
