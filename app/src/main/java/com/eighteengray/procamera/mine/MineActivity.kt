package com.eighteengray.procamera.mine

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.AtyMineBinding
import com.eighteengray.procamera.widget.SnackbarUtil
import com.supaur.baseactivity.baseactivity.BaseActivity


class MineActivity : BaseActivity() {
    private val binding by lazy { AtyMineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.tilNameMine.editText
        binding.tilNameMine.hint = "请输入姓名："
        binding.tilPasswordMine.editText
        binding.tilPasswordMine.hint = "请输入密码："

        var snackBarView = LayoutInflater.from(this).inflate(R.layout.view_snackbar, null)
        binding.btnMine.setOnClickListener(View.OnClickListener { showSnackBar() })
    }

    private fun showSnackBar() {
        val snackbar = SnackbarUtil.ShortSnackbar(binding.cdnlMine, "妹子删了你发出的消息", SnackbarUtil.Warning)
                .setActionTextColor(Color.RED).setAction("再次发送") {
                    SnackbarUtil.LongSnackbar(binding.cdnlMine, "妹子已将你拉黑", SnackbarUtil.Alert).setActionTextColor(Color.WHITE).show()
                }
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 0)
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 2)
        snackbar.show()
    }

}
