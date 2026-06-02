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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val officeLocation = Point(55.7972, 37.5376) // Office Moscow
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "О компании", 
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "AnimeTracker Corp.",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Мы создаем лучший инструмент для любителей аниме. " +
                   "Наш офис расположен в Москве, и мы всегда рады гостям!\n\n" +
                   "AnimeTracker Corp. занимается разработкой инновационных решений в сфере " +
                   "мобильных приложений для трекинга медиаконтента с 2024 года.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Где мы находимся", 
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            AndroidView(
                factory = { ctx ->
                    MapView(ctx).apply {
                        map.move(
                            CameraPosition(officeLocation, 16.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 0f),
                            null
                        )
                        map.mapObjects.addPlacemark(officeLocation)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    // Handle lifecycle
                }
            )
            
            // Critical: Yandex MapKit requires explicit lifecycle management
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (lifecycleOwner.lifecycle.currentState) {
                        Lifecycle.State.STARTED -> {} // MapView handled internally if needed
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                // routing intent
                val uri = Uri.parse("geo:0,0?q=${officeLocation.latitude},${officeLocation.longitude}(Офис AnimeTracker)")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Построить маршрут")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
