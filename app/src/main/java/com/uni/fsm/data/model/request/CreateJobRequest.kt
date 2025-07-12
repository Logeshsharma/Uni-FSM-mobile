package com.uni.fsm.data.model.request

data class CreateJobRequest(
    val user_id: String,
    val title: String,
    val description: String,
    val job_category: String,
    val job_date: String,
    val job_time: String,
    val address: String,
)