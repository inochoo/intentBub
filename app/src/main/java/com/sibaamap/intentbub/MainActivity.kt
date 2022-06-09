package com.sibaamap.intentbub

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

@Parcelize
data class Student(
    val name: String = "Anupam",
    val age: Int = 24
) : Parcelable

data class Blog(val name: String = "Androidly", val year: Int = 2022) : java.io.Serializable
class MainActivity : AppCompatActivity(), View.OnClickListener {

    fun Context.gotoClass(targetType: Class<*>) =
        ComponentName(this, targetType)

    fun Context.startActivity(f: Intent.() -> Unit): Unit =
        Intent().apply(f).run(this::startActivity)

    inline fun <reified T : Activity> Context.start(
        noinline createIntent: Intent.() -> Unit = {}
    ) = startActivity {
        component = gotoClass(T::class.java)
        createIntent(this)
    }

    var arrayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSimpleIntent.setOnClickListener(this)
        btnSimpleIntentAndData.setOnClickListener(this)
        btnParcelableIntent.setOnClickListener(this)
        btnSerializableIntent.setOnClickListener(this)
        btnBrowserIntent.setOnClickListener(this)
        btnMapsIntent.setOnClickListener(this)
        btnGenericIntent.setOnClickListener(this)

        arrayList.add("Androidly")
        arrayList.add("Android")
        arrayList.add("Intents")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSimpleIntent -> {
                val intent = Intent(this, OtherActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSimpleIntentAndData -> {
                val intent = Intent(this, OtherActivity::class.java)
                with(intent)
                {
                    putExtra("keyString", "Androidly String data")
                    putStringArrayListExtra("arrayList", arrayList)
                    putExtra("keyBoolean", true)
                    putExtra("keyFloat", 1.2f)
                }
                startActivity(intent)
            }
            R.id.btnParcelableIntent -> {

                val student = Student()
                val intent = Intent(this, OtherActivity::class.java)
                intent.putExtra("studentData", student)
                startActivity(intent)
            }
            R.id.btnSerializableIntent -> {
                val blog = Blog("a", 1)
                val intent = Intent(this, OtherActivity::class.java)
                intent.putExtra("blogData", blog as Serializable)
                startActivity(intent)
            }
            R.id.btnBrowserIntent -> {
                val url = "https://www.androidly.net"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "No application found", LENGTH_LONG).show()
                }
            }
            R.id.btnMapsIntent -> {
                val loc = "12.9538477,77.3507442"

                val addressUri = Uri.parse("geo:0,0?q=" + loc)
                val intent = Intent(Intent.ACTION_VIEW, addressUri)


                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "No application found", LENGTH_LONG).show()
                }
            }
            else -> start<OtherActivity> {
                putExtra("keyString", "Androidly Generic Intent")
            }
        }

    }
}