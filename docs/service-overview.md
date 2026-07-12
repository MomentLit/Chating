# Service Overview

## Service Name

Space

## Service Responsibility

Space creation, listing, detail lookup, update, soft deletion, owner space listing, schedule creation/listing/update/deletion, image URL persistence, internal admin approval status lookup/update, and internal admin space listing/detail lookup.

## Technology Stack

- Language: Java 21
- Framework: Spring Boot 3.4.5
- Build Tool: Gradle
- Database: PostgreSQL via Spring Data JPA; H2 test runtime dependency
- Other: Lombok is used where visible. JWT and Spring Security are used where security classes are visible.

## Main Package Structure

- Main package: `com.example.space`
- Controller: SpaceController handles all visible /spaces APIs.
- Service: SpaceService owns transaction boundaries and use-case orchestration for spaces, images, and schedules.
- Repository: Repositories are persistence boundaries and use ID values rather than JPA associations between Space, SpaceImage, and SpaceSchedule.
- DTO: Request records are under dto.request; response records are under dto.response. ApiResponse<T> wraps successful non-204 responses.
- Entity/domain: Space soft-deletes with deletedAt and isActive=false. New spaces start with adminStatus=PENDING and isActive=true. SpaceSchedule validates start/end time order.

## Main Domains

Space, SpaceImage, and SpaceSchedule entities with ApprovalStatus, SpaceCategory, and Role enums. SpaceImage and SpaceSchedule reference Space by spaceId value.

## Main Features

Space creation, listing, detail lookup, update, soft deletion, owner space listing, schedule creation/listing/update/deletion, image URL persistence, internal admin approval status lookup/update, and internal admin space listing/detail lookup.

## Main APIs

Visible APIs under /spaces. Full details are in API_SPEC.yaml.

## Data Access Structure

SpaceRepository, SpaceImageRepository, and SpaceScheduleRepository extend JpaRepository and use derived queries for soft-deleted spaces, owner lists, categories, names, images, and schedules.

## Exception Handling

The service throws IllegalArgumentException and SecurityException directly. No @ControllerAdvice or global exception response mapper is visible.

## Test Structure

Only SpaceApplicationTests context load test is visible.

## API Documentation

This service uses `API_SPEC.yaml` as the main API specification.
When API behavior changes, `API_SPEC.yaml` must be updated in the same PR.

## Development Notes

- Preserve the current single-module service structure.
- Follow the existing package and naming conventions.
- Keep controller, service, repository, entity, and DTO responsibilities separate where those layers exist.
- Do not add cross-service behavior unless it is visible in code or explicitly specified by an Issue.
- If implementation changes API behavior, update `API_SPEC.yaml` in the same PR.

## Needs Confirmation

- Whether public lists should filter only APPROVED spaces is Needs confirmation.
- Schedule overlap and booking conflict policy are not visible.
- Image upload/storage integration is not visible; only image URLs are persisted.
- Security matcher behavior for GET /spaces/me needs confirmation.
