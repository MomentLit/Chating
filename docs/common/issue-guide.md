# Issue Guide

## Issue Rules

- Issue drafts may be written with AI.
- A human must confirm requirements and acceptance criteria.
- One Issue should contain one clear task.
- Split large Issues by domain action, API endpoint, or independently reviewable behavior.
- If API behavior changes, include the required `API_SPEC.yaml` update task.

## Required Issue Sections

- Purpose
- Task description
- Acceptance criteria
- Exception cases
- API impact
- Reference Docs
- Area
- API draft if needed
- Questions that need confirmation

## API Impact Section

The Issue must state whether the change affects paths, methods, request fields, response fields, status codes, error responses, authentication, authorization, query parameters, path parameters, validation, pagination, sorting, or filtering.

## AI Issue Prompt

```text
Convert the following feature requirement into a GitHub Issue.

Feature:
[Describe feature]

Project rules:
- Follow docs/service-overview.md.
- Follow docs/service-policy.md.
- Preserve the existing backend architecture.
- Separate Request DTOs and Response DTOs.
- Do not return entities directly from API responses.
- Follow the existing exception handling style.
- Write acceptance criteria as a checklist.
- Keep one Issue focused on one clear task.
- If API behavior changes, include the required API_SPEC.yaml update task.

Output format:
1. Issue title
2. Purpose
3. Task description
4. Acceptance criteria
5. Exception cases
6. API impact
7. API draft
8. Reference Docs
9. Area
10. Questions that need confirmation
```
