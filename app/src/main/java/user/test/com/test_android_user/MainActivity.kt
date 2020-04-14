package user.test.com.test_android_user

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import user.test.com.test_android_user.activity.RateCalActivity
import user.test.com.test_android_user.utils.AppContext
import user.test.com.test_android_user.utils.RateCalUtils


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppContext.init(getApplicationContext());

        ratecal.setOnClickListener { intentRateCal() }
    }

    fun intentRateCal() {
        var intent = Intent()
        intent.setClass(this, RateCalActivity::class.java)
        startActivity(intent)
    }

}
