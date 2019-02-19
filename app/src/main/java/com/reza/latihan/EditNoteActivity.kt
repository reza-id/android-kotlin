package com.reza.latihan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_note.*

class EditNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.reza.latihan.EXTRA_ID"
        const val EXTRA_TITLE = "com.reza.latihan.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.reza.latihan.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.reza.latihan.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        np_priority.minValue = 1
        np_priority.maxValue = 10

        val intent = intent

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            et_title.setText(intent.getStringExtra(EXTRA_TITLE))
            et_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            np_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = "Add Note"
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    private fun saveNote() {
        val title = et_title.text.toString()
        val description = et_description.text.toString()
        val priority = np_priority.value

        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Please insert a title and description.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
            putExtra(EXTRA_PRIORITY, priority)

            val id = intent.getIntExtra(EXTRA_ID, -1)
            if (id != -1)
                putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
