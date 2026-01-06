package `in`.phanindra.sdxlhunyuanpipeline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.phanindra.sdxlhunyuanpipeline.data.JobPayload
import `in`.phanindra.sdxlhunyuanpipeline.data.ObjectModel
import `in`.phanindra.sdxlhunyuanpipeline.ui.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(
    viewModel: JobViewModel,
    onJobCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var view by remember { mutableStateOf("") }
    var initial by remember { mutableStateOf("") }
    var final by remember { mutableStateOf("") }
    var motion by remember { mutableStateOf("") }
    var environment by remember { mutableStateOf("studio environment") }
    var seed by remember { mutableStateOf("2000") }
    var resolution by remember { mutableStateOf("480") }
    var duration by remember { mutableStateOf("5.0") }
    
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Job") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Object Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = view,
                onValueChange = { view = it },
                label = { Text("View") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = initial,
                onValueChange = { initial = it },
                label = { Text("Initial State") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = final,
                onValueChange = { final = it },
                label = { Text("Final State") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = motion,
                onValueChange = { motion = it },
                label = { Text("Motion Description") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = environment,
                onValueChange = { environment = it },
                label = { Text("Environment") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = seed,
                onValueChange = { seed = it },
                label = { Text("Seed") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = resolution,
                onValueChange = { resolution = it },
                label = { Text("Resolution") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (seconds)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    val payload = JobPayload(
                        obj = ObjectModel(
                            name = name,
                            view = view,
                            initial = initial,
                            final = final,
                            motion = motion,
                            environment = environment
                        ),
                        seed = seed.toIntOrNull() ?: 2000,
                        resolution = resolution.toIntOrNull() ?: 480,
                        duration = duration.toFloatOrNull() ?: 5.0f
                    )
                    viewModel.createJob(payload)
                    onJobCreated()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && name.isNotBlank() && view.isNotBlank() && initial.isNotBlank() && final.isNotBlank() && motion.isNotBlank()
            ) {
                Text("Create Job")
            }
        }
    }
}

