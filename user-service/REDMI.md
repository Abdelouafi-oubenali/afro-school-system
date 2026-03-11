# User Service - Endpoints via API Gateway

## Base URL

- API Gateway: `http://localhost:8080`
- User service via discovery route: `http://localhost:8080/users-service`

Le gateway est configure avec:

- `spring.cloud.gateway.discovery.locator.enabled=true`
- `spring.cloud.gateway.discovery.locator.lower-case-service-id=true`

Donc les endpoints du `user-service` sont appeles avec le prefixe `/users-service`.

## Auth endpoints

- `POST /users-service/api/auth/login`
- `POST /users-service/api/auth/refresh`

## User endpoints

### Test / utilitaire

- `GET /users-service/api/users/test`
- `POST /users-service/api/users/create-admin/test`
- `GET /users-service/api/users/list`

### Admin

- `POST /users-service/api/users/admins`
- `GET /users-service/api/users/admins`
- `GET /users-service/api/users/admins/{id}`
- `PUT /users-service/api/users/admins/{id}`
- `DELETE /users-service/api/users/admins/{id}`

### Enseignant

- `POST /users-service/api/users/enseignent`
- `GET /users-service/api/users/enseignent`
- `GET /users-service/api/users/enseignent/{id}`
- `PUT /users-service/api/users/enseignent/{id}`
- `DELETE /users-service/api/users/enseignent/{id}`

### Eleve

- `POST /users-service/api/users/eleve`
- `GET /users-service/api/users/eleve`
- `GET /users-service/api/users/eleve/{id}`
- `PUT /users-service/api/users/eleve/{id}`
- `DELETE /users-service/api/users/eleve/{id}`
- `GET /users-service/api/users/{classId}/class`
- `GET /users-service/api/users/class/{classId}`
- `POST /users-service/api/users/eleve/{id}/assign-class/{classId}`

### Parent

- `POST /users-service/api/users/parent`
- `GET /users-service/api/users/parent`
- `GET /users-service/api/users/parent/{id}`
- `PUT /users-service/api/users/parent/{id}`
- `DELETE /users-service/api/users/parent/{id}`

### Endpoints inter-services (class-service)

- `GET /users-service/api/users/students/{id}/classe`
- `PUT /users-service/api/users/students/{id}/classe/{classeId}`
- `PUT /users-service/api/users/enseignants/{id}/classe/{classeId}`
- `GET /users-service/api/users/enseignants/class/{classeId}`
- `GET /users-service/api/users/enseignents/{id}`

## Exemple complet

- Login: `POST http://localhost:8080/users-service/api/auth/login`
- Liste des users: `GET http://localhost:8080/users-service/api/users/list`
