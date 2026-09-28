package com.example.quickwadb;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Ueberspringt den Bestaetigungsdialog der Wireless-Debugging-QS-Kachel.
 * Scope: com.android.settings
 */
public class MainHook implements IXposedHookLoadPackage {

    private static final String TILE_CLASS =
            "com.android.settings.development.qstile.AdbWirelessDebuggingDevelopmentTile";
    private static final String ENABLER_CLASS =
            "com.android.settings.development.AdbWirelessDebuggingEnabler";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"com.android.settings".equals(lpparam.packageName)) return;

        try {
            XposedHelpers.findAndHookMethod(TILE_CLASS, lpparam.classLoader,
                    "setIsEnabled", boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (!(boolean) param.args[0]) return; // Ausschalten: kein Dialog

                            try {
                                Object tile = param.thisObject;

                                Object keyguard = XposedHelpers.getObjectField(tile, "mKeyguardManager");
                                if ((boolean) XposedHelpers.callMethod(keyguard, "isKeyguardLocked")) {
                                    param.setResult(null); // wie Original: nichts bei Sperrbildschirm
                                    return;
                                }

                                Object context = XposedHelpers.getObjectField(tile, "mContext");
                                Class<?> enabler = XposedHelpers.findClass(ENABLER_CLASS, lpparam.classLoader);
                                XposedHelpers.callStaticMethod(enabler, "setAdbWifi", context, true);
                            } catch (Throwable t) {
                                XposedBridge.log("QuickWadb failed, falling back to dialog: " + t);
                                return; // Original (mit Dialog) laeuft weiter
                            }

                            param.setResult(null); // Dialog ueberspringen
                        }
                    });
        } catch (Throwable t) {
            XposedBridge.log("QuickWadb: could not hook " + TILE_CLASS + ": " + t);
        }
    }
}
