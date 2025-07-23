package com.edustack.edustack

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.edustack.edustack.Controller.ClassesViewModel
import com.edustack.edustack.Models.Course
import com.edustack.edustack.Models.Hall
import com.edustack.edustack.Models.TeacherDropdown
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class admin_classes : Fragment() {
<<<<<<< HEAD
    private lateinit var viewModel: ClassesViewModel
    private lateinit var hallAdapter: ArrayAdapter<String>
    private lateinit var teacherAdapter: ArrayAdapter<String>
    private lateinit var dayAdapter: ArrayAdapter<String>

    private val hallMap = mutableMapOf<String, Hall>()
    private val teacherMap = mutableMapOf<String, TeacherDropdown>()
    // TODO: Rename and change types of parameters
=======
>>>>>>> upstream/main
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
<<<<<<< HEAD
        viewModel = ViewModelProvider(this)[ClassesViewModel::class.java]
        // Initialize dropdowns
        setupDropdowns()
        // Load data
        loadHallsAndTeachers()
        // Handle add class button
        view.findViewById<MaterialButton>(R.id.btnAddStudent).setOnClickListener {
            createNewClass()
        }
        //calender part
=======

        // Calendar button
>>>>>>> upstream/main
        val calenderBtn: Button = view.findViewById(R.id.btnNewButton)
        calenderBtn.setOnClickListener {
            val intentCalender = Intent(activity, ClassCalender::class.java)
            startActivity(intentCalender)
        }

        // View all classes button
        val btnViewAllClasses: Button = view.findViewById(R.id.btnViewClasses)
        btnViewAllClasses.setOnClickListener {
            val intent = Intent(activity, AllClasses::class.java)
            startActivity(intent)
        }

        // Time Picker Dialogs
        val timePicker = Dialog(requireContext())
        timePicker.setContentView(R.layout.time_picker)

        val timePickerEnd = Dialog(requireContext())
        timePickerEnd.setContentView(R.layout.time_picker_end)

        val getTimeStart: Button = view.findViewById(R.id.timeShow)
        val timeUI: TimePicker = timePicker.findViewById(R.id.timePickerUI)
        val setTimeBtn: Button = timePicker.findViewById(R.id.setTimeButton)

        val getTimeEnd: Button = view.findViewById(R.id.timeShowEnd)
        val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)
        val setTimeBtnEnd: Button = timePickerEnd.findViewById(R.id.setTimeButtonEnd)

        getTimeStart.setOnClickListener {
            timePicker.window?.setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            timePicker.setCancelable(false)
            timePicker.show()
        }

        getTimeEnd.setOnClickListener {
            timePickerEnd.window?.setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            timePickerEnd.setCancelable(false)
            timePickerEnd.show()
        }

        timeUI.setOnTimeChangedListener { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            getTimeStart.text = selectedTime
        }

        timeUIEnd.setOnTimeChangedListener { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            getTimeEnd.text = selectedTime
        }

        setTimeBtn.setOnClickListener {
            timePicker.dismiss()
        }

        setTimeBtnEnd.setOnClickListener {
            timePickerEnd.dismiss()
        }
    }
    private fun setupDropdowns() {
        // Hall dropdown
        val hallDropdown = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall)
        hallAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        hallDropdown?.setAdapter(hallAdapter)

        // Teacher dropdown
        val teacherDropdown = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher)
        teacherAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        teacherDropdown?.setAdapter(teacherAdapter)

        // Day dropdown
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val dayDropdown = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDDay)
        dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, days)
        dayDropdown?.setAdapter(dayAdapter)
    }
    private fun loadHallsAndTeachers() {
        lifecycleScope.launch {
            // Load halls
            val halls = viewModel.getAvailableHalls()
            hallMap.clear()
            halls.forEach { hall ->
                val displayText = "Hall ${hall.id} (Seats: ${hall.SeatCount}, AC: ${if (hall.AirCondition) "Yes" else "No"})"
                hallMap[displayText] = hall
            }
            hallAdapter.clear()
            hallAdapter.addAll(hallMap.keys.toList())

            // Load teachers
            val teachers = viewModel.getAllTeachers()
            teacherMap.clear()
            teachers.forEach { teacher ->
                teacherMap[teacher.name] = teacher
            }
            teacherAdapter.clear()
            teacherAdapter.addAll(teacherMap.keys.toList())
        }
    }
    private fun createNewClass() {
        val className = view?.findViewById<EditText>(R.id.className)?.text.toString()
        val description = view?.findViewById<EditText>(R.id.descrionClass)?.text.toString()
        val startTime = view?.findViewById<Button>(R.id.timeShow)?.text.toString()
        val endTime = view?.findViewById<Button>(R.id.timeShowEnd)?.text.toString()

        // Get selected hall
        val hallSelection = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall)?.text.toString()
        val hall = hallMap[hallSelection]?.id ?: ""

        // Get selected teacher
        val teacherSelection = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher)?.text.toString()
        val teacherId = teacherMap[teacherSelection]?.id ?: ""

        // Get selected day
        val day = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDDay)?.text.toString()

        // Validate inputs
        if (className.isEmpty() || description.isEmpty() || startTime == "06:30" ||
            endTime == "06:30" || hall.isEmpty() || teacherId.isEmpty() || day.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val course = Course(
            DefaultHall = hall,
            Description = description,
            EndTime = endTime,
            Name = className,
            StartTime = startTime,
            TeacherID = teacherId,
            WeekDay = day
        )

        lifecycleScope.launch {
            val success = viewModel.createNewClass(course)
            if (success) {
                Toast.makeText(requireContext(), "Class created successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            } else {
                Toast.makeText(requireContext(), "Failed to create class", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun clearForm() {
        view?.findViewById<EditText>(R.id.className)?.text?.clear()
        view?.findViewById<EditText>(R.id.descrionClass)?.text?.clear()
        view?.findViewById<Button>(R.id.timeShow)?.text = "06:30"
        view?.findViewById<Button>(R.id.timeShowEnd)?.text = "06:30"
        view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall)?.text?.clear()
        view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher)?.text?.clear()
        view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDDay)?.text?.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_classes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
