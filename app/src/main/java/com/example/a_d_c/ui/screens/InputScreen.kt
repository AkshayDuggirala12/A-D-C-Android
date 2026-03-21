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
import com.example.a_d_c.data.model.PlanRequest
import com.example.a_d_c.data.model.RoomsRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onGenerateClick: (PlanRequest) -> Unit
) {
    var plotWidth by remember { mutableStateOf("30") }
    var plotLength by remember { mutableStateOf("32") }
    var facing by remember { mutableStateOf("north") }
    var entrance by remember { mutableStateOf("auto") }
    var bedrooms by remember { mutableStateOf("2") }
    var bathrooms by remember { mutableStateOf("2") }
    var kitchen by remember { mutableStateOf("1") }
    var hall by remember { mutableStateOf("1") }
    var pooja by remember { mutableStateOf("1") }

    val facings = listOf("north", "south", "east", "west")
    val entrances = listOf("auto", "north", "south", "east", "west", "north-east", "north-west", "south-east", "south-west")
    
    var facingExpanded by remember { mutableStateOf(false) }
    var entranceExpanded by remember { mutableStateOf(false) }

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
                    value = plotLength,
                    onValueChange = { if (it.all { char -> char.isDigit() }) plotLength = it },
                    label = { Text("Length (ft)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Text("Orientation & Entrance", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            // Facing Dropdown
            ExposedDropdownMenuBox(
                expanded = facingExpanded,
                onExpandedChange = { facingExpanded = !facingExpanded }
            ) {
                OutlinedTextField(
                    value = facing.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Facing Direction") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facingExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = facingExpanded,
                    onDismissRequest = { facingExpanded = false }
                ) {
                    facings.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                facing = selectionOption
                                facingExpanded = false
                            }
                        )
                    }
                }
            }

            // Entrance Dropdown
            ExposedDropdownMenuBox(
                expanded = entranceExpanded,
                onExpandedChange = { entranceExpanded = !entranceExpanded }
            ) {
                OutlinedTextField(
                    value = entrance.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Entrance Location") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = entranceExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = entranceExpanded,
                    onDismissRequest = { entranceExpanded = false }
                ) {
                    entrances.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                entrance = selectionOption
                                entranceExpanded = false
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
                    val request = PlanRequest(
                        plotWidth = plotWidth.toIntOrNull() ?: 30,
                        plotLength = plotLength.toIntOrNull() ?: 30,
                        facing = facing,
                        entrance = if (entrance == "auto") null else entrance,
                        rooms = RoomsRequest(
                            bedroom = bedrooms.toIntOrNull() ?: 1,
                            bathroom = bathrooms.toIntOrNull() ?: 1,
                            kitchen = kitchen.toIntOrNull() ?: 1,
                            hall = hall.toIntOrNull() ?: 1,
                            pooja = pooja.toIntOrNull() ?: 0
                        )
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
