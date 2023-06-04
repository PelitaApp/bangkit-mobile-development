package com.dicoding.pelitaapps.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.pelitaapps.data.LoginUser
import com.dicoding.pelitaapps.databinding.ActivityRegisterBinding
import com.dicoding.pelitaapps.login.LoginActivity
import com.dicoding.pelitaapps.viewmodel.MainViewModel
import com.dicoding.pelitaapps.viewmodel.ViewModelFactory
import com.dicoding.pelitaapps.remotedata.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameVal = binding.edRegisterName.text
        val emailVal = binding.etEditTextEmail.text
        val passwordVal = binding.etEditTextPassword.text

        binding.btnRegis.setOnClickListener {
            val edRegisPasswordError = binding.etEditTextPassword.text

            if (edRegisPasswordError?.length!! < 8) {
                binding.btnRegis.error = "Password yang anda inputkan harus lebih dari 8 Karakter"
            } else {
                loadingProcess()
                mainViewModel.registerNewUser(
                    nameVal.toString(),
                    passwordVal.toString(),
                    emailVal.toString()
                ).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            val response = result.data
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            sendDataToLoginActivity(
                                LoginUser(
                                    emailVal.toString(),
                                    passwordVal.toString()
                                )
                            )
                        }

                        is Result.Error -> {
                            val errorMessage = result.error
                            wrongDataGiven()
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun wrongDataGiven() {
        binding.edRegisterName.isCursorVisible = true
        binding.etEditTextEmail.isCursorVisible = true
        binding.etEditTextPassword.isCursorVisible = true
    }

    private fun sendDataToLoginActivity(data: LoginUser) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.putExtra("extra_email_username", data)
        startActivity(intent)
        finish()
    }

    private fun loadingProcess() {
        binding.edRegisterName.isCursorVisible = false
        binding.etEditTextEmail.isCursorVisible = false
        binding.etEditTextPassword.isCursorVisible = false
    }
}