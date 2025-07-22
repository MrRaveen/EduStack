package com.edustack.edustack.Controller

// CalendarViewModel.kt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edustack.edustack.Models.CalendarEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

//class CalendarViewModel : ViewModel() {
//    private val db = FirebaseFirestore.getInstance()
//    private val eventsRef = db.collection("Calender")
//    var selectedDate = ""
//
//    fun createEvent(event: Map<String, Any>) {
//        eventsRef.add(event)
//    }
//
//    fun updateEvent(eventId: String, updates: Map<String, Any>) {
//        eventsRef.document(eventId).update(updates)
//    }
//
//    fun deleteEvent(eventId: String) {
//        eventsRef.document(eventId).delete()
//    }
//
//    fun getEventsByDate(date: String, callback: (List<Pair<String, Map<String, Any>>>) -> Unit) {
//        eventsRef.whereEqualTo("Date", date)
//            .get()
//            .addOnSuccessListener { documents ->
//                val events = documents.map { it.id to it.data!! }
//                callback(events)
//            }
//    }
//}

class CalendarViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val events = MutableLiveData<List<CalendarEvent>>()
    val courses = MutableLiveData<List<String>>()
    val halls = MutableLiveData<List<String>>()

//    fun loadEventsForDate(date: Date) {
//        db.collection("Calender")
//            .whereEqualTo("Date", date)
//            .get()
//            .addOnSuccessListener { result ->
//                val eventList = result.map { doc ->
//                    CalendarEvent(
//                        id = doc.id,
//                        courseId = doc.getString("CourseID") ?: "",
//                        hallId = doc.getString("Ha11ID") ?: "",
//                        date = doc.getDate("Date") ?: Date(),
//                        startTime = doc.getDate("StartTime") ?: Date(),
//                        endTime = doc.getDate("EndTime") ?: Date(),
//                        status = doc.getBoolean("Status") ?: true
//                    )
//                }
//                events.value = eventList
//            }
//    }
fun loadEventsForDate(date: Date) {
    // Convert to start of day for query
    val cal = Calendar.getInstance().apply { time = date }
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val startOfDay = cal.time

    // End of day
    cal.add(Calendar.DAY_OF_MONTH, 1)
    val endOfDay = cal.time

    db.collection("Calender")
        .whereGreaterThanOrEqualTo("Date", startOfDay)
        .whereLessThan("Date", endOfDay)
        .get()
        .addOnSuccessListener { result ->
            val eventList = result.map { doc ->
                CalendarEvent(
                    id = doc.id,
                    courseId = doc.getString("CourseID") ?: "",
                    hallId = doc.getString("Ha11ID") ?: "",
                    date = doc.getDate("Date") ?: Date(),
                    startTime = doc.getDate("StartTime") ?: Date(),
                    endTime = doc.getDate("EndTime") ?: Date(),
                    status = doc.getBoolean("Status") ?: true
                )
            }
            events.value = eventList
        }
}


    fun loadDropdownData() {
        // Load courses
        db.collection("Courses").get().addOnSuccessListener {
            courses.value = it.documents.map { doc -> doc.id }
        }

        // Load halls
        db.collection("Halls").get().addOnSuccessListener {
            halls.value = it.documents.map { doc -> doc.id }
        }
    }

    fun createEvent(event: CalendarEvent) {
        val data = hashMapOf(
            "CourseID" to event.courseId,
            "Ha11ID" to event.hallId,
            "Date" to event.date,
            "StartTime" to event.startTime,
            "EndTime" to event.endTime,
            "Status" to event.status
        )
        db.collection("Calender").add(data)
    }

    fun updateEvent(event: CalendarEvent) {
        val data = hashMapOf<String, Any>(
            "CourseID" to event.courseId,
            "Ha11ID" to event.hallId,
            "Date" to event.date,
            "StartTime" to event.startTime,
            "EndTime" to event.endTime,
            "Status" to event.status
        )
        db.collection("Calender").document(event.id).update(data)
    }

    fun deleteEvent(eventId: String) {
        db.collection("Calender").document(eventId).delete()
    }
}