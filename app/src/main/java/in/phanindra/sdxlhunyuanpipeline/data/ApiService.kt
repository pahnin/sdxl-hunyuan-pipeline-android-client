package `in`.phanindra.sdxlhunyuanpipeline.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("jobs/")
    suspend fun createJob(@Body payload: JobPayload): Response<CreateJobResponse>

    @GET("jobs/")
    suspend fun listJobs(
        @Query("status_filter") statusFilter: String? = null,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<List<JobResponse>>

    @GET("jobs/{job_id}")
    suspend fun getJob(@Path("job_id") jobId: Int): Response<JobResponse>

    @DELETE("jobs/{job_id}")
    suspend fun cancelJob(@Path("job_id") jobId: Int): Response<CancelJobResponse>

    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>
}

