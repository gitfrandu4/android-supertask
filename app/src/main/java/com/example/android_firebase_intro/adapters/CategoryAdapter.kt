package com.example.android_firebase_intro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.models.Category
import org.w3c.dom.Text

class CategoryAdapter(private val allCategories: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var v: View) : RecyclerView.ViewHolder(v) {

        val category_amount: TextView = v.findViewById(R.id.item_category_amount)
        val category_text: TextView = v.findViewById(R.id.item_category_text)

        fun bindData(data: Category) {
            category_text.text = data.name.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val data = allCategories[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return allCategories.size
    }
}