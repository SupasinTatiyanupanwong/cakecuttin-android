package dev.supasintatiyanupanwong.apps.android.cakecuttin

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.text.HtmlCompat
import androidx.core.view.OneShotPreDrawListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dev.supasintatiyanupanwong.apps.android.cakecuttin.databinding.CuttinActivityBinding
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.app.activity.policy.applyEdgeToEdge
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.app.activity.setOnApplyWindowInsetsListener
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.graphics.plus
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets.displayCutouts
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets.navigationBars.isLightNavigationBarsForeground
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets.statusBars.isLightStatusBarsForeground
import dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets.systemBars
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.math.abs

private val COLORS = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.GRAY, Color.BLACK)
private val SHAPES = arrayOf(
    CuttinOverlay.SHAPE_TYPE_CIRCLE,
    CuttinOverlay.SHAPE_TYPE_SECT_X,
    CuttinOverlay.SHAPE_TYPE_SECT_Y,
    CuttinOverlay.SHAPE_TYPE_RECT
)
private const val CAMERA_REQUEST_CODE = 0x00226372 // 0x00CAMERA

class CuttinActivity : Activity(), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle() = lifecycleRegistry

    private val binding by lazy(NONE) { CuttinActivityBinding.inflate(layoutInflater) }
    private val adView by lazy(NONE) { AdView(this) }

    private val cameraProvider by lazy(NONE) { ProcessCameraProvider.getInstance(this).get() }
    private val cameraSelector by lazy(NONE) { CameraSelector.DEFAULT_BACK_CAMERA }
    private val cameraPreview by lazy(NONE) {
        Preview.Builder()
            .build()
            .also { it.surfaceProvider = binding.preview.surfaceProvider }
    }

    private var isCameraActivated = false
    private var isCountsScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        applyEdgeToEdge()
        super.onCreate(savedInstanceState)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        isLightStatusBarsForeground = true
        isLightNavigationBarsForeground = true

        setContentView(binding.root)
        setOnApplyWindowInsetsListener { view, insets ->
            val safeInsets = insets.systemBars + insets.displayCutouts
            view.setPadding(safeInsets.left, safeInsets.top, safeInsets.right, safeInsets.bottom)
            insets
        }

        OneShotPreDrawListener.add(binding.adContainer) {
            val adSize =
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    this,
                    (binding.adContainer.width / resources.displayMetrics.density).toInt()
                )
            adView.adUnitId = "/21775744923/example/adaptive-banner"
            adView.setAdSize(adSize)
            binding.adContainer.removeAllViews()
            binding.adContainer.layoutParams = binding.adContainer.layoutParams
                .apply { height = adSize.getHeightInPixels(binding.adContainer.context) }
            binding.adContainer.addView(adView)
            adView.loadAd(AdRequest.Builder().build())
        }

        OneShotPreDrawListener.add(binding.countsScroll) {
            val halfWidth = binding.countsScroll.width / 2
            OneShotPreDrawListener.add(binding.counts) {
                binding.counts.setPadding(
                    halfWidth - binding.counts.getChildAt(0).width / 2,
                    0,
                    halfWidth - binding.counts.getChildAt(binding.counts.childCount - 1).width / 2,
                    0
                )
            }
        }

        // noinspection ClickableViewAccessibility
        binding.countsScroll.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isCountsScrolling = true
                }

                MotionEvent.ACTION_UP -> {
                    isCountsScrolling = false
                    invalidateCount()
                }
            }
            false
        }
        binding.countsScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            invalidateCount()
        }

        binding.color.setOnClickListener {
            val colorIdx = COLORS.indexOf(binding.cuttin.color)
            val color = if (colorIdx >= COLORS.size - 1) COLORS[0] else COLORS[colorIdx + 1]
            invalidateColor(color)
        }

        binding.shape.setOnClickListener {
            val shapeIdx = SHAPES.indexOf(binding.cuttin.shape)
            val shape = if (shapeIdx >= SHAPES.size - 1) SHAPES[0] else SHAPES[shapeIdx + 1]
            invalidateShape(shape)
        }

        binding.capture.setOnClickListener { toggleCameraState() }

        binding.clear.setOnClickListener { toggleCameraState() }

        binding.hand.setOnClickListener {
            binding.controls.scaleX = -binding.controls.scaleX
        }

        invalidateColor(COLORS[0])
        invalidateShape(SHAPES[0])
        invalidateCount()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        adView.resume()

        if (!isCameraActivated) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onPause() {
        adView.pause()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onStop()
    }

    override fun onDestroy() {
        adView.destroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.any { result -> result == PackageManager.PERMISSION_GRANTED }) {
                toggleCameraState()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun invalidateColor(color: Int) {
        binding.cuttin.color = color
        binding.color.imageTintList = ColorStateList.valueOf(color)
    }

    private fun invalidateShape(shape: Int) {
        binding.cuttin.shape = shape
        binding.shape.setImageResource(
            when (shape) {
                CuttinOverlay.SHAPE_TYPE_CIRCLE -> R.drawable.ic_add_circle_outlined_24
                CuttinOverlay.SHAPE_TYPE_SECT_X -> R.drawable.app_ic_cross_x_outlined_24
                CuttinOverlay.SHAPE_TYPE_SECT_Y -> R.drawable.app_ic_cross_y_outlined_24
                CuttinOverlay.SHAPE_TYPE_RECT -> R.drawable.ic_add_box_outlined_24
                else -> 0
            }
        )
    }

    private fun invalidateCount() {
        val halfWidth = binding.countsScroll.width / 2

        val scrollX = binding.countsScroll.scrollX
        val centerX = scrollX + halfWidth

        var closestDistance = Int.MAX_VALUE
        var closestView: TextView? = null

        for (i in 0 until binding.counts.childCount) {
            val item = binding.counts.getChildAt(i) as TextView
            item.text = item.text.toString() // Clear bold style
            item.textSize = 12f

            val itemCenterX = item.left + item.width / 2
            val distance = abs(centerX - itemCenterX)
            if (distance < closestDistance) {
                closestDistance = distance
                closestView = item
            }
        }

        if (closestView != null) {
            if (!isCountsScrolling) {
                binding.countsScroll.smoothScrollTo(
                    closestView.left - halfWidth + closestView.width / 2,
                    0
                )
            }

            binding.cuttin.count = closestView.tag.toString().toInt()
            closestView.text = HtmlCompat.fromHtml("<b>${closestView.text}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            closestView.textSize = 14f
        }
    }

    private fun toggleCameraState() {
        if (isCameraActivated) {
            cameraProvider.unbindAll()

            isCameraActivated = false

            binding.capture.visibility = View.INVISIBLE
            binding.clear.visibility = View.VISIBLE
        } else {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)

            isCameraActivated = true

            binding.capture.visibility = View.VISIBLE
            binding.clear.visibility = View.INVISIBLE
        }
    }
}
