package com.example.android_firebase_intro.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.android_firebase_intro.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        if(currentUser != null){
            view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            view.findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }

//        val btnHome = view.findViewById<Button>(R.id.btnSplashHome)
//        val btnlogin = view.findViewById<Button>(R.id.btnSplashLogin)
//
//        btnHome.setOnClickListener {
//            view.findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
//        }
//
//        btnlogin.setOnClickListener {
//            view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        }
    }
}