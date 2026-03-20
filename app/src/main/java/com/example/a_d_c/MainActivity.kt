package com.example.a_d_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a_d_c.ui.screens.InputScreen
import com.example.a_d_c.ui.screens.ResultScreen
import com.example.a_d_c.ui.theme.ADCTheme
import com.example.a_d_c.ui.viewmodel.VastuUiState
import com.example.a_d_c.ui.viewmodel.VastuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ADCTheme {
                val viewModel: VastuViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "input") {
                    composable("input") {
                        InputScreen(onGenerateClick = { request ->
                            viewModel.generatePlan(request)
                            navController.navigate("result")
                        })
                    }
                    composable("result") {
                        when (val state = uiState) {
                            is VastuUiState.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                            is VastuUiState.Success -> {
                                ResultScreen(
                                    response = state.response,
                                    onBackClick = {
                                        viewModel.resetState()
                                        navController.popBackStack()
                                    }
                                )
                            }
                            is VastuUiState.Error -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                                }
                            }
                            else -> {
                                // Fallback or Idle state
                            }
                        }
                    }
                }
            }
        }
    }
}
