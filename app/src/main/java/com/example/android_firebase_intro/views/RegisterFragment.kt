package com.example.android_firebase_intro.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.databinding.FragmentRegisterBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = binding.topAppBar

        toolbar.setNavigationOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val btnLogUp: Button = binding.btnNext
        val inputName: EditText = binding.inputName
        val inputEmail: EditText = binding.inputEmail
        val inputPassword: EditText = binding.inputPassword
        val inputRepeatPassword: EditText = binding.inputRepeatPassword

        btnLogUp.setOnClickListener {
            val name: String = inputName.text.toString().trim()
            val email: String = inputEmail.text.toString().trim()
            val password: String = inputPassword.text.toString().trim()
            val repeatPassword: String = inputRepeatPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                if (name.isEmpty()) binding.inputName.error = "Required"
                if (password.isEmpty()) binding.inputPassword.error = "Required"
                if (email.isEmpty()) binding.inputEmail.error = "Required"
            } else if (password != repeatPassword) {
                binding.inputRepeatPassword.error = "Not equal"
                binding.inputPassword.error = "Not equal"
            } else if (password.length <= 8) {
                binding.inputPassword.error = "Password must be > 8"
            } else {
                createUser(email, password, name) { uid ->
                    saveUserData(name, email, uid)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createUser(email: String, password: String, name: String, callback: (status: String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("RegisterFragment", "createUserWithEmail:success")

                    val user = auth.currentUser
                    if (user !== null) callback.invoke(user.uid)

                    view?.findNavController()
                        ?.navigate(R.id.action_registerFragment_to_splashFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("RegisterFragment", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUserData(name: String, email: String, uid: String) {
        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )
        db.collection("users").document(uid).set(user)
    }
}