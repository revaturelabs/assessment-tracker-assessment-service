# assessment-tracker-assessment-service

# API routes
- GET: "/assessments": gets all assessments
- POST: "/assessments": creates an assessment
- GET: "/assessments/:traineeId": gets all assessments for a trainee
- GET: "/batches/:batchId/assessments": gets all assessments in a batch for a certain week *QUERY PARAMS*
  -  EX. batches/5/assessments?week=4
- PUT: "/assessments/:assessmentId/weight": updates the weight of an assessment *QUERY PARAMS*
  - EX. assessments/3/weight?weight=55
- PUT: "/assessments/:assessmentId/type/:typeId": updates the assessment type with a type id
- GET: "/assessments/:assessmentId/grade": gets a grade for an associate/trainee from an assessment id *QUERY PARAMS*
  -  EX. assessments/3/grade?associateId=3
- GET: "/grades": gets grades for a trainee in a certain week *QUERY PARAMS*
  - EX. grades?traineeId=1&week=3
- PUT: "/grades": updates a grade
  - Note: If the assessment already exists, this route will update the grade for it. If the assessment doesn't exist, it will create a new one with the grade.
  - It works as a put/post depending on the body


- POST: "/types": creates an assessment type

- GET: "/categories": gets all categories
- POST: "/category": creates a category
- GET: "/category/:id": gets a category by id
- PATCH: "/category": updates a category
- DELETE: "/category/:id": deletes a category
