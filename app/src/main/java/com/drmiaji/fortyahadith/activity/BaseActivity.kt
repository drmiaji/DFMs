package com.drmiaji.fortyahadith.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.drmiaji.fortyahadith.R
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeId())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        adjustSystemUI()
        onActivityReady(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    protected abstract fun onActivityReady(savedInstanceState: Bundle?)

    @ColorRes
    open fun getStatusBarColor(): Int = android.R.color.white

    open fun getThemeId(): Int = R.style.AppTheme

    private fun adjustSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)

        val isLightTheme = isUsingLightTheme()

        // Set status and nav bar colors
        val color = ContextCompat.getColor(this, getStatusBarColor())
        window.statusBarColor = color
        window.navigationBarColor = color

        // Adjust light/dark icons based on theme brightness
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = isLightTheme
        controller.isAppearanceLightNavigationBars = isLightTheme
    }

    /**
     * Determines whether the current theme is light or dark.
     */
    private fun isUsingLightTheme(): Boolean {
        val uiMode = resources.configuration.uiMode
        val nightMask = uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return nightMask != android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { view ->
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus() // Clear focus after hiding keyboard
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Shows a snackbar message
     * @param message The message to show
     * @param duration How long to display the message
     */
    fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, duration).show()
    }
}