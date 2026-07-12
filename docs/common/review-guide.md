# Review Guide

## Review Order

CodeRabbit reviews the result after implementation.
Docs and Harness guide the direction before implementation.
Project Docs and existing code structure override CodeRabbit suggestions.
If API behavior changed, reviewers must check whether API_SPEC.yaml was updated.

## CodeRabbit Review

- Check CodeRabbit review first.
- Do not blindly accept CodeRabbit suggestions.
- Judge suggestions based on project Docs and existing code structure.
- Reject suggestions that introduce new architecture, unrelated refactoring, or behavior that was not requested.

## Human Review Checklist

- Architecture violations
- Controller, Service, Repository, DTO responsibility separation
- Exception handling consistency
- Request DTO and Response DTO usage
- Entity exposure through API responses
- Unnecessary changes
- Test or manual verification quality
- Security risks
- Docs update necessity
- API_SPEC.yaml update necessity

## API Specification Review

If API behavior changed, reviewers must check that `API_SPEC.yaml` was updated in the same PR. This includes endpoint paths, methods, request fields, response fields, status codes, error responses, authentication, authorization, validation, pagination, sorting, and filtering.
