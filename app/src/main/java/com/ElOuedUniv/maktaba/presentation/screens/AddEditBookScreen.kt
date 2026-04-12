package com.ElOuedUniv.maktaba.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    viewModel: BookViewModel,
    existingBook: Book? = null,
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.formState.collectAsState()
    val isEditMode = existingBook != null

    LaunchedEffect(existingBook) {
        if (existingBook != null) viewModel.loadBookForEdit(existingBook)
        else viewModel.resetForm()
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.resetForm()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditMode) "تعديل الكتاب" else "إضافة كتاب جديد")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
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
            // ── العنوان ──────────────────────────
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("عنوان الكتاب *") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.titleError != null,
                supportingText = {
                    state.titleError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.MenuBook, contentDescription = null)
                },
                singleLine = true
            )

            // ── المؤلف ───────────────────────────
            OutlinedTextField(
                value = state.author,
                onValueChange = viewModel::onAuthorChange,
                label = { Text("اسم المؤلف *") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.authorError != null,
                supportingText = {
                    state.authorError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                singleLine = true
            )

            // ── ISBN ─────────────────────────────
            OutlinedTextField(
                value = state.isbn,
                onValueChange = viewModel::onIsbnChange,
                label = { Text("رقم ISBN") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            // ── التصنيف ──────────────────────────
            OutlinedTextField(
                value = state.genre,
                onValueChange = viewModel::onGenreChange,
                label = { Text("التصنيف") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("رواية، علوم، تاريخ...") },
                singleLine = true
            )

            // ── عدد الصفحات ──────────────────────
            OutlinedTextField(
                value = state.totalPages,
                onValueChange = viewModel::onTotalPagesChange,
                label = { Text("عدد الصفحات") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            // ── الوصف ────────────────────────────
            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("وصف الكتاب") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // ── حالة القراءة ─────────────────────
            ReadingStatusSelector(
                selectedStatus = state.readingStatus,
                onStatusChange = viewModel::onReadingStatusChange
            )

            // ── زر الحفظ ─────────────────────────
            Button(
                onClick = { viewModel.saveBook(existingBook?.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isEditMode) "حفظ التعديلات" else "إضافة الكتاب",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

// ── Reading Status Selector ──────────────────────
@Composable
private fun ReadingStatusSelector(
    selectedStatus: ReadingStatus,
    onStatusChange: (ReadingStatus) -> Unit
) {
    val statuses = listOf(
        ReadingStatus.TO_READ to "📋 سأقرأه",
        ReadingStatus.READING to "📖 أقرأه الآن",
        ReadingStatus.COMPLETED to "✅ أكملته"
    )

    Column {
        Text(
            "حالة القراءة",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statuses.forEach { (status, label) ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { onStatusChange(status) },
                    label = {
                        Text(label, style = MaterialTheme.typography.labelSmall)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}