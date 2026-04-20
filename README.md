# CI/CD Configuration Guide

## GitHub Actions Workflow

The project includes a comprehensive GitHub Actions workflow (`.github/workflows/maven-tests.yml`) that supports:

### Trigger Options

| Event | Description |
|-------|-------------|
| Push to main/master/develop | Auto-run tests on code changes |
| Pull Request | Run tests on PRs to protected branches |
| Manual Dispatch | Trigger tests manually with custom parameters |

### Override Parameters (via `workflow_dispatch` or Maven)

| Parameter | Description | Example |
|-----------|-------------|----------|
| `browser` | Browser to run tests | `chrome`, `firefox` |
| `tags` | Run tests with specific tags | `smoke`, `sanity,regression` |
| `tests` | Run specific test IDs | `TC001,TC002` |
| `threads` | Parallel thread count | `5`, `10` |
| `env` | Environment | `qa`, `staging`, `prod` |
| `override_report` | Override existing report | `true`, `false` |

### Maven Command Examples

```bash
# Run all tests
mvn test

# Run with custom tags
mvn test -Dtags=smoke

# Run specific tests
mvn test -Dtests=TC001,TC002

# Run with custom threads
mvn test -Dthreads=10

# Run smoke tests with 10 threads
mvn test -Dtags=smoke -Dthreads=10

# Run with custom config file
mvn test -DconfigFile=config.staging.properties
```

### Environment-Specific Configs

Pre-configured environment files:
- `config.properties` - Default configuration
- `config.qa.properties` - QA environment
- `config.staging.properties` - Staging environment
- `config.prod.properties` - Production environment

### GitHub Secrets (Optional)

For sensitive configurations, add these to GitHub Secrets:
- `SELENIUM_HOST` - Custom Selenium grid URL
- `APP_USERNAME` - Application username
- `APP_PASSWORD` - Application password
