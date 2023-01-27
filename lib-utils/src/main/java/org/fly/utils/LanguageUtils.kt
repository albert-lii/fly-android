package org.fly.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.*

/**
 * 语言相关工具类
 */
object LanguageUtils {

    /**
     * 配置多语言切换的Context
     */
    @JvmStatic
    fun attachBaseContext(context: Context, locale: Locale): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            createConfigurationContext(context, locale)
        } else {
            updateConfiguration(context, locale)
        }
    }

    /**
     * Android 7.1 以上通过 createConfigurationContext
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private fun createConfigurationContext(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        val localeList = LocaleList(locale)
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration)
    }

    /**
     * Android 7.1 以下通过 updateConfiguration
     */
    private fun updateConfiguration(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocales(LocaleList(locale))
        } else {
            // updateConfiguration
            configuration.locale = locale
            val dm = resources.displayMetrics
            resources.updateConfiguration(configuration, dm)
        }
        return context
    }

    /**
     * 多语言切换
     */
    @JvmStatic
    fun updateApplicationLocale(context: Context, locale: Locale) {
        val resources = context.resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.setLocale(locale)
        }
        val dm = resources.displayMetrics;
        resources.updateConfiguration(configuration, dm)
    }
}