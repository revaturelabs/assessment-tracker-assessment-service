package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import models.Assessment;
import models.AssessmentType;
import models.Grade;
import org.junit.After;
import org.junit.Assert;
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

    private AssessmentDAO assessmentDAO;
    private AssessmentType assessType;
    private Assessment assessment;

    @Before
    public void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        try {
            Assessment sampleAssessment1 = new Assessment(0, "Test Assessment 1", 1, 1, "1", 30, 2);
            assessment = assessmentDAO.createAssessment(sampleAssessment1);
            assessType = assessmentDAO.createAssessmentType("QC", 100);
        } catch(InvalidValue e) {
            //BUG - Display something if we get here
        }
    }

    @Test
    public void testAssignAssessmentTypeValid() {
        try {
            boolean result = assessmentDAO.assignAssessmentType(assessment.getAssessmentId(), assessType.getTypeId());
            Assert.assertTrue(result);
        } catch(SQLException e) {
            fail();
        }
    }

    @Test
    public void testAssignAssessmentInvalidAssessId() {
        try {
            assessmentDAO.assignAssessmentType(-1, assessType.getTypeId());
            fail();
        } catch(SQLException e) {
            //Success
        }
    }

    @Test
    public void testAssignAssessmentInvalidTypeId() {
        try {
            assessmentDAO.assignAssessmentType(assessment.getAssessmentId(), -1);
            fail();
        } catch(SQLException e) {
            //Success
        }
    }

    @After
    public void cleanup() {
        try {
            assessmentDAO.deleteAssessment(assessment.getAssessmentId());
            //BUG - Delete assessmentType
        } catch(ResourceNotFound e) {
            //BUG - Shouldnt get here
        }
    }

}
