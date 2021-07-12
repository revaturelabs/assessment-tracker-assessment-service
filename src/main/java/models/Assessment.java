package models;

import exceptions.InvalidValue;

public class Assessment {
    private int assessmentId;
    private String assessmentTitle;
    private int typeId;
    private int batchId;
    private String weekId;
    private int assessmentWeight;
    private int categoryId;

    public Assessment() {
        super();
        setDefault();
    }

    public Assessment(int assessmentId, String assessmentTitle, int typeId, int batchId, String weekId,
                      int assessmentWeight, int categoryId) throws InvalidValue {
        super();
        setDefault();
        setAssessmentId(assessmentId);
        setAssessmentTitle(assessmentTitle);
        setTypeId(typeId);
        setBatchId(batchId);
        setWeekId(weekId);
        setAssessmentWeight(assessmentWeight);
        setCategoryId(categoryId);
    }

    //Getters
    public int getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getBatchId() {
        return batchId;
    }

    public String getWeekId() {
        return weekId;
    }

    public int getAssessmentWeight() {
        return assessmentWeight;
    }

    public int getCategoryId() {
        return categoryId;
    }

    //Setters
    private void setDefault() {
        assessmentId = 0;
        assessmentTitle = "";
        typeId = 0;
        batchId = 0;
        weekId = "";
        assessmentWeight = 0;
        categoryId = 0;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public void setAssessmentTitle(String assessmentTitle) throws InvalidValue {
        if(assessmentTitle == null) throw new InvalidValue("Please set a valid title");
        this.assessmentTitle = assessmentTitle;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public void setWeekId(String weekId) throws InvalidValue {
        if(weekId == null) throw new InvalidValue("Please set a valid week");
        this.weekId = weekId;
    }

    public void setAssessmentWeight(int assessmentWeight) throws InvalidValue {
        if(assessmentWeight < 0 || assessmentWeight > 100)
            throw new InvalidValue(String.format("%d is not a valid weight", assessmentWeight));
        this.assessmentWeight = assessmentWeight;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void verifyAssessment() throws InvalidValue {
        //Ensure all properties are correct values
        setAssessmentId(this.assessmentId);
        setAssessmentTitle(this.assessmentTitle);
        setTypeId(this.typeId);
        setBatchId(this.batchId);
        setWeekId(this.weekId);
        setAssessmentWeight(this.assessmentWeight);
        setCategoryId(this.categoryId);
    }

    @Override
    public String toString() {
        return "Assessment [assessmentId=" + assessmentId + ", assessmentTitle=" + assessmentTitle
                + ", assessmentWeight=" + assessmentWeight + ", batchId=" + batchId + ", categoryId=" + categoryId
                + ", typeId=" + typeId + ", weekId=" + weekId + "]";
    }

}