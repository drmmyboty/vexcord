# Vexcord

Vexcord is a standalone Android app with its own Vexcord-branded launcher and a built-in view of the official Discord web app.

## Download

The latest APK is published under **GitHub Releases** as `Vexcord.apk`. Install that APK directly on Android. There is no separate Manager or patch-installer app.

## What this build is

- One standalone Android APK named Vexcord.
- Opens the official Discord web app at https://discord.com/app.
- Uses Discord's own website for sign-in and account activity.
- Does not collect Discord passwords, tokens, or recovery codes.
- Does not patch or repackage Discord's proprietary Android APK.

## Important limitation

This is a Vexcord-branded Android wrapper around Discord Web, not a modified native Discord client. Native Discord client plugins, fake Nitro unlocks, token-based account switching, and other unsupported modifications are not included.

## Build

GitHub Actions builds the APK on pushes to `main` and manual workflow runs. The APK is uploaded as a workflow artifact and published as `Vexcord.apk` in the `latest` GitHub Release.

Vexcord is an unofficial independent project and is not affiliated with or endorsed by Discord.
