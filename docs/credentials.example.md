# Credentials & Secrets (template)

Copy this file to `docs/credentials.md` (git-ignored) and fill in the real values.
Never commit the filled-in version.

## Database

| Key | Value |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://<host>:5432/<database>` |
| `DB_USERNAME` | `<username>` |
| `DB_PASSWORD` | `<password>` |

## JWT

| Key | Value |
| --- | --- |
| `JWT_SECRET` | `<64-char-random-hex>` |

## Deployed environments

| What | URL |
| --- | --- |
| Backend (Railway) | `<backend url>` |
| Frontend (Vercel) | `<frontend url>` |
| API docs | `<docs url + password>` |
