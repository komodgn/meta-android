package com.metasearch.android.di

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import com.metasearch.android.App
import dev.zacsweers.metro.Provider
import kotlin.reflect.KClass

class MetroAppComponentFactory : AppComponentFactory() {

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application {
        val app = super.instantiateApplicationCompat(cl, className) as App

        activityProviders = app.appGraph.activityProviders
        return app
    }

    override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity {
        val clazz = Class.forName(className, false, cl).asSubclass(Activity::class.java)

        return activityProviders[clazz.kotlin]?.invoke()
            ?: super.instantiateActivityCompat(cl, className, intent)
    }

    companion object {
        private lateinit var activityProviders: Map<KClass<out Activity>, Provider<Activity>>
    }
}
