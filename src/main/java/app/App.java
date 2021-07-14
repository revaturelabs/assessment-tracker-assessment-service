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

import static io.javalin.apibuilder.ApiBuilder.*;

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
        // Need a Repo
        // AssessmentRepo ar= new AssessmentRepo();
        // Need a Service
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

        // app.get("/assessments/:assessmentId/grade", ac.getGradeForAssociate); //assessments/3/grade?associateId=3
        // app.get("/grades", ac.getGradesForWeek); //grades?traineeId=1&week=3
        // app.put("/grades", ac.insertGrade);
        // app.get("/grades/average", ac.getAverageGrade);
        //app.get("/notes/:id/:weekid/", ac.getNotesForTrainee);

        app.post("/grades", gradeController.createGrade);
        app.get("/assessments/:assessmentId/associates/:associateId/grades", gradeController.getGradeForAssociateAssessment);
        app.put("/grades", gradeController.updateGrade);
        app.delete("/grades/:gradeId", gradeController.deleteGrade);
        app.get("/associates/:associateId/week/:weekId/grades", gradeController.getGradesForWeek);
        app.get("/assessments/:assessmentId/average/grades", gradeController.getAverageGrade);

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
