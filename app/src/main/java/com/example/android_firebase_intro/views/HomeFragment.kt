package com.example.android_firebase_intro.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.databinding.FragmentHomeBinding
import com.example.android_firebase_intro.models.Category
import com.example.android_firebase_intro.models.DataHolder
import com.example.android_firebase_intro.models.Task
import com.example.android_firebase_intro.views.home.TaskFragment
import com.example.android_firebase_intro.views.home.TimerFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.android_firebase_intro.models.User

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topAppBar = binding.homeTopAppBar

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    val logout = auth.signOut()
                    Log.i("HomeFragment", "${logout}")
                    view.findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
                }
            }
            true
        }

        getLoggedUserData()

        val bottomNavigationView = binding.bottomNavigationViewHome
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_tasks -> {
                    goToFragment(TaskFragment())
                    true
                }
                R.id.menu_timer -> {
                    goToFragment(TimerFragment())
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.menu_tasks


        // ==============
//        val myCategory = Category()
//        myCategory.name = "Comprar"
//        createCategory(myCategory)

//        readCategories()

//        val myTask = Task()
//        myTask.title = "Probar suspiro de Moya"
//        myTask.description = "Ir con Kevin"
//        myTask.priority = 2
//        myTask.categoryId = "PGYvnB73mNORL6W8F4sN"
//        createTask(myTask)

//        readAllPendingTasks("PGYvnB73mNORL6W8F4sN")

//        readAllDoneTasks("PGYvnB73mNORL6W8F4sN")

//        readTask("k7OdwSO5bBUenCEPr95b")
//
//        Handler(Looper.getMainLooper()).postDelayed(
//            {
//                // This method will be executed once the timer is over
//                deleteTask("k7OdwSO5bBUenCEPr95b")
//                readTask("k7OdwSO5bBUenCEPr95b")
//            },
//            1000 // value in milliseconds
//        )

//        setCompletedTask("a9WaeKZKugF2p7l1J3se")
//        readTask("a9WaeKZKugF2p7l1J3se")
    }

    private fun goToFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
    }

    private fun getLoggedUserData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_splashFragment)
        } else {
            val docReference = db.collection("users").document(currentUser.uid)

            val document = docReference
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val myUser = documentSnapshot.toObject(User::class.java)
                        topAppBar.title = "${topAppBar.title} de ${myUser?.name}"
                    } else {
                        Log.d("HomeFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("HomeFragment", "get failed with ", exception)
                }
            Log.i("HomeFragment", "user: $document")
//            if(document.isSuccessful) {
//                return document.result.data
//            }
        }
    }

    private fun createCategory(category: Category) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.CATEGORIES)
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.d("HomeFragment", "Categoría creada con el id: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.d("HomeFragment", "Error creando la categoría", e)
            }
    }

    fun createTask(task: Task) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .add(task)
            .addOnSuccessListener { documentReference ->
                Log.d("HomeFragment", "Tarea creada con el id: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure createTask", exception)
            }
    }

    private fun readAllDoneTasks(categoryId: String) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .whereEqualTo("categoryId", categoryId)
            .whereEqualTo("completed", true)
            .get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    // TODO: convert to object and save in ArrayList
                    Log.d("HomeFragment", "readAllTasksPending: ${task.id} => ${task.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure readAllPendingTasks", exception)
            }
    }

    private fun readAllPendingTasks(categoryId: String) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .whereEqualTo("categoryId", categoryId)
            .whereEqualTo("completed", false)
            .get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    // TODO: convert to object and save in ArrayList
                    Log.d("HomeFragment", "readAllTasksPending: ${task.id} => ${task.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure readAllPendingTasks", exception)
            }
    }

    fun readTask(taskId: String) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .document(taskId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("HomeFragment", "readTask data: ${document.data}")
                } else {
                    Log.d("HomeFragment", "No such readTask")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "get failed with ", exception)
            }
    }

    private fun deleteTask(taskId: String) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .document(taskId)
            .delete()
            .addOnSuccessListener { document ->
                Log.d("HomeFragment", "task deleted")
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure deleteTask", exception)
            }
    }

    private fun setCompletedTask(taskId: String) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .document(taskId)
            .update("completed", true)
            .addOnSuccessListener { document ->
                Log.d("HomeFragment", "task updated")
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure setCompletedTask", exception)
            }
    }
}