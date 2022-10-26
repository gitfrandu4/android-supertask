package com.example.android_firebase_intro.views.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.adapters.CategoryAdapter
import com.example.android_firebase_intro.models.Category
import com.example.android_firebase_intro.models.DataHolder
import com.example.android_firebase_intro.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TaskFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private lateinit var adapter: CategoryAdapter
    private val allCategories: ArrayList<Category> = arrayListOf()
    private val myPendingTasks: ArrayList<Task> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        // RecyclerView y Adapter
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_task_categories)
        adapter = CategoryAdapter(allCategories)
        recycler.adapter = adapter

        readCategories()
    }

    private fun readCategories() {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.CATEGORIES)
            .get()
            .addOnSuccessListener { categories ->
                for (category in categories) {
                    // TODO: Convert to object and save in arrayList
                    Log.d("TaskFragment", "${category.id} => ${category.data}")

                    val myCategory = category.toObject(Category::class.java)
                    myCategory.id = category.id
                    allCategories.add(myCategory)
                }
                adapter.notifyItemChanged(allCategories.size - 1)
                readAllPendingTasks()
            }
            .addOnFailureListener { exception ->
                Log.d("TaskFragment", "Error readCategories", exception)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readAllPendingTasks() {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .whereEqualTo("completed", false)
            .get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    val myTask = task.toObject(Task::class.java)
                    myPendingTasks.add(myTask)
                }
                for (category in allCategories) {
                    val count = myPendingTasks.filter {
                        it.categoryId == category.id
                    }.size
                    category.nTasks = count
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "Failure readAllPendingTasks", exception)
            }
    }

}