# Threadly

Threadly is an Android email client that organizes your emails by Clubs rather than traditional inboxes. It features threaded conversations, AI integration, and a modern Compose UI.

## Features

- Club-Based Organization: Group your emails into distinct clubs.
- Intelligent Threading: Emails are grouped into conversation threads for easy reading.
- AI Assistant: Integrated with OpenRouter to provide summaries, Q&A, and draft replies for your email threads (requires an OpenRouter API key).
- Modern UI: Built entirely with Jetpack Compose.
- Google OAuth Integration: Securely log in with your Google account.

## Getting Started

### Requirements
- Android Studio (latest stable version recommended)
- Java 17
- Android SDK 34

### Building the App
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the `app` configuration.

### Google OAuth Configuration
To allow anyone to log in with their Google account:
1. Go to the Google Cloud Console.
2. Select the project associated with this app.
3. Navigate to "APIs & Services" > "OAuth consent screen".
4. Ensure the Publishing status is set to "In production". If it is set to "Testing", only explicitly added test users will be able to log in. Note that switching to production may require app verification from Google since restricted scopes are used.

## License
MIT License
