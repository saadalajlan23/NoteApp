package com.example.noteapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHandler

    private lateinit var rvNotes: RecyclerView
    private lateinit var editText: EditText
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHandler(this)

        editText = findViewById(R.id.tvNewNote)
        submitBtn = findViewById(R.id.btSubmit)
        submitBtn.setOnClickListener { postNote() }

        rvNotes = findViewById(R.id.rvNotes)
        updateRV()
    }

    private fun updateRV(){
        rvNotes.adapter = NoteAdapter(this, getItemsList())
        rvNotes.layoutManager = LinearLayoutManager(this)
    }

    private fun getItemsList(): ArrayList<NoteModel>{
        return db.viewNotes()
    }

    private fun postNote(){
        db.addNote(NoteModel(0, editText.text.toString()))
        editText.text.clear()
        Toast.makeText(this, "Note Added", Toast.LENGTH_LONG).show()
        updateRV()
    }

    private fun editNote(noteID: Int, noteText: String){
        db.updateNote(NoteModel(noteID, noteText))
        Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show()
        updateRV()
    }

    fun deleteNote(noteID: Int){
        db.deleteNote(NoteModel(noteID, ""))
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_LONG).show()

        updateRV()
    }

    fun raiseDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> editNote(id, updatedNote.text.toString())
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }
    fun deleteDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val textNote = TextView(this)
        textNote.text = "Delete this Note ?"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("DELETE", DialogInterface.OnClickListener {
                    _, _ -> deleteNote(id, )
            })
            .setNegativeButton("CANCEL", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete")
        alert.setView(textNote)
        alert.show()
    }
}