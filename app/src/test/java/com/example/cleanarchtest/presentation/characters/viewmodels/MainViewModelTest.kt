package com.example.cleanarchtest.presentation.characters.viewmodels

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.entities.Characters
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.CharacterLocation
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times


//не хватило времени на тесты
class MainViewModelTest {

    private val characterInteractor = mock<CharacterInteractor>()
// либо создать тут lateinit var viewModel и в beforeEach инициализировать
    @AfterEach
    fun afterEach(){
        Mockito.reset(characterInteractor)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun beforeEach(){
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor(){
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

        })
    }


//    @Test
//    fun `should load all characters`(){
//
//        val saveResult = true
//        val testSaveText = "Test user name"
//        val testParams = Characters()
//        Mockito.`when`(characterInteractor.getAllCharacters(testParams)).thenReturn(saveResult)
//
//        val viewModel = MainViewModel(getUserNameUseCase, saveUserNameUseCase)
//        viewModel.save(text = testSaveText)
//
//        val expected = "Saved result is true"
//        val actual = viewModel.resultLiveData.value
//
//        Mockito.verify(saveUserNameUseCase, times(1)).execute(param = testParams)
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should save character if network successful`(){
//        val saveResult = false
//        val testSaveText = "Test user name"
//        val testParams = SaveUserNameParam(name = testSaveText)
//        Mockito.`when`(saveUserNameUseCase.execute(testParams)).thenReturn(saveResult)
//
//        val viewModel = MainViewModel(getUserNameUseCase, saveUserNameUseCase)
//        viewModel.save(text = testSaveText)
//
//        val expected = "Saved result is false"
//        val actual = viewModel.resultLiveData.value
//
//        Mockito.verify(saveUserNameUseCase, times(1)).execute(param = testParams)
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should load character`(){
//
//        val testUserName = Characters(results=[Character(id=1, name="Rick Sanchez", species="Human", type=, status="Alive", gender="Male", image="https://rickandmortyapi.com/api/character/avatar/1.jpeg", origin= CharacterLocation(name="Earth (C-137)", url="https://rickandmortyapi.com/api/location/1"), location=CharacterLocation(name="Citadel of Ricks", url="https://rickandmortyapi.com/api/location/3"), episode=listOf("https://rickandmortyapi.com/api/episode/1", "https://rickandmortyapi.com/api/episode/2")]))
//
//        Mockito. `when`(getUserNameUseCase.execute()).thenReturn(testUserName)
//
//        val viewModel = MainViewModel(getUserNameUseCase, saveUserNameUseCase)
//        viewModel.load()
//        val expected = "${testUserName.firstName} ${testUserName.lastName}"
//        val actual = viewModel.resultLiveData.value
//
//        Assertions.assertEquals(expected, actual)
//    }

}