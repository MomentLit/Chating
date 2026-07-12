# PR Guide

## PR Rules

- Summarize the change clearly.
- Link the related Issue.
- List reference Docs that were checked.
- Disclose AI usage.
- Include manual verification results.
- Explain core logic in terms the developer can defend in review.
- State whether Docs were updated.
- State whether `API_SPEC.yaml` was updated.

If API behavior changed but API_SPEC.yaml was not updated, the PR should not be merged unless there is explicit approval.

## PR Template

```md
## Summary
-

## Related Issue
- closes #

## Reference Docs
- [ ] docs/service-overview.md
- [ ] docs/service-policy.md
- [ ] docs/common/architecture-guide.md
- [ ] docs/common/coding-convention.md
- [ ] docs/common/ai-usage-guide.md
- [ ] API_SPEC.yaml

## AI Usage
- [ ] Issue draft
- [ ] Implementation planning
- [ ] Code draft
- [ ] Code self-review
- [ ] Test draft
- [ ] API_SPEC.yaml update
- [ ] Not used

## Manual Verification
- [ ] Server starts successfully
- [ ] Normal request verified
- [ ] Failure request verified
- [ ] Database save/read verified
- [ ] Error response format verified
- [ ] Not applicable

## API_SPEC.yaml Update
- [ ] API behavior did not change
- [ ] API behavior changed and API_SPEC.yaml was updated
- [ ] API behavior changed but update is intentionally deferred with approval

## Explainable Core Logic
-

## Docs Update
- [ ] No documentation update needed
- [ ] Documentation updated
- [ ] Documentation update needed but separated into another Issue
```
