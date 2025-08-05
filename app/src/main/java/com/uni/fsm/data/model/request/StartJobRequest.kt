package com.uni.fsm.data.model.request

data class StartJobRequest(
    val job_id: String,
    val technician_id: String
)
