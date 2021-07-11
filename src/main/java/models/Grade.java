package models;

import exceptions.InvalidValue;

public class Grade {
    private int gradeId;
    private int assessmentId;
    private int associateId;
    private double score;

    public Grade() {
        super();
        setDefault();
    }

    public Grade(int gradeId, int assessmentId, int associateId, double score) throws InvalidValue {
        super();
        setDefault();
        setGradeId(gradeId);
        setAssessmentId(assessmentId);
        setAssociateId(associateId);
        setScore(score);
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getAssociateId() {
        return associateId;
    }

    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) throws InvalidValue{
        if (score < 0 || score > 100)
            throw new InvalidValue("Grade must be between 0 and 100");

        this.score = score;
    }

    private void setDefault() {
        gradeId = 0;
        assessmentId = 0;
        associateId = 0;
        score = 0;
    }
    @Override
    public String toString() {
        return "Grade [assessmentId=" + assessmentId + ", gradeId=" + gradeId + ", score=" + score + ", traineeId="
                + associateId + "]";
    }

}
