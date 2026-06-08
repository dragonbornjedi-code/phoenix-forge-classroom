package com.phoenixforge.profile.ui.memory

import android.Manifest
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.model.ArtifactType
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.model.MemoryCategory

@Composable
fun MemoryCapsuleScreen(
    viewModel: MemoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.refreshCloudGate() }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> viewModel.onCameraResult(success) }

    val cameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.prepareCamera()?.let { cameraLauncher.launch(it) }
    }

    fun launchCamera() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
        if (granted) {
            viewModel.prepareCamera()?.let { cameraLauncher.launch(it) }
        } else {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.importFromGallery(it) } }

    val cloudLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { viewModel.importFromCloud(it) } }

    val micPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.startRecording()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Memory Capsule", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Photos of Ezra, family time, and school outings — offline from your phone; cloud when on Wi‑Fi.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Save as", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MemoryCategory.entries.forEach { category ->
                FilterChip(
                    selected = state.selectedCategory == category,
                    onClick = { viewModel.selectCategory(category) },
                    label = { Text(category.displayName) },
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = { launchCamera() },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Text(" Camera", modifier = Modifier.padding(start = 4.dp))
            }
            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Text(" Phone", modifier = Modifier.padding(start = 4.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = {
                    if (state.canImportCloud) cloudLauncher.launch(arrayOf("image/*"))
                },
                enabled = state.canImportCloud,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Cloud, contentDescription = null)
                Text(
                    if (state.canImportCloud) " Drive" else " Drive (Wi‑Fi)",
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
            OutlinedButton(
                onClick = {
                    if (state.isRecording) {
                        viewModel.stopRecording()
                    } else {
                        val micGranted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO,
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        if (micGranted) viewModel.startRecording() else micPermission.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Text(
                    if (state.isRecording) " Stop" else " Voice",
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }

        state.message?.let {
            Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Filter", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.filterCategory == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("All") },
            )
            MemoryCategory.entries.forEach { category ->
                FilterChip(
                    selected = state.filterCategory == category,
                    onClick = { viewModel.setFilter(category) },
                    label = { Text(category.displayName) },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val visible = viewModel.visibleArtifacts()
        if (visible.isEmpty()) {
            Text("No memories yet. Capture or import a photo above.")
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(visible, key = { it.id }) { artifact ->
                    ArtifactItem(artifact)
                }
            }
        }

        if (state.selectedCategory == MemoryCategory.SCHOOL) {
            Text(
                AppBoundaryCopy.memorySchoolHint(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
fun ArtifactItem(artifact: MemoryArtifact) {
    Card(modifier = Modifier.aspectRatio(1f)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (artifact.type) {
                ArtifactType.PHOTO -> {
                    val bitmap = remember(artifact.localPath) {
                        BitmapFactory.decodeFile(artifact.localPath)
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = artifact.category.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Text("🖼️", style = MaterialTheme.typography.displaySmall)
                    }
                }
                ArtifactType.AUDIO -> Text("🎤", style = MaterialTheme.typography.displaySmall)
                ArtifactType.PROJECT -> Text("🔨", style = MaterialTheme.typography.displaySmall)
                else -> Text("📦", style = MaterialTheme.typography.displaySmall)
            }
        }
    }
}
