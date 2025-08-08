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
    val assignedTo: JobUser,
    val afterImageUploaded: Boolean,
    val afterImages: List<String>,
    val beforeImageUploaded: Boolean,
    val beforeImages: List<String>,
)

data class JobUser(
    val userId: String,
    val username: String,
)

