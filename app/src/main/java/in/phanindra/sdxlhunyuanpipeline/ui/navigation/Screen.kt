package `in`.phanindra.sdxlhunyuanpipeline.ui.navigation

sealed class Screen(val route: String) {
    data object CreateJob : Screen("create_job")
    data object CurrentJob : Screen("current_job")
    data object JobQueue : Screen("job_queue")
    data object Settings : Screen("settings")
    data object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: Int) = "job_detail/$jobId"
    }
}

