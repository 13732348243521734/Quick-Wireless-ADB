# QuickWadb

[English](README.md)

Ein winziges Xposed/LSPosed-Modul, das den Bestätigungsdialog der
**Wireless-Debugging**-Kachel in den Schnelleinstellungen entfernt. Kachel
antippen, und Wireless Debugging ist sofort an.

Keine Oberfläche, keine Einstellungen, kein Launcher-Icon. Installieren,
aktivieren, neu starten.

## Warum

Der Bestätigungsdialog an dieser Kachel taucht bei mir seit der Pixel
Android 17 QPR2 Beta 4 auf. Ob er auch im September-2026-Release kommt, habe
ich nicht getestet. Wahrscheinlich tritt er bei AOSP-basierten Builds aber
ab etwa September 2026 auf. Bei älteren Builds hat die Kachel keinen Dialog,
dann wird das Modul nicht gebraucht.

## Was es macht

Beim Antippen der Kachel erscheint normalerweise der Dialog „Wireless
Debugging in diesem Netzwerk zulassen?“. QuickWadb hookt
`AdbWirelessDebuggingDevelopmentTile#setIsEnabled` in der Einstellungen-App
und schaltet Wireless Debugging direkt ein.

- **Ausschalten** bleibt unverändert.
- Bei **gesperrtem** Gerät passiert nichts, wie im Original.
- Bei einem Fehler erscheint der normale Dialog.

## Voraussetzungen

- Android 11 oder neuer (Wireless-Debugging-Kachel)
- Root mit LSPosed oder einem kompatiblen Xposed-Framework
- Einstellungen-App im AOSP-Stil (`com.android.settings`). Stark angepasste
  ROMs nutzen evtl. andere Klassennamen, dann greift der Hook nicht.

## Installation

1. APK unter Releases herunterladen (mit Debug-Schlüssel signiert, dafür völlig ausreichend) oder selbst bauen (siehe unten) und installieren.
2. In LSPosed das Modul mit dem Scope **Einstellungen** (`com.android.settings`) aktivieren.
3. **Das Handy neu starten.** Nur die Einstellungen-App neu zu starten reicht meist nicht.
4. Die Kachel „Wireless Debugging“ zu den Schnelleinstellungen hinzufügen und antippen.

## Bauen

```sh
gradle wrapper          # einmalig, falls kein Wrapper vorhanden ist
./gradlew assembleDebug
```

Die APK liegt danach unter `app/build/outputs/apk/debug/app-debug.apk`.
Debug-Builds werden automatisch mit einem lokalen Debug-Schlüssel signiert,
es ist also keine Signatur-Einrichtung nötig. Da dieser Schlüssel pro Rechner
verschieden ist, muss man eine anderswo gebaute Installation für ein Update
erst deinstallieren.

## Fehlersuche

Fehler werden mit dem Tag `QuickWadb` ins Xposed-Log geschrieben. Erscheint
der Dialog nach einem Neustart weiterhin, prüfen, ob das Modul für die
Einstellungen aktiviert ist, und die LSPosed-Logs ansehen.

## Lizenz

MIT, siehe [LICENSE](LICENSE).
