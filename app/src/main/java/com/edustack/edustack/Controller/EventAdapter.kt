import com.edustack.edustack.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edustack.edustack.Models.CalendarEvent
import java.text.SimpleDateFormat

class EventAdapter(
    var events: List<CalendarEvent>, // MAKE THIS VAR
    private val onDelete: (String) -> Unit,
    private val onEdit: (CalendarEvent) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val className: TextView = view.findViewById(R.id.className)
        val startTime: TextView = view.findViewById(R.id.startTime)
        val endTime: TextView = view.findViewById(R.id.endTime)
        val hallId: TextView = view.findViewById(R.id.hallId)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        val editButton: Button = view.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.className.text = event.courseId
        holder.startTime.text = "Start: ${SimpleDateFormat("HH:mm").format(event.startTime)}"
        holder.endTime.text = "End: ${SimpleDateFormat("HH:mm").format(event.endTime)}"
        holder.hallId.text = "Hall: ${event.hallId}"

        holder.deleteButton.setOnClickListener { onDelete(event.id) }
        holder.editButton.setOnClickListener { onEdit(event) }
    }

    override fun getItemCount() = events.size
}