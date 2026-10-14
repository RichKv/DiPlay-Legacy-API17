// SPDX-License-Identifier: AGPL-3.0-only
package com.shilapi.xcertplay

import android.annotation.TargetApi
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.Locale

/**
 * API 33+ implementation of the platform app-locale APIs.
 *
 * android.app.LocaleManager only exists on API 33+. This class directly
 * references it, so it must never be loaded on API < 33. [AppLocale] guards
 * all calls with a SDK_INT check.
 */
@TargetApi(33)
internal object AppLocaleApi33 {
    fun getPreferenceLanguage(context: Context): String? {
        val locales = context.getSystemService(LocaleManager::class.java).applicationLocales
        return if (locales.isEmpty) null else locales[0].language
    }

    fun saveLanguage(context: Context, locale: Locale?) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            locale?.let { LocaleList(it) } ?: LocaleList.getEmptyLocaleList()
    }

    fun migrateFromPrefs(context: Context, previous: Locale?) {
        val manager = context.getSystemService(LocaleManager::class.java)
        // Never overwrite a language already chosen through Android Settings.
        if (manager.applicationLocales.isEmpty && previous != null) {
            manager.applicationLocales = LocaleList(previous)
        }
    }
}
