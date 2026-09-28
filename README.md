# QuickWadb

[Deutsch](README.de.md)

A tiny Xposed/LSPosed module that removes the confirmation dialog from the
**Wireless debugging** Quick Settings tile. Tap the tile and wireless
debugging turns on right away.

No UI, no settings, no launcher icon. Install, enable, reboot.

## Why

The confirmation dialog on this tile appeared for me starting with the
Pixel Android 17 QPR2 Beta 4. I have not tested whether it also shows up on
the September 2026 release, but it will likely appear on AOSP-based builds
from around September 2026 onward. On older builds the tile has no dialog and
you do not need this module.

## What it does

Tapping the tile normally shows an "Allow wireless debugging on this
network?" dialog. QuickWadb hooks
`AdbWirelessDebuggingDevelopmentTile#setIsEnabled` in the Settings app and
enables wireless debugging directly.

- Turning it **off** is unchanged.
- While the device is **locked**, nothing happens, same as stock.
- If anything goes wrong, the original dialog is shown as usual.

## Requirements

- Android 11 or newer (wireless debugging tile)
- Root with LSPosed or a compatible Xposed framework
- AOSP-style Settings app (`com.android.settings`). Heavily modified ROMs may
  use different class names and the hook will not apply.

## Install

1. Download the APK from the Releases page (signed with a debug key, which is fine for this purpose) or build it yourself (see below), and install it.
2. In LSPosed, enable the module with scope **Settings** (`com.android.settings`).
3. **Reboot the phone.** Restarting only the Settings app is usually not enough.
4. Add the "Wireless debugging" tile to Quick Settings and tap it.

## Build

```sh
gradle wrapper          # once, if the wrapper is not present
./gradlew assembleDebug
```

The APK ends up in `app/build/outputs/apk/debug/app-debug.apk`. Debug builds
are signed automatically with a local debug key, so no signing setup is
needed. Because that key differs per machine, updating an install that was
built elsewhere requires uninstalling it first.

## Troubleshooting

The module logs failures to the Xposed log with the tag `QuickWadb`. If the
dialog still appears after a reboot, check that the module is enabled for
Settings and look at the LSPosed logs.

## License

MIT, see [LICENSE](LICENSE).
