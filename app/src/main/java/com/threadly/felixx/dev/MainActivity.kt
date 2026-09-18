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
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    NavHost(
        navController = nav, 
        startDestination = "home",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
    ) {
        composable("home") { HomeScreen(onClub = { nav.navigate("club/$it") }, onProfile = { nav.navigate("profile") }, onCompose = { nav.navigate("compose") }, onManageClub = { nav.navigate("manageClub/$it") }) }
        composable("club/{id}") { entry -> 
            val id = entry.arguments?.getString("id") ?: "unsorted"
            ClubScreen(id, onBack = { nav.popBackStack() }, onThread = { nav.navigate("thread/$it") }, onManage = { nav.navigate("manageClub/$id") }) 
        }
        composable("thread/{id}") { entry -> ThreadScreen(entry.arguments?.getString("id") ?: "", onBack = { nav.popBackStack() }) }
        composable("compose") { ComposeScreen(onBack = { nav.popBackStack() }) }
        composable("profile") { ProfileScreen(onBack = { nav.popBackStack() }, onSettings = { nav.navigate("settings") }, onAddAccount = { nav.navigate("addAccount") }) }
        composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
        composable("addAccount") { ProviderSelectionScreen(onBack = { nav.popBackStack() }, onImapSelected = { nav.navigate("addImapAccount") }, onAccountAdded = { nav.popBackStack() }) }
        composable("addImapAccount") { AddImapAccountScreen(onBack = { nav.popBackStack() }, onAccountAdded = { nav.popBackStack("profile", false) }) }
        composable("manageClub/{id}") { entry -> ManageClubScreen(entry.arguments?.getString("id"), onBack = { nav.popBackStack() }) }
    }
}
