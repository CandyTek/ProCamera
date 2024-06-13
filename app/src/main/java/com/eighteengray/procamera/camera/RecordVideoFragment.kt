package com.eighteengray.procamera.camera

import android.hardware.camera2.CameraAccessException
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eighteengray.procamera.R
import com.eighteengray.procamera.common.JumpActivityUtils
import com.eighteengray.procamera.databinding.LayoutFragmentVideoBinding
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.dataevent.CameraConfigure.Flash
import com.eighteengray.procameralibrary.dataevent.RecordVideoEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class RecordVideoFragment: Fragment() {
    private var isRecording = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        _binding = LayoutFragmentVideoBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }
    private var _binding: LayoutFragmentVideoBinding? = null
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.recordTextureView.openCamera()
    }

    override fun onPause() {
        if (binding.recordTextureView != null) {
            binding.recordTextureView.closeCamera()
        }
        super.onPause()
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initView() {
        binding.ivFlashCamera.setOnClickListener {
            val location1 = IntArray(2)
            binding.ivFlashCamera.getLocationOnScreen(location1)
            PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(
                binding.ivFlashCamera,
                Gravity.NO_GRAVITY,
                location1[0] + binding.ivFlashCamera.width,
                location1[1] - binding.ivFlashCamera.height
            )
        }

        binding.ivSwitchCamera.setOnClickListener {
            binding.recordTextureView.switchCamera()
        }

        binding.recordTextureView.setOnClickListener {
        }

        binding.tvModeSelect.setOnClickListener {
            val modeSelectDialogFragment = ModeSelectDialogFragment()
            modeSelectDialogFragment.show(requireFragmentManager(), "Camera")
        }

        binding.ivAlbumCamera.setOnClickListener {
            JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false)
        }

        binding.ivShutterCamera.setOnClickListener {
            if (!isRecording) {
                isRecording = true
                binding.recordTextureView.startRecordVideo()
            } else if (isRecording) {
                isRecording = false
                binding.recordTextureView.stopRecordVideo()
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Throws(CameraAccessException::class)
    fun onFlashSelect(flash: Flash) {
        binding.recordTextureView.setFlashMode(flash.flash)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Throws(CameraAccessException::class)
    fun onRecordVideo(recordVideoEvent: RecordVideoEvent) {
        if (recordVideoEvent.isRecording) {
            binding.ivShutterCamera.setImageResource(R.drawable.btn_shutter_recording)
        } else {
            binding.ivShutterCamera.setImageResource(R.drawable.btn_shutter_record)
        }
    }

}
