package com.emotus.app.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.emotus.app.ui.MainActivity
import com.emotus.app.data.remote.response.Token
import com.emotus.app.databinding.ActivitySignInBinding
import com.emotus.app.models.UserModel
import com.emotus.app.ui.ViewModelFactory
import com.emotus.app.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<SIgnInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = Firebase.auth

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusView = this.currentFocus
            if (currentFocusView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
            }
            userLogin()

        }


    }

    private fun userLogin() {
        val email = binding.etSinInEmail.text.toString()
        val password = binding.etSinInPassword.text.toString()

        if (validateForm(email, password)) {
            binding.btnSignIn.text = "Mencoba Masuk..."
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val token: String? = tokenTask.result.token
                                val tok = Token(token!!)
                                viewModel.loginPost(tok)
                                viewModel.login.observe(this) { respon ->
                                    if (respon != null) {
                                        val builder = AlertDialog.Builder(this)
                                            .setMessage("Berhasil Masuk Akun!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK") { dialog, _ ->
                                                dialog.dismiss()
                                                viewModel.saveSession(
                                                    UserModel(
                                                        email = respon.user.email,
                                                        token = "Bearer $token"
                                                    )
                                                )

                                                startActivity(Intent(this, MainActivity::class.java))
                                                finish()
                                            }

                                        val alertDialog = builder.create()
                                        alertDialog.show()
                                    }
                                }
                            }
                        }
                    } else {
                        binding.btnSignIn.text = "Login"
                        val builder = AlertDialog.Builder(this)
                            .setMessage("Gagal Masuk Akun!")
                            .setCancelable(true)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alertDialog = builder.create()
                        alertDialog.show()
                        Log.e("LoginError", "Login failed: ${task.exception?.message}")
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                binding.tilEmail.error = "Enter an email address"
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Enter a valid email address"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding.tilPassword.error = "Enter a password"
                binding.tilEmail.error = null
                false
            }

            else -> {
                binding.tilEmail.error = null
                binding.tilPassword.error = null
                true
            }
        }
    }


}
