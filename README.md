# Vexcord

Vexcord is an independent Android customization project for Discord, using the open-source Revenge project as its technical foundation.

## Current build approach

Vexcord is being built as a **branded installer that installs a modified Discord app alongside the official Discord app**. Revenge works inside Discord; its JavaScript bundle is not a standalone Android app. The installer APK and the modified Discord app are two different things.

The GitHub Actions workflow named **Build Vexcord Android APK** builds the Vexcord-branded installer. After a successful run, download the artifact named `vexcord-installer-apk` from that workflow run. This installer build is not yet a finished Vexcord client release, and the build must complete successfully before an APK can be considered available.

## Planned features

- AMOLED-friendly dark appearance with electric-blue branding
- Theme and font customization using compatible Revenge features
- Theme import/export where supported
- Compatible plugin support
- Further Vexcord-specific features after they are implemented and tested

Not every desktop Vencord plugin works on Android. Features are not considered available until implemented and tested.

## Build status and downloads

- **Installer APK:** generated through GitHub Actions; check the latest workflow run and its artifacts.
- **Finished, tested Vexcord release:** not yet published.
- **Releases page:** https://github.com/drmmyboty/vexcord/releases

If the workflow fails, no APK artifact will be available from that run. If there is no APK in the artifact or Releases page, do not assume a build exists.

## Safety and privacy

- Vexcord is unofficial and is not affiliated with or endorsed by Discord.
- Client modifications can break when Discord updates and may carry account or service risks. Review Discord's terms and use your account responsibly.
- Never share your Discord password, account token, or recovery codes.
- Do not add private-message logging or collect other people's data without a legitimate, consent-based reason. Any future logging feature must respect privacy and applicable rules.
- Only install builds you trust and review their source.

## Contributions

Bug reports and feature suggestions are welcome. Include your Android version, device model, expected behavior, and actual behavior. Never include passwords, tokens, private messages, or other sensitive information in reports.

## Open-source foundation

Vexcord's current foundation is [Revenge Manager](https://github.com/revenge-mod/revenge-manager) and [Revenge Bundle](https://github.com/revenge-mod/revenge-bundle). These upstream projects have their own licenses and branding; Vexcord is an independent project. Review upstream license notices before redistributing derivative builds.
