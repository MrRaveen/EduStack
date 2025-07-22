package com.edustack.edustack

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edustack.edustack.Controller.ClassesAdapter
import com.edustack.edustack.Controller.ClassesViewModel
import com.edustack.edustack.Models.Course
import com.edustack.edustack.Models.Hall
import com.edustack.edustack.Models.TeacherDropdown
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



//class AllClasses : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_all_classes)
//        val updateBtn = findViewById<Button>(R.id.updateBottomSheetBtn)
//        val viewClassBtn = findViewById<Button>(R.id.viewClassBtn)
//        viewClassBtn.setOnClickListener {
//            val dialogView = BottomSheetDialog(this)
//            val viewBottom = layoutInflater.inflate(R.layout.view_class_info, null)
//            val closePanelBtn = viewBottom.findViewById<Button>(R.id.closePanelBtn)
//            closePanelBtn.setOnClickListener {
//                dialogView.dismiss()
//            }
//            // set cancelable to avoid closing of dialog box when clicking on the screen.
//            dialogView.setCancelable(false)
//            // set content view to our view.
//            dialogView.setContentView(viewBottom)
//            // call a show method to display a dialog
//            dialogView.show()
//        }
//        updateBtn.setOnClickListener {
//            val dialog = BottomSheetDialog(this)
//
//            // inflate the layout file of bottom sheet
//            val view = layoutInflater.inflate(R.layout.bottom_sheet_update_class, null)
//
//            // initialize variable for dismiss button
//            val dismissButton = view.findViewById<Button>(R.id.cancelButton)
//
//            //start time
//            val timePicker = Dialog(this)
//            timePicker.setContentView(R.layout.time_picker)
//            //end time
//            val timePickerEnd = Dialog(this)
//            timePickerEnd.setContentView(R.layout.time_picker_end)
//
//            val getTimeStart: Button = view.findViewById<Button>(R.id.timeShowBottom)//button 1 (start time)
//            val timeUI: TimePicker = timePicker.findViewById(R.id.timePickerUI)//start date
//            val setTimeBtn: Button = timePicker.findViewById(R.id.setTimeButton)//start date
//
//            val getTimeEnd: Button = view.findViewById(R.id.timeShowEndBottom)//button 2 (end time)
//            val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)//end
//            val setTimeBtnEnd: Button = timePickerEnd.findViewById(R.id.setTimeButtonEnd)//end
//            getTimeEnd.setOnClickListener{
//                timePickerEnd.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePickerEnd.setCancelable(false)
//                timePickerEnd.show()
//            }
//            getTimeStart.setOnClickListener{
//                timePicker.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePicker.setCancelable(false)
//                timePicker.show()
//
//            }
//            timeUI.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                getTimeStart.setText(selectedTime)
//            }
//            timeUIEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                getTimeEnd.setText(selectedTime)
//            }
//            setTimeBtn.setOnClickListener {
//                timePicker.dismiss()
//            }
//            setTimeBtnEnd.setOnClickListener {
//                timePickerEnd.dismiss()
//            }
//
//            // on click event for dismiss button
//            dismissButton.setOnClickListener {
//                // call dismiss method to close the dialog
//                dialog.dismiss()
//            }
//            // set cancelable to avoid closing of dialog box when clicking on the screen.
//            dialog.setCancelable(false)
//            // set content view to our view.
//            dialog.setContentView(view)
//            // call a show method to display a dialog
//            dialog.show()
//        }
//    }
//}
class AllClasses : AppCompatActivity() {
    val hallDisplayMap = mutableMapOf<String, Hall>()
    val teacherDisplayMap = mutableMapOf<String, TeacherDropdown>()
    private lateinit var viewModel: ClassesViewModel
    private lateinit var adapter: ClassesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_classes)

        viewModel = ViewModelProvider(this)[ClassesViewModel::class.java]
        recyclerView = findViewById(R.id.classesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        adapter = ClassesAdapter(
            emptyList(),
            onDelete = { deleteClass(it) },
            onView = { viewClassDetails(it) },
            onUpdate = { showUpdateDialog(it) }
        )
        recyclerView.adapter = adapter

        // Load classes
        loadClasses()

        // Setup search
        val searchView = findViewById<SearchView>(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String): Boolean {
                filterClasses(newText)
                return true
            }
        })
    }

    private fun loadClasses() {
        lifecycleScope.launch {
            val classes = viewModel.getAllClasses()
            adapter = ClassesAdapter(
                classes,
                onDelete = { deleteClass(it) },
                onView = { viewClassDetails(it) },
                onUpdate = { showUpdateDialog(it) }
            )
            recyclerView.adapter = adapter
        }
    }

    private fun filterClasses(query: String) {
        val filtered = adapter.classes.filter {
            it.Name.contains(query, true) ||
                    it.Description.contains(query, true)
        }
        adapter = ClassesAdapter(
            filtered,
            onDelete = { deleteClass(it) },
            onView = { viewClassDetails(it) },
            onUpdate = { showUpdateDialog(it) }
        )
        recyclerView.adapter = adapter
    }

    private fun deleteClass(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Delete Class")
            .setMessage("Are you sure you want to delete ${course.Name}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    val success = viewModel.deleteClass(course.id)
                    if (success) {
                        Toast.makeText(this@AllClasses, "Class deleted", Toast.LENGTH_SHORT).show()
                        loadClasses()
                    } else {
                        Toast.makeText(this@AllClasses, "Deletion failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun viewClassDetails(course: Course) {
        val dialogView = BottomSheetDialog(this)
        val viewBottom = layoutInflater.inflate(R.layout.view_class_info, null)

        // Set class data
        viewBottom.findViewById<TextView>(R.id.classNameText).text = "Class name: ${course.Name}"
        viewBottom.findViewById<TextView>(R.id.DescriptionText).text = "Description: ${course.Description}"
        viewBottom.findViewById<TextView>(R.id.StartTimeText).text = "Start time: ${course.StartTime}"
        viewBottom.findViewById<TextView>(R.id.EndTimeText).text = "End time: ${course.EndTime}"
        viewBottom.findViewById<TextView>(R.id.DefaultHallNoText).text = "Hall number: ${course.DefaultHall}"
        viewBottom.findViewById<TextView>(R.id.TeacherText).text = "Teacher ID: ${course.TeacherID}"
        viewBottom.findViewById<TextView>(R.id.WeekNameText).text = "Week name: ${course.WeekDay}"

        // Load teacher name (optional)
        lifecycleScope.launch {
            val teacher = getTeacherName(course.TeacherID)
            viewBottom.findViewById<TextView>(R.id.TeacherNameText).text = "Teacher: $teacher"
        }

        viewBottom.findViewById<Button>(R.id.removeClassBtn).setOnClickListener {
            deleteClass(course)
            dialogView.dismiss()
        }

        viewBottom.findViewById<Button>(R.id.closePanelBtn).setOnClickListener {
            dialogView.dismiss()
        }

        dialogView.setContentView(viewBottom)
        dialogView.show()
    }

    private suspend fun getTeacherName(teacherId: String): String {
        return try {
            val doc = FirebaseFirestore.getInstance()
                .collection("Teacher")
                .document(teacherId)
                .get()
                .await()

            "${doc.getString("FirstName") ?: ""} ${doc.getString("LastName") ?: ""}"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun showUpdateDialog(course: Course) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_update_class, null)

        // Populate fields with current data
        view.findViewById<EditText>(R.id.className).setText(course.Name)
        view.findViewById<EditText>(R.id.descrionClass).setText(course.Description)
        view.findViewById<Button>(R.id.timeShowBottom).text = course.StartTime
        view.findViewById<Button>(R.id.timeShowEndBottom).text = course.EndTime

        // Initialize dropdowns
        setupHallDropdown(view, course.DefaultHall)
        setupTeacherDropdown(view, course.TeacherID)
        setupDayDropdown(view, course.WeekDay)

        // Time pickers
        setupTimePickers(view)

        view.findViewById<Button>(R.id.updateInfoBtn).setOnClickListener {
            updateClass(course, view, dialog)
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupHallDropdown(view: View, selectedHallId: String) {
        val dropdown = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)

        lifecycleScope.launch {
            val halls = viewModel.getAvailableHalls()
            halls.forEach { hall ->
                adapter.add("Hall ${hall.id} (Seats: ${hall.SeatCount})")
                if (hall.id == selectedHallId) {
                    dropdown.setText("Hall ${hall.id} (Seats: ${hall.SeatCount})", false)
                }
            }
        }

        dropdown.setAdapter(adapter)
    }

    private fun setupTeacherDropdown(view: View, selectedTeacherId: String) {
        val dropdown = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)

        lifecycleScope.launch {
            val teachers = viewModel.getAllTeachers()
            teachers.forEach { teacher ->
                adapter.add(teacher.name)
                if (teacher.id == selectedTeacherId) {
                    dropdown.setText(teacher.name, false)
                }
            }
        }

        dropdown.setAdapter(adapter)
    }

    private fun setupDayDropdown(view: View, selectedDay: String) {
        val dropdown = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDDay)
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, days)
        dropdown.setAdapter(adapter)
        dropdown.setText(selectedDay, false)
    }

    private fun setupTimePickers(view: View) {
        //            //start time
            val timePicker = Dialog(this)
            timePicker.setContentView(R.layout.time_picker)
            //end time
            val timePickerEnd = Dialog(this)
            timePickerEnd.setContentView(R.layout.time_picker_end)

            val getTimeStart: Button = view.findViewById<Button>(R.id.timeShowBottom)//button 1 (start time)
            val timeUI: TimePicker = timePicker.findViewById(R.id.timePickerUI)//start date
            val setTimeBtn: Button = timePicker.findViewById(R.id.setTimeButton)//start date

            val getTimeEnd: Button = view.findViewById(R.id.timeShowEndBottom)//button 2 (end time)
            val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)//end
            val setTimeBtnEnd: Button = timePickerEnd.findViewById(R.id.setTimeButtonEnd)//end
            getTimeEnd.setOnClickListener{
                timePickerEnd.window?.setLayout(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timePickerEnd.setCancelable(false)
                timePickerEnd.show()
            }
            getTimeStart.setOnClickListener{
                timePicker.window?.setLayout(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timePicker.setCancelable(false)
                timePicker.show()

            }
            timeUI.setOnTimeChangedListener { view, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                getTimeStart.setText(selectedTime)
            }
            timeUIEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                getTimeEnd.setText(selectedTime)
            }
            setTimeBtn.setOnClickListener {
                timePicker.dismiss()
            }
            setTimeBtnEnd.setOnClickListener {
                timePickerEnd.dismiss()
            }
    }

    private fun updateClass(course: Course, view: View, dialog: BottomSheetDialog) {
        val name = view.findViewById<EditText>(R.id.className).text.toString()
        val description = view.findViewById<EditText>(R.id.descrionClass).text.toString()
        val startTime = view.findViewById<Button>(R.id.timeShowBottom).text.toString()
        val endTime = view.findViewById<Button>(R.id.timeShowEndBottom).text.toString()

        // Get selected values
        val hallText = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall).text.toString()
        val teacherText = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher).text.toString()
        val day = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDDay).text.toString()

        // Extract IDs
        val hallId = hallText.substringAfter("Hall ").substringBefore(" ").trim()
        val teacherId = viewModel.teacherMap[teacherText]?.id ?: ""

        val updates = hashMapOf<String, Any>(
            "Name" to name,
            "Description" to description,
            "StartTime" to startTime,
            "EndTime" to endTime,
            "DefaultHall" to hallId,
            "TeacherID" to teacherId,
            "WeekDay" to day
        )

        lifecycleScope.launch {
            val success = viewModel.updateClass(course.id, updates)
            if (success) {
                Toast.makeText(this@AllClasses, "Class updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                loadClasses()
            } else {
                Toast.makeText(this@AllClasses, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}