package com.uni.fsm.domain.model

data class Job(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val date: String,
    val time: String,
    val address: String,
    val status: String,
    val createdBy: JobUser,
    val assigned_to: JobUser,
)

data class JobUser(
    val user_id: String,
    val username: String,
)

