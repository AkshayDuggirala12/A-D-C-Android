package com.example.a_d_c.ui.screens

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.example.a_d_c.data.api.RetrofitClient
import com.example.a_d_c.data.model.VastuResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    response: VastuResponse,
    onBackClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Vastu Plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { sharePlan(context, response.vastuScore) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { downloadFloorPlan(context) }) {
                        Icon(Icons.Default.Download, contentDescription = "Download DXF")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // SVG Display Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    webViewClient = WebViewClient()
                                    settings.javaScriptEnabled = true
                                    settings.builtInZoomControls = true
                                    settings.displayZoomControls = false
                                    settings.useWideViewPort = true
                                    settings.loadWithOverviewMode = true
                                    settings.domStorageEnabled = true
                                    setBackgroundColor(0x00000000) // Transparent background
                                }
                            },
                            update = { webView ->
                                try {
                                    val svgData = response.svg
                                    if (svgData.isNotEmpty()) {
                                        val encodedHtml = Base64.encodeToString(svgData.toByteArray(), Base64.NO_PADDING)
                                        webView.loadData(encodedHtml, "image/svg+xml", "base64")
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        Surface(
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.TopEnd),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        ) {
                            Text(
                                "Pinch to zoom",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                // Score Visualization
                val scoreColor = try {
                    Color(response.scoreColor.toColorInt())
                } catch (e: Exception) {
                    MaterialTheme.colorScheme.primary
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = scoreColor.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Vastu Compliance Score",
                            style = MaterialTheme.typography.labelLarge,
                            color = scoreColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${response.vastuScore}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = scoreColor
                            )
                            Text(
                                text = "/100",
                                style = MaterialTheme.typography.headlineSmall,
                                color = scoreColor.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = { response.vastuScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = scoreColor,
                            trackColor = scoreColor.copy(alpha = 0.2f)
                        )
                    }
                }

                // Warnings & Recommendations
                if (response.warnings.isNotEmpty() || response.recommendations.isNotEmpty()) {
                    Text("Vastu Analysis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    
                    response.warnings.forEach { warning ->
                        AnalysisItem(
                            icon = Icons.Default.Warning,
                            text = warning,
                            color = Color(0xFFE53935),
                            backgroundColor = Color(0xFFFFEBEE)
                        )
                    }
                    
                    response.recommendations.forEach { recommendation ->
                        AnalysisItem(
                            icon = Icons.Default.Info,
                            text = recommendation,
                            color = Color(0xFF1E88E5),
                            backgroundColor = Color(0xFFE3F2FD)
                        )
                    }
                }

                // Room Details List
                Text("Room Inventory", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                response.rooms.forEach { room ->
                    RoomItem(room.type, room.zone, room.width, room.height)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AnalysisItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium, color = color.copy(alpha = 0.9f))
        }
    }
}

@Composable
fun RoomItem(type: String, zone: String, width: Int, height: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    type.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Zone: $zone",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "${width}x${height} ft",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun downloadFloorPlan(context: Context) {
    try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val url = "${RetrofitClient.BASE_URL}plan/generate-dxf"
        val fileName = "vastu_floor_plan_${System.currentTimeMillis()}.dxf"
        
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Floor Plan Download")
            .setDescription("Vastu Floor Plan (DXF Format)")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        
        downloadManager.enqueue(request)
        Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun sharePlan(context: Context, score: Int) {
    try {
        val shareText = """
            🏠 Check out my Vastu Floor Plan!
            
            📊 Vastu Score: $score/100 ✅
            
            Generated by Vastu Plan Generator
        """.trimIndent()
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Share Your Vastu Plan"))
    } catch (e: Exception) {
        Toast.makeText(context, "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
