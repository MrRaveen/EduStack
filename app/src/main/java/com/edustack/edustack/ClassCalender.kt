package com.edustack.edustack

import EventAdapter
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edustack.edustack.Controller.CalendarViewModel
import com.edustack.edustack.Models.CalendarEvent
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*



//class ClassCalender : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_class_calender)
//        //create calender event
//        //bottom sheet
//        val createCalender = findViewById<Button>(R.id.createCalenderEventBtn)
//        createCalender.setOnClickListener {
//            val dialog = BottomSheetDialog(this)
//            val view = layoutInflater.inflate(R.layout.edit_calender_event, null)
//
//            //pop up setup (calender)
//            val datePickerStart = Dialog(this)
//            datePickerStart.setContentView(R.layout.date_picker_start)
//
//            val changeEventDateBtn = view.findViewById<Button>(R.id.change_event_btn_new)//trigger btn for calender popup (create calender event)
//            val dateUIStart: DatePicker = datePickerStart.findViewById(R.id.datePickerStart)//date ui/ start
//            val dateActionBtnStart: Button = datePickerStart.findViewById(R.id.setDateStart)//date select btn/end
//            changeEventDateBtn.setOnClickListener {
//                datePickerStart.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                datePickerStart.setCancelable(false)
//                datePickerStart.show()
//            }
//            dateActionBtnStart.setOnClickListener {
//                with(dateUIStart){
//                    val day = dayOfMonth
//                    val month = month
//                    val year = year
//                    changeEventDateBtn.text = "$day/$month/$year"
//                }
//                datePickerStart.dismiss()
//            }
//            //end
//            //popup clock (start time)
//            //start time
//            val timePicker = Dialog(this)
//            timePicker.setContentView(R.layout.time_picker)
//            val startTiBot = view.findViewById<Button>(R.id.timeShowBottom)//start time trigger button
//            val timeUI: TimePicker = timePicker.findViewById(R.id.timePickerUI)//start date
//            val setTimeBtn: Button = timePicker.findViewById(R.id.setTimeButton)//start date
//            startTiBot.setOnClickListener {
//                timePicker.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePicker.setCancelable(false)
//                timePicker.show()
//            }
//            timeUI.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                startTiBot.setText(selectedTime)
//            }
//            setTimeBtn.setOnClickListener {
//                timePicker.dismiss()
//            }
//            //end
//            //popup clock (end time)
//            val timePickerEnd = Dialog(this)
//            timePickerEnd.setContentView(R.layout.time_picker_end)
//            val endTiBot = view.findViewById<Button>(R.id.timeShowEndBottom)//end time trigger button
//            val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)//end
//            val setTimeBtnEnd: Button = timePickerEnd.findViewById(R.id.setTimeButtonEnd)//end
//            endTiBot.setOnClickListener {
//                timePickerEnd.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePickerEnd.setCancelable(false)
//                timePickerEnd.show()
//            }
//            timeUIEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                endTiBot.setText(selectedTime)
//            }
//            setTimeBtnEnd.setOnClickListener {
//                timePickerEnd.dismiss()
//            }
//            //end
//            val dismissButton = view.findViewById<Button>(R.id.updateCancelButtonEvent)
//            dismissButton.setOnClickListener {
//                dialog.dismiss()
//            }
//            //change the btn name
//            val updateBtn = view.findViewById<Button>(R.id.updateInfoBtn)
//            updateBtn.text = "Create Event"
//            // set cancelable to avoid closing of dialog box when clicking on the screen.
//            dialog.setCancelable(false)
//            // set content view to our view.
//            dialog.setContentView(view)
//            // call a show method to display a dialog
//            dialog.show()
//        }
//        //TODO: end
//        //date picker part
//        val datePickerStart = Dialog(this)
//        datePickerStart.setContentView(R.layout.date_picker_start)
//        val dateUIStart: DatePicker = datePickerStart.findViewById(R.id.datePickerStart)//date ui/ start
//        val dateActionBtnStart: Button = datePickerStart.findViewById(R.id.setDateStart)//date select btn/end
//
//        val picDateBtn = findViewById<Button>(R.id.picDateBtn)
//        picDateBtn.setOnClickListener {
//            datePickerStart.window?.setLayout(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            datePickerStart.setCancelable(false)
//            dateActionBtnStart.setOnClickListener {
//                with(dateUIStart){
//                    val day = dayOfMonth
//                    val month = month
//                    val year = year
//                    picDateBtn.text = "$day/$month/$year"
//                }
//                datePickerStart.dismiss()
//            }
//            datePickerStart.show()
//        }
//        //calender popup
//        //bottom sheet
//        val updateBtn = findViewById<Button>(R.id.editEventBtn)
//        updateBtn.setOnClickListener {
//            val dialog = BottomSheetDialog(this)
//            val view = layoutInflater.inflate(R.layout.edit_calender_event, null)
//
//            //pop up setup (calender)
//            val datePickerStart = Dialog(this)
//            datePickerStart.setContentView(R.layout.date_picker_start)
//
//            val changeEventDateBtn = view.findViewById<Button>(R.id.change_event_btn_new)//trigger btn for calender popup
//            val dateUIStart: DatePicker = datePickerStart.findViewById(R.id.datePickerStart)//date ui/ start
//            val dateActionBtnStart: Button = datePickerStart.findViewById(R.id.setDateStart)//date select btn/end
//            changeEventDateBtn.setOnClickListener {
//                datePickerStart.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                datePickerStart.setCancelable(false)
//                datePickerStart.show()
//            }
//            dateActionBtnStart.setOnClickListener {
//                with(dateUIStart){
//                    val day = dayOfMonth
//                    val month = month
//                    val year = year
//                    changeEventDateBtn.text = "$day/$month/$year"
//                }
//                datePickerStart.dismiss()
//            }
//            //end
//            //popup clock (start time)
//            //start time
//            val timePicker = Dialog(this)
//            timePicker.setContentView(R.layout.time_picker)
//            val startTiBot = view.findViewById<Button>(R.id.timeShowBottom)//start time trigger button
//            val timeUI: TimePicker = timePicker.findViewById(R.id.timePickerUI)//start date
//            val setTimeBtn: Button = timePicker.findViewById(R.id.setTimeButton)//start date
//            startTiBot.setOnClickListener {
//                timePicker.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePicker.setCancelable(false)
//                timePicker.show()
//            }
//            timeUI.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                startTiBot.setText(selectedTime)
//            }
//            setTimeBtn.setOnClickListener {
//                timePicker.dismiss()
//            }
//            //end
//            //popup clock (end time)
//            val timePickerEnd = Dialog(this)
//            timePickerEnd.setContentView(R.layout.time_picker_end)
//            val endTiBot = view.findViewById<Button>(R.id.timeShowEndBottom)//end time trigger button
//            val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)//end
//            val setTimeBtnEnd: Button = timePickerEnd.findViewById(R.id.setTimeButtonEnd)//end
//            endTiBot.setOnClickListener {
//                timePickerEnd.window?.setLayout(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                timePickerEnd.setCancelable(false)
//                timePickerEnd.show()
//            }
//            timeUIEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
//                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
//                endTiBot.setText(selectedTime)
//            }
//            setTimeBtnEnd.setOnClickListener {
//                timePickerEnd.dismiss()
//            }
//            //end
//            val dismissButton = view.findViewById<Button>(R.id.updateCancelButtonEvent)
//            dismissButton.setOnClickListener {
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
class ClassCalender : AppCompatActivity() {
    private lateinit var viewModel: CalendarViewModel
    private lateinit var adapter: EventAdapter
    private var selectedDate = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_calender)

        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        setupRecyclerView()
        setupObservers()
        setupDatePicker()
        setupCreateButton()

        // Set initial date header
        findViewById<TextView>(R.id.dateHeader).text = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)

        // Load initial data
        viewModel.loadDropdownData()
        viewModel.loadEventsForDate(selectedDate)
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(emptyList(),
            onDelete = { eventId -> viewModel.deleteEvent(eventId) },
            onEdit = { event -> showEditDialog(event) }
        )
        findViewById<RecyclerView>(R.id.eventsRecyclerView).adapter = adapter
    }

    private fun setupObservers() {
        viewModel.events.observe(this) { events ->
            adapter.events = events  // Just update the existing adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupDatePicker() {
        val datePickerStart = Dialog(this).apply {
            setContentView(R.layout.date_picker_start)
        }

        val dateUIStart: DatePicker = datePickerStart.findViewById(R.id.datePickerStart)
        val dateActionBtnStart: Button = datePickerStart.findViewById(R.id.setDateStart)
        val picDateBtn = findViewById<Button>(R.id.picDateBtn)

        picDateBtn.text = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)

        picDateBtn.setOnClickListener {
            // Set initial date in picker
            val cal = Calendar.getInstance().apply { time = selectedDate }
            dateUIStart.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            dateActionBtnStart.setOnClickListener {
                selectedDate = Calendar.getInstance().apply {
                    set(dateUIStart.year, dateUIStart.month, dateUIStart.dayOfMonth)
                }.time

                picDateBtn.text = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)
                findViewById<TextView>(R.id.dateHeader).text = picDateBtn.text
                viewModel.loadEventsForDate(selectedDate)
                datePickerStart.dismiss()
            }
            datePickerStart.show()
        }
    }

    private fun setupCreateButton() {
        findViewById<Button>(R.id.createCalenderEventBtn).setOnClickListener {
            showCreateDialog()
        }
    }

    private fun showCreateDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.edit_calender_event, null)
        setupDialogViews(view, dialog, null)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showEditDialog(event: CalendarEvent) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.edit_calender_event, null)
        setupDialogViews(view, dialog, event)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupDialogViews(view: View, dialog: BottomSheetDialog, event: CalendarEvent?) {
        // Date Picker
        val datePickerDialog = Dialog(this).apply {
            setContentView(R.layout.date_picker_start)
        }

        val dateUI: DatePicker = datePickerDialog.findViewById(R.id.datePickerStart)
        val dateActionBtn: Button = datePickerDialog.findViewById(R.id.setDateStart)
        val dateBtn = view.findViewById<Button>(R.id.change_event_btn_new)

        // Time Pickers
        val timePickerStart = Dialog(this).apply { setContentView(R.layout.time_picker) }
        val timeUIStart: TimePicker = timePickerStart.findViewById(R.id.timePickerUI)
        val startTimeBtn = view.findViewById<Button>(R.id.timeShowBottom)

        val timePickerEnd = Dialog(this).apply { setContentView(R.layout.time_picker_end) }
        val timeUIEnd: TimePicker = timePickerEnd.findViewById(R.id.timePickerUIEnd)
        val endTimeBtn = view.findViewById<Button>(R.id.timeShowEndBottom)

        // Dropdowns
        val hallDropdown = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDHall)
        val courseDropdown = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteIDTeacher)

        // Initialize with event data if editing
        event?.let {
            dateBtn.text = SimpleDateFormat("dd/MM/yyyy").format(it.date)
            startTimeBtn.text = SimpleDateFormat("HH:mm").format(it.startTime)
            endTimeBtn.text = SimpleDateFormat("HH:mm").format(it.endTime)
            hallDropdown.setText(it.hallId)
            courseDropdown.setText(it.courseId)
        }

        // Set dropdown adapters
        viewModel.courses.observe(this) { courses ->
            courseDropdown.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, courses))
        }

        viewModel.halls.observe(this) { halls ->
            hallDropdown.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, halls))
        }

        // Date picker setup
        dateBtn.setOnClickListener {
            dateActionBtn.setOnClickListener {
                val selectedDate = Calendar.getInstance().apply {
                    set(dateUI.year, dateUI.month, dateUI.dayOfMonth)
                }.time
                dateBtn.text = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)
                datePickerDialog.dismiss()
            }
            datePickerDialog.show()
        }

