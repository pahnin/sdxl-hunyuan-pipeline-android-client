package `in`.phanindra.sdxlhunyuanpipeline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.phanindra.sdxlhunyuanpipeline.ui.JobViewModel
import androidx.compose.ui.Alignment
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentJobScreen(
    viewModel: JobViewModel,
    onViewJobDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentJob by viewModel.currentJob.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadJobs()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Current Job Status") }
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
                currentJob != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                                    text = "Job #${currentJob!!.id}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Status: ${currentJob!!.status.uppercase()}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Object: ${currentJob!!.payload.obj.name}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { onViewJobDetail(currentJob!!.id) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("View Details")
                                }
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No job is currently running",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

