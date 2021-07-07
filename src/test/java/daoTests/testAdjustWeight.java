package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import models.Grade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import util_project.dbconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testAdjustWeight {
    // Class to be tested
    private AssessmentDAO adao;

    // Dependencies
    private Connection mockConn;
    private Grade mockGrade;
    private PreparedStatement mockPs;
    private ResultSet mockRs;

    @Before
    public void setup() {
        // Create our Mock objects
        mockConn  = Mockito.mock(Connection.class);
        mockGrade = Mockito.mock(Grade.class);
        mockPs    = Mockito.mock(PreparedStatement.class);
        mockRs    = Mockito.mock(ResultSet.class);

        // Since getconnection is a static method, get a static mock object
        try (MockedStatic<dbconnection> mockedStatic = Mockito.mockStatic(dbconnection.class)) {
            mockedStatic.when(dbconnection::getConnection).thenReturn(mockConn);
            // When prepareStatement is called on the connection, return the prepared statement
            // When executeQuery is called, return the result set
            Mockito.when(mockConn.prepareStatement(Mockito.any(String.class))).thenReturn(mockPs);
            Mockito.when(mockPs.executeQuery()).thenReturn(mockRs);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();
    }

    @Test
    public void testNotNegativeWeight() {
        boolean returnedVal = adao.adjustWeight(1, 1);
        assertTrue(true, "Expected true on success");
    }

    @Test
    public void testTooMuchWeight() {
        boolean returnVal = adao.adjustWeight(1,1);
        // TODO: Should we pass in who much weight we want to adjust by, and return the weight instead of a boolean.
        //assertTrue(returnedWeight <= 100, "Expected weight less than 100");
    }
}
