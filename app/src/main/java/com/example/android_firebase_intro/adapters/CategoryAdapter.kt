package com.example.android_firebase_intro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_firebase_intro.R
import com.example.android_firebase_intro.models.Category

class CategoryAdapter(private val allCategories: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val categoryAmount: TextView = v.findViewById(R.id.item_category_amount)
        private val categoryText: TextView = v.findViewById(R.id.item_category_text)

        fun bindData(data: Category) {
            categoryText.text = data.name.toString()
            categoryAmount.text = data.nTasks.toString()
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