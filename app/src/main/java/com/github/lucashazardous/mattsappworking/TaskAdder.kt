package com.github.lucashazardous.mattsappworking

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lucashazardous.mattsappworking.ui.theme.MidGreen


var openDialog = mutableStateOf(false)

@Composable
fun TaskAdder() {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val close = {
        title = ""
        description = ""
        openDialog.value = false
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            confirmButton = {
                Button(content = {
                    Text("Sausage Roll")
                }, onClick = {
                    val newId = if (todos.size > 0) todos.last().id+1 else 0
                    todos.add(Todo(newId, title, description, false))
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = MidGreen))
            },
            dismissButton = {
                Button(content = {
                    Text("Sling your hook")
                }, onClick = {
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = MidGreen))
            },
            text = {
                Column {
                    BasicTextField(value = title,
                        onValueChange = { title = it },
                        decorationBox = {
                            BoxDecoration(title, "Title")
                        })
                    Text(text = "")
                    BasicTextField(value = description,
                        onValueChange = { description = it },
                        decorationBox = {
                            BoxDecoration(description, "Description")
                        })
                }
            }
        )
    }
}

@Composable
fun BoxDecoration(value: String, placeholder: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 64.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MidGreen,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, modifier = Modifier.align(Alignment.Center))
        } else {
            Text(text = value, modifier = Modifier.align(Alignment.Center))
        }
    }
}