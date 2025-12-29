package com.example.vuvur.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LockScreen(onUnlock: () -> Unit) {
    var enteredCode by remember { mutableStateOf("") }
    val correctCode = "357159"
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Passcode", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = enteredCode,
            onValueChange = { },
            label = { Text("Passcode") },
            visualTransformation = PasswordVisualTransformation(),
            readOnly = true,
            modifier = Modifier.width(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        NumericKeypad(onKeyPress = { key ->
            if (key == "backspace") {
                if (enteredCode.isNotEmpty()) {
                    enteredCode = enteredCode.dropLast(1)
                }
            } else if (enteredCode.length < 6) {
                enteredCode += key
            }
        })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { enteredCode = "" }) {
                Text("Clear")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (enteredCode == correctCode) {
                    onUnlock()
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Incorrect passcode")
                    }
                }
            }) {
                Text("Enter")
            }
        }
    }
    }
}

@Composable
fun NumericKeypad(onKeyPress: (String) -> Unit) {
    Column {
        (1..9).chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { number ->
                    Button(onClick = { onKeyPress(number.toString()) }) {
                        Text(number.toString())
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { onKeyPress("0") }) {
                Text("0")
            }
            Button(onClick = { onKeyPress("backspace") }) {
                Text("<-")
            }
        }
    }
}
