package user.test.com.test_android_user.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rate_cal.*
import user.test.com.test_android_user.R
import user.test.com.test_android_user.utils.RateCalUtils


class RateCalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_cal)

        jisuan.setOnClickListener { jisuan() }
    }

    fun jisuan() {
        var shouyi = shouyi.text.toString()
        var cost = bengjin.text.toString()
        var time = shijian.text.toString()
        lilv.text = "日利率" + RateCalUtils.formatPercent(RateCalUtils.dayRate(shouyi.toDouble(), cost.toDouble(), time.toInt())) +
                "\n月利率" + RateCalUtils.formatPercent(RateCalUtils.monthRate(shouyi.toDouble(), cost.toDouble(), time.toInt())) +
                "\n年利率" + RateCalUtils.formatPercent(RateCalUtils.yearRate(shouyi.toDouble(), cost.toDouble(), time.toInt()))
    }

}
