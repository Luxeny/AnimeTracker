package com.example.feature.about.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val officeLocation = LatLng(55.7972, 37.5376) // VK Office Moscow, Leningradsky Ave, 39, Bldg 79
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(officeLocation, 16f)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "О компании", 
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "AnimeTracker Inc.",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Мы — команда энтузиастов, объединенных любовью к японской анимации. " +
                   "AnimeTracker начинался как небольшой студенческий проект, но вырос в " +
                   "полноценный инструмент для тысяч фанатов по всему миру. " +
                   "Наша миссия — сделать поиск и отслеживание аниме максимально простым и приятным.\n\n" +
                   "Наш главный офис находится в самом сердце Москвы, где мы работаем над " +
                   "новыми функциями, такими как интеграция нейросетей для рекомендаций и " +
                   "социальные функции для общения фанатов.",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Наше местоположение", 
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                Marker(
                    state = MarkerState(position = officeLocation),
                    title = "Офис VK (AnimeTracker HQ)",
                    snippet = "Ленинградский пр-т, 39, стр. 79, Москва"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val context = LocalContext.current
        Button(
            onClick = { 
                val gmmIntentUri = Uri.parse("google.navigation:q=${officeLocation.latitude},${officeLocation.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Построить маршрут в Google Maps")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
