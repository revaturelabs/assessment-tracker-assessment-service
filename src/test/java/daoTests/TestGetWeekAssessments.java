package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import models.Assessment;

public class TestGetWeekAssessments {
    static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    static Assessment testGradeAssignment = new Assessment();

    static {
        try {
            assert testGradeAssignment != null;
            testGradeAssignment = new Assessment(0, "Test 1", 1, 3, "5", 100, 1);
        } catch (InvalidValue invalidValue) {
            invalidValue.printStackTrace();
        }
    }

    //Unimplemented


}
