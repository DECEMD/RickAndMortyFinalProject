package com.example.cleanarchtest.domain.Interactors
//
//import com.example.cleanarchtest.domain.other.models.SaveUserNameParam
//import com.example.cleanarchtest.domain.other.models.UserName
//import com.example.cleanarchtest.domain.other.repository.UserRepository
//import com.example.cleanarchtest.domain.other.usecase.SaveUserNameUseCase
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito
//import org.mockito.Mockito.never
//import org.mockito.Mockito.times
//import org.mockito.kotlin.any
//import org.mockito.kotlin.mock
//
//class SaveUserNameUseCaseTest {
//
//    private val userRepository = mock<UserRepository>()
//
//    @AfterEach
//    fun tearDown(){
//        Mockito.reset(userRepository)
//    }
//
//    @Test
//    fun `should not save data if name was already saved`(){
//
//        val testUserName = UserName("test name", "mavrodi")
//        Mockito.`when`(userRepository.getName()).thenReturn(testUserName)
//
//        val useCase = SaveUserNameUseCase(userRepository = userRepository)
//        val testParams = SaveUserNameParam(name = "test name")
//        val actual = useCase.execute(testParams)
//        val expected = true
//        Assertions.assertEquals(expected, actual)
//        Mockito.verify(userRepository, never()).saveName(any())
//    }
//
//    @Test
//    fun `should return true if save was successful`(){
//
//        val testUserName = UserName("test name", "mavrodi")
//        Mockito.`when`(userRepository.getName()).thenReturn(testUserName)
//
//        val testParams = SaveUserNameParam(name = "test2 name")
//        val expected = true
//        Mockito.`when`(userRepository.saveName(testParams)).thenReturn(expected)
//
//        val useCase = SaveUserNameUseCase(userRepository = userRepository)
//        val actual = useCase.execute(testParams)
//        Assertions.assertEquals(expected, actual)
//        Mockito.verify(userRepository, times(1)).saveName(testParams)
//
//    }
//
//    @Test
//    fun `should return false if save was successful`(){
//
//        val testUserName = UserName("test name", "mavrodi")
//        Mockito.`when`(userRepository.getName()).thenReturn(testUserName)
//
//        val testParams = SaveUserNameParam(name = "test2 name")
//        val expected = false
//        Mockito.`when`(userRepository.saveName(testParams)).thenReturn(expected)
//
//        val useCase = SaveUserNameUseCase(userRepository = userRepository)
//        val actual = useCase.execute(testParams)
//        Assertions.assertEquals(expected, actual)
//        Mockito.verify(userRepository, times(1)).saveName(testParams)
//
//    }
//}