@file:OptIn(ExperimentalMaterial3Api::class)
package com.threadly.felixx.dev.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.threadly.felixx.dev.ThreadlyApplication
import com.threadly.felixx.dev.data.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable private fun Shell(title: String, onBack: (() -> Unit)? = null, action: (@Composable () -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }, navigationIcon = { if (onBack != null) IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { action?.invoke() }) }, content = { padding -> Column(Modifier.padding(padding).fillMaxSize(), content = content) })
}

@Composable
fun HomeScreen(onThread: (String) -> Unit, onProfile: () -> Unit, onCompose: () -> Unit, onManageClub: (String) -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val clubs by app.repository.clubs.collectAsState(emptyList())
    val accounts by app.repository.accounts.collectAsState(emptyList())
    val isSyncing by app.isSyncing.collectAsState()
    val settings by app.repository.settings.collectAsState(null)
    
    var selectedClubId by rememberSaveable { mutableStateOf("unsorted") }
    LaunchedEffect(settings?.defaultClubId) {
        if (selectedClubId == "unsorted" && settings?.defaultClubId != null) {
            selectedClubId = settings!!.defaultClubId
        }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text("Threadly", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
                
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        NavigationDrawerItem(
                            label = { Text("Unsorted Mail") },
                            selected = selectedClubId == "unsorted",
                            onClick = { selectedClubId = "unsorted"; scope.launch { drawerState.close() } },
                            icon = { Icon(Icons.Default.Inbox, "Unsorted") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    items(clubs.filter { it.id != "unsorted" }, key = { it.id }) { club ->
                        NavigationDrawerItem(
                            label = { Text(club.name) },
                            selected = selectedClubId == club.id,
                            onClick = { selectedClubId = club.id; scope.launch { drawerState.close() } },
                            icon = { Avatar(club.name, club.avatarColor, size = 24.dp) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                        NavigationDrawerItem(
                            label = { Text("Add Club") },
                            selected = false,
                            onClick = { scope.launch { drawerState.close() }; onManageClub("new") },
                            icon = { Icon(Icons.Default.Add, "Add") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Profile & Settings") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onProfile() },
                    icon = { Icon(Icons.Default.AccountCircle, "Profile") },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        val selectedClub by app.repository.club(selectedClubId).collectAsState(null)
        val threads by app.repository.threads(selectedClubId).collectAsState(emptyList())

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedClub?.name ?: if (selectedClubId == "unsorted") "Unsorted Mail" else "Club", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        if (selectedClubId != "unsorted") {
                            IconButton(onClick = { onManageClub(selectedClubId) }) { Icon(Icons.Default.Settings, "Manage") }
                        }
                        IconButton(onClick = { app.triggerSync() }, enabled = !isSyncing) {
                            if (isSyncing) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Refresh, "Refresh")
                            }
                        }
                    }
                )
            },
            floatingActionButton = { FloatingActionButton(onClick = onCompose) { Icon(Icons.Default.Edit, "New thread") } }
        ) { padding ->
            LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (accounts.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Column(Modifier.padding(16.dp)) {
                                Text("No account connected", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                                Text("Open the side menu and go to Profile to add an account.", color = MaterialTheme.colorScheme.onErrorContainer)
                            }
                        }
                    }
                }
                
                items(threads, key = { it.id }) { thread -> 
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            when (it) {
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    val action = settings?.swipeActionRight ?: SwipeAction.ARCHIVE
                                    if (action == SwipeAction.ARCHIVE) {
                                        scope.launch { app.repository.archive(thread.id) }
                                        true
                                    } else {
                                        if (action == SwipeAction.MARK_READ) scope.launch { app.repository.markRead(thread.id) }
                                        // TODO handle SNOOZE later
                                        false
                                    }
                                }
                                SwipeToDismissBoxValue.EndToStart -> {
                                    val action = settings?.swipeActionLeft ?: SwipeAction.MARK_READ
                                    if (action == SwipeAction.ARCHIVE) {
                                        scope.launch { app.repository.archive(thread.id) }
                                        true
                                    } else {
                                        if (action == SwipeAction.MARK_READ) scope.launch { app.repository.markRead(thread.id) }
                                        // TODO handle SNOOZE later
                                        false
                                    }
                                }
                                else -> false
                            }
                        },
                        positionalThreshold = { it * 0.5f }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val targetValue = dismissState.targetValue
                            if (targetValue != SwipeToDismissBoxValue.Settled) {
                                val action = if (targetValue == SwipeToDismissBoxValue.StartToEnd) (settings?.swipeActionRight ?: SwipeAction.ARCHIVE) else (settings?.swipeActionLeft ?: SwipeAction.MARK_READ)
                                val color = if (action == SwipeAction.ARCHIVE) Color(0xFFE57373) else Color(0xFF81C784)
                                val icon = if (action == SwipeAction.ARCHIVE) Icons.Default.Archive else Icons.Default.MarkEmailRead
                                val alignment = if (targetValue == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                                Box(Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(color).padding(horizontal = 24.dp), contentAlignment = alignment) {
                                    Icon(icon, contentDescription = action.name, tint = Color.White)
                                }
                            }
                        }
                    ) {
                        ThreadRow(thread) { onThread(thread.id) }
                    }
                }
                
                if (threads.isEmpty() && accounts.isNotEmpty()) {
                    item { EmptyState("No conversations", "New messages will appear here.") }
                }
            }
        }
    }
}

