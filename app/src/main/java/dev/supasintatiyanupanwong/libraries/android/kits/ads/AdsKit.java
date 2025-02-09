package dev.supasintatiyanupanwong.libraries.android.kits.ads;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.huawei.hms.ads.HwAds;

public final class AdsKit {

    private static boolean sGoogleImplChecks = false;
    private static boolean sGoogleImpl = false;
    private static boolean sHuaweiImplChecks = false;
    private static boolean sHuaweiImpl = false;

    private AdsKit() {
        checkGoogleImpl();
        checkHuaweiImpl();
    }

    public static void init(@NonNull Context context) {
        final boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
        if (sGoogleImpl) {
            if (isMainThread) {
                new Thread(() -> MobileAds.initialize(context)).start();
            } else {
                MobileAds.initialize(context);
            }
        }

        if (sHuaweiImpl) {
            if (isMainThread) {
                new Thread(() -> HwAds.init(context)).start();
            } else {
                HwAds.init(context);
            }
        }
    }

    private static void checkGoogleImpl() {
        if (!sGoogleImplChecks) {
            try {
                Class.forName(com.google.android.gms.ads.MobileAds.class.getName());
                sGoogleImpl = true;
            } catch (Exception ex) {
                sGoogleImpl = false;
            }
            sGoogleImplChecks = true;
        }
    }

    private static void checkHuaweiImpl() {
        if (!sHuaweiImplChecks) {
            try {
                Class.forName(com.huawei.hms.ads.HwAds.class.getName());
                sHuaweiImpl = true;
            } catch (Exception ex) {
                sHuaweiImpl = false;
            }
            sHuaweiImplChecks = true;
        }
    }
}
