package com.example.demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val client = OkHttpClient()
    val apiUrl = "http://10.0.2.2/php-api/login.php" // Emulator localhost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login("test@email.com", "123456")
    }

    private fun login(email: String, password: String) {
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(apiUrl)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val json = JSONObject(it.string())
                    runOnUiThread {
                        if (json.getBoolean("success")) {
                            Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, json.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
