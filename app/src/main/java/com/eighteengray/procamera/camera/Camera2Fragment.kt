package com.eighteengray.procamera.camera

import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.eighteengray.cardlibrary.bean.BaseDataBean
import com.eighteengray.commonutillibrary.FileUtils
import com.eighteengray.commonutillibrary.SDCardUtils
import com.eighteengray.procamera.R
import com.eighteengray.procamera.common.JumpActivityUtils
import com.eighteengray.procamera.databinding.LayoutFragmentCamera2Binding
import com.eighteengray.procamera.widget.TextureViewTouchListener
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.CameraConfigure.*
import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent.ImagePathAvailable
import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent.ImageReaderAvailable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


class Camera2Fragment: Fragment() {
    var mFile: File? = null
    var textureViewTouchListener: TextureViewTouchListener? = null
    var sceneArrayList: MutableList<String> = mutableListOf()
    var effectArrayList: MutableList<String> = mutableListOf()
    var isHDRVisible = false
    var isEFFECTVisible = false
    private var delayTime = 0

    private var _binding: LayoutFragmentCamera2Binding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFile = FileUtils.createFile(SDCardUtils.getAppFile(activity).absolutePath, "saveImg")

        EventBus.getDefault().register(this)
        _binding = LayoutFragmentCamera2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        binding.rlScene.showRecyclerView(generateSceneData(), Constants.viewModelPackage)
        binding.rlEffect.showRecyclerView(generateEffectData(), Constants.viewModelPackage)

        binding.seekbarCamera2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    binding.cameraTextureView.changeFocusDistance(progress)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.ivFlashCamera.setOnClickListener {
            binding.tvModeGpufileter.visibility = View.GONE
            val location1 = IntArray(2)
            binding.ivFlashCamera.getLocationOnScreen(location1)
            PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(
                binding.ivFlashCamera,
                Gravity.LEFT or Gravity.TOP,
                location1[0] + binding.ivFlashCamera.width,
                location1[1] - binding.ivFlashCamera.height + 60
            )
        }

        binding.ivSwitchCamera.setOnClickListener {
            binding.cameraTextureView.switchCamera()
        }

        binding.tvSceneCamera.setOnClickListener {
            if (binding.rlEffect.visibility == View.VISIBLE) {
                binding.rlEffect.visibility = View.GONE
            }
            if (!isHDRVisible) {
                binding.rlScene.visibility = View.VISIBLE
                isHDRVisible = true
            } else {
                binding.rlScene.visibility = View.GONE
                isHDRVisible = false
            }
        }

        binding.tvModeSelect.setOnClickListener {
            val modeSelectDialogFragment = ModeSelectDialogFragment()
            modeSelectDialogFragment.show(requireFragmentManager(), "mode")
        }

        binding.ivGpufilterCamera.setOnClickListener {
            if (binding.rlScene.visibility == View.VISIBLE) {
                binding.rlScene.visibility = View.GONE
            }
            if (!isEFFECTVisible) {
                binding.rlEffect.visibility = View.VISIBLE
                isEFFECTVisible = true
            } else {
                binding.rlEffect.visibility = View.GONE
                isEFFECTVisible = false
            }
        }

