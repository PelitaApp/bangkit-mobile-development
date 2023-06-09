package com.dicoding.pelitaapps.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.pelitaapps.data.LoginUser
import com.dicoding.pelitaapps.databinding.ActivityRegisterBinding
import com.dicoding.pelitaapps.home.HomePageActivity
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

        //Get value from edit text
        val nameVal = binding.edRegisterName.text
        val emailVal = binding.etEditTextEmail.text
        val passwordVal = binding.etEditTextPassword.text

        binding.btnRegis.setOnClickListener {
            val edRegisterPasswordError = binding.etEditTextPassword.text
            // Digunakan
            // Jika Password yang dimasukan kurang dari 8 maka Button Register akan error
            if (edRegisterPasswordError?.length!! < 8) {
                binding.btnRegis.error =
                    "Password yang anda inputkan harus lebih dari 8 Karakter"
            } else {
                loadingProcess()
                mainViewModel.registerNewUser(
                    nameVal.toString(),
                    emailVal.toString(),
                    passwordVal.toString()
                ).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do something before going back to the previous activity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    private fun loadingProcess() {
        binding.progressBar.visibility = View.VISIBLE
        binding.edRegisterName.isCursorVisible = false
        binding.etEditTextEmail.isCursorVisible = false
        binding.etEditTextPassword.isCursorVisible = false
    }

    private fun wrongDataGiven() {
        binding.progressBar.visibility = View.GONE
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
}