# API routes

- GET: "/assessments": gets all assessments
- POST: "/assessments": creates an assessment
- GET: "/assessments/associates/:associateId": gets all assessments for an associate
- GET: "/assessments/batches/:batchId/weeks/:weekId": gets all assessments in a batch for a certain week
- PATCH: "/assessments/:assessmentId/weight/:weight": updates the weight of an assessment
- PATCH: "/assessments/:assessmentId/types/:typeId": updates the assessment type with a type id

- POST: "/grades": creates a grade
- GET: "/grades/assessments/:assessmentId/associates/:associateId": gets a grade for a specific associate and assessment
- PUT: "/grades": updates a grade
- DELETE: "/grades/:gradeId": deletes a grade
- GET: "/grades/associates/:associateId/week": gets the week grades for an associate
- GET: "/grades/assessments/:assessmentId/average": gets the average grade for an assessment

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
