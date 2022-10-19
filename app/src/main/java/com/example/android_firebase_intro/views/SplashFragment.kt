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

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        Handler(Looper.getMainLooper()).postDelayed({
            view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 4000)

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