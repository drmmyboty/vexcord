# Vexcord

Vexcord is an experimental, community-made Android customization project inspired by the plugin and theme ecosystem around Revenge.

## Status

**Early setup only. No APK has been built or verified yet.** This repository currently begins with Vexcord's manually authored SVG icon and project documentation. A real Android installer requires a compatible loader/client integration and a successful build; the Revenge bundle itself is JavaScript, not an APK.

## Planned direction

- Dark, AMOLED-friendly visual identity with electric-blue accents
- Theme and font customization
- Plugin discovery/management where supported by the Android loader
- Import/export for compatible themes
- Privacy-conscious local message logging only where permitted and visible to the user

Desktop Vencord plugins are not automatically compatible with Android. Features will be marked supported only after testing.

## Logo

The icon in `assets/vexcord-icon.svg` is authored directly from vector paths and shapes, with no generated image. It contains no text. It can be imported into Canva for further manual editing.

## Upstream

Vexcord's planned technical starting point is [Revenge](https://github.com/revenge-mod/revenge-bundle), an open-source Discord Android client modification. Vexcord is unofficial and is not affiliated with Discord or the Revenge project.

Before redistributing any build, review the upstream license and Discord's terms. Never enter a Discord password into a third-party login page or share account tokens.

## Build status

Builds are not configured until the compatible Android loader integration and supported build path are verified. Do not treat this repository as an installable app yet.
