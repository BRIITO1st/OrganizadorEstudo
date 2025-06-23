package com.example.organizadorestudo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.organizadorestudo.ui.StudyPlannerScreen
import com.example.organizadorestudo.ui.theme.OrganizadorEstudoTheme
import com.example.organizadorestudo.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    // Inicializa o ViewModel
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrganizadorEstudoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Passa o ViewModel para a tela principal
                    StudyPlannerScreen(taskViewModel = taskViewModel)
                }
            }
        }
    }
}
