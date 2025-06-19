//Student Nr.: ST10495452
// Name: Mokete Mantjane
package com.example.musicplaylistmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast // Import Toast for showing messages
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var etSongTitle: EditText
    private lateinit var etArtistName: EditText
    private lateinit var etRating: EditText
    private lateinit var etComment: EditText
    private lateinit var btnAddPlaylist: Button
    private lateinit var btnViewPlaylists: Button
    private lateinit var btnExit: Button

    // Parallel arrays to store playlist data
    private var titles = arrayListOf<String>()
    private var artists = arrayListOf<String>()
    private var ratings = arrayListOf<Int>()
    private var comments = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this matches your XML file name

        // Initialize UI elements by finding them by their IDs from the layout
        etSongTitle = findViewById(R.id.songnames)
        etArtistName = findViewById(R.id.textView) // Mapped to Artist's Name
        etRating = findViewById(R.id.textView2)    // Mapped to Rating (1-5)
        etComment = findViewById(R.id.textView3)   // Mapped to Comment

        btnAddPlaylist = findViewById(R.id.button2)
        btnViewPlaylists = findViewById(R.id.button)
        btnExit = findViewById(R.id.button3)

        // Set click listeners for the buttons
        btnAddPlaylist.setOnClickListener {
            addSongToPlaylist()
        }

        btnViewPlaylists.setOnClickListener {
            viewPlaylists()
        }

        // Closes all activities in the task and exits the app
        btnExit.setOnClickListener {
            finishAffinity()
        }
    }

    private fun addSongToPlaylist() {
        val title = etSongTitle.text.toString().trim()
        val artist = etArtistName.text.toString().trim()
        val ratingString = etRating.text.toString().trim()
        val comment = etComment.text.toString().trim()

        // Basic input validation for empty fields
        try {
            if (title.isEmpty() || artist.isEmpty() || ratingString.isEmpty() || comment.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return // Stop execution if any field is empty
            }

            val rating: Int

            // Using try-catch for number format exception for demonstration
            // Although toIntOrNull() is generally preferred for this specific validation
            try {
                rating = ratingString.toInt()
                if (rating < 1 || rating > 5) {
                    Toast.makeText(this, "Rating must be a number between 1 and 5.", Toast.LENGTH_SHORT).show()
                    return // Stop execution if rating is out of range
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Rating must be a valid whole number.", Toast.LENGTH_SHORT).show()
                return // Stop execution if rating is not a number
            }

            // Add data to parallel arrays
            titles.add(title)
            artists.add(artist)
            ratings.add(rating)
            comments.add(comment)

            // Clear input fields after adding
            etSongTitle.text.clear()
            etArtistName.text.clear()
            etRating.text.clear()
            etComment.text.clear()

            Toast.makeText(this, "$title by $artist added to playlist!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Catch any unexpected general exceptions during the add operation
            Toast.makeText(this, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace() // Log the stack trace for debugging
        }
    }

    private fun viewPlaylists() {
        try {
            if (titles.isEmpty()) {
                Toast.makeText(this, "No songs in the playlist yet. Add some first!", Toast.LENGTH_SHORT).show()
                return
            }

            val intent = Intent(this, ViewScreen::class.java).apply {
                // Pass the parallel arrays as ArrayLists to the ViewScreen
                putStringArrayListExtra("titles", titles)
                putStringArrayListExtra("artists", artists)
                putIntegerArrayListExtra("ratings", ratings)
                putStringArrayListExtra("comments", comments)
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Catch any unexpected general exceptions during intent creation/start
            Toast.makeText(this, "Could not view playlists: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace() // Log the stack trace for debugging
        }
    }
}