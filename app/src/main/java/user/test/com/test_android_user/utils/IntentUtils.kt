package user.test.com.test_android_user.utils

import android.content.Context
import android.content.Intent

object IntentUtils{
    fun intent(context: Context, claszz: Class<Any>) {
        var intent = Intent()
        intent.setClass(context, claszz)
        context.startActivity(intent)
    }
}

