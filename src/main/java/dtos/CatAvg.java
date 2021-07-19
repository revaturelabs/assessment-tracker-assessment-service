package dtos;

public class CatAvg {
    private int assessmentId;
    private double averageScore;
    private String title;

    public CatAvg(int assessmentId, double averageScore, String title) {
        this.assessmentId = assessmentId;
        this.averageScore = averageScore;
    }

    public CatAvg() {
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

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
}
