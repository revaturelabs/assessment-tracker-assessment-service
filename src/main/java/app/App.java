package app;

import dao.AssessmentDAOImpl;
import dao.AssessmentTypeDAOImpl;
import dao.GradeDAOImpl;
import io.javalin.Javalin;
import controllers.AssessmentController;
import controllers.AssessmentTypeController;
import controllers.CategoryController;
import controllers.GradeController;
import dao.CategoryDAOImpl;
import services.*;

import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;


public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.enableDevLogging();
            config.registerPlugin(getConfiguredOpenApiPlugin());
        });
        establishRoutes(app);
        app.start(7001);
    }

    private static void establishRoutes(Javalin app) {

        CategoryController categoryController = new CategoryController(new CategoryServiceImpl(new CategoryDAOImpl()));
        AssessmentTypeController assessmentTypeController = new AssessmentTypeController(new AssessmentTypeServiceImpl(new AssessmentTypeDAOImpl()));
        GradeController gradeController = new GradeController(new GradeServiceImpl(new GradeDAOImpl()));
        AssessmentController ac = new AssessmentController(new AssessmentServiceImpl(new AssessmentDAOImpl()));
        app.get("/Testing", context -> context.result("Testing"));

        // EndPoints
        app.get("/assessments", ac.getAssessments);
        app.post("/assessments", ac.createAssessment);
        app.get("/associates/:associateId/assessments", ac.getAssessmentsByTraineeId);
        app.get("/batches/:batchId/weeks/:weekId/assessments", ac.getBatchWeek);
        app.patch("/weight/:weight/assessments/:assessmentId", ac.adjustWeight);
        app.patch("/types/:typeId/assessments/:assessmentId",ac.assignAssessmentType);

        app.post("/grades", gradeController.createGrade);
        app.get("/assessments/:assessmentId/associates/:associateId/grades", gradeController.getGradeForAssociateAssessment);
        app.put("/grades", gradeController.updateGrade);
        app.delete("/grades/:gradeId", gradeController.deleteGrade);
        app.get("/associates/:associateId/week/:weekId/grades", gradeController.getGradesForWeek);
        app.get("/assessments/:assessmentId/grades/average", gradeController.getAverageGrade);
        app.get("/batches/:batchId/week/:weekId/grades", gradeController.getGradesForBatchWeek);
        app.get("/batches/:batchId/week/:weekId/grades/average", gradeController.getAvgForBatchWeek);

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
        app.post("/assessments/:assessmentId/categories/:categoryId", categoryController.addCategoryToAssessment);
        app.get("/assessments/:assessmentId/categories", categoryController.getCategoriesForAssessment);
        app.delete("/assessments/:assessmentId/categories/:categoryId", categoryController.deleteCategoryForAssessment);
    }

    private static OpenApiPlugin getConfiguredOpenApiPlugin() {
        Info info = new Info().version("1.0").description("User API");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("app")
                .path("/swagger-docs") // endpoint for OpenAPI json
                .swagger(new SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
                .reDoc(new ReDocOptions("/redoc")); // endpoint for redo
        return new OpenApiPlugin(options);
    }

}
