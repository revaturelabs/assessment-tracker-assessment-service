package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import models.AssessmentType;
import models.Grade;
import org.junit.Assert;
import util.ConnectionDB;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateAssessmentType {

    private AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private AssessmentType testAssessmentType = new AssessmentType(0, "testAssessmentType", 50);

    @Test
    public void testCreateAssessmentType() {
        AssessmentType assessmentType = assessmentDAO.createAssessmentType(testAssessmentType.getName(),testAssessmentType.getDefaultWeight());
        testAssessmentType = assessmentType;
        Assert.assertTrue(assessmentType.getTypeId() != 0);
    }

}
