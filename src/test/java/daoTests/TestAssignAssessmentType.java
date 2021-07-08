package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import models.Assessment;
import models.AssessmentType;
import models.Grade;
import util.ConnectionDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignAssessmentType {

    static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    static AssessmentType testAssessmentType = new AssessmentType(0,"QC", 100);


    @Test
    public void testNotNull() {
        Boolean returnedAssessmentType = assessmentDAO.assignAssessmentType(testAssessmentType.getTypeId(), testAssessmentType.getDefaultWeight());
        assertNotNull(returnedAssessmentType);
    }

    @Test
    public void testEmptyString(){
        boolean returnedAssessmentType = assessmentDAO.assignAssessmentType(0, 1);
        assertTrue(returnedAssessmentType);
    }

}
