package user.test.com.test_android_user

object test {

    @JvmStatic
    fun main(args: Array<String>) {
        parseInt("a")
    }

    fun parseInt(str: String): Int? {
        print("str == $str")
        return parseInt(str)
    }
}