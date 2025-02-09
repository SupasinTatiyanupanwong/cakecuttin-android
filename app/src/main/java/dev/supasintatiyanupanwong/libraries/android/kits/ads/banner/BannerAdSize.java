package dev.supasintatiyanupanwong.libraries.android.kits.ads.banner;

import android.content.Context;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public class BannerAdSize {
    static final Size SIZE_INVALID = new Size(0, 0);

    private static boolean sGoogleImplChecks = false;
    private static boolean sGoogleImpl = false;
    private static boolean sHuaweiImplChecks = false;
    private static boolean sHuaweiImpl = false;

    private BannerAdSize() {}

    public static Size getCurrentOrientationBannerAdSize(@NonNull Context context, @Px int widthPx) {
        //noinspection ConstantValue
        if (context == null || widthPx <= 0) {
            return SIZE_INVALID;
        }

        if (isGoogleImpl()) {
            final com.google.android.gms.ads.AdSize adSize = com.google.android.gms.ads.AdSize
                    .getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, widthPx);
            return new Size(widthPx, adSize.getHeightInPixels(context));
        } else if (isHuaweiImpl()) {
            final com.huawei.hms.ads.AdSize adSize = com.huawei.hms.ads.BannerAdSize
                    .getCurrentDirectionBannerSize(context, widthPx);
            return new Size(widthPx, adSize.getHeightPx(context));
        } else {
            return SIZE_INVALID;
        }
    }

    private static boolean isGoogleImpl() {
        if (!sGoogleImplChecks) {
            try {
                Class.forName(com.google.android.gms.ads.AdSize.class.getName());
                sGoogleImpl = true;
            } catch (Exception ex) {
                sGoogleImpl = false;
            }
            sGoogleImplChecks = true;
        }
        return sGoogleImpl;
    }

    private static boolean isHuaweiImpl() {
        if (!sHuaweiImplChecks) {
            try {
                Class.forName(com.huawei.hms.ads.BannerAdSize.class.getName());
                sHuaweiImpl = true;
            } catch (Exception ex) {
                sHuaweiImpl = false;
            }
            sHuaweiImplChecks = true;
        }
        return sHuaweiImpl;
    }
}
