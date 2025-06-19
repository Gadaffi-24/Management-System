//Student Nr.: ST10495452
// Name: Mokete Mantjane
package com.example.musicplaylistmanagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast // Import Toast for showing messages
import androidx.appcompat.app.AppCompatActivity

class ViewScreen : AppCompatActivity() {
    private lateinit var listView: TextView
    private lateinit var avgView: TextView
    private lateinit var btnBack: Button
    private lateinit var btnList: Button
    private lateinit var btnAverage: Button

    // Parallel arrays to store playlist data received from MainActivity
    private var titles = arrayListOf<String>()
    private var artists = arrayListOf<String>()
    private var ratings = arrayListOf<Int>()
    private var comments = arrayListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_screen) // This will link to the XML we create below

        try {
            // Initialize UI elements by finding them by their IDs
            listView = findViewById(R.id.textlist)
            avgView = findViewById(R.id.textAverage)
            btnBack = findViewById(R.id.btnBack)
            btnList = findViewById(R.id.BtnDisplaylist) // Ensure this ID matches your XML
            btnAverage = findViewById(R.id.btnAverage)

            // Retrieve data passed from MainActivity using Intent extras
            // Use arrayListOf() for initializing empty lists if no data is found
            titles = intent.getStringArrayListExtra("titles") ?: arrayListOf()
            artists = intent.getStringArrayListExtra("artists") ?: arrayListOf()
            ratings = intent.getIntegerArrayListExtra("ratings") ?: arrayListOf()
            comments = intent.getStringArrayListExtra("comments") ?: arrayListOf()

            // Set click listeners for the buttons
            btnList.setOnClickListener {
                try {
                    displaylist()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error displaying list: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            btnAverage.setOnClickListener {
                try {
                    displayAverage()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error calculating average: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            btnBack.setOnClickListener { finish() } // finish() closes the current activity and returns to the previous one

        } catch (e: Exception) {
            // Catch any exception that occurs during onCreate, e.g., layout inflation issues,
            // or if intent data retrieval somehow throws an unexpected exception (less common with ?: )
            Toast.makeText(this, "An error occurred loading the view: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace() // Log the stack trace for debugging
            finish() // Attempt to close the activity if it can't initialize properly
        }
    }

    private fun displaylist() {
        try {
            if (titles.isEmpty()) {
                listView.text = "No songs to display. Go back and add some!"
                return
            }

            val builder = StringBuilder()
            // It's good practice to ensure all parallel arrays have the same size
            // However, assuming they are consistent based on MainActivity's data passing
            for (i in titles.indices) {
                // Defensive check to prevent IndexOutOfBoundsException if arrays are somehow inconsistent
                if (i < artists.size && i < ratings.size && i < comments.size) {
                    builder.append("Song: ${titles[i]}\n")
                    builder.append("Artist: ${artists[i]}\n")
                    builder.append("Rating: ${ratings[i]}\n")
                    builder.append("Comments: ${comments[i]}\n")
                    builder.append("\n") // Add an extra newline for separation between songs
                } else {
                    builder.append("Inconsistent data found for song at index $i.\n\n")
                }
            }
            listView.text = builder.toString()
            avgView.text = "" // Clear average view when displaying list
        } catch (e: Exception) {
            Toast.makeText(this, "Error building song list: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            listView.text = "An error occurred while trying to display songs."
        }
    }

    private fun displayAverage() {
        try {
            if (ratings.isNotEmpty()) {
                val total = ratings.sum()
                // Division by zero is prevented by ratings.isNotEmpty() check, but good to be aware.
                val avg = total.toDouble() / ratings.size
                avgView.text = "Average Rating: %.2f".format(avg)
                listView.text = "" // Clear list view when displaying average
            } else {
                avgView.text = "No ratings available to calculate an average."
                listView.text = "" // Clear list view
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error calculating average rating: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            avgView.text = "An error occurred while calculating the average."
        }
    }
}