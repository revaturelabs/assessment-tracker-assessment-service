package models;

public class Assessment {
    private int assessmentId;
    private String assessmentTitle;
    private int typeId;
    private int batchId;
    private int weekId;
    private int assessmentWeight;
    private int categoryId;

    public Assessment() {
        super();
        setDefault();
    }

    // //DON'T USE THIS CONSTRUCTOR
    // public Assessment(int assessmentId, String assessmentTitle, int typeId, int batchId, int weekId,
    //         int assessmentWeight, int categoryId, List<String> notes) throws InvalidValue {
    //     super();
    //     setDefault();
    //     setAssessmentId(assessmentId);
    //     setAssessmentTitle(assessmentTitle);
    //     setTypeId(typeId);
    //     setBatchId(batchId);
    //     setWeekId(weekId);
    //     setAssessmentWeight(assessmentWeight);
    //     setCategoryId(categoryId);
    //     this.notes = notes;
    // }

    //USE THIS CONSTRUCTOR
    public Assessment(int assessmentId, String assessmentTitle, int typeId, int batchId, int weekId,
                      int assessmentWeight, int categoryId){
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

    public int getWeekId() {
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
        weekId = 0;
        assessmentWeight = 0;
        categoryId = 0;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public void setAssessmentTitle(String assessmentTitle){
        this.assessmentTitle = assessmentTitle;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public void setWeekId(int weekId){
        this.weekId = weekId;
    }

    public void setAssessmentWeight(int assessmentWeight){
        this.assessmentWeight = assessmentWeight;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    // public void setNotes(List<String> notes) {
    //     this.notes = notes;
    // }

    @Override
    public String toString() {
        return "Assessment [assessmentId=" + assessmentId + ", assessmentTitle=" + assessmentTitle
                + ", assessmentWeight=" + assessmentWeight + ", batchId=" + batchId + ", categoryId=" + categoryId
                + ", typeId=" + typeId + ", weekId=" + weekId + "]";
    }

}
