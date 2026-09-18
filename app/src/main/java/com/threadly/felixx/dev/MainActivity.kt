package com.threadly.felixx.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.threadly.felixx.dev.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            val app = application as ThreadlyApplication
            val settings by app.repository.settings.collectAsState(initial = null)
            ThreadlyTheme(settings) { ThreadlyApp() } 
        }
    }
}

@Composable
fun ThreadlyApp() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(onClub = { nav.navigate("club/$it") }, onProfile = { nav.navigate("profile") }, onCompose = { nav.navigate("compose") }) }
        composable("club/{id}") { entry -> ClubScreen(entry.arguments?.getString("id") ?: "unsorted", onBack = { nav.popBackStack() }, onThread = { nav.navigate("thread/$it") }) }
        composable("thread/{id}") { entry -> ThreadScreen(entry.arguments?.getString("id") ?: "", onBack = { nav.popBackStack() }) }
        composable("compose") { ComposeScreen(onBack = { nav.popBackStack() }) }
        composable("profile") { ProfileScreen(onBack = { nav.popBackStack() }, onSettings = { nav.navigate("settings") }, onAddAccount = { nav.navigate("addAccount") }) }
        composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
        composable("addAccount") { ProviderSelectionScreen(onBack = { nav.popBackStack() }, onImapSelected = { nav.navigate("addImapAccount") }, onAccountAdded = { nav.popBackStack() }) }
        composable("addImapAccount") { AddImapAccountScreen(onBack = { nav.popBackStack() }, onAccountAdded = { nav.popBackStack("profile", false) }) }
    }
}
