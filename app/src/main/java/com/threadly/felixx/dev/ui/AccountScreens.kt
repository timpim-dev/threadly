package com.threadly.felixx.dev.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.ApiException
import com.threadly.felixx.dev.ThreadlyApplication
import com.threadly.felixx.dev.data.AccountEntity
import com.threadly.felixx.dev.data.MailTypes
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderSelectionScreen(
    onBack: () -> Unit,
    onImapSelected: () -> Unit,
    onAccountAdded: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val app = context.applicationContext as ThreadlyApplication
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val email = account?.email
            if (email != null) {
                isLoading = true
                scope.launch {
                    val entity = AccountEntity(
                        id = UUID.randomUUID().toString(),
                        email = email,
                        provider = MailTypes.GMAIL,
                        imapHost = "",
                        smtpHost = ""
                    )
                    // Save immediately — no blocking connection test
                    app.repository.saveAccount(entity)
                    // Kick off a background sync
                    app.triggerSync()
                    onAccountAdded()
                }
            } else {
                error = "Failed to get email from Google Sign In"
            }
        } catch (e: ApiException) {
            error = "Google Sign In failed: ${e.statusCode}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(Scope("https://mail.google.com/"))
                        .build()
                    val client = GoogleSignIn.getClient(context, gso)
                    launcher.launch(client.signInIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Continue with Gmail")
                }
            }
            
            OutlinedButton(
                onClick = onImapSelected,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Other (IMAP)")
            }
            
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddImapAccountScreen(onBack: () -> Unit, onAccountAdded: () -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val scope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imapHost by remember { mutableStateOf("") }
    var smtpHost by remember { mutableStateOf("") }
    
    var testing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add IMAP Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password (or App Password)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = imapHost,
                onValueChange = { imapHost = it },
                label = { Text("IMAP Host (e.g. imap.mail.me.com)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = smtpHost,
                onValueChange = { smtpHost = it },
                label = { Text("SMTP Host (e.g. smtp.mail.me.com)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            
            Button(
                onClick = {
                    testing = true
                    error = null
                    scope.launch {
                        val id = UUID.randomUUID().toString()
                        val account = AccountEntity(
                            id = id,
                            email = email,
                            provider = MailTypes.IMAP,
                            imapHost = imapHost,
                            smtpHost = smtpHost
                        )
                        app.secretsStore.saveAccountSecret(id, password)
                        val provider = app.providerRegistry.forAccount(account)
                        val result = provider.testConnection(account)
                        
                        if (result.isSuccess) {
                            app.repository.saveAccount(account)
                            onAccountAdded()
                        } else {
                            error = result.exceptionOrNull()?.message ?: "Unknown error connecting to IMAP"
                            app.secretsStore.removeAccount(id)
                            testing = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !testing && email.isNotBlank() && password.isNotBlank() && imapHost.isNotBlank() && smtpHost.isNotBlank()
            ) {
                if (testing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Save Account")
                }
            }
        }
    }
}
