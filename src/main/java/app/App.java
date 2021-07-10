package app;

import io.javalin.Javalin;
import controllers.AssessmentController;
import controllers.CategoryController;
import dao.CategoryDAOImpl;
import services.AssessmentService;
import services.CategoryServiceImpl;

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
        AssessmentService as = new AssessmentService();
        app.get("/Testing", context -> context.result("Testing"));

        // EndPoints
        AssessmentController ac = new AssessmentController(as);
        app.get("/assessments", ac.getAssessments);
        app.get("/grade/:associateId/:assessmentId", ac.getGradeForAssociate);
        app.get("/assessments/:id/", ac.getAssessmentsByTraineeId);
        app.get("/grades/:id/:weekid", ac.getGradesForWeek);
        app.get("/assessments/batch/:id/:weekid", ac.getBatchWeek);
        app.get("/notes/:id/:weekid/", ac.getNotesForTrainee);
        app.post("/assessments", ac.createAssessment);
        app.put("/grades/", ac.insertGrade);
        app.put("/assessments/weight/:assessmentId/:weight", ac.adjustWeight);
        app.post("/types", ac.createAssessmentType);
        app.put("/assessments/type/:assessmentId/:typeId",ac.assignAssessmentType);

        app.get("/categories", categoryController.getCategories);
        app.post("/category", categoryController.createCategory);
        app.get("/category/:id", categoryController.getCategoryById);
        app.patch("/category", categoryController.updateCategory);
        app.delete("/category/:id", categoryController.deleteCategory);
    }

}
