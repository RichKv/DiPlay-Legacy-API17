package com.shilapi.xcertplay

import android.net.Uri
import androidx.annotation.RequiresApi
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

/** Loaded only after an API check, never while constructing an API17 activity. */
@RequiresApi(19)
internal object DocumentExportApi19 {
    fun contract(): ActivityResultContract<String, Uri?> = ActivityResultContracts.CreateDocument("text/plain")
}
