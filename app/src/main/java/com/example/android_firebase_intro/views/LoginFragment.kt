package com.example.android_firebase_intro.views

import android.content.Context
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister = binding.btnLoginRegister
        val btnLogin = binding.btnLogin
        // val btnLoginWithPhone = binding.btnLoginPhone
        val inputEmail: EditText = binding.inputEmail
        val inputPassword: EditText = binding.inputPassword

        btnLogin.setOnClickListener {
            it.hideKeyboard()

            val email: String = inputEmail.text.toString().trim()
            val password: String = inputPassword.text.toString().trim()

            if (!isEmpty(email) && !isEmpty(password)) {
                login(email, password) { user ->
                    Log.i("LoginFragment", "FirebaseUser: $user")
                    if (user != null) {
                        view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login(email: String, password: String, callback: (status: FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                Log.i("LoginFragment", "$task - ${task.isSuccessful}")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("LoginFragment", "signInWithEmail:success")
                    val user = auth.currentUser
                    callback.invoke(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("LoginFragment", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    callback.invoke(null)
                    // TODO: make stuffs
                }
            }
            .addOnFailureListener(requireActivity()) {
                Log.i("LoginFragment", "Failure")
                callback.invoke(null)
            }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}