package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.timer
import kotlin.random.Random


class LancioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Recupera parametro passato dalla classe chiamante
        var inputString = intent.getStringExtra("INPUT_STRING") ?: ""

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisegnaPagina(
                        modifier = Modifier.padding(innerPadding),
                        inputString
                    )
                }
            }
        }
    }
}

@Composable
fun DisegnaPagina(modifier: Modifier = Modifier, inputString: String) {

    //split dati
    val parts = inputString.split(";")
    val dadi = parts.drop(1).filter { it != "Vuoto" }

    //local context
    val context = LocalContext.current

    //lanci iniziali
    var diceValues by remember { mutableStateOf(dadi.map { LancioDado(it) }) }
    EmettiSuono(context)

    //totale lancio
    var totale = diceValues.sum()
    var progressivoLanci by remember { mutableStateOf<Int>(0) }

    //lista lanci
    val itemsCronologia = remember { mutableStateListOf<String>() }

    //pagina srollabile
    LazyColumn(modifier = modifier) {

        //pulsante Back
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    val intent = Intent(context, MainActivity::class.java);
                    context.startActivity(intent);
                }) {
                    Text("<-")
                }
                Text(
                    text = parts[0],
                    modifier = modifier
                )
            }
        }

        //righe per singolo dado
        itemsIndexed(dadi) { index, dado ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$dado = ${diceValues[index]}",
                    modifier = modifier
                )
                Button(onClick = {
                    diceValues = diceValues.toMutableList().apply {
                        this[index] = LancioDado(dado)
                    }
                    itemsCronologia.add("$dado rilanciato: ${diceValues[index]}")
                    EmettiSuono(context)
                }) {
                    Text("Rilancia dado")
                }
            }
        }

        //label totale lancio
        item {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Totale: $totale",
                    modifier = modifier
                )
            }
        }

        //lancia tutti i dadi
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    progressivoLanci++;
                    diceValues = dadi.map { dado -> LancioDado(dado) }
                    val lancio =
                        "Lancio $progressivoLanci: " + diceValues.joinToString(" + ") + " = $totale"
                    itemsCronologia.add(lancio)

//                    val mediaPlayer = MediaPlayer.create(context, R.raw.lancio1)
//                    mediaPlayer.start()
                    EmettiSuono(context)
                }) {
                    Text("Lancia dadi")
                }
            }
        }

        // Cronologia lanci
        item {
            Text("Cronologia", modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(itemsCronologia.reversed()) { itemCronologia ->
                        var orario = SimpleDateFormat("HH:mm:ss").format(Date())
                        Text(itemCronologia + " " + orario)
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    MyApplicationTheme {
//        DisegnaPagina()
//    }
//}

fun LancioDado(dado: String): Int {

    return when (dado) {
        "D2" -> Random.nextInt(1, 3)
        "D3" -> Random.nextInt(1, 4)
        "D4" -> Random.nextInt(1, 5)
        "D6" -> Random.nextInt(1, 7)
        "D8" -> Random.nextInt(1, 9)
        "D10" -> Random.nextInt(1, 11)
        "D12" -> Random.nextInt(1, 13)
        "D20" -> Random.nextInt(1, 21)
        "D100" -> Random.nextInt(1, 101)
        else -> 0
    }
}

fun EmettiSuono(context: Context) {

    var lancio = 0
    when (Random.nextInt(1, 5)) {
        1 -> lancio = R.raw.lancio1
        2 -> lancio = R.raw.lancio2
        3 -> lancio = R.raw.lancio3
        4 -> lancio = R.raw.lancio4
    }
    val mediaPlayer = MediaPlayer.create(context, lancio)
    mediaPlayer.start()
}