package com.ElOuedUniv.maktaba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ElOuedUniv.maktaba.data.local.database.MaktabaDatabase
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.presentation.navigation.AppNavigation
import com.ElOuedUniv.maktaba.presentation.theme.MaktabaTheme
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = MaktabaDatabase.getDatabase(this)
        val repository = BookRepository(database.bookDao())

        setContent {
            MaktabaTheme {
                val bookViewModel: BookViewModel = viewModel(
                    factory = BookViewModel.factory(repository)
                )
                // ← استبدل BookListScreen بـ AppNavigation
                AppNavigation(viewModel = bookViewModel)
            }
        }
    }
}