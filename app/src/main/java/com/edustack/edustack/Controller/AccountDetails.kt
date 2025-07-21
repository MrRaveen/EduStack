package com.edustack.edustack.Controller

import android.util.Log
import androidx.lifecycle.ViewModel
import com.edustack.edustack.Models.StudentAccounts
import com.edustack.edustack.Models.updateRequest
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AccountDetails : ViewModel() {
    val db = Firebase.firestore

    suspend fun getStudentAccDetails(): List<StudentAccounts> = withContext(Dispatchers.IO) {
        try {
            suspendCoroutine<List<StudentAccounts>> { continuation ->
                val list1 = mutableListOf<StudentAccounts>()
                db.collection("StudentInfo")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val accObject = StudentAccounts(
                                id = document.id.toString(), // Add document ID
                                address = document.getString("Address") ?: "",
                                city = document.getString("City") ?: "",
                                contactNumber = document.getString("ContactNumber") ?: "",
                                dob = document.getTimestamp("DOB")?.toDate()?.toInstant()
                                    ?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: LocalDateTime.MIN,
                                email = document.getString("Email") ?: "",
                                firstName = document.getString("Fname") ?: "",
                                gender = document.getString("Gender") ?: "",
                                joinedDate = document.getTimestamp("JoinedDate")?.toDate()?.toInstant()
                                    ?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: LocalDateTime.MIN,
                                lastName = document.getString("Lname") ?: "",
                                school = document.getString("School") ?: ""
                            )
                            list1.add(accObject)
                        }
                        continuation.resume(list1)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        } catch (e: Exception) {
            throw Exception("Error fetching student data: ${e.message}")
        }
    }
    suspend fun updateAccDetails(updatedValues: updateRequest): Boolean = withContext(Dispatchers.IO) {
        try {

            // Get student doc ID from StudentInfoID
            val querySnapshot = db.collection("StudentAcc")
                .whereEqualTo("StudentInfoID", updatedValues.id)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.e("AccountDetails", "No matching document found for StudentInfoID: ${updatedValues.id}")
                return@withContext false
            }

            val document = querySnapshot.documents[0]

            // Update StudentAcc collection
            db.collection("StudentAcc").document(document.id)
                .update(
                    "UserName", updatedValues.userNameNew,
                    "Password", updatedValues.newPassword
                ).await()

            // Update StudentInfo collection
            db.collection("StudentInfo").document(updatedValues.id)
                .update(
                    "Address", updatedValues.address,
                    "City", updatedValues.city,
                    "ContactNumber", updatedValues.contactNumber,
                    "Email", updatedValues.email,
                    "Fname", updatedValues.firstName,
                    "Lname", updatedValues.lastName,
                    "School", updatedValues.school
                ).await()

            return@withContext true
        } catch (e: Exception) {
            Log.e("AccountDetails", "Error updating account details: ${e.message}")
            return@withContext false
        }
    }
}