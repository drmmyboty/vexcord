# Vexcord

Vexcord is an unofficial Android customization project for Discord. The current Android installer uses the open-source Revenge Manager project as its technical foundation; required upstream attribution and license notices must remain.

## Current status

- **Android installer:** Vexcord-branded installer scaffold; not yet an independent patching app.
- **Patch-engine prototype:** Node.js catalog, utility-module registry, and local enabled-state manager under `patch-engine/`.
- **Standalone utility modules:** eight working, framework-independent helpers are registered. They are not yet injected into or connected to Discord Android.
- **Discord patching:** no finished modified Discord APK or integrated Android plugin runtime is available yet.

## Patch engine commands

Requires Node.js 18 or newer. From `patch-engine/`:

- `npm run validate` validates catalog IDs and checks that every entry marked implemented has a module.
- `npm run list` lists the 46 catalog entries and distinguishes registered utility modules from planned plugins.
- `npm run enable -- <plugin-id>` and `npm run disable -- <plugin-id>` update local enabled-state metadata for registered modules. This does not activate code inside Discord.

## Registered utility modules

- `amoled-theme.js`: true-black theme palette and a supported adapter hook.
- `theme-manager.js`: three theme presets and adapter-based application.
- `quick-replies.js`: local in-memory reusable reply snippets with JSON export.
- `message-bookmarks.js`: local in-memory message bookmarks with JSON export.
- `custom-timestamps.js`: 12-hour or 24-hour timestamp formatting.
- `markdown-preview.js`: escaped basic Markdown preview HTML.
- `plugin-backup.js`: export/import settings JSON with format validation.
- `plugin-error-console.js`: bounded in-memory error log with JSON export.

These modules provide utility logic and can be tested independently. They do not currently have Discord Android UI hooks, persistent Android storage integration, or a secure third-party plugin sandbox.

## Plugin catalog

The catalog includes appearance plugins (AMOLED Black, Theme Manager, Nitro Style Preview, Custom Backgrounds, Custom Fonts, Accent Color Editor, Compact Mode, Animated Themes, Custom App Icon, Profile Decorator, Theme Store); chat and account plugins (Quick Replies, Message Bookmarks, Chat Translator, Account Switcher, Advanced Search, Edit History, Deleted Message Indicator, Chat Exporter, Local Chat Archive); media and voice plugins (Emoji Organizer, GIF Favorites, Media Downloader, Image Preview Enhancer, Audio Player Customizer, Volume Booster, Voice Activity Indicator); productivity plugins (Reminder, Unread Channel Tracker, Custom Status Scheduler, Notification Rules, Focus Mode, Server Notes, Friend Notes); and developer tools (Plugin Error Console, Plugin Backup, Plugin Permissions, Plugin Importer, Compatibility Checker, Update Checker, Safe Mode, Custom CSS Editor, Layout Editor, Plugin Hub, Markdown Preview, Custom Timestamps).

Catalog entries marked `planned` are not working plugins yet. The next engineering milestone is connecting the standalone utilities to a native Android manager and a tested, compatible patch pipeline.

## Security and limits

- Never collect or request Discord passwords, account tokens, or recovery codes.
- Account switching must use supported authentication controls; no token extraction or authentication bypass.
- Nitro-style features are cosmetic previews only and do not unlock paid entitlements.
- Message logging/archive features must be transparent, local by default, and limited to content the user is authorized to save. No covert uploads.
- Do not evade Discord rate limits or other platform safeguards.
- Client modifications may break after updates and can carry account/service risks.

## Build status

The GitHub Actions workflow named **Build Vexcord Android APK** builds the current installer scaffold; it does not prove a patched client works. The **Validate Vexcord Patch Engine** workflow checks the catalog and JavaScript modules.

- [GitHub Actions](https://github.com/drmmyboty/vexcord/actions)
- [Releases](https://github.com/drmmyboty/vexcord/releases)
- [Revenge Manager upstream](https://github.com/revenge-mod/revenge-manager)
- [Revenge Bundle upstream](https://github.com/revenge-mod/revenge-bundle)

Vexcord is not affiliated with or endorsed by Discord. Preserve all required upstream license and attribution notices.
