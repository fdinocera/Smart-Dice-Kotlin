package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Stato per gestire il messaggio
    //val message = remember { mutableStateOf("Hello $name!") }
    val context = LocalContext.current

    Column(modifier = modifier) {
        // Mostra il messaggio
        //Text(text = message.value)

        // Pulsante con gestione click
        Button(onClick = {
            //message.value = "Button clicked!"
            val inputString = "Quattro dadi;D2;D3;D4;D6;D12;D20;D100;Vuoto;Vuoto;Vuoto"
            val intent = Intent(context, LancioActivity::class.java).apply {
                putExtra("INPUT_STRING", inputString)
            }
            context.startActivity(intent)
        }) {
            Text("Lancio 1")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}