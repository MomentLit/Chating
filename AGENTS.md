# AGENTS.md

## Role

You are an AI coding assistant working inside this backend service repository.
Your job is to help implement backend features while strictly following the existing architecture, conventions, API specification, and documentation.

Service context: Space uses `com.example.chating` and currently has this responsibility: Space creation, listing, detail lookup, update, soft deletion, owner space listing, schedule creation/listing/update/deletion, and image URL persistence.

## Required Reading

Before suggesting or modifying code, read:

- `docs/service-overview.md`
- `docs/service-policy.md`
- `docs/common/development-flow.md`
- `docs/common/ai-usage-guide.md`
- `docs/common/coding-convention.md`
- `docs/common/architecture-guide.md`
- `docs/common/issue-guide.md`
- `docs/common/pr-guide.md`
- `docs/common/review-guide.md`
- `API_SPEC.yaml`

## Core Rules

- Do not introduce a new architecture unless explicitly requested.
- Follow the existing package structure of this service.
- Keep changes minimal and focused on the Issue.
- Do not modify unrelated files.
- Do not expose secrets, API keys, credentials, or personal information.
- Do not generate large speculative abstractions.
- Prefer consistency with the existing code over generic best practices.
- If requirements are unclear, identify questions instead of guessing.
- The project Docs, API specification, and existing code structure override general AI recommendations.
- Issue and PR responsibility belongs to the human developer, not the AI.

## API Specification Rule

- `API_SPEC.yaml` is the source of truth for this service's API contract.
- If implementation changes API behavior, update `API_SPEC.yaml` in the same PR.
- API behavior includes paths, methods, request fields, response fields, status codes, error responses, authentication, authorization, query parameters, path parameters, validation behavior, pagination, sorting, and filtering.
- Do not leave API documentation updates for a later PR unless explicitly approved.
- If API behavior is unclear, mark it as `Needs confirmation` instead of guessing.

## Backend Rules

- Controllers handle HTTP request/response only.
- Services contain business logic.
- Repositories handle persistence.
- Do not return entities directly from API responses.
- Use request and response DTOs when the service already follows that pattern.
- Use project-specific exceptions when they exist.
- Do not throw raw `RuntimeException` unless the existing service explicitly uses that pattern and no project-specific exception exists.
- Do not change package structure without explicit instruction.
- Do not add dependencies without explicit instruction.

## Work Flow

1. Read the Issue.
2. Read the relevant Docs.
3. Read `API_SPEC.yaml` when API behavior is involved.
4. Create an implementation plan before writing code.
5. Keep changes small and focused.
6. Review the code against the Docs and `API_SPEC.yaml`.
7. Update `API_SPEC.yaml` if API behavior changed.
8. Provide verification steps.
9. Mention whether Docs need updates.

## Output Rules

When proposing code changes, provide:

1. Changed files
2. Reason for each change
3. Implementation plan
4. Code changes
5. API_SPEC.yaml changes, if any
6. Test or verification method
7. Risks or assumptions
8. Docs update necessity
