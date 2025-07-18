package com.bp.locator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bp.locator.ui.theme.MyApplicationTheme
import androidx.compose.ui.draw.shadow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF5F5F5))
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(color = Color(0xFFF5F5F5))
                    ) {
                        FuelStationApp()
                    }
                }
            }
        }
    }
}

@Composable
fun FuelStationApp() {
    var selectedRadius by remember { mutableIntStateOf(5) }
    var open24h by remember { mutableStateOf(false) }
    var hasStore by remember { mutableStateOf(false) }
    var hotFood by remember { mutableStateOf(false) }
    var bpCards by remember { mutableStateOf(false) }

    val filteredStations = mockStations.filter {
        it.distance <= selectedRadius &&
                (!open24h || it.open24Hours) &&
                (!hasStore || it.hasConvenienceStore) &&
                (!hotFood || it.servesHotFood) &&
                (!bpCards || it.acceptsBpFuelCards)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        AppHeader()
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        "Radius:",
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color(0xFF222222),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    RadiusDropdown(selectedRadius) { selectedRadius = it }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterCheckbox("Open 24h", open24h) { open24h = it }
                        FilterCheckbox("Store", hasStore) { hasStore = it }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterCheckbox("Hot Food", hotFood) { hotFood = it }
                        FilterCheckbox("BP Cards", bpCards) { bpCards = it }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(filteredStations) { station ->
                StationCard(station)
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun RadiusDropdown(selected: Int, onSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(5, 10, 15)
    Box {
        Button(onClick = { expanded = true }) {
            Text("$selected miles")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelected(option)
                    expanded = false
                }, text = { Text("$option miles") })
            }
        }
    }
}

@Composable
fun FilterCheckbox(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChecked,
            modifier = Modifier.size(28.dp)
        )
        Text(label, color = Color(0xFF222222), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun StationCard(station: Station) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_bp_logo),
                contentDescription = "BP Logo",
                modifier = Modifier
                    .size(56.dp)
                    .align(androidx.compose.ui.Alignment.CenterVertically)
                    .padding(end = 18.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    station.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF222222),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    station.address,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF444444))
                )
                Text(
                    String.format("%.2f miles", station.distance),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                )
                Row {
                    if (station.open24Hours) Text("24h  ", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF007A33), fontWeight = FontWeight.Medium))
                    if (station.hasConvenienceStore) Text("Store  ", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF007A33), fontWeight = FontWeight.Medium))
                    if (station.servesHotFood) Text("Hot Food  ", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF007A33), fontWeight = FontWeight.Medium))
                    if (station.acceptsBpFuelCards) Text("BP Card", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF007A33), fontWeight = FontWeight.Medium))
                }
            }
        }
    }
}

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF007A33))
            .padding(vertical = 18.dp, horizontal = 16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_bp_logo),
            contentDescription = "BP Logo",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Fuel Station Locator",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}