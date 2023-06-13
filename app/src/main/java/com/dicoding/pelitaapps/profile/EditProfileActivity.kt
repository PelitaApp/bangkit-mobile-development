package com.dicoding.pelitaapps.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.customview.EditTextEmail
import com.dicoding.pelitaapps.customview.EditTextPassword
import com.dicoding.pelitaapps.databinding.ActivityEditProfileBinding
import com.dicoding.pelitaapps.login.LoginActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.etEditTextEmail.text.toString().trim()
        val phone = binding.edRegisterNomor.text.toString().trim()

        binding.btnSimpan.setOnClickListener {
            if (name.isEmpty()) {
                binding.edRegisterName.error = "Username harus diisi"

            }

            if (email.isEmpty()) {
                binding.etEditTextEmail.error = "Email harus diisi"

            }

            if (phone.isEmpty()) {
                binding.edRegisterNomor.error =
                    "Gunakan Format 082"

            }

            if (!isValidEmail(email)) {
                binding.etEditTextEmail.error = "Format Email menggunakan @gmail atau @yahoo"

            }

            val requestBody = FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("phone", phone)
                .build()

            val request = Request.Builder()
                .url("https://pelita-app.et.r.appspot.com/")
                .put(requestBody)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Gagal melakukan Update Akun",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Akun berhasil di Update, Silahkan Login ulang !! ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            logout()
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Akun Gagal di Update",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
    }

    private fun logout() {
        // Menghapus data sesi menggunakan SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Arahkan pengguna ke layar login atau halaman lain yang sesuai
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
