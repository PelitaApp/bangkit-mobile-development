package com.dicoding.pelitaapps.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityLoginBinding
import com.dicoding.pelitaapps.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tv_register = binding.tvRegister
        val btnLogin = binding.btnLogin
        val edEmail = binding.etEditTextEmail
        val edPassword = binding.etEditTextPassword

        // Digunakan untuk mengecek login

        // Digunakan untuk masuk halaman register
        tv_register.setOnClickListener {
            val register = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(register)
            finish()
        }

//        if (Build.VERSION.SDK_INT >= 33) {
//            val data = intent.getParcelableExtra("extra_email_username", LoginUser::class.java)
//
//            if (data != null) {
//
//            }
//        }

        btnLogin.setOnClickListener {
            if (edPassword.text?.isEmpty() == true) {
                edPassword.error = "Mohon untuk di Isi, Tidak boleh kosong !!"
            }

            if (edEmail.text?.isEmpty() == true) {
                edEmail.error = "Mohon isi dengan format @gmail"
            }

            if (edPassword.error == null && edEmail.error == null) {
                userLogin(edEmail.text.toString(), edPassword.text.toString())
            }
        }
    }

    private fun userLogin(email: String, password: String) {

    }
}