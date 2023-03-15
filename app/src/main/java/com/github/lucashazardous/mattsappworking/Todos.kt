package com.github.lucashazardous.mattsappworking

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset

val gson = Gson()

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    var isDone: Boolean,
)

var todos = mutableStateListOf<Todo>()

fun saveTodosToFile(context: Context) {
    val json = gson.toJson(todos)
    val file = File(context.filesDir, "todos.json")
    file.delete()
    file.createNewFile()
    file.writeText(json)
}

fun readFromFile(context: Context) {
    if(!File(context.filesDir, "todos.json").createNewFile()) {
        val text = File(context.filesDir, "todos.json").readText(Charset.defaultCharset())
        val a = gson.fromJson(text, Array<Todo>::class.java)
        if(a != null) {
            todos.addAll(a)
        }
    }
}