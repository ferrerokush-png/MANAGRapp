# ReleaseFlow

Modular Android app to manage music release workflows. This commit fixes outstanding TODOs around notifications and UI polish, and prepares the repo for GitHub.

## What changed
- Replaced placeholder notification icons with a proper vector small icon.
- Implemented PendingIntents for notification taps and actions.
- Added BroadcastReceiver handlers for "Mark Uploaded" and "Mark Task Complete" actions and registered them in the core:data manifest.
- Safe runtime check for `POST_NOTIFICATIONS` on Android 13+ before showing notifications.
- Implemented the demo UI TODOs: options menu on project details and a FAB click handler on project list.
- Cleaned data extraction rules XML to remove TODO comment.
- Added a standard Android `.gitignore`.

## Build
Requires Android Studio Giraffe+ and JDK 17.

- Compile SDK: 34
- Min SDK: 28

To build from the terminal (Windows `cmd.exe`):

```
cd C:\Users\ferre\Downloads\ReleaseFlow\ReleaseFlow
gradlew.bat assembleDebug
```

## Run
Open the project in Android Studio and run the `app` configuration on an emulator or device.

## Push to GitHub (Windows cmd)
1. Create a new empty repository on GitHub (no README/gitignore/license).
2. In `cmd.exe`, run:

```
cd C:\Users\ferre\Downloads\ReleaseFlow\ReleaseFlow
git init
git add .
git commit -m "Initial repo + fix TODOs: notifications, UI polish, receivers, icon, .gitignore"
```

3. Add your GitHub remote and push. Replace `YOUR_USERNAME` and `REPO_NAME`.

Using GitHub CLI (recommended):
```
gh repo create YOUR_USERNAME/REPO_NAME --source=. --private --push
```

Or using HTTPS remote:
```
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git
git push -u origin main
```

If prompted for credentials, use Git Credential Manager or a Personal Access Token with `repo` scope.

## Notes
- Notification tap currently opens `MainActivity` with an extra `projectId`; navigation can be enhanced to deep-link directly into feature/projects detail if desired.
- Receivers currently log and cancel notifications; you can hook them up to repositories to persist state changes.

