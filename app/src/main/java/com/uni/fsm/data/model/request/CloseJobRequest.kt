package com.uni.fsm.data.model.request

data class CloseJobRequest(
    val job_id: String,
    val student_id: String
)