//        // Start time picker
//        startTimeBtn.setOnClickListener {
//            timeUIStart.setOnTimeChangedListener { _, hour, minute ->
//                startTimeBtn.text = String.format("%02d:%02d", hour, minute)
//            }
//            timePickerStart.show()
//        }
//
//        // End time picker
//        endTimeBtn.setOnClickListener {
//            timeUIEnd.setOnTimeChangedListener { _, hour, minute ->
//                endTimeBtn.text = String.format("%02d:%02d", hour, minute)
//            }
//            timePickerEnd.show()
//        }
        // Start time picker
        startTimeBtn.setOnClickListener {
            timePickerStart.show()

            // Set initial time
            val currentTime = startTimeBtn.text.toString().split(":")
            if (currentTime.size == 2) {
                timeUIStart.hour = currentTime[0].toInt()
                timeUIStart.minute = currentTime[1].toInt()
            }

            timeUIStart.setOnTimeChangedListener { _, hour, minute ->
                startTimeBtn.text = String.format("%02d:%02d", hour, minute)
            }

            timePickerStart.findViewById<Button>(R.id.setTimeButton).setOnClickListener {
                timePickerStart.dismiss()
            }
        }

// End time picker
        endTimeBtn.setOnClickListener {
            timePickerEnd.show()

            // Set initial time
            val currentTime = endTimeBtn.text.toString().split(":")
            if (currentTime.size == 2) {
                timeUIEnd.hour = currentTime[0].toInt()
                timeUIEnd.minute = currentTime[1].toInt()
            }

            timeUIEnd.setOnTimeChangedListener { _, hour, minute ->
                endTimeBtn.text = String.format("%02d:%02d", hour, minute)
            }

            timePickerEnd.findViewById<Button>(R.id.setTimeButtonEnd).setOnClickListener {
                timePickerEnd.dismiss()
            }
        }

        // Save button
        val saveBtn = view.findViewById<Button>(R.id.updateInfoBtn)
        saveBtn.text = if (event == null) "Create Event" else "Update Event"

        saveBtn.setOnClickListener {
            try {
                // Get raw values
                val dateStr = dateBtn.text.toString()
                val startStr = startTimeBtn.text.toString()
                val endStr = endTimeBtn.text.toString()

                // Parse date
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(dateStr)!!

                // Parse times (just split the string)
                val (startHour, startMinute) = startStr.split(":").map { it.toInt() }
                val (endHour, endMinute) = endStr.split(":").map { it.toInt() }

                // Create calendar instances
                val startCal = Calendar.getInstance().apply {
                    time = date
                    set(Calendar.HOUR_OF_DAY, startHour)
                    set(Calendar.MINUTE, startMinute)
                    set(Calendar.SECOND, 0)
                }

                val endCal = Calendar.getInstance().apply {
                    time = date
                    set(Calendar.HOUR_OF_DAY, endHour)
                    set(Calendar.MINUTE, endMinute)
                    set(Calendar.SECOND, 0)
                }

                // Create event
                val newEvent = CalendarEvent(
                    id = event?.id ?: "",
                    courseId = courseDropdown.text.toString(),
                    hallId = hallDropdown.text.toString(),
                    date = date,
                    startTime = startCal.time,
                    endTime = endCal.time,
                    status = true
                )

                // Save to Firebase
                if (event == null) viewModel.createEvent(newEvent) else viewModel.updateEvent(newEvent)

                // Refresh events
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.loadEventsForDate(selectedDate)
                }, 1000)

                dialog.dismiss()

            } catch (e: Exception) {
                // Show error if something breaks
                Toast.makeText(this, "FUCKING ERROR: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        view.findViewById<Button>(R.id.updateCancelButtonEvent).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun combineDateAndTime(date: Date, time: Date): Date {
        val calDate = Calendar.getInstance().apply { this.time = date }
        val calTime = Calendar.getInstance().apply { this.time = time }

        calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY))
        calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE))
        calDate.set(Calendar.SECOND, 0)
        calDate.set(Calendar.MILLISECOND, 0)

        return calDate.time
    }
}
