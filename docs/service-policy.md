# Service Policy

## Confirmed Policies

- New spaces are created with adminStatus=PENDING and isActive=true.
- Space deletion is soft deletion by setting deletedAt and isActive=false, then deleting related image and schedule rows.
- Only the host owner may update or delete a space or mutate schedules.
- Public space lookup filters by deletedAt only in visible repository calls.
- Space images are stored as image URL rows by spaceId.
- Schedule creation defaults isBookable to true.
- Schedule startTime and endTime are required and startTime must be before endTime.
- Schedule update preserves omitted values and validates the resulting time range.
- Internal admin-status endpoints (GET/PATCH /internal/spaces/{space-id}/admin-status) let admins check and change a space's adminStatus.
- Admin-status endpoints require a JWT whose role is ADMIN (Role enum), validated in the service layer; otherwise 403.
- adminStatus can be changed only while the current status is PENDING; otherwise 400.
- Admin-status lookup/change on a deleted or missing space returns 404.
- Internal admin space endpoints (GET /internal/spaces, GET /internal/spaces/{space-id}) let admins list all non-deleted spaces and look up a non-deleted space's detail regardless of approval status or active state, without filters.
- Internal admin space endpoints require a JWT whose role is ADMIN (Role enum), validated in the service layer; otherwise 403.
- Internal admin space detail lookup on a deleted or missing space returns 404.

## Validation Rules

Visible validation rules are documented from DTO annotations, entity methods, and service methods only. Any validation behavior not present in code is Needs confirmation.

## Authorization Rules

JWT stateless security is configured. GET /spaces, GET /spaces/*, and GET /spaces/*/schedule are permitAll; create/update/delete and owner schedule mutations are authenticated. The matcher order may make GET /spaces/me match the public /spaces/* rule; this requires confirmation/testing.

Internal admin-status and admin space lookup endpoints are permitAll at the SecurityConfig level; the ADMIN role check is performed in the service layer using the JWT role claim.

## Creation Policy

Creation behavior is documented only where visible in service or entity factory methods.

## Update Policy

Update behavior is documented only where visible in service or entity update methods.

## Deletion Policy

Deletion behavior is documented only where visible in service or entity delete methods.

## State Transition Rules

State transitions are documented only where visible in entity or service methods. Missing transitions are Needs confirmation.

## Exception Cases

The service throws IllegalArgumentException and SecurityException directly. No @ControllerAdvice or global exception response mapper is visible. HTTP status mapping for these exceptions is Needs confirmation unless explicitly handled in code.

## API Behavior Policy

- API behavior must be documented in `API_SPEC.yaml`.
- If API behavior changes, `API_SPEC.yaml` must be updated in the same PR.

## Needs Confirmation

- Whether public lists should filter only APPROVED spaces is Needs confirmation.
- Schedule overlap and booking conflict policy are not visible.
- Image upload/storage integration is not visible; only image URLs are persisted.
- Security matcher behavior for GET /spaces/me needs confirmation.
