package com.emotus.app.ui.signup


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.emotus.app.data.remote.response.Register
import com.emotus.app.databinding.ActivitySignUpBinding
import com.emotus.app.ui.ViewModelFactory
import com.emotus.app.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvLoginPage.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            binding.btnSignUp.text = "Mencoba Buat Akun Baru..."
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusView = this.currentFocus
            if (currentFocusView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
            }

            val name = binding.etSinUpName.text.toString()
            val email = binding.etSinUpEmail.text.toString()
            val password = binding.etSinUpPassword.text.toString()
            val text = Register(password, email, name)
            viewModel.regis(text)

            val builder = AlertDialog.Builder(this)
                .setMessage("Berhasil Buat Akun Baru!")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }

    }
}