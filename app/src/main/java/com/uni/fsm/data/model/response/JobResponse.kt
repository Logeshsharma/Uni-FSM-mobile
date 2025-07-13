package com.uni.fsm.data.model.response

import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.model.JobUser

data class JobResponse(
    val job_id: String?,
    val title: String?,
    val description: String?,
    val job_category: String?,
    val job_date: String?,
    val job_time: String?,
    val address: String?,
    val status: String?,
    val created_by: UserDto?,
    val assigned_to: UserDto?,
)

data class UserDto(
    val user_id: String?,
    val username: String?,
)

fun JobResponse.toDomain(): Job {
    return Job(
        id = job_id ?: "",
        title = title ?: "",
        description = description ?: "",
        category = job_category ?: "",
        date = job_date ?: "",
        time = job_time ?: "",
        address = address ?: "",
        status = status ?: "",
        createdBy = JobUser(
            user_id = created_by?.user_id ?: "",
            username = created_by?.username ?: "",
        ),
        assigned_to = JobUser(
            user_id = assigned_to?.user_id ?: "",
            username = assigned_to?.username ?: "Not yet Assigned",
        )
    )
}