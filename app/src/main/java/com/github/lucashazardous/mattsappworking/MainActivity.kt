package com.github.lucashazardous.mattsappworking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.lucashazardous.mattsappworking.ui.theme.MattsAppWorkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val owner: LifecycleOwner = LocalLifecycleOwner.current
            CycleObserver(context = applicationContext, lifecycle = owner)
            MattsAppWorkingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MattsAppView()
                }
            }
        }
    }
}

@Composable
fun CycleObserver(context: Context, lifecycle: LifecycleOwner) {
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver {
            _, event ->
            run {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        saveTodosToFile(context)
                    }
                    Lifecycle.Event.ON_CREATE -> {
                        readFromFile(context)
                    }
                    else -> {}
                }
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose { lifecycle.lifecycle.removeObserver(observer) }
    }
}

var openDialog = mutableStateOf(false)

@Composable
fun TaskAdder() {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                Button(content = {
                        Text("")
                    }, onClick = {
                    openDialog.value = false
                })
            },
            dismissButton = {
                Button(content = {
                    Text("Sling your hook")
                }, onClick = {
                    openDialog.value = false
                })
            },
            text = {
                Column {

                }
            }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MattsAppView() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(title = {
                Button(content = { Text(text = "Clear them")}, onClick = { todos.clear() })
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onErrorContainer
            ))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value = !openDialog.value }) {
                
            }
        }) { _ ->
        TaskAdder()
        LazyVerticalGrid(cells = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 10.dp, 10.dp, 0.dp)) {
            items(todos.size) {
                    item -> TodoItem(todos[item])
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    val selectText = {
        if (todo.isDone) "Done" else "To do"
    }

    var doneText by rememberSaveable {
        mutableStateOf(selectText())
    }

    Box {
        Column {
            Text(todo.title)
            Text(todo.description)

            Row {
                Button(onClick = {
                    todo.isDone = !todo.isDone
                    doneText = selectText()
                }, Modifier.weight(5F),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (todo.isDone) MaterialTheme.colorScheme.primaryContainer else Color.Green
                    )) {
                    Text(doneText)
                }

                IconButton(onClick  = { todos.remove(todo) }, Modifier.weight(1F)) {
                    Icon(Icons.Filled.Delete, "Delete thingy",
                        modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
