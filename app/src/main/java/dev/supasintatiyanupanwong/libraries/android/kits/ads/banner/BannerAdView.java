package dev.supasintatiyanupanwong.libraries.android.kits.ads.banner;

import static dev.supasintatiyanupanwong.libraries.android.kits.ads.banner.BannerAdSize.SIZE_INVALID;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;

public class BannerAdView extends FrameLayout {

    private static boolean sGoogleImplChecks = false;
    private static boolean sGoogleImpl = false;
    private static boolean sHuaweiImplChecks = false;
    private static boolean sHuaweiImpl = false;

    private final @Nullable AdView mGoogleAdView;
    private final @Nullable BannerView mHuaweiAdView;

    private @NonNull Size mAdSize = SIZE_INVALID;
    private String mAdUnitId;

    public BannerAdView(@NonNull Context context) {
        super(context);

        if (isGoogleImpl()) {
            mGoogleAdView = new AdView(context);
            mHuaweiAdView = null;
            addView(mGoogleAdView);
        } else if (isHuaweiImpl()) {
            mGoogleAdView = null;
            mHuaweiAdView = new BannerView(context);
            addView(mHuaweiAdView);
        } else {
            mGoogleAdView = null;
            mHuaweiAdView = null;
        }
    }

    public BannerAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (isGoogleImpl()) {
            mGoogleAdView = new AdView(context);
            mHuaweiAdView = null;
            addView(mGoogleAdView);
        } else if (isHuaweiImpl()) {
            mGoogleAdView = null;
            mHuaweiAdView = new BannerView(context);
            addView(mHuaweiAdView);
        } else {
            mGoogleAdView = null;
            mHuaweiAdView = null;
        }
    }

    public @NonNull Size getAdSize() {
        return mAdSize;
    }

    public void setAdSize(@NonNull Size adSize) {
        mAdSize = adSize;
    }

    public @NonNull String getAdUnitId() {
        return mAdUnitId;
    }

    public void setAdUnitId(@NonNull String adUnitId) {
        mAdUnitId = adUnitId;
    }

    public void loadAd() {
        if (mGoogleAdView != null) {
            mGoogleAdView.setAdUnitId(mAdUnitId);
            mGoogleAdView.setAdSize(new AdSize(mAdSize.getWidth(), mAdSize.getHeight()));
            mGoogleAdView.loadAd(new AdRequest.Builder().build());
        }

        if (mHuaweiAdView != null) {
            mHuaweiAdView.setAdId(mAdUnitId);
            mHuaweiAdView.setBannerAdSize(new BannerAdSize(mAdSize.getWidth(), mAdSize.getHeight()));
            mHuaweiAdView.loadAd(new AdParam.Builder().build());
        }
    }

    public void pause() {
        if (mGoogleAdView != null) {
            mGoogleAdView.pause();
        }

        if (mHuaweiAdView != null) {
            mHuaweiAdView.pause();
        }
    }

    public void resume() {
        if (mGoogleAdView != null) {
            mGoogleAdView.resume();
        }

        if (mHuaweiAdView != null) {
            mHuaweiAdView.resume();
        }
    }

    public void destroy() {
        if (mGoogleAdView != null) {
            mGoogleAdView.destroy();
        }

        if (mHuaweiAdView != null) {
            mHuaweiAdView.destroy();
        }
    }

    private static boolean isGoogleImpl() {
        if (!sGoogleImplChecks) {
            try {
                Class.forName(com.google.android.gms.ads.AdView.class.getName());
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
                Class.forName(com.huawei.hms.ads.banner.BannerView.class.getName());
                sHuaweiImpl = true;
            } catch (Exception ex) {
                sHuaweiImpl = false;
            }
            sHuaweiImplChecks = true;
        }
        return sHuaweiImpl;
    }
}
