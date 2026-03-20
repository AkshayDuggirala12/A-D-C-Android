package com.example.a_d_c.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.a_d_c.data.model.RoomCounts
import com.example.a_d_c.data.model.VastuRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onGenerateClick: (VastuRequest) -> Unit
) {
    var plotWidth by remember { mutableStateOf("30") }
    var plotHeight by remember { mutableStateOf("30") }
    var facing by remember { mutableStateOf("east") }
    var bedrooms by remember { mutableStateOf("2") }
    var bathrooms by remember { mutableStateOf("1") }
    var kitchen by remember { mutableStateOf("1") }
    var hall by remember { mutableStateOf("1") }
    var pooja by remember { mutableStateOf("0") }

    val facings = listOf("east", "west", "north", "south")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("Vastu Plan Generator", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) 
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Plot Dimensions", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = plotWidth,
                    onValueChange = { if (it.all { char -> char.isDigit() }) plotWidth = it },
                    label = { Text("Width (ft)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = plotHeight,
                    onValueChange = { if (it.all { char -> char.isDigit() }) plotHeight = it },
                    label = { Text("Height (ft)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Text("Orientation", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = facing.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Facing Direction") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    facings.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                facing = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text("Room Requirements", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            RoomCountRow("Bedrooms", bedrooms, { bedrooms = it })
            RoomCountRow("Bathrooms", bathrooms, { bathrooms = it })
            RoomCountRow("Kitchen", kitchen, { kitchen = it })
            RoomCountRow("Hall", hall, { hall = it })
            RoomCountRow("Pooja Room", pooja, { pooja = it })

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val request = VastuRequest(
                        plotWidth = plotWidth.toIntOrNull() ?: 30,
                        plotHeight = plotHeight.toIntOrNull() ?: 30,
                        facing = facing,
                        rooms = RoomCounts(
                            bedroom = bedrooms.toIntOrNull() ?: 1,
                            bathroom = bathrooms.toIntOrNull() ?: 1,
                            kitchen = kitchen.toIntOrNull() ?: 1,
                            hall = hall.toIntOrNull() ?: 1,
                            pooja = pooja.toIntOrNull() ?: 0
                        ),
                        entrancePreferences = mapOf("main_entrance" to facing)
                    )
                    onGenerateClick(request)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("🚀 Generate Intelligent Plan", style = MaterialTheme.typography.titleMedium)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoomCountRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            IconButton(onClick = { 
                val current = value.toIntOrNull() ?: 0
                if (current > 0) onValueChange((current - 1).toString())
            }) {
                Text("-", style = MaterialTheme.typography.headlineMedium)
            }
            OutlinedTextField(
                value = value,
                onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
                modifier = Modifier.width(60.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            IconButton(onClick = { 
                val current = value.toIntOrNull() ?: 0
                onValueChange((current + 1).toString())
            }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
