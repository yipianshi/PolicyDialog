package com.d.policydialogdemo

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.d.policydialog.PolicyDialog
import com.d.policydialog.utils.StringUtils
import com.d.policydialogdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding;
    lateinit var policyDialog: PolicyDialog;

    private val policyURL = "https://www.jianshu.com/p/c44d171298ce"
    private val policyURL2 = "http://www.mplanet.cn/wap/components/privacy.htm"
    private val policyURL3 = "https://www.baidu.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.listener = mOnClickListener

        //必须设置，不然没有点击效果
        binding.policyTipsTv.movementMethod = LinkMovementMethod.getInstance()

        val keyTexts = arrayOf("《用户协议与隐私政策》")
        val tipsText =
            "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n《用户协议与隐私政策》\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        //富文本
        val spannableStringBuilder = StringUtils.createSpannableString(
            tipsText,
            arrayOf("用户协议与隐私政策提示"),
            arrayOf(policyURL),
            intArrayOf(Color.RED),
            keyTexts
        )

        binding.policyTipsTv.text = spannableStringBuilder;
    }

    private var mOnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.show_dialog_btn -> {
                showPolicyDialog()
            }
        }
    }

    /**
     * 展示政策对话框
     */
    private fun showPolicyDialog() {
        //还没展示过
        //展示对话框
        val keyTexts = arrayOf("《用户协议》", "《隐私政策》", "《儿童政策》")
        val tipsText = String.format(
            "欢迎使用%1\$s!\n在您使用%1\$s前，请您仔细阅读并了解《用户协议》、《隐私政策》和《儿童政策》。\n点击“同意”即表示您已阅读并同意全部条款。",
            resources.getString(R.string.app_name)
        )
        policyDialog = PolicyDialog.Builder(this)
            .dialogTitle("用户协议与隐私政策提示") //                .contentText(spannableStringBuilder)
            .webViewTitles(arrayOf("用户协议", "隐私政策", "儿童政策"))
            .contentText(
                tipsText,
                arrayOf<String>(policyURL, policyURL2, policyURL3),
                intArrayOf(Color.RED, Color.BLUE, Color.GREEN),
                keyTexts
            )
            .setNegativeButton("不同意并退出APP>>") {
                policyDialog.dismiss()
            }
            .setPositiveButton("同意") {
                Toast.makeText(this, "同意协议", Toast.LENGTH_SHORT).show()
                policyDialog.dismiss()
            }
            .show()
    }
}