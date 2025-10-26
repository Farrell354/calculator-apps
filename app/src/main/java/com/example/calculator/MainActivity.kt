package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(true) }

            CalculatorTheme {
                val backgroundColor = if (darkTheme) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
                val textColor = if (darkTheme) Color.White else Color.Black

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    CalculatorScreen(
                        darkTheme = darkTheme,
                        textColor = textColor,
                        onToggleTheme = { darkTheme = !darkTheme }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(darkTheme: Boolean, textColor: Color, onToggleTheme: () -> Unit) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val buttons = listOf(
        listOf("C", "√", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Toggle theme button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onToggleTheme) {
                Text(if (darkTheme) "Light" else "Dark")
            }
        }

        // Display
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                color = textColor.copy(alpha = 0.7f),
                fontSize = 32.sp
            )
            Text(
                text = result,
                color = textColor,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { label ->
                    CalculatorButton(label = label, darkTheme = darkTheme) {
                        when (label) {
                            "C" -> {
                                expression = ""
                                result = ""
                            }
                            "=" -> {
                                result = evaluateExpression(expression)
                                expression = result
                            }
                            "√" -> expression += "√("
                            "%" -> expression += "%"
                            "+", "-", "×", "÷" -> expression += " $label "
                            "." -> expression += "."
                            else -> expression += label
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun RowScope.CalculatorButton(
    label: String,
    darkTheme: Boolean,
    onClick: () -> Unit
) {
    val isOperator = label in listOf("+","-","×","÷","=","√","%")
    var pressed by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (pressed) Color.Gray
        else if (isOperator) Color(0xFFFF9800)
        else if (darkTheme) Color(0xFF333333) else Color(0xFFDDDDDD)
    )

    val contentColor = if (darkTheme) Color.White else Color.Black

    Button(
        onClick = {
            pressed = true
            onClick()
            pressed = false
        },
        modifier = Modifier
            .weight(if (label == "0") 2f else 1f) // sekarang aman
            .aspectRatio(1f)
            .scale(if (pressed) 0.9f else 1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = CircleShape
    ) {
        Text(text = label, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}


// Evaluasi ekspresi sederhana mendukung +, -, ×, ÷, %, √
fun evaluateExpression(expr: String): String {
    return try {
        var expression = expr

        // Tangani akar kuadrat
        while (expression.contains("√(")) {
            val start = expression.indexOf("√(")
            val end = expression.indexOf(")", start)
            if (end == -1) break
            val number = expression.substring(start + 2, end).toDouble()
            expression = expression.replaceRange(start, end + 1, sqrt(number).toString())
        }

        // Tangani %
        expression = expression.replace("%", "*0.01")

        // Ganti operator untuk evaluasi
        expression = expression.replace("×","*").replace("÷","/")

        // Tokenisasi sederhana (operasi berurutan, tanpa prioritas kompleks)
        val tokens = expression.split(" ").filter { it.isNotEmpty() }.toMutableList()
        var result = tokens[0].toDouble()
        var i = 1
        while (i < tokens.size) {
            val op = tokens[i]
            val next = tokens[i + 1].toDouble()
            result = when(op) {
                "+" -> result + next
                "-" -> result - next
                "*" -> result * next
                "/" -> result / next
                else -> result
            }
            i += 2
        }

        if (result % 1 == 0.0) result.toInt().toString() else result.toString()
    } catch (e: Exception) {
        "Error"
    }
}
