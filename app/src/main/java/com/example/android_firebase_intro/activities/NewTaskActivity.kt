package com.example.android_firebase_intro.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.models.Category
import com.example.android_firebase_intro.models.DataHolder
import com.example.android_firebase_intro.models.Task
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NewTaskActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    lateinit var adapterCategories: ArrayAdapter<String>
    private val allCategories: ArrayList<Category> = arrayListOf()
    private val allCategoriesOfString: ArrayList<String> = arrayListOf()
    var prioridad = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        auth = Firebase.auth

        readCategories()

        adapterCategories = ArrayAdapter(this, R.layout.item_dropdown, allCategoriesOfString)
        val txtAuto = findViewById<AutoCompleteTextView>(R.id.txtAuto)

        txtAuto.setAdapter(adapterCategories)

        val btnAddCategory = findViewById<ImageButton>(R.id.btnAddCategory)
        btnAddCategory.setOnClickListener {
            showdialog()
        }

        val btnToogle = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)

        btnToogle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        prioridad = 0
                        toggleButton[1].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[2].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[0].setBackgroundColor(Color.parseColor("#349ebc"))
                    }
                    R.id.button2 -> {
                        prioridad = 1
                        toggleButton[0].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[2].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[1].setBackgroundColor(Color.parseColor("#f8a308"))
                    }
                    R.id.button3 -> {
                        prioridad = 2
                        toggleButton[0].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[1].setBackgroundColor(Color.parseColor("#7C4C4F5E"))
                        toggleButton[2].setBackgroundColor(Color.parseColor("#DA343C"))
                    }
                }
            }
        }

        val title = findViewById<TextInputEditText>(R.id.txtTitle)
        val descr = findViewById<TextInputEditText>(R.id.txtDescription)

        val btnAdd = findViewById<Button>(R.id.btnAddTask)
        btnAdd.setOnClickListener {
            val newTask = Task()
            newTask.title = title.text.toString()
            newTask.description = descr.text.toString()
            newTask.priority = prioridad

            val result = allCategories.find { it.name == txtAuto.text.toString() }
            if (result != null) {
                newTask.categoryId = result.id
            }

            createTask(newTask)
        }

    }

    fun createTask(miTarea: Task) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.TASKS)
            .add(miTarea)
            .addOnSuccessListener { documentReference ->
                Log.d("SUPER", "Tarea creada con el id: ${documentReference.id}")
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("SUPER", "Error creando la tarea", e)
            }
    }

    fun showdialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Añadir categoria")

        val input = EditText(this)

        input.setHint("Escribir categoria")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Añadir") { dialog, which ->
            val m_Text = input.text.toString()
            val newCat = Category()
            newCat.name = m_Text

            // TODO comprobar si ya existe

            createCategory(newCat)
        }

        builder.setNegativeButton("Cancelar", {
                dialog,
                which -> dialog.cancel() })

        builder.show()
    }

    fun createCategory(miCategory: Category) {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.CATEGORIES)
            .add(miCategory)
            .addOnSuccessListener { documentReference ->
                Log.d("SUPER", "Categoria creada con el id: ${documentReference.id}")
                readCategories()
            }
            .addOnFailureListener { e ->
                Log.w("SUPER", "Error creando la categoria", e)
            }
    }

    fun readCategories() {
        db.collection(DataHolder.USERS)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.CATEGORIES)
            .get()
            .addOnSuccessListener { categories ->
                allCategories.clear()
                allCategoriesOfString.clear()

                for (category in categories) {
                    val mCategory = category.toObject(Category::class.java)
                    mCategory.id = category.id

                    allCategories.add(mCategory)
                    mCategory.name?.let { allCategoriesOfString.add(it) }
                }

                adapterCategories.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.w("SUPER", "Error readCategories: ", exception)
            }

    }
}