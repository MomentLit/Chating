# Architecture Guide

## Current Architecture

Space is a single-module Gradle service.

- Technology: Java 21, Spring Boot 3.4.5
- Main package: `com.example.space`
- Main responsibility: Idle Space (유휴공간) listing and owner-managed schedules based on visible code.
- Security: JWT stateless security is configured. GET /spaces, GET /spaces/*, and GET /spaces/*/schedule are permitAll; create/update/delete and owner schedule mutations are authenticated. The matcher order may make GET /spaces/me match the public /spaces/* rule; this requires confirmation/testing.

## Layer Structure

- Controller layer: SpaceController handles all visible /spaces APIs.
- Service layer: SpaceService owns transaction boundaries and use-case orchestration for spaces, images, and schedules.
- Repository layer: Repositories are persistence boundaries and use ID values rather than JPA associations between Space, SpaceImage, and SpaceSchedule.
- DTO layer: Request records are under dto.request; response records are under dto.response. ApiResponse<T> wraps successful non-204 responses.
- Entity/domain layer: Space soft-deletes with deletedAt and isActive=false. New spaces start with adminStatus=PENDING and isActive=true. SpaceSchedule validates start/end time order.
- Exception handling: The service throws IllegalArgumentException and SecurityException directly. No @ControllerAdvice or global exception response mapper is visible.

## Layer Responsibilities

Controllers should focus on HTTP request and response handling.
Services should contain business logic.
Repositories should handle persistence.
Entities should not be returned directly as API responses.
Request DTOs and Response DTOs should be separated.
Existing architecture must not be changed without explicit instruction.
If API behavior changes, API_SPEC.yaml must be updated in the same PR.

## Transaction Boundary

Transaction boundaries are defined in service classes when `@Transactional` is present. If a service has no transaction annotations, transaction policy is Needs confirmation.

## API Documentation Responsibility

`API_SPEC.yaml` is the source of truth for the service API contract. Any API behavior change must update `API_SPEC.yaml` in the same PR.

## Forbidden Patterns

- Do not move business logic into controllers.
- Do not perform persistence directly from controllers.
- Do not expose JPA entities as API responses when response DTOs exist.
- Do not introduce a different architecture without explicit approval.
- Do not add cross-service assumptions that are not visible from code.
- Do not change build files, dependencies, or application configuration as part of ordinary feature work unless explicitly approved.
