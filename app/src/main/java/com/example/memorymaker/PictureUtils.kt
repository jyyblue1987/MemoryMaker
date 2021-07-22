package com.example.memorymaker

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

fun getScaledBitmap(path: String, activity: Activity): Bitmap{
    val size = Point()
    activity.windowManager.defaultDisplay.getSize(size)

    return getScaledBitmap(path, size.x, size.y)
}

fun getScaledBitmap(path: String, desWidth: Int, desHeight: Int ): Bitmap {
    var options =  BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    var inSampleSize = 1
    if (srcHeight > desHeight || srcWidth > desWidth) {
        val heightScale = srcHeight / desHeight
        val widthScale = srcWidth / desWidth

        val sampleScale = if (heightScale > widthScale){
            heightScale
        } else{
            widthScale
        }

        inSampleSize = Math.round(sampleScale)
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    return BitmapFactory.decodeFile(path, options)

}