@Composable private fun Avatar(label: String, color: Long, size: androidx.compose.ui.unit.Dp = 52.dp) { 
    val textSize = if (size < 40.dp) MaterialTheme.typography.labelSmall.fontSize else MaterialTheme.typography.bodyLarge.fontSize
    Box(Modifier.size(size).clip(CircleShape).background(Color(color)), contentAlignment = Alignment.Center) { 
        Text(label.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = textSize) 
    } 
}

@Composable private fun ThreadRow(thread: ThreadEntity, onClick: () -> Unit) { Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(thread.subject.ifBlank { "(No subject)" }, fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.Normal); Text(thread.preview, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant) }; if (thread.unreadCount > 0) Badge { Text(thread.unreadCount.toString()) } } }

@Composable
fun ThreadScreen(id: String, onBack: () -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val thread by app.repository.thread(id).collectAsState(null)
    val messages by app.repository.messages(id).collectAsState(emptyList())
    val settings by app.repository.settings.collectAsState(null)
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    // Mark thread as read when opened
    LaunchedEffect(id) {
        app.repository.markRead(id)
    }

    var showAiSheet by remember { mutableStateOf(false) }

    Shell(thread?.subject?.ifBlank { "Conversation" } ?: "Conversation", onBack, action = {
        if (!settings?.openRouterApiKey.isNullOrBlank()) {
            IconButton(onClick = { showAiSheet = true }) {
                Icon(Icons.Default.AutoAwesome, "AI Assistant")
            }
        }
        IconButton(onClick = { scope.launch { app.repository.setMuted(id, !(thread?.muted ?: false)) } }) {
            Icon(if (thread?.muted == true) Icons.Default.NotificationsOff else Icons.Default.Notifications, "Mute")
        }
    }) {
        val spacing = if (settings?.compactLayout == true) 4.dp else 12.dp
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            items(messages, key = { it.id }) { message -> MessageBubble(message, settings) }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    if (settings?.enterToSend == true && it.endsWith("\n")) {
                        val toSend = it.trim()
                        if (toSend.isNotBlank()) {
                            scope.launch { app.repository.sendReply(id, toSend) }
                            text = ""
                        }
                    } else {
                        text = it
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text(if (settings?.enterToSend == true) "Type and press Enter" else "Reply…") },
                maxLines = 4
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    val toSend = text.trim()
                    if (toSend.isNotBlank()) {
                        scope.launch { app.repository.sendReply(id, toSend) }
                        text = ""
                    }
                },
                enabled = text.isNotBlank()
            ) {
                Icon(Icons.Default.Send, "Send")
            }
        }
    }

    if (showAiSheet) {
        var aiMode by remember { mutableStateOf("menu") } // menu, ask, reply, summary
        var aiQuery by remember { mutableStateOf("") }
        var aiResponse by remember { mutableStateOf("") }
        var isGenerating by remember { mutableStateOf(false) }

        fun generate(systemPrompt: String, userPrompt: String, onDone: (String) -> Unit = {}) {
            val key = settings?.openRouterApiKey
            val model = settings?.openRouterModel ?: "meta-llama/llama-3.3-70b-instruct:free"
            if (key.isNullOrBlank()) return
            isGenerating = true
            aiResponse = ""
            scope.launch {
                try {
                    val res = com.threadly.felixx.dev.mail.AiAssistant.sendPrompt(key, model, systemPrompt, userPrompt)
                    aiResponse = res
                    onDone(res)
                } catch (e: Exception) {
                    aiResponse = "Error: ${e.message}"
                } finally {
                    isGenerating = false
                }
            }
        }

        val threadContext = remember(messages) {
            messages.joinToString("\n\n") { "From: ${it.senderName}\n${it.body}" }
        }

        ModalBottomSheet(onDismissRequest = { showAiSheet = false }, modifier = Modifier.fillMaxHeight(0.9f)) {
            Column(Modifier.padding(16.dp).fillMaxWidth()) {
                if (aiMode == "menu") {
                    Text("AI Assistant", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        aiMode = "summary"
                        generate("You are a helpful assistant. Summarize the following email thread concisely.", threadContext)
                    }, modifier = Modifier.fillMaxWidth()) { Text("Summarize Thread") }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { aiMode = "ask" }, modifier = Modifier.fillMaxWidth()) { Text("Ask about this thread") }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { aiMode = "reply" }, modifier = Modifier.fillMaxWidth()) { Text("Draft a reply") }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { aiMode = "menu"; aiResponse = ""; aiQuery = "" }) { Icon(Icons.Default.ArrowBack, "Back") }
                        Text(
                            when(aiMode) {
                                "summary" -> "Summary"
                                "ask" -> "Ask a question"
                                "reply" -> "Draft Reply"
                                else -> ""
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (aiMode == "ask" || aiMode == "reply") {
                        OutlinedTextField(
                            value = aiQuery,
                            onValueChange = { aiQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(if (aiMode == "ask") "What do you want to know?" else "What should the reply say?") },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        val sys = if (aiMode == "ask") "Answer the user's question based ONLY on this email thread:\n\n$threadContext"
                                                  else "Write an email reply based on this thread. The user's instructions are: $aiQuery\n\nThread context:\n$threadContext"
                                        generate(sys, if (aiMode == "ask") aiQuery else "Draft a reply")
                                    },
                                    enabled = !isGenerating && aiQuery.isNotBlank()
                                ) {
                                    Icon(Icons.Default.Send, "Send")
                                }
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (isGenerating) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (aiResponse.isNotBlank()) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(aiResponse, modifier = Modifier.verticalScroll(rememberScrollState()))
                                if (aiMode == "reply" && !aiResponse.startsWith("Error")) {
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            text = aiResponse
                                            showAiSheet = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text("Use this draft") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun MessageBubble(message: MessageEntity, settings: AppSettingsEntity?) {
    val isMe = message.isFromUser
    val timeStr = remember(message.sentAt) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.sentAt))
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe && settings?.showAvatarsInThread == true) {
            Avatar(message.senderName, 0xFF7A7A7A)
            Spacer(Modifier.width(8.dp))
        }
        Surface(
            shape = RoundedCornerShape((settings?.messageCornerRadius ?: 18).dp),
            color = if (isMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(Modifier.padding(if (settings?.compactLayout == true) 8.dp else 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (isMe) "You" else message.senderName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(timeStr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(5.dp))
                Text(message.body)
            }
        }
    }
}

@Composable fun ComposeScreen(onBack: () -> Unit) { var subject by remember { mutableStateOf("") }; var body by remember { mutableStateOf("") }; Shell("New thread", onBack, action = { TextButton(onClick = onBack, enabled = subject.isNotBlank() && body.isNotBlank()) { Text("Send") } }) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Choose a club and sending account before sending.", color = MaterialTheme.colorScheme.onSurfaceVariant); OutlinedTextField(subject, { subject = it }, Modifier.fillMaxWidth(), label = { Text("Subject") }); OutlinedTextField(body, { body = it }, Modifier.fillMaxWidth().weight(1f), label = { Text("Message") }) } } }

@Composable fun ProfileScreen(onBack: () -> Unit, onSettings: () -> Unit, onAddAccount: () -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val accounts by app.repository.accounts.collectAsState(emptyList())
    val scope = rememberCoroutineScope()
    Shell("Profile", onBack) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Text("Accounts", style = MaterialTheme.typography.titleLarge) }
            items(accounts, key = { it.id }) { account ->
                ListItem(
                    headlineContent = { Text(account.email) },
                    supportingContent = { Text(account.provider) },
                    leadingContent = { Icon(Icons.Default.AccountCircle, null) },
                    trailingContent = {
                        IconButton(onClick = {
                            scope.launch {
                                app.repository.deleteAccount(account)
                                app.secretsStore.removeAccount(account.id)
                            }
                        }) { Icon(Icons.Default.Delete, "Remove") }
                    }
                )
            }
            item {
                OutlinedButton(onClick = onAddAccount, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("Add Account")
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                ListItem(
                    headlineContent = { Text("Settings") },
                    leadingContent = { Icon(Icons.Default.Settings, null) },
                    modifier = Modifier.clickable(onClick = onSettings),
                    trailingContent = { Icon(Icons.Default.ChevronRight, null) }
                )
            }
            item { Spacer(Modifier.height(24.dp)); Text("Threadly 1.0", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable fun SettingsScreen(onBack: () -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val settings by app.repository.settings.collectAsState(null)
    val scope = rememberCoroutineScope()
    
    val updateSettings: (AppSettingsEntity.() -> AppSettingsEntity) -> Unit = { updater ->
        scope.launch { settings?.let { app.repository.saveSettings(it.updater()) } }
    }

    Shell("Settings", onBack) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            item { Text("Appearance", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp)) }
            item {
                ListItem(headlineContent = { Text("Dark Mode") }, supportingContent = { Text(settings?.theme ?: "system") }, modifier = Modifier.clickable { updateSettings { copy(theme = if (theme == "dark") "light" else "dark") } })
                ListItem(headlineContent = { Text("Dynamic Colors") }, trailingContent = { Switch(settings?.dynamicColors ?: true, { v -> updateSettings { copy(dynamicColors = v) } }) })
            }
            
            item { HorizontalDivider(Modifier.padding(vertical = 16.dp)) }
            
            item { Text("Chat & UX", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp)) }
            item {
                var showClubDialog by remember { mutableStateOf(false) }
                val clubs by app.repository.clubs.collectAsState(emptyList())
                val currentClub = clubs.find { it.id == (settings?.defaultClubId ?: "unsorted") }?.name ?: "Unsorted Mail"
                
                ListItem(
                    headlineContent = { Text("Default Club on Startup") },
                    supportingContent = { Text(currentClub) },
                    modifier = Modifier.clickable { showClubDialog = true }
                )
                if (showClubDialog) {
                    AlertDialog(
                        onDismissRequest = { showClubDialog = false },
                        title = { Text("Select Default Club") },
                        text = {
                            LazyColumn {
                                item {
                                    ListItem(
                                        headlineContent = { Text("Unsorted Mail") },
                                        modifier = Modifier.clickable { updateSettings { copy(defaultClubId = "unsorted") }; showClubDialog = false }
                                    )
                                }
                                items(clubs.filter { it.id != "unsorted" }, key = { it.id }) { club ->
                                    ListItem(
                                        headlineContent = { Text(club.name) },
                                        modifier = Modifier.clickable { updateSettings { copy(defaultClubId = club.id) }; showClubDialog = false }
                                    )
                                }
                            }
                        },
                        confirmButton = { TextButton(onClick = { showClubDialog = false }) { Text("Cancel") } }
                    )
                }
                ListItem(headlineContent = { Text("Show Avatars in Thread") }, trailingContent = { Switch(settings?.showAvatarsInThread ?: true, { v -> updateSettings { copy(showAvatarsInThread = v) } }) })
                ListItem(headlineContent = { Text("Enter to Send") }, trailingContent = { Switch(settings?.enterToSend ?: false, { v -> updateSettings { copy(enterToSend = v) } }) })
                ListItem(headlineContent = { Text("Compact Layout") }, trailingContent = { Switch(settings?.compactLayout ?: false, { v -> updateSettings { copy(compactLayout = v) } }) })
                ListItem(headlineContent = { Text("Bubble Corner Radius: ${settings?.messageCornerRadius ?: 18}") }, trailingContent = { Slider(value = (settings?.messageCornerRadius ?: 18).toFloat(), onValueChange = { v -> updateSettings { copy(messageCornerRadius = v.toInt()) } }, valueRange = 0f..32f, modifier = Modifier.width(100.dp)) })
            }

            item { HorizontalDivider(Modifier.padding(vertical = 16.dp)) }

            item { Text("Sync & Notifications", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp)) }
            item {
                ListItem(headlineContent = { Text("Faster sync") }, supportingContent = { Text("Checks every few minutes and uses more battery") }, trailingContent = { Switch(settings?.fasterSync ?: false, { v -> updateSettings { copy(fasterSync = v) } }) })
                ListItem(headlineContent = { Text("New mail notifications") }, trailingContent = { Switch(settings?.notificationsEnabled ?: true, { v -> updateSettings { copy(notificationsEnabled = v) } }) })
            }
            item { HorizontalDivider(Modifier.padding(vertical = 16.dp)) }

            item { Text("AI Integrations", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp)) }
            item {
                var showKeyDialog by remember { mutableStateOf(false) }
                ListItem(
                    headlineContent = { Text("OpenRouter API Key") },
                    supportingContent = { Text(if (settings?.openRouterApiKey.isNullOrBlank()) "Not set" else "••••••••••••••••") },
                    modifier = Modifier.clickable { showKeyDialog = true }
                )
                if (showKeyDialog) {
                    var tempKey by remember { mutableStateOf(settings?.openRouterApiKey ?: "") }
                    AlertDialog(
                        onDismissRequest = { showKeyDialog = false },
                        title = { Text("OpenRouter API Key") },
                        text = {
                            OutlinedTextField(
                                value = tempKey,
                                onValueChange = { tempKey = it },
                                label = { Text("API Key") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                updateSettings { copy(openRouterApiKey = tempKey.trim()) }
                                showKeyDialog = false
                            }) { Text("Save") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showKeyDialog = false }) { Text("Cancel") }
                        }
                    )
                }
                
                var showModelDialog by remember { mutableStateOf(false) }
                ListItem(
                    headlineContent = { Text("OpenRouter Model") },
                    supportingContent = { Text(settings?.openRouterModel ?: "meta-llama/llama-3.3-70b-instruct:free") },
                    modifier = Modifier.clickable { showModelDialog = true }
                )
                if (showModelDialog) {
                    var tempModel by remember { mutableStateOf(settings?.openRouterModel ?: "meta-llama/llama-3.3-70b-instruct:free") }
                    AlertDialog(
                        onDismissRequest = { showModelDialog = false },
                        title = { Text("OpenRouter Model") },
                        text = {
                            OutlinedTextField(
                                value = tempModel,
                                onValueChange = { tempModel = it },
                                label = { Text("Model ID") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                updateSettings { copy(openRouterModel = tempModel.trim()) }
                                showModelDialog = false
                            }) { Text("Save") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showModelDialog = false }) { Text("Cancel") }
                        }
                    )
                }
            }
        }
    }
}
@Composable private fun EmptyState(title: String, body: String) { Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
