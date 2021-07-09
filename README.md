# assessment-tracker-assessment-service

# API routes
- "/assessments": gets all assessments
- "/grade/:associateId/:assessmentId": gets a grade for given assessment with "assessmentId" for an associate with "associateId"
- "/assessments/:id/": gets all assessments for an associate/trainee with "id"
- "/grades/:id/:weekid": gets all grades for a trainee with "id" in week "weekid"
- "/assessments/batch/:id/:weekid": gets all assessments for batch with "id" and week with "weekid"
- "/notes/:id/:weekid/": gets all notes for trainee with "id" in week with "weekid"
- POST:"/assessments": creates an assessment
- PUT:"/grades/": updates a grade
- PUT:"/assessments/weight/:assessmentId/:weight": changes the weight of an assessment with "assessmentId"
- POST:"/types": creates an assessment type
- PUT: "/assessments/type/:assessmentId/:typeId": changes the type of an assessment with "assessmentId" to "typeId"
