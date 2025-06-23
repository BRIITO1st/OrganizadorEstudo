package com.example.organizadorestudo.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.organizadorestudo.data.Task
import com.example.organizadorestudo.ui.theme.GreenCompleteText
import com.example.organizadorestudo.ui.theme.RedDelete
import com.example.organizadorestudo.ui.theme.GreyText
import com.example.organizadorestudo.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlannerScreen(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.allTasks.collectAsState(initial = emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    // --- ESTADOS PARA A ANIMAÇÃO ---
    var showSuccessAnimation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Organizador de Estudos", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AddTaskCard(onAddTask = { title, subject ->
                    // disparar a animação
                    taskViewModel.addTask(title, subject)
                    scope.launch {
                        showSuccessAnimation = true
                        delay(1500L)
                        showSuccessAnimation = false
                    }
                })
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Minhas Tarefas",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                TaskList(
                    tasks = tasks,
                    onTaskCheckedChange = { task, isChecked ->
                        taskViewModel.updateTask(task.copy(isCompleted = isChecked))
                    },
                    onEditClick = { task ->
                        taskToEdit = task
                        showEditDialog = true
                    },
                    onDeleteClick = { task ->
                        taskViewModel.deleteTask(task)
                    }
                )
            }
        }

        if (showEditDialog && taskToEdit != null) {
            EditTaskDialog(
                task = taskToEdit!!,
                onDismiss = { showEditDialog = false },
                onConfirm = { updatedTask ->
                    taskViewModel.updateTask(updatedTask)
                    showEditDialog = false
                }
            )
        }

        // --- COMPOSABLE DA ANIMAÇÃO ---
        // sobrepõe o conteudo
        AnimatedVisibility(
            visible = showSuccessAnimation,
            enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
        ) {
            SuccessOverlay()
        }
    }
}

@Composable
fun SuccessOverlay() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // fica semi-transparente
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Sucesso",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tarefa Adicionada!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun AddTaskCard(onAddTask: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Adicionar Nova Tarefa",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título da Tarefa") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Matéria/Disciplina") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (title.isNotBlank() && subject.isNotBlank()) {
                        onAddTask(title, subject)
                        // Limpa os campos após o clique
                        title = ""
                        subject = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
                Spacer(modifier = Modifier.width(8.dp))
                Text("ADICIONAR TAREFA", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// O restante do código (TaskList, TaskItem, etc.) continua o mesmo.
// ... (cole o restante do seu código aqui se necessário)

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { isChecked -> onTaskCheckedChange(task, isChecked) },
                    onEditClick = { onEditClick(task) },
                    onDeleteClick = { onDeleteClick(task) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val cardColors = if (task.isCompleted) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    }

    val textColor = if (task.isCompleted) GreenCompleteText else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = GreenCompleteText,
                    uncheckedColor = MaterialTheme.colorScheme.primary
                )
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LibraryBooks, contentDescription = null, modifier = Modifier.size(16.dp), tint = GreyText)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.subject,
                        fontSize = 14.sp,
                        color = GreyText,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                    )
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.DeleteForever, contentDescription = "Deletar", tint = RedDelete)
            }
        }
    }
}


@Composable
fun EditTaskDialog(task: Task, onDismiss: () -> Unit, onConfirm: (Task) -> Unit) {
    var title by remember { mutableStateOf(task.title) }
    var subject by remember { mutableStateOf(task.subject) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Tarefa", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Matéria") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(task.copy(title = title, subject = subject))
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Checklist,
            contentDescription = "Nenhuma tarefa",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tudo em ordem!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Você não tem tarefas pendentes.\nAdicione uma nova tarefa para começar a organizar seus estudos.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = GreyText
        )
    }
}
