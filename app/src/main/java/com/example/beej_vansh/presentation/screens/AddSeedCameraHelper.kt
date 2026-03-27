package com.example.beej_vansh.presentation.screens

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.io.File

/**
 * Helper function to capture a photo using CameraX and save it to the app's cache.
 */
fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageSaved: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // 1. Define the file location (Internal Cache is best for temporary Telegram uploads)
    val photoFile = File(
        context.cacheDir,
        "seed_capture_${System.currentTimeMillis()}.jpg"
    )

    // 2. Set up the output options
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // 3. Trigger the capture
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.d("BeejVanshCamera", "Photo capture succeeded: ${photoFile.absolutePath}")
                onImageSaved(photoFile)
            }

            override fun onError(exc: ImageCaptureException) {
                // Log the exact error for debugging in Logcat
                Log.e("BeejVanshCamera", "Photo capture failed: ${exc.message}", exc)

                // Detailed errorA breakdown
                when (exc.imageCaptureError) {
                    ImageCapture.ERROR_FILE_IO -> Log.e("BeejVanshCamera", "Error: Storage full or file permission denied.")
                    ImageCapture.ERROR_CAMERA_CLOSED -> Log.e("BeejVanshCamera", "Error: Camera closed before saving.")
                    ImageCapture.ERROR_CAPTURE_FAILED -> Log.e("BeejVanshCamera", "Error: Hardware capture failed.")
                    else -> Log.e("BeejVanshCamera", "Error: Unknown camera error.")
                }

                onError(exc)
            }
        }
    )
}