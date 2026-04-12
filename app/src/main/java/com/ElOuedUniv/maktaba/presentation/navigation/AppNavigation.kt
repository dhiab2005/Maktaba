// presentation/navigation/AppNavigation.kt
package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ElOuedUniv.maktaba.presentation.screens.AddEditBookScreen
import com.ElOuedUniv.maktaba.presentation.screens.BookDetailScreen
import com.ElOuedUniv.maktaba.presentation.screens.BookListScreen
import com.ElOuedUniv.maktaba.presentation.screens.FavoritesScreen
import com.ElOuedUniv.maktaba.presentation.screens.StatisticsScreen
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

// ── Routes ───────────────────────────────────────
object Routes {
    const val BOOK_LIST = "book_list"
    const val ADD_BOOK = "add_book"
    const val EDIT_BOOK = "edit_book/{bookId}"
    const val BOOK_DETAIL = "book_detail/{bookId}"
    const val FAVORITES = "favorites"
    const val STATISTICS = "statistics"

    fun editBook(bookId: Int) = "edit_book/$bookId"
    fun bookDetail(bookId: Int) = "book_detail/$bookId"
}

// ── Bottom Nav Items ─────────────────────────────
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.BOOK_LIST, Icons.Default.Home, "المكتبة"),
    BottomNavItem(Routes.FAVORITES, Icons.Default.Favorite, "المفضلة"),
    BottomNavItem(Routes.STATISTICS, Icons.Default.Info, "إحصاءات"),
)

// ── Main NavHost ─────────────────────────────────
@Composable
fun AppNavigation(
    viewModel: BookViewModel,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.BOOK_LIST,
        Routes.FAVORITES,
        Routes.STATISTICS
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Routes.BOOK_LIST) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.BOOK_LIST,
            modifier = Modifier.padding(paddingValues)  // ← الإصلاح هنا
        ) {

            // ── قائمة الكتب ──────────────────────
            composable(Routes.BOOK_LIST) {
                BookListScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { bookId ->
                        navController.navigate(Routes.bookDetail(bookId))
                    },
                    onNavigateToAdd = {
                        navController.navigate(Routes.ADD_BOOK)
                    },
                    onNavigateToFavorites = {
                        navController.navigate(Routes.FAVORITES)
                    }
                )
            }

            // ── إضافة كتاب ───────────────────────
            composable(Routes.ADD_BOOK) {
                AddEditBookScreen(
                    viewModel = viewModel,
                    existingBook = null,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ── تعديل كتاب ───────────────────────
            composable(
                route = Routes.EDIT_BOOK,
                arguments = listOf(
                    navArgument("bookId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId")
                    ?: return@composable
                val state by viewModel.listState.collectAsState()
                val book = state.books.find { it.id == bookId }

                book?.let {
                    AddEditBookScreen(
                        viewModel = viewModel,
                        existingBook = it,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            // ── تفاصيل كتاب ──────────────────────
            composable(
                route = Routes.BOOK_DETAIL,
                arguments = listOf(
                    navArgument("bookId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId")
                    ?: return@composable
                val state by viewModel.listState.collectAsState()
                val book = state.books.find { it.id == bookId }

                book?.let {
                    BookDetailScreen(
                        book = it,
                        viewModel = viewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToEdit = { bookToEdit ->
                            navController.navigate(Routes.editBook(bookToEdit.id))
                        }
                    )
                }
            }

            // ── المفضلة ───────────────────────────
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { bookId ->
                        navController.navigate(Routes.bookDetail(bookId))
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ── الإحصاءات ─────────────────────────
            composable(Routes.STATISTICS) {
                StatisticsScreen(viewModel = viewModel)
            }
        }
    }
}