package com.eighteengray.procamera.widget.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.DialogfragmentModeselectBinding
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ModeSelectEvent
import org.greenrobot.eventbus.EventBus


class ModeSelectDialogFragment: DialogFragment() {
    private var _binding: DialogfragmentModeselectBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //全屏显示
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(-1, -2)

        _binding = DialogfragmentModeselectBinding.inflate(inflater, container, false)
        initView()
        return binding.root

    }

    private fun initView() {
        binding.llCameraModeselect.setOnClickListener {
            val modeSelectEvent1 = ModeSelectEvent()
            modeSelectEvent1.mode = Constants.MODE_CAMERA
            EventBus.getDefault().post(modeSelectEvent1)
            dismiss()
        }

        binding.llVideoModeselect.setOnClickListener {
            val modeSelectEvent2 = ModeSelectEvent()
            modeSelectEvent2.mode = Constants.MODE_RECORD
            EventBus.getDefault().post(modeSelectEvent2)
            dismiss()
        }


    }

}

