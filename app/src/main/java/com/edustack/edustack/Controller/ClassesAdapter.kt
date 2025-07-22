package com.edustack.edustack.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edustack.edustack.Models.Course
import com.edustack.edustack.R
import com.google.android.material.button.MaterialButton

class ClassesAdapter(
    val classes: List<Course>,
    private val onDelete: (Course) -> Unit,
    private val onView: (Course) -> Unit,
    private val onUpdate: (Course) -> Unit
) : RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.className)
        val classTime: TextView = itemView.findViewById(R.id.classTime)
        val classDay: TextView = itemView.findViewById(R.id.classDay)
        val classHall: TextView = itemView.findViewById(R.id.classHall)
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)
        val btnView: MaterialButton = itemView.findViewById(R.id.viewClassBtn)
        val btnUpdate: MaterialButton = itemView.findViewById(R.id.updateBottomSheetBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.class_name_card, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val course = classes[position]

        holder.className.text = "Class name: ${course.Name}"
        holder.classTime.text = "Time: ${course.StartTime} - ${course.EndTime}"
        holder.classDay.text = "Day: ${course.WeekDay}"
        holder.classHall.text = "Default hall: ${course.DefaultHall}"

        holder.btnDelete.setOnClickListener { onDelete(course) }
        holder.btnView.setOnClickListener { onView(course) }
        holder.btnUpdate.setOnClickListener { onUpdate(course) }
    }

    override fun getItemCount() = classes.size
}