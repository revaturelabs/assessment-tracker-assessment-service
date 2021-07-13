package app;

import dao.AssessmentDAOImpl;
import dao.AssessmentTypeDAOImpl;
import dao.GradeDAOImpl;
import io.javalin.Javalin;
import controllers.AssessmentController;
import controllers.AssessmentTypeController;
import controllers.CategoryController;
import dao.CategoryDAOImpl;
import services.*;

public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.enableDevLogging();
        });
        establishRoutes(app);
        app.start(7001);
    }

    private static void establishRoutes(Javalin app) {
        // Need a Repo
        // AssessmentRepo ar= new AssessmentRepo();
        // Need a Service
        CategoryController categoryController = new CategoryController(new CategoryServiceImpl(new CategoryDAOImpl()));
        AssessmentTypeController assessmentTypeController = new AssessmentTypeController(new AssessmentTypeServiceImpl(new AssessmentTypeDAOImpl()));
        AssessmentService as = new AssessmentServiceImpl(new AssessmentDAOImpl());
        AssessmentTypeService ats = new AssessmentTypeServiceImpl(new AssessmentTypeDAOImpl());
        GradeService gs = new GradeServiceImpl(new GradeDAOImpl());
        app.get("/Testing", context -> context.result("Testing"));

        // EndPoints
        AssessmentController ac = new AssessmentController(as, ats, gs);
        app.get("/assessments", ac.getAssessments);
        app.post("/assessments", ac.createAssessment);
        app.get("/assessments/:traineeId", ac.getAssessmentsByTraineeId);
        app.get("/batches/:batchId/assessments", ac.getBatchWeek); //batches/5/assessments?week=4
        app.put("/assessments/:assessmentId/weight", ac.adjustWeight); //assessments/3/weight?weight=55
        app.put("/assessments/:assessmentId/type/:typeId",ac.assignAssessmentType);

        app.get("/assessments/:assessmentId/grade", ac.getGradeForAssociate); //assessments/3/grade?associateId=3
        app.get("/grades", ac.getGradesForWeek); //grades?traineeId=1&week=3
        app.put("/grades", ac.insertGrade);
        app.get("/grades/average", ac.getAverageGrade);
        //app.get("/notes/:id/:weekid/", ac.getNotesForTrainee);

        app.post("types", assessmentTypeController.createAssessmentType);
        app.get("types", assessmentTypeController.getAssessmentTypes);
        app.get("types/:typeId", assessmentTypeController.getAssessmentTypeById);
        app.put("types", assessmentTypeController.updateAssessmentType);
        app.delete("types/:typeId", assessmentTypeController.deleteAssessmentType);

        app.post("/categories", categoryController.createCategory);
        app.get("/categories", categoryController.getCategories);
        app.get("/categories/:categoryId", categoryController.getCategoryById);
        app.put("/categories", categoryController.updateCategory);
        app.delete("/categories/:categoryId", categoryController.deleteCategory);
    }

}
