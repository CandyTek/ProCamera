package com.eighteengray.procamera.imageprocess.activity

import android.os.Bundle
import android.view.Gravity
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.AtyImageProcessBinding
import com.eighteengray.procamera.imageprocess.widget.OutputMenuDialog
import com.eighteengray.procamera.imageprocess.widget.RedoMenuDialog
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent
import com.supaur.baseactivity.baseactivity.BaseActivity
// import kotlinx.android.synthetic.main.aty_cut.iv_right
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ImageProcessActivity: BaseActivity() {
    private val binding by lazy { AtyImageProcessBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val bundle = intent.extras
        var imagePath = bundle!!.getString(Constants.IMAGE_PATH)

        EventBus.getDefault().register(this)

        binding.ivRight.setBackgroundResource(R.mipmap.redo_menu_white_24dp)
        binding.ivRight.setOnClickListener {
            val redoMenuDialog = RedoMenuDialog()
            redoMenuDialog.show(supportFragmentManager, "redo")
        }

//        ImageLoader.getInstance().with(this@ImageProcessActivity).load(File(imagePath)).into(imageview_process).execute()

        binding.tvFilterImageProcess.setOnClickListener {
            //                PopupWindowFactory.createFilterPopupWindow(ImageProcessActivity.this).showAtLocation(ll_bottommenu_image_process, Gravity.BOTTOM, 0, 288);
//            JumpActivityUtils.jump2FilterActivity(this)
        }

        binding.tvToolsImageProcess.setOnClickListener {
            PopupWindowFactory.createProcessPopupWindow(this@ImageProcessActivity)
                .showAtLocation(binding.llBottommenuImageProcess, Gravity.BOTTOM, 0, 288)
        }

        binding.tvOutputImageProcess.setOnClickListener {
            val outputMenuDialog = OutputMenuDialog()
            outputMenuDialog.show(supportFragmentManager, "output")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageFolderSelected(imageFolderEvent: ImageFolderEvent) {
        val currentImageFolderNum = imageFolderEvent.currentImageFolderNum
    }

}
