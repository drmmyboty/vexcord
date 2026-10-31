# Vexcord

Vexcord is an independent Android customization project for Discord, using the open-source Revenge project as its current technical foundation.

## Current build status

Vexcord currently has two separate parts:

- **Android installer:** a Vexcord-branded installer build based on the upstream Revenge Manager project.
- **Patch-engine foundation:** a new Node.js catalog/state-management prototype under `patch-engine/`.

The patch engine currently validates plugin catalog metadata and defines a local enabled-plugin state format. It does **not** yet inject patches into Discord, load runtime plugins, or produce a finished modified Discord APK. Catalog entries marked `planned` are ideas, not working features.

## Patch engine

From the `patch-engine` directory with Node.js 18 or newer:

- `npm run validate` checks catalog metadata.
- `npm run list` lists catalog entries and local state.
- `npm run enable -- <plugin-id>` and `npm run disable -- <plugin-id>` manage state only for plugins marked implemented. Planned entries are intentionally blocked.

The initial catalog includes AMOLED Black, Theme Manager, Quick Replies, Message Bookmarks, Chat Translator, Account Switcher, Nitro Style Preview, and Local Chat Archive. These are currently marked **planned**.

Plugin packages will need manifest validation, version compatibility checks, permission review, and safe install/uninstall behavior before runtime execution is added. Do not treat a plugin catalog entry as an implemented feature.

## Planned architecture

1. **Vexcord Installer:** Android interface for client version checks, patch status, installation progress, errors, and plugin management.
2. **Vexcord Patch Engine:** compatible patch application, integrity checks, and rollback.
3. **Plugin Hub:** discover, review permissions, install, update, disable, and remove compatible plugins.
4. **Client build pipeline:** produce and test a modified APK, with signing and compatibility checks.

## Planned plugin ideas

- AMOLED theme and theme manager
- Quick replies and message bookmarks
- Chat translator
- Account-switching shortcuts using supported account controls
- Cosmetic Nitro-style previews only
- Transparent local archive for content the user is authorized to save

No feature should steal account tokens, bypass authentication, evade platform rate limits, or secretly upload private messages. Cosmetic previews do not unlock paid Nitro entitlements.

## Android APK builds

The GitHub Actions workflow named **Build Vexcord Android APK** builds the current installer scaffold. A successful installer build does not mean a modified Discord client or plugin runtime is ready.

Check the [GitHub Actions runs](https://github.com/drmmyboty/vexcord/actions) for build results and artifacts. The [Releases page](https://github.com/drmmyboty/vexcord/releases) is where finished, tested releases can be published.

## Safety, privacy, and attribution

- Vexcord is unofficial and is not affiliated with or endorsed by Discord.
- Client modifications may break after Discord updates and may carry account or service risks.
- Never share Discord passwords, account tokens, or recovery codes.
- Any logging feature must be transparent, local by default, and limited to content the user is authorized to save.
- Review the licenses and notices for the upstream [Revenge Manager](https://github.com/revenge-mod/revenge-manager) and [Revenge Bundle](https://github.com/revenge-mod/revenge-bundle). Required upstream attribution and license notices must remain, even as Vexcord's own user-facing branding is developed.

## Contributions

When reporting a bug, include the Android version, device model, expected behavior, and actual behavior. Never include passwords, tokens, private messages, or other sensitive information.
