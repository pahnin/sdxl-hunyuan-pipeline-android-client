package `in`.phanindra.sdxlhunyuanpipeline.data

import com.google.gson.annotations.SerializedName

data class ObjectModel(
    val name: String,
    val view: String,
    val initial: String,
    val final: String,
    val motion: String,
    val environment: String = "studio environment",
    @SerializedName("negative_prompt")
    val negativePrompt: String = "pixelated, deformed, bad anatomy, incohesive, inconsistent, double edges, outlines, halos, sharp contours"
)

data class JobPayload(
    val obj: ObjectModel,
    val seed: Int = 2000,
    val resolution: Int = 480,
    val duration: Float = 5.0f,
    @SerializedName("latent_window")
    val latentWindow: Int = 8,
    @SerializedName("num_steps")
    val numSteps: Int = 30,
    @SerializedName("cfg_scale")
    val cfgScale: Float = 1.0f,
    @SerializedName("cfg_rescale")
    val cfgRescale: Float = 0.0f,
    @SerializedName("magcache_threshold")
    val magcacheThreshold: Float = 0.15f,
    @SerializedName("magcache_max_consecutive_skips")
    val magcacheMaxConsecutiveSkips: Int = 10,
    @SerializedName("magcache_retention_ratio")
    val magcacheRetentionRatio: Float = 0.9f
)

data class JobResponse(
    val id: Int,
    val status: String,
    val payload: JobPayload,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("attempt_count")
    val attemptCount: Int,
    @SerializedName("last_error")
    val lastError: String?,
    @SerializedName("output_dir")
    val outputDir: String?,
    @SerializedName("video_url")
    val videoUrl: String?,
    @SerializedName("start_frame_url")
    val startFrameUrl: String?,
    @SerializedName("end_frame_url")
    val endFrameUrl: String?
)

data class CreateJobResponse(
    @SerializedName("job_id")
    val jobId: Int,
    val status: String,
    val message: String
)

data class HealthResponse(
    val status: String,
    val timestamp: String,
    val database: String
)

data class CancelJobResponse(
    @SerializedName("job_id")
    val jobId: Int,
    val status: String,
    val message: String
)

