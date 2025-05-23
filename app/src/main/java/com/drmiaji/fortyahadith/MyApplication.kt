package com.drmiaji.fortyahadith

import android.app.Application
import com.drmiaji.fortyahadith.utils.ThemeUtils

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeUtils.applyTheme(this)
    }
}