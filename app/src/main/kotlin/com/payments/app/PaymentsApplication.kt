package com.payments.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Hilt's application-level container; every module's @Module is aggregated here. */
@HiltAndroidApp
class PaymentsApplication : Application()
