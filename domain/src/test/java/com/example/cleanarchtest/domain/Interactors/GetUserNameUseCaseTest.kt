package com.example.cleanarchtest.domain.Interactors
//
//import com.example.cleanarchtest.domain.other.models.UserName
//import com.example.cleanarchtest.domain.other.repository.UserRepository
//import com.example.cleanarchtest.domain.other.usecase.GetUserNameUseCase
//import org.junit.jupiter.api.Assertions
//import org.mockito.kotlin.mock
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito
//
//class GetUserNameUseCaseTest {
//
//    private val userRepository = mock<UserRepository>()
//
//    @Test
//    fun `should return the same data as in repository`(){
//
//        val testUserName = UserName("michael", "mavrodi")
//        Mockito.`when`(userRepository.getName()).thenReturn(testUserName)
//
//        val useCase = GetUserNameUseCase(userRepository)
//        val actual = useCase.execute()
//        val expected = UserName("michael", "mavrodi")
//
//        Assertions.assertEquals(expected, actual)
//
//    }
//}