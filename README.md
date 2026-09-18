# Threadly

Threadly is a native Kotlin/Jetpack Compose email client that presents mail as organization clubs and chat-style conversations.

## Local Gmail OAuth configuration

Create a `local.properties` file in the project root (it is intentionally not committed) and add:

```properties
google_server_client_id=YOUR_OAUTH_WEB_CLIENT_ID.apps.googleusercontent.com
```

The app does not contain a default OAuth client ID and does not store credentials in the Room database. Generic provider secrets are kept in Android Keystore-backed encrypted preferences.

## Architecture

- Room is the local source of truth for accounts, clubs, threads, messages, attachments, and preferences.
- Provider adapters normalize Gmail REST and IMAP/SMTP mail into the same repository model.
- WorkManager polls accounts every 15 minutes; Faster sync uses a visible foreground service.
- Compose navigation provides the home, club, thread, composer, account, club editor, and settings surfaces.

The adaptive launcher icon is a temporary placeholder and should be replaced before release.
