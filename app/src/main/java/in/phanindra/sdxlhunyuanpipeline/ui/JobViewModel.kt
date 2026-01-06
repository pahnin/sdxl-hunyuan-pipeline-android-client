package `in`.phanindra.sdxlhunyuanpipeline.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.phanindra.sdxlhunyuanpipeline.data.JobPayload
import `in`.phanindra.sdxlhunyuanpipeline.data.ObjectModel
import `in`.phanindra.sdxlhunyuanpipeline.data.JobRepository
import `in`.phanindra.sdxlhunyuanpipeline.data.JobResponse
import `in`.phanindra.sdxlhunyuanpipeline.data.HealthResponse
import `in`.phanindra.sdxlhunyuanpipeline.data.SettingsManager
import `in`.phanindra.sdxlhunyuanpipeline.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JobViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)

    private val _apiUrl = MutableStateFlow("")
    val apiUrl: StateFlow<String> = _apiUrl.asStateFlow()

    private var repository: JobRepository? = null

    private val _jobs = MutableStateFlow<List<JobResponse>>(emptyList())
    val jobs: StateFlow<List<JobResponse>> = _jobs.asStateFlow()

    private val _currentJob = MutableStateFlow<JobResponse?>(null)
    val currentJob: StateFlow<JobResponse?> = _currentJob.asStateFlow()

    private val _healthStatus = MutableStateFlow<HealthResponse?>(null)
    val healthStatus: StateFlow<HealthResponse?> = _healthStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            _apiUrl.value = settingsManager.apiUrl.first()
            initializeRepository()
            checkHealth()
            loadJobs()
        }
    }

    private fun initializeRepository() {
        if (_apiUrl.value.isNotEmpty()) {
            repository = JobRepository(RetrofitClient.create(_apiUrl.value))
        }
    }

    fun updateApiUrl(url: String) {
        viewModelScope.launch {
            settingsManager.saveApiUrl(url)
            _apiUrl.value = url
            initializeRepository()
            checkHealth()
            loadJobs()  // Add this line to reload jobs after URL change
        }
    }

    fun checkHealth() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository?.healthCheck()?.onSuccess { health ->
                    _healthStatus.value = health
                }?.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository?.listJobs()?.onSuccess { jobs ->
                    _jobs.value = jobs
                    val runningJob = jobs.find { it.status == "running" }
                    _currentJob.value = runningJob
                }?.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadJob(jobId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository?.getJob(jobId)?.onSuccess { job ->
                    if (job.status == "running") {
                        _currentJob.value = job
                    }
                }?.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createJob(payload: JobPayload) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository?.createJob(payload)?.onSuccess { response ->
                    loadJob(response.jobId)
                    loadJobs()
                }?.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelJob(jobId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository?.cancelJob(jobId)?.onSuccess {
                    loadJobs()
                }?.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
