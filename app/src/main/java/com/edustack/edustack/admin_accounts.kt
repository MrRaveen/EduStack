
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.edustack.edustack.Controller.AccountDetails
import com.edustack.edustack.Controller.CreateAccounts
import com.edustack.edustack.Controller.GetStudentCount
import com.edustack.edustack.Controller.TeacherAccountDetails
import com.edustack.edustack.Models.CreateStudentAccRequest
import com.edustack.edustack.Models.CreateTeacherAccRequest
import com.edustack.edustack.Models.StudentAccounts
import com.edustack.edustack.Models.TeacherAccounts
import com.edustack.edustack.Models.TeacherUpdateRequest
import com.edustack.edustack.Models.updateRequest
import com.edustack.edustack.Notifications
import com.edustack.edustack.R
import com.edustack.edustack.databinding.ActivityAdminMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class admin_accounts : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var cardsContainer: LinearLayout
    val accountDetailsViewModel by viewModels<AccountDetails>()//acc details controller call
    val accountDetailsTeacher by viewModels<TeacherAccountDetails>()
    val CreateAccountsObj by viewModels<CreateAccounts>()
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
        return inflater.inflate(R.layout.fragment_admin_accounts, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardsContainer = view.findViewById(R.id.cardsContainer)//card section in the XML
        //notification part
        val notificationIcon = view.findViewById<ImageButton>(R.id.notificationButton)
        notificationIcon.setOnClickListener{
            val intent = Intent(requireContext(), Notifications::class.java) //creates the intent
            startActivity(intent)
        }
        //TODO: HERE
//        //handle view student info
//        val studentInfoView = view.findViewById<Button>(R.id.btnView)
//        studentInfoView.setOnClickListener {
//            val dialogViewStuAcc = BottomSheetDialog(requireContext())
//            val viewBottomAcc = layoutInflater.inflate(R.layout.view_student_acc_info, null)
//            val closePanelAccInfo = viewBottomAcc.findViewById<Button>(R.id.closeAccInfoBtn)
//            closePanelAccInfo.setOnClickListener {
//                dialogViewStuAcc.dismiss()
//            }
//            dialogViewStuAcc.setCancelable(false)
//            dialogViewStuAcc.setContentView(viewBottomAcc)
//            dialogViewStuAcc.show()
//        }
//        //handle edit students accounts
//        val studentAccEditBtn = view.findViewById<Button>(R.id.btnEdit)
//        studentAccEditBtn.setOnClickListener {
//            val dialogView = BottomSheetDialog(requireContext())
//            val viewBottom = layoutInflater.inflate(R.layout.edit_student_acc, null)
//            val closePanelBtn = viewBottom.findViewById<Button>(R.id.closeUpdatePanel)
//            closePanelBtn.setOnClickListener {
//                dialogView.dismiss()
//            }
//            dialogView.setCancelable(false)
//            dialogView.setContentView(viewBottom)
//            dialogView.show()
//        }
//TODO:HERE
        //TODO: Dropdown menu
        val accountTypeDropdown = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteID)
        val accountTypes = listOf(
            "Student",
            "Teacher"
        )
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            accountTypes
        )
        if (accountTypeDropdown != null) {
            accountTypeDropdown.setAdapter(adapter)
        }
        if (accountTypeDropdown != null) {
            lifecycleScope.launch {
                try {
                    val students = accountDetailsViewModel.getStudentAccDetails()
                    renderStudentCards(students)//render student acc details as cards
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            accountTypeDropdown.setOnItemClickListener { _, _, position, _ ->
                if(accountTypes[position] == "Student"){
                    lifecycleScope.launch {
                        try {
                            val students = accountDetailsViewModel.getStudentAccDetails()
                            renderStudentCards(students)//render student acc details as cards
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else if(accountTypes[position] == "Teacher") {
                    lifecycleScope.launch {
                        try {
                            val teachers = accountDetailsTeacher.getTeacherAccountDetails()
                            //Toast.makeText(requireContext(), "out: ${teachers.toString()}", Toast.LENGTH_SHORT).show()
                            renderTeacherCards(teachers)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
//        //TODO: Add teacher
//        val addTeacherBtn = view.findViewById<Button>(R.id.btnAddTeacher)
//        addTeacherBtn.setOnClickListener {
//            val dialogView = BottomSheetDialog(requireContext())
//            val viewBottom = layoutInflater.inflate(R.layout.create_new_teacher, null)
//            val closePanelBtn = viewBottom.findViewById<Button>(R.id.closeCreateAccTeaPanel)
//            closePanelBtn.setOnClickListener {
//                dialogView.dismiss()
//            }
//            //handle calender part
//            //date popup
//            val datePickerStart = Dialog(requireContext())
//            datePickerStart.setContentView(R.layout.date_picker_start)
//            val dateTrigButton = viewBottom.findViewById<Button>(R.id.dateForDOBTeacher)//panel side triger
//            val dateUIStart: DatePicker = datePickerStart.findViewById(R.id.datePickerStart)//date ui/ start
//            val dateActionBtnStart: Button = datePickerStart.findViewById(R.id.setDateStart)//date select btn/end
//            dateTrigButton.setOnClickListener {
//                datePickerStart.window?.setLayout(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                datePickerStart.setCancelable(false)
//                datePickerStart.show()
//            }
//            dateActionBtnStart.setOnClickListener {
//                with(dateUIStart) {
//                    val day = dayOfMonth
//                    val month = month
//                    val year = year
//                    dateTrigButton.text = "$day/$month/$year"
//                }
//                datePickerStart.dismiss()
//            }
//            dialogView.setCancelable(false)
//            dialogView.setContentView(viewBottom)
//            dialogView.show()
//        }

        val addTeacherBtn = view.findViewById<Button>(R.id.btnAddTeacher)
        addTeacherBtn.setOnClickListener {
            val dialogView = BottomSheetDialog(requireContext())
            val viewBottom = layoutInflater.inflate(R.layout.create_new_teacher, null)

            // Initialize UI components
            val firstNameET = viewBottom.findViewById<EditText>(R.id.FirstName)
            val lastNameET = viewBottom.findViewById<EditText>(R.id.lastName)
            val addressET = viewBottom.findViewById<EditText>(R.id.address)
            val specialityET = viewBottom.findViewById<EditText>(R.id.schoolName)  // Note: schoolName is actually speciality
            val emailET = viewBottom.findViewById<EditText>(R.id.email)
            val contactET = viewBottom.findViewById<EditText>(R.id.contactNumber)
            val cityET = viewBottom.findViewById<EditText>(R.id.city)
            val usernameET = viewBottom.findViewById<EditText>(R.id.userNameNew)
            val passwordET = viewBottom.findViewById<EditText>(R.id.userPassword)
            val confirmPasswordET = viewBottom.findViewById<EditText>(R.id.confirmPassword)
            val dateBtn = viewBottom.findViewById<Button>(R.id.dateForDOBTeacher)
            val createBtn = viewBottom.findViewById<Button>(R.id.createStudentAccBtn)  // Note: Same ID as student

            // Add gender selection (add spinner to XML)
            val genderSpinner = viewBottom.findViewById<Spinner>(R.id.genderSpinner)
            val genders = arrayOf("Male", "Female", "Other")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter

            // Date picker setup
            val datePickerDialog = DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, day ->
                    // Format date as "dd/MM/yyyy"
                    dateBtn.text = "${day.toString().padStart(2, '0')}/${(month + 1).toString().padStart(2, '0')}/$year"
                }
            }

            dateBtn.setOnClickListener {
                datePickerDialog.show()
            }

            createBtn.setOnClickListener {
                // Collect data
                val firstName = firstNameET.text.toString()
                val lastName = lastNameET.text.toString()
                val address = addressET.text.toString()
                val speciality = specialityET.text.toString()
                val email = emailET.text.toString()
                val contact = contactET.text.toString()
                val city = cityET.text.toString()
                val username = usernameET.text.toString()
                val password = passwordET.text.toString()
                val confirmPassword = confirmPasswordET.text.toString()
                val gender = genderSpinner.selectedItem.toString()
                val dobString = dateBtn.text.toString()
                // Validate data
                if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() ||
                    speciality.isEmpty() || email.isEmpty() || contact.isEmpty() ||
                    city.isEmpty() || username.isEmpty() || password.isEmpty() ||
                    dobString == "Select here" || gender.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Convert DOB string to Firestore Timestamp
                val dobTimestamp = try {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(dobString)
                    Timestamp(date.time / 1000, (date.time % 1000).toInt() * 1000000)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Invalid date format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Create request object
                val createData = CreateTeacherAccRequest(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    city = city,
                    contactNumber = contact,
                    email = email,
                    speciality = speciality,
                    dob = dobTimestamp,
                    gender = gender,
                    userName = username,
                    password = password
                )

                // Call ViewModel to create account
                lifecycleScope.launch {
                    try {
                        val success = CreateAccountsObj.CreateTeacherAccount(createData)
                        if (success) {
                            Toast.makeText(requireContext(), "Teacher account created", Toast.LENGTH_SHORT).show()
                            dialogView.dismiss()
                            // Refresh fragment
                            view?.findViewById<FrameLayout>(R.id.panelID1Accounts)?.removeAllViews()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.panelID1Accounts, admin_accounts())
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), "Failed to create account", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewBottom.findViewById<Button>(R.id.closeCreateAccTeaPanel).setOnClickListener {
                dialogView.dismiss()
            }
            dialogView.setCancelable(false)
            dialogView.setContentView(viewBottom)
            dialogView.show()
        }

        //TODO: Add student
        val addStudentBtn = view?.findViewById<Button>(R.id.btnAddStudent)
        addStudentBtn?.setOnClickListener {
            val dialogView = BottomSheetDialog(requireContext())
            val viewBottom = layoutInflater.inflate(R.layout.create_new_student, null)

            // Initialize UI components
            val firstNameET = viewBottom.findViewById<EditText>(R.id.FirstName)
            val lastNameET = viewBottom.findViewById<EditText>(R.id.lastName)
            val addressET = viewBottom.findViewById<EditText>(R.id.address)
            val schoolET = viewBottom.findViewById<EditText>(R.id.schoolName)
            val emailET = viewBottom.findViewById<EditText>(R.id.email)
            val contactET = viewBottom.findViewById<EditText>(R.id.contactNumber)
            val cityET = viewBottom.findViewById<EditText>(R.id.city)
            val usernameET = viewBottom.findViewById<EditText>(R.id.userNameNew)
            val passwordET = viewBottom.findViewById<EditText>(R.id.userPassword)
            val confirmPasswordET = viewBottom.findViewById<EditText>(R.id.confirmPassword)
            val dateBtn = viewBottom.findViewById<Button>(R.id.dateForDOB)
            val createBtn = viewBottom.findViewById<Button>(R.id.createStudentAccBtn)

            // Add gender selection (add this to your XML)
            val genderSpinner = viewBottom.findViewById<Spinner>(R.id.genderSpinner)
            val genders = arrayOf("Male", "Female", "Other")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter

            // Date picker setup
            val datePickerDialog = DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, day ->
                    // Format date as "dd/MM/yyyy"
                    dateBtn.text = "${day.toString().padStart(2, '0')}/${(month + 1).toString().padStart(2, '0')}/$year"
                }
            }

            dateBtn.setOnClickListener {
                datePickerDialog.show()
            }

            createBtn.setOnClickListener {
                // Collect data
                val firstName = firstNameET.text.toString()
                val lastName = lastNameET.text.toString()
                val address = addressET.text.toString()
                val school = schoolET.text.toString()
                val email = emailET.text.toString()
                val contact = contactET.text.toString()
                val city = cityET.text.toString()
                val username = usernameET.text.toString()
                val password = passwordET.text.toString()
                val confirmPassword = confirmPasswordET.text.toString()
                val gender = genderSpinner.selectedItem.toString()
                val dobString = dateBtn.text.toString()

                // Validate data
                if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() ||
                    school.isEmpty() || email.isEmpty() || contact.isEmpty() ||
                    city.isEmpty() || username.isEmpty() || password.isEmpty() ||
                    dobString == "Select here" || gender.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Convert DOB string to Firestore Timestamp
                val dobTimestamp = try {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(dobString)
                    Timestamp(date.time / 1000, (date.time % 1000).toInt() * 1000000)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Invalid date format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Create request object
                val createData = CreateStudentAccRequest(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    city = city,
                    contactNumber = contact,
                    email = email,
                    school = school,
                    dob = dobTimestamp,  // Use the Timestamp
                    gender = gender,
                    userName = username,
                    password = password
                )

                // Call ViewModel to create account
                lifecycleScope.launch {
                    try {
                        val success = CreateAccountsObj.CreateStudentAccount(createData)
                        if (success) {
                            Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                            dialogView.dismiss()

                            // Refresh fragment
                            view?.findViewById<FrameLayout>(R.id.panelID1Accounts)?.removeAllViews()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.panelID1Accounts, admin_accounts())
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), "Failed to create account", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewBottom.findViewById<Button>(R.id.closeCreateAccStuPanel).setOnClickListener {
                dialogView.dismiss()
            }

            dialogView.setCancelable(false)
            dialogView.setContentView(viewBottom)
            dialogView.show()
        }
        //TODO: setup students count UI
         val studentCountViewModel by viewModels<GetStudentCount>()
        lifecycleScope.launch {
            try{
                val stuCount = studentCountViewModel.getStudentCount()
                val stuCountText = view.findViewById<TextView>(R.id.studentCount)
                val teacherCountText = view.findViewById<TextView>(R.id.teacherCountAmu)
                stuCountText.text = stuCount.toString()
                teacherCountText.text = studentCountViewModel.getTeacherCount().toString()
            }catch(e : Exception){
                Toast.makeText(
                    requireContext(),
                    "Error occured when setting data: ${e.toString()}",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
    private fun renderTeacherCards(teachers: List<TeacherAccounts>) {
        cardsContainer.removeAllViews()  // Clear existing views
        val inflater = LayoutInflater.from(requireContext())

        teachers.forEach { teacher ->
            val cardView = inflater.inflate(R.layout.student_card_acc, cardsContainer, false) as CardView

            // Set teacher information
            cardView.findViewById<TextView>(R.id.tvName).text = "Name: ${teacher.FirstName} ${teacher.LastName}"
            cardView.findViewById<TextView>(R.id.tvAccountId).text = "Account ID: ${teacher.id}"
            cardView.findViewById<TextView>(R.id.tvEmail).text = "Email: ${teacher.Speciality}"

            // Edit Button Click
            cardView.findViewById<MaterialButton>(R.id.btnEdit).setOnClickListener {
                val dialogView = BottomSheetDialog(requireContext())
                val viewBottom = layoutInflater.inflate(R.layout.edit_teacher_info, null)

                // Pre-fill form with current data
                viewBottom.findViewById<EditText>(R.id.FirstName).setText(teacher.FirstName)
                viewBottom.findViewById<EditText>(R.id.lastName).setText(teacher.LastName)
                viewBottom.findViewById<EditText>(R.id.address).setText(teacher.Address)
                viewBottom.findViewById<EditText>(R.id.speciality).setText(teacher.Speciality)
                viewBottom.findViewById<EditText>(R.id.email).setText(teacher.Email)
                viewBottom.findViewById<EditText>(R.id.contactNumber).setText(teacher.ContactNumber)
                viewBottom.findViewById<EditText>(R.id.city).setText(teacher.City)

                // Set up buttons
                val updateButton = viewBottom.findViewById<Button>(R.id.updateInfoBtn)
                val closePanelBtn = viewBottom.findViewById<Button>(R.id.closeUpdatePanel)

                //update btn
                updateButton.setOnClickListener {
                    var count = 0
                    // Validation checks (same as student)
                    val fields = mapOf(
                        R.id.FirstName to "First name",
                        R.id.lastName to "Last name",
                        R.id.address to "Address",
                        R.id.speciality to "Speciality",
                        R.id.email to "Email",
                        R.id.contactNumber to "Contact number",
                        R.id.city to "City"
                    )

                    fields.forEach { (id, name) ->
                        val field = viewBottom.findViewById<EditText>(id)
                        if (field.text.isNullOrBlank()) {
                            field.setBackgroundColor(Color.parseColor("#FFD8D8"))
                            field.setText("$name is empty")
                            count++
                        }
                    }

                    if (count == 0) {
                        // Create update request
                        val updateData = TeacherUpdateRequest(
                            id = teacher.id,
                            address = viewBottom.findViewById<EditText>(R.id.address).text.toString(),
                            city = viewBottom.findViewById<EditText>(R.id.city).text.toString(),
                            contactNumber = viewBottom.findViewById<EditText>(R.id.contactNumber).text.toString(),
                            email = viewBottom.findViewById<EditText>(R.id.email).text.toString(),
                            firstName = viewBottom.findViewById<EditText>(R.id.FirstName).text.toString(),
                            lastName = viewBottom.findViewById<EditText>(R.id.lastName).text.toString(),
                            speciality = viewBottom.findViewById<EditText>(R.id.speciality).text.toString()
                        )

                        lifecycleScope.launch {
                            try {
                                val result = accountDetailsTeacher.updateTeacherDetails(updateData)
                                if (result) {
                                    dialogView.dismiss()
                                    Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
                                    //TODO: REFRESH PART
                                    view?.findViewById<FrameLayout>(R.id.panelID1Accounts)
                                        ?.removeAllViews()
                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val fragmentTransaction = fragmentManager.beginTransaction()
                                    fragmentTransaction.replace(R.id.panelID1Accounts, admin_accounts())
                                    fragmentTransaction.commit()
                                } else {
                                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                closePanelBtn.setOnClickListener { dialogView.dismiss() }
                dialogView.setCancelable(false)
                dialogView.setContentView(viewBottom)
                dialogView.show()
            }

            // Delete Button Click
            cardView.findViewById<MaterialButton>(R.id.btnDelete).setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Teacher Account")
                    .setMessage("Are you sure you want to delete this teacher account?")
                    .setPositiveButton("Delete") { dialog, _ ->
                        lifecycleScope.launch {
                            try {
                                // Assuming you have a ViewModel function for deleting teachers
                                val result = accountDetailsTeacher.removeTeacherAccount(teacher.id)
                                if (result) {
                                    Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
                                    //Refresh part
                                    view?.findViewById<FrameLayout>(R.id.panelID1Accounts)
                                        ?.removeAllViews()
                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val fragmentTransaction = fragmentManager.beginTransaction()
                                    fragmentTransaction.replace(R.id.panelID1Accounts, admin_accounts())
                                    fragmentTransaction.commit()
                                } else {
                                    Toast.makeText(requireContext(), "Deletion failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
            }

            // View Button Click
            cardView.findViewById<MaterialButton>(R.id.btnView).setOnClickListener {
                val dialogViewTeacherAcc = BottomSheetDialog(requireContext())
                val viewBottomAcc = layoutInflater.inflate(R.layout.view_teacher_acc_info, null)

                // Set teacher details
                viewBottomAcc.findViewById<TextView>(R.id.Fname).text = "First name: ${teacher.FirstName}"
                viewBottomAcc.findViewById<TextView>(R.id.Lname).text = "Last name: ${teacher.LastName}"
                viewBottomAcc.findViewById<TextView>(R.id.speciality).text = "Speciality: ${teacher.Speciality}"
                viewBottomAcc.findViewById<TextView>(R.id.joinedDate).text = "Joined date: ${teacher.JoinedDate}"
                viewBottomAcc.findViewById<TextView>(R.id.gender).text = "Gender: ${teacher.Gender}"
                viewBottomAcc.findViewById<TextView>(R.id.mail).text = "Email: ${teacher.Email}"
                viewBottomAcc.findViewById<TextView>(R.id.DOB).text = "Date of birth: ${teacher.DOB}"
                viewBottomAcc.findViewById<TextView>(R.id.contactNo).text = "Contact: ${teacher.ContactNumber}"
                viewBottomAcc.findViewById<TextView>(R.id.city).text = "City: ${teacher.City}"
                viewBottomAcc.findViewById<TextView>(R.id.Address).text = "Address: ${teacher.Address}"
                viewBottomAcc.findViewById<Button>(R.id.closeAccInfoBtnTeacher).setOnClickListener {
                    dialogViewTeacherAcc.dismiss()
                }
                dialogViewTeacherAcc.setCancelable(false)
                dialogViewTeacherAcc.setContentView(viewBottomAcc)
                dialogViewTeacherAcc.show()
            }

            cardsContainer.addView(cardView)
        }
    }

    private fun renderStudentCards(students: List<StudentAccounts>) {
        cardsContainer.removeAllViews()//remove contents
        val inflater = LayoutInflater.from(requireContext())

        //loop of students data to cards
        students.forEach { student ->
            val cardView = inflater.inflate(R.layout.student_card_acc, cardsContainer, false) as CardView//combination of cards

            //get acc details
            cardView.findViewById<TextView>(R.id.tvName).text = "Name: ${student.firstName} ${student.lastName}"
            cardView.findViewById<TextView>(R.id.tvAccountId).text = "Account ID: ${student.id}"
            cardView.findViewById<TextView>(R.id.tvEmail).text = "Email: ${student.email}"

            cardView.findViewById<MaterialButton>(R.id.btnEdit).setOnClickListener {
                //edit stu acc
                val dialogView = BottomSheetDialog(requireContext())
                val viewBottom = layoutInflater.inflate(R.layout.edit_student_acc, null)
                viewBottom.findViewById<EditText>(R.id.FirstName).setText(student.firstName)
                viewBottom.findViewById<EditText>(R.id.lastName).setText(student.lastName)
                viewBottom.findViewById<EditText>(R.id.address).setText(student.address)
                viewBottom.findViewById<EditText>(R.id.schoolName).setText(student.school)
                viewBottom.findViewById<EditText>(R.id.email).setText(student.email)
                viewBottom.findViewById<EditText>(R.id.contactNumber).setText(student.contactNumber)
                viewBottom.findViewById<EditText>(R.id.city).setText(student.city)
                //buttons
                val updateButton = viewBottom.findViewById<Button>(R.id.updateInfoBtn)
                val closePanelBtn = viewBottom.findViewById<Button>(R.id.closeUpdatePanel)
                updateButton.setOnClickListener {
                    var count : Int = 0
                    if (viewBottom.findViewById<EditText>(R.id.FirstName).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.FirstName)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.FirstName).setText("First name is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.lastName).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.lastName)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.lastName).setText("Last name is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.address).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.address)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.address).setText("Address is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.schoolName).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.schoolName)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.schoolName).setText("School name is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.email).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.email)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.email).setText("Email is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.contactNumber).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.contactNumber)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.contactNumber).setText("Contact number is empty")
                        count++
                    }

                    if (viewBottom.findViewById<EditText>(R.id.city).text.isNullOrBlank()) {
                        viewBottom.findViewById<EditText>(R.id.city)
                            .setBackgroundColor(Color.parseColor("#FFD8D8"))
                        viewBottom.findViewById<EditText>(R.id.city).setText("City is empty")
                        count++
                    }
                    if(count != 0){
                        //update call
                        val updateData = updateRequest(
                            id = student.id,
                            address = viewBottom.findViewById<EditText>(R.id.address).text.toString(),
                            city = viewBottom.findViewById<EditText>(R.id.city).text.toString(),
                            contactNumber = viewBottom.findViewById<EditText>(R.id.contactNumber).text.toString(),
                            email = viewBottom.findViewById<EditText>(R.id.email).text.toString(),
                            firstName = viewBottom.findViewById<EditText>(R.id.FirstName).text.toString(),
                            lastName = viewBottom.findViewById<EditText>(R.id.lastName).text.toString(),
                            school = viewBottom.findViewById<EditText>(R.id.schoolName).text.toString(),
                            userNameNew = viewBottom.findViewById<EditText>(R.id.userNameNew).text.toString(),
                            newPassword = viewBottom.findViewById<EditText>(R.id.userPassword).text.toString()
                        )
                        lifecycleScope.launch {
                            try{
                                val result = accountDetailsViewModel.updateAccDetails(updateData)
                                if(result == true){
                                    dialogView.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "Data updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    view?.findViewById<FrameLayout>(R.id.panelID1Accounts)
                                        ?.removeAllViews()
                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val fragmentTransaction = fragmentManager.beginTransaction()
                                    fragmentTransaction.replace(R.id.panelID1Accounts, admin_accounts())
                                    fragmentTransaction.commit()
                                }else{
                                    Toast.makeText(
                                        requireContext(),
                                        "Error occured when updating data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }catch (e : Exception){
                                Toast.makeText(
                                    requireContext(),
                                    "Error occured when updating data: ${e.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
            closePanelBtn.setOnClickListener {
                dialogView.dismiss()
            }
            dialogView.setCancelable(false)
            dialogView.setContentView(viewBottom)
            dialogView.show()
            }

            cardView.findViewById<MaterialButton>(R.id.btnDelete).setOnClickListener {
                // remove account
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete this student account?")
                    .setPositiveButton("Delete") { dialog, _ ->
                        lifecycleScope.launch {
                            try {
                                val result = accountDetailsViewModel.removeAccount(student.id)
                                if (result == true) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Account deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Refresh the fragment
                                    view?.findViewById<FrameLayout>(R.id.panelID1Accounts)
                                        ?.removeAllViews()
                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val fragmentTransaction = fragmentManager.beginTransaction()
                                    fragmentTransaction.replace(R.id.panelID1Accounts, admin_accounts())
                                    fragmentTransaction.commit()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to delete account",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            cardView.findViewById<MaterialButton>(R.id.btnView).setOnClickListener {
                //view student info
                val dialogViewStuAcc = BottomSheetDialog(requireContext())
                val viewBottomAcc = layoutInflater.inflate(R.layout.view_student_acc_info, null)
                //access UI parts
                viewBottomAcc.findViewById<TextView>(R.id.Fname).text = "Student first name: ${student.firstName}"
                viewBottomAcc.findViewById<TextView>(R.id.Lname).text = "Student last name: ${student.lastName}"
                viewBottomAcc.findViewById<TextView>(R.id.school).text = "School: ${student.school}"
                viewBottomAcc.findViewById<TextView>(R.id.joinedDate).text = "Joined date: ${student.joinedDate}"
                viewBottomAcc.findViewById<TextView>(R.id.gender).text = "Gender: ${student.gender}"
                viewBottomAcc.findViewById<TextView>(R.id.mail).text = "Email: ${student.email}"
                viewBottomAcc.findViewById<TextView>(R.id.DOB).text = "Date of birth: ${student.dob}"
                viewBottomAcc.findViewById<TextView>(R.id.contactNo).text = "Contact number: ${student.contactNumber}"
                viewBottomAcc.findViewById<TextView>(R.id.city).text = "City: ${student.city}"
                viewBottomAcc.findViewById<TextView>(R.id.Address).text = "Address: ${student.address}"
                val closePanelAccInfo = viewBottomAcc.findViewById<Button>(R.id.closeAccInfoBtn)
                closePanelAccInfo.setOnClickListener {
                   dialogViewStuAcc.dismiss()
                }
                dialogViewStuAcc.setCancelable(false)
                dialogViewStuAcc.setContentView(viewBottomAcc)
                dialogViewStuAcc.show()
            }
            cardsContainer.addView(cardView)
        }

    }
//    private fun showEditStudentDialog(student: StudentAccounts) {
//        val dialog = BottomSheetDialog(requireContext())
//        val view = layoutInflater.inflate(R.layout.edit_student_acc, null)
//
//        // Populate fields with student data
//        view.findViewById<EditText>(R.id.etFirstName).setText(student.firstName)
//        view.findViewById<EditText>(R.id.etLastName).setText(student.lastName)
//        // Populate other fields similarly...
//
//        view.findViewById<Button>(R.id.updateStudentBtn).setOnClickListener {
//            // Handle update logic
//            updateStudent(student.id)
//            dialog.dismiss()
//        }
//
//        dialog.setContentView(view)
//        dialog.show()
//    }
//    private fun deleteStudentAccount(studentId: String) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                Firebase.firestore.collection("StudentInfo")
//                    .document(studentId)
//                    .delete()
//                    .await()
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), "Student deleted", Toast.LENGTH_SHORT).show()
//                    // Refresh data
//                    val viewModel by viewModels<AccountDetails>()
//                    renderStudentCards(viewModel.getStudentAccDetails())
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

//    private fun showStudentDetails(student: StudentAccounts) {
//        val dialog = BottomSheetDialog(requireContext())
//        val view = layoutInflater.inflate(R.layout.view_student_acc_info, null)
//
//        // Populate view with student data
//        view.findViewById<TextView>(R.id.tvFullName).text = "${student.firstName} ${student.lastName}"
//        view.findViewById<TextView>(R.id.tvEmail).text = student.email
//        // Populate other fields...
//
//        view.findViewById<Button>(R.id.closeAccInfoBtn).setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.setContentView(view)
//        dialog.show()
//    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_accounts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}