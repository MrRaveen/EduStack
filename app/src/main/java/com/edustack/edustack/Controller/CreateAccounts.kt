package com.edustack.edustack.Controller

import androidx.lifecycle.ViewModel
import com.edustack.edustack.Models.CreateStudentAccRequest
import com.edustack.edustack.Models.CreateTeacherAccRequest
import com.edustack.edustack.Models.TeacherAccounts
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CreateAccounts : ViewModel(){
    val db = Firebase.firestore

    suspend fun CreateStudentAccount(createData: CreateStudentAccRequest): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Create StudentInfo document
            val studentInfoRef = db.collection("StudentInfo").document()
            val studentInfoId = studentInfoRef.id  // Get the auto-generated ID

            val studentInfoData = hashMapOf(
                "Address" to createData.address,
                "City" to createData.city,
                "ContactNumber" to createData.contactNumber,
                "DOB" to createData.dob,  // Firestore Timestamp
                "Email" to createData.email,
                "Fname" to createData.firstName,
                "Lname" to createData.lastName,
                "School" to createData.school,
                "Gender" to createData.gender,
                "JoinedDate" to FieldValue.serverTimestamp()
            )

            studentInfoRef.set(studentInfoData).await()

            // 2. Create StudentAcc document
            val studentAccData = hashMapOf(
                "Password" to createData.password,
                "Status" to true,
                "StudentInfoID" to studentInfoId,  // Link to StudentInfo document
                "UserName" to createData.userName
            )

            db.collection("StudentAcc").document().set(studentAccData).await()

            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
    suspend fun CreateTeacherAccount(createData: CreateTeacherAccRequest): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Create TeacherInfo document
            val teacherInfoRef = db.collection("TeachersInfo").document()
            val teacherInfoId = teacherInfoRef.id  // Get auto-generated ID

            val teacherInfoData = hashMapOf(
                "Address" to createData.address,
                "City" to createData.city,
                "ContactNo" to createData.contactNumber,
                "DOB" to createData.dob,
                "Email" to createData.email,
                "Fname" to createData.firstName,
                "Lname" to createData.lastName,
                "Speciality" to createData.speciality,
                "Gender" to createData.gender,
                "JoinedDate" to FieldValue.serverTimestamp()
            )

            teacherInfoRef.set(teacherInfoData).await()

            // 2. Create TeacherAcc document
            val teacherAccData = hashMapOf(
                "Password" to createData.password,
                "Status" to "ACTIVE",  // Matches your screenshot
                "TeacherInfoID" to teacherInfoId,  // Reference to TeacherInfo
                "UserName" to createData.userName
            )

            db.collection("TeacherAcc").document().set(teacherAccData).await()

            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
}