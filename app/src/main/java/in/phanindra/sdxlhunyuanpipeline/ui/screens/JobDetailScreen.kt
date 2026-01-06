package `in`.phanindra.sdxlhunyuanpipeline.ui.screens

import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import `in`.phanindra.sdxlhunyuanpipeline.ui.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    jobId: Int,
    viewModel: JobViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val jobs by viewModel.jobs.collectAsState()
    val job = jobs.find { it.id == jobId }
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(jobId) {
        viewModel.loadJob(jobId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job #$jobId Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.loadJob(jobId) }
                    ) {
                        Text("↻")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                job != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Job #${job.id}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Status: ${job.status.uppercase()}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Object: ${job.payload.obj.name}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "View: ${job.payload.obj.view}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Motion: ${job.payload.obj.motion}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Created: ${job.createdAt}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Updated: ${job.updatedAt}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                
                                if (job.lastError != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Error: ${job.lastError}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (job.videoUrl != null) {
                            Text(
                                text = "Generated Video",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .background(Color.Black)
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        VideoView(context).apply {
                                            val mediaController = MediaController(context)
                                            mediaController.setAnchorView(this)
                                            setMediaController(mediaController)
                                            
                                            setOnPreparedListener { mp ->
                                                mp.isLooping = true
                                                mp.start()
                                            }
                                            setOnErrorListener { _, what, extra ->
                                                android.widget.Toast.makeText(context, "Video Error: $what, $extra", android.widget.Toast.LENGTH_LONG).show()
                                                true
                                            }
                                        }
                                    },
                                    update = { videoView ->
                                        val uri = android.net.Uri.parse(job.videoUrl)
                                        if (videoView.tag != job.videoUrl) {
                                            videoView.setVideoURI(uri)
                                            videoView.tag = job.videoUrl
                                            // Start is called in OnPreparedListener
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        if (job.startFrameUrl != null) {
                            Text(
                                text = "Start Frame",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(job.startFrameUrl),
                                    contentDescription = "Start frame",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        if (job.endFrameUrl != null) {
                            Text(
                                text = "End Frame",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(job.endFrameUrl),
                                    contentDescription = "End frame",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Job not found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
