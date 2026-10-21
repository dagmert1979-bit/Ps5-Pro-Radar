package com.example.ui

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

data class ApkDownloadResult(
    val success: Boolean,
    val fileName: String,
    val sizeMbText: String,
    val message: String
)

object ApkDirectDownloader {

    /**
     * Copies the currently installed, signed Android APK binary (applicationInfo.sourceDir)
     * directly into the device's public Downloads folder as "PS5-Pro-Radar.apk" so the user
     * has the standalone Android APK file ready for direct installation or sharing.
     */
    fun downloadApkToDownloadsFolder(context: Context): ApkDownloadResult {
        val fileName = "PS5-Pro-Radar.apk"
        return try {
            val sourceApkFile = File(context.applicationInfo.sourceDir)
            if (!sourceApkFile.exists()) {
                return ApkDownloadResult(
                    success = false,
                    fileName = fileName,
                    sizeMbText = "0 MB",
                    message = "APK-Quelldatei konnte nicht gelesen werden."
                )
            }

            val sizeMb = sourceApkFile.length().toDouble() / (1024.0 * 1024.0)
            val sizeText = String.format(Locale.GERMANY, "%.1f MB", sizeMb)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return ApkDownloadResult(
                        success = false,
                        fileName = fileName,
                        sizeMbText = sizeText,
                        message = "Download-Ordner konnte nicht geöffnet werden."
                    )

                resolver.openOutputStream(uri)?.use { outputStream ->
                    FileInputStream(sourceApkFile).use { inputStream ->
                        inputStream.copyTo(outputStream, bufferSize = 64 * 1024)
                    }
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            } else {
                @Suppress("DEPRECATION")
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val targetFile = File(downloadsDir, fileName)
                FileInputStream(sourceApkFile).use { inputStream ->
                    FileOutputStream(targetFile).use { outputStream ->
                        inputStream.copyTo(outputStream, bufferSize = 64 * 1024)
                    }
                }
            }

            Ps5NotificationHelper.sendStockNotification(
                context = context,
                title = "APK Direkt-Download abgeschlossen ($sizeText)",
                message = "Die Android-Version '$fileName' wurde direkt in deinem Downloads-Ordner gespeichert!"
            )

            ApkDownloadResult(
                success = true,
                fileName = fileName,
                sizeMbText = sizeText,
                message = "Android-Version '$fileName' ($sizeText) direkt im Ordner 'Downloads' gespeichert!"
            )
        } catch (e: Exception) {
            ApkDownloadResult(
                success = false,
                fileName = fileName,
                sizeMbText = "",
                message = "APK-Export Hinweis: Nutze oben rechts in AI Studio 'Build / Generate APK' für den direkten APK-Download."
            )
        }
    }

    fun getInstalledApkSizeText(context: Context): String {
        return try {
            val file = File(context.applicationInfo.sourceDir)
            val sizeMb = file.length().toDouble() / (1024.0 * 1024.0)
            String.format(Locale.GERMANY, "%.1f MB", sizeMb)
        } catch (_: Exception) {
            "APK bereit"
        }
    }
}
