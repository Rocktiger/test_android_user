package user.test.com.test_android_user

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import user.test.com.test_android_user.activity.AccountFlowActivity
import user.test.com.test_android_user.utils.IntentUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = sum(5, 7).toString()
        tv.setOnClickListener { view ->
            sumToString()
            logSum(5, 7)
            var intent = Intent()
            intent.setClass(this, AccountFlowActivity::class.java)
            startActivity(intent)

        }
        button.setOnClickListener { view ->
            if (tv.visibility == View.GONE) {
                visible()
            } else {
                gone()
            }
        }

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


}
