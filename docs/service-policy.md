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

## Chat Policies

- Chat room creation (POST /chat) sets the authenticated user as seller and the space owner as host.
- The space owner cannot create a chat room for their own space; this returns 400.
- If a chat room for the same space and seller already exists, creation returns the existing chat_room_id with 201.
- Space, host, and seller names are denormalized into the chat room at creation time and are not refreshed afterward.
- Space and user data are fetched via internal API clients (SpaceClient: GET /spaces/{space-id}, UserClient: GET /internal/users/{user-id}); client failures return 502.
- Chat message history is visible only to the chat room's host or seller; other users receive 403.
- WebSocket chat topic subscriptions are validated the same way; non-participants are rejected.
- Fetching message history (GET /chat/{chat-room-id}/messages) marks the counterpart's unread messages as read.
- Messages are stored with is_read=false; receiving a message over WebSocket does not mark it read.
- Sending a message to a chat room whose space is deleted is rejected with a bad request; room list and message history remain viewable.
- Real-time messaging uses STOMP over WebSocket as documented in docs/websocket-spec.md.

## Validation Rules

Visible validation rules are documented from DTO annotations, entity methods, and service methods only. Any validation behavior not present in code is Needs confirmation.

## Authorization Rules

JWT stateless security is configured. POST /chat, GET /chat, and GET /chat/*/messages require authentication; all other requests are permitAll. Unauthenticated requests to authenticated endpoints receive 403 with the globalResponse body (JwtAuthenticationEntryPoint).

WebSocket connections authenticate with a JWT passed in the STOMP CONNECT Authorization header; chat topic subscriptions are allowed only for chat room participants (validated in StompAuthChannelInterceptor via the service layer).

## Creation Policy

Creation behavior is documented only where visible in service or entity factory methods.

## Update Policy

Update behavior is documented only where visible in service or entity update methods.

## Deletion Policy

Deletion behavior is documented only where visible in service or entity delete methods.

## State Transition Rules

State transitions are documented only where visible in entity or service methods. Missing transitions are Needs confirmation.

## Exception Cases

GlobalExceptionHandler (@RestControllerAdvice) maps project-specific exceptions to ApiResponse.fail bodies: BadRequestException → 400, ForbiddenException → 403, ChatRoomNotFoundException and SpaceNotFoundException → 404, SpaceClientException and UserClientException → 502, unhandled exceptions → 500. WebSocket message handling errors are returned to the sender via /user/queue/errors (@MessageExceptionHandler).

## API Behavior Policy

- API behavior must be documented in `API_SPEC.yaml`.
- If API behavior changes, `API_SPEC.yaml` must be updated in the same PR.

## Needs Confirmation

- Whether public lists should filter only APPROVED spaces is Needs confirmation.
- Schedule overlap and booking conflict policy are not visible.
- Image upload/storage integration is not visible; only image URLs are persisted.
- Security matcher behavior for GET /spaces/me needs confirmation.
