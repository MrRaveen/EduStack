package com.edustack.edustack.Controller

import androidx.lifecycle.ViewModel
import com.edustack.edustack.Models.Course
import com.edustack.edustack.Models.Hall
import com.edustack.edustack.Models.TeacherDropdown
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClassesViewModel : ViewModel() {
    private val hallMap = mutableMapOf<String, Hall>()
    val teacherMap = mutableMapOf<String, TeacherDropdown>()
    private val db = FirebaseFirestore.getInstance()

    // Fetch available halls
    suspend fun getAvailableHalls(): List<Hall> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection("HallInformation")
                .whereEqualTo("AvailabilityStatus", true)
                .get()
                .await()

            return@withContext querySnapshot.documents.map { doc ->
                Hall(
                    id = doc.id,
                    AirCondition = doc.getBoolean("AirCondition") ?: false,
                    AvailabilityStatus = doc.getBoolean("AvailabilityStatus") ?: false,
                    SeatCount = doc.getLong("SeatCount")?.toInt() ?: 0
                )
            }
        } catch (e: Exception) {
            return@withContext emptyList()
        }
    }

    // Fetch all teachers
    suspend fun getAllTeachers(): List<TeacherDropdown> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection("TeachersInfo")
                .get()
                .await()

            return@withContext querySnapshot.documents.map { doc ->
                val firstName = doc.getString("Fname") ?: ""
                val lastName = doc.getString("Lname") ?: ""
                TeacherDropdown(
                    id = doc.id,
                    name = "$firstName $lastName"
                )
            }
        } catch (e: Exception) {
            return@withContext emptyList()
        }
    }

    // Create new class
    suspend fun createNewClass(course: Course): Boolean = withContext(Dispatchers.IO) {
        try {
            val data = hashMapOf(
                "DefaultHall" to course.DefaultHall,
                "Description" to course.Description,
                "EndTime" to course.EndTime,
                "Name" to course.Name,
                "StartTime" to course.StartTime,
                "StartedDate" to FieldValue.serverTimestamp(),
                "Status" to true,
                "TeacherID" to course.TeacherID,
                "WeekDay" to course.WeekDay,
                "isCanceled" to false
            )

            db.collection("Cources").add(data).await()
            return@withContext true
        } catch (e: Exception) {
            return@withContext false
        }
    }
    suspend fun getAllClasses(): List<Course> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection("Cources")
                .get()
                .await()

            return@withContext querySnapshot.documents.map { doc ->
                Course(
                    id = doc.id,
                    DefaultHall = doc.getString("DefaultHall") ?: "",
                    Description = doc.getString("Description") ?: "",
                    EndTime = doc.getString("EndTime") ?: "",
                    Name = doc.getString("Name") ?: "",
                    StartTime = doc.getString("StartTime") ?: "",
                    StartedDate = doc.get("StartedDate") ?: FieldValue.serverTimestamp(),
                    Status = doc.getBoolean("Status") ?: true,
                    TeacherID = doc.getString("TeacherID") ?: "",
                    WeekDay = doc.getString("WeekDay") ?: "",
                    isCanceled = doc.getBoolean("isCanceled") ?: false
                )
            }
        } catch (e: Exception) {
            return@withContext emptyList()
        }
    }

    // Delete a class
    suspend fun deleteClass(classId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            db.collection("Cources").document(classId).delete().await()
            return@withContext true
        } catch (e: Exception) {
            return@withContext false
        }
    }

    // Update a class
    suspend fun updateClass(classId: String, updates: Map<String, Any>): Boolean = withContext(Dispatchers.IO) {
        try {
            db.collection("Cources").document(classId).update(updates).await()
            return@withContext true
        } catch (e: Exception) {
            return@withContext false
        }
    }
}