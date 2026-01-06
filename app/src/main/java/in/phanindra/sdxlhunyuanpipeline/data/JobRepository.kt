package `in`.phanindra.sdxlhunyuanpipeline.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JobRepository(private val apiService: ApiService) {
    private val _currentJob = MutableStateFlow<JobResponse?>(null)
    val currentJob: StateFlow<JobResponse?> = _currentJob.asStateFlow()

    suspend fun createJob(payload: JobPayload): Result<CreateJobResponse> {
        return try {
            val response = apiService.createJob(payload)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create job: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJob(jobId: Int): Result<JobResponse> {
        return try {
            val response = apiService.getJob(jobId)
            if (response.isSuccessful) {
                val job = response.body()!!
                if (job.status == "running") {
                    _currentJob.value = job
                }
                Result.success(job)
            } else {
                Result.failure(Exception("Failed to get job: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listJobs(statusFilter: String? = null): Result<List<JobResponse>> {
        return try {
            val response = apiService.listJobs(statusFilter = statusFilter)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                // Add logging
                android.util.Log.e("JobRepository", "Failed to list jobs: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Failed to list jobs: ${response.message()}"))
            }
        } catch (e: Exception) {
            // Add logging
            android.util.Log.e("JobRepository", "Error listing jobs", e)
            Result.failure(e)
        }
    }

    suspend fun cancelJob(jobId: Int): Result<CancelJobResponse> {
        return try {
            val response = apiService.cancelJob(jobId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to cancel job: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun healthCheck(): Result<HealthResponse> {
        return try {
            val response = apiService.healthCheck()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Health check failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun clearCurrentJob() {
        _currentJob.value = null
    }
}

