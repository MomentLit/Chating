# Development Flow

## Purpose

This document defines the minimum development flow for Space so human developers and AI coding assistants work from the same context.

## Standard Workflow

Create an Issue draft with AI
-> Human confirms Issue scope and acceptance criteria
-> Developer reads the Issue
-> Developer reads relevant Docs
-> Developer asks AI for an implementation plan
-> Developer reviews the plan
-> Developer writes code
-> Developer runs AI self-review
-> Developer manually runs and tests the change
-> Developer updates API_SPEC.yaml if API behavior changed
-> Developer opens a PR
-> Developer checks CodeRabbit review
-> Human reviewer reviews the PR
-> Developer checks whether Docs need updates
-> Merge

## Required Reading Before Implementation

- `docs/service-overview.md`
- `docs/service-policy.md`
- `docs/common/architecture-guide.md`
- `docs/common/coding-convention.md`
- `docs/common/ai-usage-guide.md`
- `API_SPEC.yaml` when API behavior is involved

## Issue Creation Workflow

- AI may draft the Issue, but a human must confirm the scope and acceptance criteria.
- One Issue should describe one clear task.
- If the Issue changes API behavior, it must include an API impact section.

## AI Planning Rule

Before writing code, ask AI to create an implementation plan based on the Issue, project Docs, current code structure, and `API_SPEC.yaml`.

## Code Writing Workflow

- Keep changes focused on the Issue.
- Follow the existing package structure and naming style.
- Do not refactor unrelated code.
- Do not add dependencies or change build files unless explicitly approved.

## AI Self-Review Workflow

After implementation, use AI to review the diff against the Docs, architecture, conventions, and `API_SPEC.yaml`.

## Manual Testing Workflow

Run the relevant local test or manual verification for the changed behavior. If a test cannot be run, document why in the PR.

## PR and Review Workflow

- Open a PR with summary, related Issue, verification steps, AI usage, and documentation status.
- Check CodeRabbit review first, but do not blindly accept suggestions.
- Human review remains required.

## Documentation Update Workflow

Update service Docs when behavior, conventions, API contracts, policies, or operational assumptions change.

## Mandatory API_SPEC.yaml Update Workflow

If API behavior changes, `API_SPEC.yaml` must be updated before opening or merging the PR.

If an implementation changes API behavior, update `API_SPEC.yaml` in the same PR.
Do not leave API documentation updates for a later PR unless explicitly approved.
