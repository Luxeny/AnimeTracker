package com.example.feature.about.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val officeLocation = LatLng(55.7539, 37.6208) // Moscow, dummy office
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(officeLocation, 15f)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "О компании", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "AnimeTracker Inc. — мы создаем лучшие инструменты для любителей аниме.")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = officeLocation),
                title = "Наш офис"
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val context = LocalContext.current
        Button(onClick = { 
            val gmmIntentUri = Uri.parse("google.navigation:q=${officeLocation.latitude},${officeLocation.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }) {
            Text("Построить маршрут")
        }
    }
}
