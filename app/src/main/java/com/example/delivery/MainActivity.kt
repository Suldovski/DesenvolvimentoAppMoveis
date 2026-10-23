package com.example.delivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.delivery.navigation.AppNavigation
import com.example.delivery.navigation.Menu
import com.example.delivery.ui.theme.DeliveryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeliveryTheme {
                val navController = rememberNavController()

                val pilhaRotas by navController.currentBackStackEntryAsState()
                val rotaAtual = pilhaRotas?.destination?.route


                val mostrarMenu = rotaAtual != null &&
                        !rotaAtual.contains("Login") &&
                        !rotaAtual.contains("Cadastro") &&
                        !rotaAtual.contains("Confirmado")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (mostrarMenu) {
                            Menu(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
