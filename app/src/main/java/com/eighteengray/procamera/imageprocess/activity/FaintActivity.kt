package com.eighteengray.procamera.imageprocess.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.eighteengray.commonutillibrary.FileUtils
import com.eighteengray.commonutillibrary.ImageUtils
import com.eighteengray.commonutillibrary.SDCardUtils
import com.eighteengray.commonutillibrary.ScreenUtils
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.AtyCutBinding
import com.eighteengray.procameralibrary.common.Constants
import com.supaur.baseactivity.baseactivity.BaseActivity
import java.io.File


class FaintActivity: BaseActivity() {
    private val binding by lazy { AtyCutBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        var width = ScreenUtils.getScreenWidth(this@FaintActivity)
        var path = intent.getStringExtra(Constants.CROPIMAGEPATH)
        var bitmap = ImageUtils.getBitmapFromPathSimple(path)
        var drawable = BitmapDrawable(bitmap)
        binding.civCut.setDrawable(drawable, width - 100, width - 100)

        binding.ivBack.setOnClickListener {
            val cutBitmap: Bitmap = binding.civCut.cropImage
            val file: File = FileUtils.createFile(
                SDCardUtils.getAppFile(this@FaintActivity).absolutePath,
                "cutBitmap.jpg"
            )
            path = file.absolutePath
            ImageUtils.saveBitmap2Album(this@FaintActivity, cutBitmap)

            val mIntent = Intent()
            mIntent.putExtra(Constants.CROPIMAGEPATH, path)
            setResult(RESULT_OK, mIntent)
            finish()
        }
    }

}
