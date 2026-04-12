// presentation/screens/StatisticsScreen.kt
package com.ElOuedUniv.maktaba.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: BookViewModel) {
    val state by viewModel.listState.collectAsState()
    val books = state.books

    // ── حساب الإحصاءات ───────────────────────────
    val totalBooks = books.size
    val favoritesCount = books.count { it.isFavorite }
    val toReadCount = books.count { it.readingStatus == ReadingStatus.TO_READ }
    val readingCount = books.count { it.readingStatus == ReadingStatus.READING }
    val completedCount = books.count { it.readingStatus == ReadingStatus.COMPLETED }
    val totalPages = books.sumOf { it.totalPages }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("📊 الإحصاءات", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── إجمالي الكتب ─────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Info,
                    value = "$totalBooks",
                    label = "إجمالي الكتب",
                    color = MaterialTheme.colorScheme.primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Favorite,
                    value = "$favoritesCount",
                    label = "المفضلة",
                    color = MaterialTheme.colorScheme.error
                )
            }

            // ── حالة القراءة ─────────────────────
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "حالة القراءة",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // سأقرأ
                    StatusRow(
                        label = "📋 سأقرأه",
                        count = toReadCount,
                        total = totalBooks,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    // أقرأ الآن
                    StatusRow(
                        label = "📖 أقرأه الآن",
                        count = readingCount,
                        total = totalBooks,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // أكملت
                    StatusRow(
                        label = "✅ أكملته",
                        count = completedCount,
                        total = totalBooks,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // ── إجمالي الصفحات ───────────────────
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "📄 إجمالي الصفحات",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$totalPages صفحة",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ── إذا لا توجد كتب ──────────────────
            if (totalBooks == 0) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📚 أضف كتباً لرؤية الإحصاءات",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ── Stat Card ────────────────────────────────────
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Status Row ───────────────────────────────────
@Composable
private fun StatusRow(
    label: String,
    count: Int,
    total: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val progress = if (total > 0) count.toFloat() / total else 0f

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(
                "$count كتاب",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = color
        )
    }
}