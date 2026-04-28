# TP 5: Supabase Integration & Data Persistence

![Supabase Logo](https://supabase.com/dashboard/img/supabase-logo.png)

## Objective
In this final TP, you will connect your **Maktaba** app to a real backend using **Supabase**. You will implement remote data synchronization for books and categories, handle image uploads to cloud storage, and ensure a seamless user experience by persisting the onboarding state.

---

## 1. Setup Supabase

To use Supabase in your Android project, you need to add the necessary dependencies and initialize the client.

### Task 1.1: Add Dependencies
Open `gradle/libs.versions.toml` and add the Supabase BOM and modules:

```toml
[versions]
# ...
supabase = "3.5.0"
ktor = "3.0.0"

[libraries]
# Supabase
supabase-bom = { group = "io.github.jan-tennert.supabase", name = "bom", version.ref = "supabase" }
supabase-postgrest = { group = "io.github.jan-tennert.supabase", name = "postgrest-kt" }
supabase-storage = { group = "io.github.jan-tennert.supabase", name = "storage-kt" }
ktor-client-android = { group = "io.ktor", name = "ktor-client-android", version.ref = "ktor" }
```

Then, in your `app/build.gradle.kts`, add the implementation:

```kotlin
plugins {
    // ...
    kotlin("plugin.serialization") version "2.2.10" // Match your Kotlin version
}

dependencies {
    // ...
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.storage)
    implementation(libs.ktor.client.android)
}
```

### Task 1.2: Initialize Supabase Client
Create a `SupabaseModule.kt` in `com.ElOuedUniv.maktaba.data.di` to provide the `SupabaseClient` instance using Hilt.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "YOUR_SUPABASE_URL",
            supabaseKey = "YOUR_SUPABASE_ANON_KEY"
        ) {
            install(Postgrest)
            install(Storage)
        }
    }
}
```

---

## 2. Implementing Repositories

Instead of modifying the existing in-memory implementations, you will create new classes specifically for Supabase.

### Task 2.1: Create New Implementations
Create two new classes in `com.ElOuedUniv.maktaba.data.repository`:
1. **`SupabaseBookRepositoryImpl`**: Implements `BookRepository`. Use the `SupabaseClient` to fetch/insert books into the `books` table.
2. **`SupabaseCategoryRepositoryImpl`**: Implements `CategoryRepository`. Use the `SupabaseClient` to fetch categories from the `categories` table.

### Task 2.2: Update `DataModule`
Now, you need to tell Hilt to use these new Supabase implementations instead of the old ones.

**Your Task**: Open `com.ElOuedUniv.maktaba.data.di.DataModule.kt` and update the provider methods for `BookRepository` and `CategoryRepository`. Ensure they now return the new Supabase-specific implementations you created in Task 2.1.

> [!IMPORTANT]
> To use Supabase with your Data Classes, ensure `Book` and `Category` are annotated with `@Serializable`.

---

## 3. Persistent Onboarding Logic

Currently, the onboarding screen appears every time the app starts. We want it to show only once.

### Task 3.1: Save Onboarding State
Use `Jetpack DataStore` (recommended) or `SharedPreferences` to save a boolean flag `hasCompletedOnboarding` when the user clicks "Start" in the `OnboardingView`.

### Task 3.2: Update Navigation
Update `NavGraph.kt` or `MainActivity.kt` to:
1. Read the onboarding flag.
2. Set the `startDestination` of your `NavHost` dynamically:
   - If `false` -> `Screen.Onboarding.route`
   - If `true` -> `Screen.BookList.route`

---

## 4. [Bonus] Supabase Storage & Image Upload

Enhance the "Add Book" feature by allowing users to upload actual images.

### Task 4.1: Camera/Gallery Integration
Implement an image picker in the `AddBookView` to select a book cover from the device.

### Task 4.2: Upload to Bucket
In `BookRepositoryImpl.addBook()`, first upload the selected image to a Supabase Storage bucket (e.g., `"book_covers"`).
- Get the public URL of the uploaded image.
- Save this URL into the `imageUrl` field of the `Book` row in the database.

### Task 4.3: Display Images
Ensure the `BookCard` in the library grid and the `BookDetail` view use **Coil** to load the image from the Supabase URL.

---

## Resources
- [Supabase Kotlin SDK Documentation](https://supabase.com/docs/reference/kotlin/introduction)
- [Jetpack DataStore Guide](https://developer.android.com/topic/libraries/architecture/datastore)
- [Coil Image Loading](https://coil-kt.github.io/coil/compose/)