        binding.ivAlbumCamera.setOnClickListener {
            JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false)
        }

        binding.ivRatioCamera.setOnClickListener {
            val location = IntArray(2)
            binding.ivRatioCamera.getLocationOnScreen(location)
            PopupWindowFactory.createRatioPopupWindow(activity).showAtLocation(binding.ivRatioCamera, Gravity.BOTTOM, 0, 500)
        }

        binding.ivShutterCamera.setOnClickListener {
            showViewTakePicture()
            binding.cameraTextureView.takePicture()
        }

        binding.ivDelayShutter.setOnClickListener {
            when (delayTime) {
                0 -> delayTime = 3
                3 -> delayTime = 10
                10 -> delayTime = 0
            }
            binding.tvDelaySecond.text = delayTime.toString() + ""
            binding.cameraTextureView.setDalayTime((delayTime * 1000).toLong())
        }

        binding.cameraTextureView.openCamera()
        textureViewTouchListener = TextureViewTouchListener(binding.cameraTextureView)
        binding.cameraTextureView.setOnTouchListener(textureViewTouchListener)
    }

    private fun generateSceneData(): List<BaseDataBean<String>>? {
        val list: MutableList<BaseDataBean<String>> = ArrayList()
        sceneArrayList.add("DISABLED")
        sceneArrayList.add("ACTION")
        sceneArrayList.add("BARCODE")
        sceneArrayList.add("BEACH")
        sceneArrayList.add("CANDLELIGHT")
        sceneArrayList.add("FACE_PRIORITY")
        sceneArrayList.add("FIREWORKS")
        sceneArrayList.add("HDR")
        sceneArrayList.add("LANDSCAPE")
        sceneArrayList.add("NIGHT")
        sceneArrayList.add("NIGHTPORTRAIT")
        sceneArrayList.add("PARTY")
        sceneArrayList.add("PORTRAIT")
        sceneArrayList.add("SNOW")
        sceneArrayList.add("SPORTS")
        sceneArrayList.add("STEADYPHOTO")
        sceneArrayList.add("SUNSET")
        sceneArrayList.add("THEATRE")
        for (i in sceneArrayList.indices) {
            val baseDataBean = BaseDataBean(2, sceneArrayList[i])
            list.add(baseDataBean)
        }
        return list
    }

    private fun generateEffectData(): List<BaseDataBean<String>>? {
        val list: MutableList<BaseDataBean<String>> = ArrayList()
        effectArrayList.add("AQUA")
        effectArrayList.add("BLACKBOARD")
        effectArrayList.add("MONO")
        effectArrayList.add("NEGATIVE")
        effectArrayList.add("POSTERIZE")
        effectArrayList.add("SEPIA")
        effectArrayList.add("SOLARIZE")
        effectArrayList.add("WHITEBOARD")
        effectArrayList.add("OFF")
        for (i in effectArrayList.indices) {
            val baseDataBean = BaseDataBean(2, effectArrayList[i])
            list.add(baseDataBean)
        }
        return list
    }

    private fun showViewTakePicture() {
        binding.ivTakepictureDone.visibility = View.VISIBLE
        binding.ivTakepictureDone.visibility = View.GONE
    }

    override fun onPause() {
        if (binding.cameraTextureView != null) {
            binding.cameraTextureView.closeCamera()
        }
        super.onPause()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    //EventBus--接收相机配置的参数
    @Subscribe(threadMode = ThreadMode.MAIN) //闪光灯设置
    @Throws(CameraAccessException::class)
    fun onFlashSelect(flash: Flash) {
        binding.tvModeGpufileter.visibility = View.VISIBLE
        when (flash.flash) {
            Constants.FLASH_AUTO -> binding.ivFlashCamera.setImageResource(R.mipmap.flash_auto_white_24dp)
            Constants.FLASH_ON -> binding.ivFlashCamera.setImageResource(R.mipmap.flash_on_white_24dp)
            Constants.FLASH_OFF -> binding.ivFlashCamera.setImageResource(R.mipmap.flash_off_white_24dp)
            Constants.FLASH_FLARE -> binding.ivFlashCamera.setImageResource(R.mipmap.flash_flare_white_24dp)
        }
        binding.cameraTextureView.setFlashMode(flash.flash)
        binding.tvModeGpufileter.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //HDR模式选择
    @Throws(CameraAccessException::class)
    fun onSceneSelect(scene: Scene) {
        binding.cameraTextureView.setSceneMode(scene.scene)
        binding.rlScene.visibility = View.GONE
        isHDRVisible = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //GPU滤镜选择
    @Throws(CameraAccessException::class)
    fun onEffectSelect(effect: Effect) {
        binding.cameraTextureView.setEffectMode(effect.effect)
        binding.tvModeGpufileter.text = effect.effect
        binding.rlEffect.visibility = View.GONE
        isEFFECTVisible = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //拍摄比例调节
    fun onRatioSelect(ratio: Ratio) {
        binding.cameraTextureView.setRatioMode(ratio.ratio)
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //延时拍摄
    fun onDelayTime(delayTime: DelayTime) {
        when (delayTime.delaytime) {
            Constants.DELAY_3 -> {}
            Constants.DELAY_5 -> {}
            Constants.DELAY_8 -> {}
            Constants.DELAY_10 -> {}
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //拍照完成后，拿到ImageReader，然后做保存图片的操作
    fun onImageReaderAvailable(imageReaderAvailable: ImageReaderAvailable) {
        Thread(
            ImageSaver(
                imageReaderAvailable.imageReader,
                getActivity()
            )
        ).start()
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //存储图像完成后，拿到ImagePath图片路径
    fun onImagePathAvailable(imagePathAvailable: ImagePathAvailable) {
        val bitmap = BitmapFactory.decodeFile(imagePathAvailable.imagePath)
        if (bitmap != null) {
            val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 120, 120)
            binding.ivAlbumCamera.setImageBitmap(thumbnail)
        }
    }

}
