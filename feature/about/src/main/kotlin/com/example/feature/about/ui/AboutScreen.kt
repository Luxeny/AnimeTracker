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
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val officeLocation = Point(55.7972, 37.5376) // VK Office Moscow
    val context = LocalContext.current

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
                   "Наш главный офис находится в БЦ «SkyLight», где мы работаем над " +
                   "новыми функциями нашего приложения.",
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
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("yandexmaps://maps.yandex.ru/?pt=${officeLocation.longitude},${officeLocation.latitude}&z=16&l=map"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Построить маршрут в Яндекс Картах")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
