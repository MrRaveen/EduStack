package com.edustack.edustack.Controller

import android.util.Log
import androidx.lifecycle.ViewModel
import com.edustack.edustack.Models.StudentAccounts
import com.edustack.edustack.Models.TeacherAccounts
import com.edustack.edustack.Models.TeacherUpdateRequest
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId

class TeacherAccountDetails : ViewModel() {
    val db = Firebase.firestore
    suspend fun getTeacherAccountDetails(): List<TeacherAccounts> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection("TeachersInfo").get().await()

            val teachers = mutableListOf<TeacherAccounts>()
            for (document in querySnapshot.documents) {
                try {
                    val teacher = TeacherAccounts(
                        id = document.id,  // Get document ID
                        Address = document.getString("Address") ?: "",
                        City = document.getString("City") ?: "",
                        ContactNumber = document.getString("ContactNo") ?: "",
                        DOB = convertTimestamp(document.getTimestamp("DOB")),
                        Email = document.getString("Email") ?: "",
                        FirstName = document.getString("Fname") ?: "",
                        Gender = document.getString("Gender") ?: "",
                        JoinedDate = convertTimestamp(document.getTimestamp("JoinedDate")),
                        LastName = document.getString("Lname") ?: "",
                        Speciality = document.getString("Speciality") ?: ""
                    )
                    teachers.add(teacher)
                } catch (e: Exception) {
                    // Log error but continue processing other documents
                    e.printStackTrace()
                }
            }
            return@withContext teachers

        } catch (e: Exception) {
            throw Exception("Error fetching teacher accounts: ${e.message}")
        }
    }
    suspend fun updateTeacherDetails(updateDataTeacher: TeacherUpdateRequest): Boolean = withContext(Dispatchers.IO) {
        try {
            // Update TeacherInfo collection directly using the ID
            db.collection("TeachersInfo").document(updateDataTeacher.id)
                .update(
                    "Address", updateDataTeacher.address,
                    "City", updateDataTeacher.city,
                    "ContactNo", updateDataTeacher.contactNumber,
                    "Email", updateDataTeacher.email,
                    "Fname", updateDataTeacher.firstName,
                    "Lname", updateDataTeacher.lastName,
                    "Speciality", updateDataTeacher.speciality
                ).await()

            return@withContext true
        } catch (e: Exception) {
            Log.e("TeacherUpdate", "Error updating teacher details: ${e.message}")
            return@withContext false
        }
    }
    suspend fun removeTeacherAccount(teacherID: String): Boolean = withContext(Dispatchers.IO){
        try {
            // 1. Find the student document using StudentInfoID
            val querySnapshot = db.collection("TeacherAcc")
                .whereEqualTo("TeacherInfoID", teacherID)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return@withContext false
            }

            val document = querySnapshot.documents[0]
            val docId = document.id

            // 2. Delete from StudentAcc collection
            db.collection("TeacherAcc").document(docId)
                .delete()
                .await()

            // 3. Delete from StudentInfo collection
            db.collection("TeachersInfo").document(teacherID)
                .delete()
                .await()
            return@withContext true
        }catch (e : Exception){
            throw Exception("Error removing teacher account: ${e.message}")
        }
    }
    private fun convertTimestamp(timestamp: Timestamp?): LocalDateTime {
        return timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
            ?: LocalDateTime.now()
    }
}