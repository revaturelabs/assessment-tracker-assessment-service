package daoTests;

import dao.AssessmentDAOImpl;
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
    // Class to be tested
    private AssessmentDAOImpl adao;

    // Dependencies
    private Connection mockConn;
    private Grade mockGrade;
    private PreparedStatement mockPs;
    private ResultSet mockRs;

    @Before
    public void setup(){
        // Create our Mock objects
        mockConn  = Mockito.mock(Connection.class);
        mockGrade = Mockito.mock(Grade.class);
        mockPs    = Mockito.mock(PreparedStatement.class);
        mockRs    = Mockito.mock(ResultSet.class);

        // Since getconnection is a static method, get a static mock object
        try (MockedStatic<ConnectionDB> mockedStatic = Mockito.mockStatic(ConnectionDB.class)) {
            mockedStatic.when(ConnectionDB::getConnection).thenReturn(mockConn);


            // When prepareStatement is called on the connection, return the prepared statement
            // When executeQuery is called, return the result set
            Mockito.when(mockConn.prepareStatement(Mockito.any(String.class))).thenReturn(mockPs);
            Mockito.when(mockPs.executeQuery()).thenReturn(mockRs);
        }catch (SQLException e){
            e.printStackTrace();
        }
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();
    }

    @Test
    public void testNotNull() {
        Boolean returnedAssessmentType = adao.assignAssessmentType(1, 1);
        assertNotNull(returnedAssessmentType);
    }

    @Test
    public void testEmptyString(){
        // TODO: Should we return the string os the assessment type here instead of a boolean
        //Boolean returnedAssessmentType = adao.assignAssessmentType(1);
        //assertTrue(!returnedAssessmentType.isEmpty());
    }

}
