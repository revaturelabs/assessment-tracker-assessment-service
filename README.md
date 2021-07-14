# API routes

- GET: "/assessments": gets all assessments
- POST: "/assessments": creates an assessment
- GET: "/associates/:associateId/assessments": gets all assessments for an associate
- GET: "/batches/:batchId/weeks/:weekId/assessments": gets all assessments in a batch for a certain week
- PATCH: "/weight/:weight/assessments/:assessmentId": updates the weight of an assessment
- PATCH: "/types/:typeId/assessments/:assessmentId": updates the assessment type with a type id

- POST: "/grades": creates a grade
- GET: "/assessments/:assessmentId/associates/:associateId/grades": gets a grade for a specific associate and assessment
- PUT: "/grades": updates a grade
- DELETE: "/grades/:gradeId": deletes a grade
- GET: "/associates/:associateId/week/:weekId/grades": gets the week grades for an associate
- GET: "/assessments/:assessmentId/grades/average": gets the average grade for an assessment

- POST: "/types": creates an assessment type
- GET: "/types": gets all assessment types
- GET: "/types/:typeId" gets a specific assessment type
- PUT: "/types": updates an assessment type
- DELETE: "/types/:typeId" deletes an assessment type

- GET: "/categories": gets all categories
- POST: "/categories": creates a category
- GET: "/categories/:id": gets a category by id
- PATCH: "/categories": updates a category
- DELETE: "/categories/:id": deletes a category
