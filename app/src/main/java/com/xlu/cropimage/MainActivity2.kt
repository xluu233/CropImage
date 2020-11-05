package com.xlu.cropimage

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity() {

    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        button3.setOnClickListener(){
            openCamera()
        }
        button4.setOnClickListener(){
            if (imageview.canRightCrop()) {
                bitmap = imageview.crop()
/*                imageview.setImageBitmap(bitmap)
                //引导线
                imageview.setShowGuideLine(false)*/

                imageview.visibility = View.GONE
                imageview_display.visibility = View.VISIBLE
                imageview_display.setImageBitmap(bitmap)

            } else {
                Toast.makeText(this, "cannot crop correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 调起相机拍照
     */
    private fun openCamera() {

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 判断是否有相机
        if (captureIntent.resolveActivity(packageManager) != null) {

            mCameraUri = createImageUri()
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri)
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(captureIntent, 110)

        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private fun createImageUri(): Uri? {
        val status: String = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        return if (status == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        } else {
            contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, ContentValues())
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 110) {
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(mCameraUri!!))
            imageview.visibility = View.VISIBLE
            imageview_display.visibility = View.GONE
            imageview.setImageToCrop(bitmap)
        } else {
            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show()
        }

    }

}