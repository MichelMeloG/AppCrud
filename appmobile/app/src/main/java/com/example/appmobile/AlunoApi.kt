package com.example.appmobile

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlunoApi {
    @GET("alunos/")
    suspend fun listAlunos(): Response<List<Aluno>>

    @GET("alunos/{matricula}")
    suspend fun getAluno(@Path("matricula") matricula: String): Response<Aluno>

    @POST("alunos/")
    suspend fun createAluno(@Body aluno: Aluno): Response<Aluno>

    @PUT("alunos/{matricula}")
    suspend fun updateAluno(
        @Path("matricula") matricula: String,
        @Body aluno: Aluno
    ): Response<Aluno>

    @DELETE("alunos/{matricula}")
    suspend fun deleteAluno(@Path("matricula") matricula: String): Response<Unit>
}
