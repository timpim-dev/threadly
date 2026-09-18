package com.threadly.felixx.dev.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.threadly.felixx.dev.ThreadlyApplication
import com.threadly.felixx.dev.data.ClubEntity
import com.threadly.felixx.dev.data.ClubMatchMode
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClubScreen(id: String?, onBack: () -> Unit) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as ThreadlyApplication
    val scope = rememberCoroutineScope()
    val isNew = id == null || id == "new"

    var name by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(0xFF465D91) }
    var matchMode by remember { mutableStateOf(ClubMatchMode.KEYWORD_OR_CONTACT) }
    var keywords by remember { mutableStateOf(listOf<String>()) }
    var contacts by remember { mutableStateOf(listOf<String>()) }
    var accountIds by remember { mutableStateOf(listOf<String>()) }

    var isLoading by remember { mutableStateOf(true) }

    // Options for match modes
    val matchModeOptions = listOf(
        ClubMatchMode.KEYWORD_OR_CONTACT to "Keywords OR Contacts",
        ClubMatchMode.KEYWORD_ONLY to "Keywords Only",
        ClubMatchMode.CONTACT_ONLY to "Contacts Only"
    )

    // Predefined colors for avatars
    val colors = listOf(0xFF465D91, 0xFF914646, 0xFF46915E, 0xFF918A46, 0xFF7D4691, 0xFF468B91, 0xFF000000)

    LaunchedEffect(id) {
        if (!isNew && id != null) {
            val club = app.repository.club(id)
            val kws = app.repository.getClubKeywords(id)
            val cts = app.repository.getClubContacts(id)
            val accs = app.repository.getClubAccounts(id)
            
            club.collect { entity ->
                if (entity != null) {
                    name = entity.name
                    color = entity.avatarColor
                    matchMode = entity.matchMode
                    keywords = kws
                    contacts = cts
                    accountIds = accs
                    isLoading = false
                }
            }
        } else {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) "Create Club" else "Edit Club") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                actions = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                val clubId = if (isNew) UUID.randomUUID().toString() else id!!
                                val club = ClubEntity(
                                    id = clubId,
                                    name = name,
                                    avatarColor = color,
                                    matchMode = matchMode
                                )
                                // If accountIds is empty, we just link all current accounts for simplicity
                                // Or we can leave it empty, but the repository allows linking.
                                // Let's just pass empty for now.
                                app.repository.saveClub(club, keywords, contacts, accountIds)
                                onBack()
                            }
                        },
                        enabled = name.isNotBlank() && !isLoading
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Club Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Text("Avatar Color", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(c))
                                .clickable { color = c }
                                .padding(4.dp)
                        ) {
                            if (color == c) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Text("Match Mode", style = MaterialTheme.typography.titleMedium)
                Text("How should incoming emails be matched to this club?", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Column {
                    matchModeOptions.forEach { (mode, label) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { matchMode = mode }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = matchMode == mode,
                                onClick = { matchMode = mode }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Text("Keywords", style = MaterialTheme.typography.titleMedium)
                Text("Matches subject and body text.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
            }
            
            items(keywords) { kw ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(kw, Modifier.weight(1f))
                    IconButton(onClick = { keywords = keywords.filter { it != kw } }) {
                        Icon(Icons.Default.Close, "Remove")
                    }
                }
            }

            item {
                var newKeyword by remember { mutableStateOf("") }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newKeyword,
                        onValueChange = { newKeyword = it },
                        placeholder = { Text("Add keyword...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newKeyword.isNotBlank() && !keywords.contains(newKeyword.trim())) {
                                keywords = keywords + newKeyword.trim()
                                newKeyword = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Text("Contacts", style = MaterialTheme.typography.titleMedium)
                Text("Matches sender or recipient email addresses.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
            }

            items(contacts) { c ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(c, Modifier.weight(1f))
                    IconButton(onClick = { contacts = contacts.filter { it != c } }) {
                        Icon(Icons.Default.Close, "Remove")
                    }
                }
            }

            item {
                var newContact by remember { mutableStateOf("") }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newContact,
                        onValueChange = { newContact = it },
                        placeholder = { Text("Add email address...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newContact.isNotBlank() && !contacts.contains(newContact.trim().lowercase())) {
                                contacts = contacts + newContact.trim().lowercase()
                                newContact = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            }

            if (!isNew) {
                item { HorizontalDivider(Modifier.padding(vertical = 16.dp)) }
                item {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                app.repository.deleteClub(id!!)
                                onBack() // pop edit screen
                                onBack() // pop club screen back to home
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Delete Club")
                    }
                }
            }
        }
    }
}
