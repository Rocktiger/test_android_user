package user.test.com.test_android_user

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import user.test.com.test_android_user.activity.AccountFlowActivity
import user.test.com.test_android_user.activity.CityPickerActivity
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import com.tbruyelle.rxpermissions.RxPermissions
import user.test.com.test_android_user.utils.*
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = sum(51, 7).toString()
        tv.setOnClickListener { view ->
            sumToString()
            logSum(5, 7)
            var intent = Intent()
            intent.setClass(this, AccountFlowActivity::class.java)
            startActivity(intent)

        }
        button.setOnClickListener { view ->
            //            RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE)
//                    .subscribe { granted ->
//                        if (granted!!) {
//                            Toast.makeText(this, getIMEI(this@MainActivity), Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            NotifcationPageHelper.open(this)

//            AESEncrypt.main();
        }
        city_button.setOnClickListener { view ->
            var intent = Intent()
            intent.setClass(this, CityPickerActivity::class.java)
            startActivity(intent)
        }
        AppContext.init(getApplicationContext());
        RSAEncrypt.main();
//        AESEncrypt.main();
    }

    fun sum(a: Int, b: Int) = a + b

    fun logSum(a: Int, b: Int) {
        var sumA = a
        var sumB = b
        Log.d("logSun", "sum of $sumA and $sumB is ${sumA + sumB}")
    }

    fun sumToString() {
        Toast.makeText(this@MainActivity, sum(5, 7).toString(), Toast.LENGTH_SHORT).show()
    }


    fun gone() {
        tv.visibility = View.GONE
        Toast.makeText(this@MainActivity, "隐藏", Toast.LENGTH_SHORT).show()
    }

    fun visible() {
        tv.visibility = View.VISIBLE
        Toast.makeText(this@MainActivity, "显示", Toast.LENGTH_SHORT).show()
    }

    fun getIMEI(context: Context): String {
        try {
            //实例化TelephonyManager对象
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            //获取IMEI号
            var imei: String? = telephonyManager.deviceId
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = ""
            }
            return imei
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

}
