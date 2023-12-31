package dev.abhaycloud.koderunner.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import dev.abhaycloud.koderunner.data.local.LanguageDataSource
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.screens.home.LanguageUiState
import dev.abhaycloud.koderunner.utils.NetworkResponse
import dev.abhaycloud.koderunner.utils.Utils.printLogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val codeRepository: CodeRepository, private val languageDataSource: LanguageDataSource): ScreenModel {
    private val _languageList = MutableStateFlow<LanguageUiState>(LanguageUiState.Loading)
    val languageListResponse: StateFlow<LanguageUiState> = _languageList
    private val _availableLanguagesList = MutableStateFlow<List<LanguageModelItem>>(arrayListOf())
    private val _filteredLanguagesList = MutableStateFlow<List<LanguageModelItem>>(arrayListOf())
    val availableLanguagesList: StateFlow<List<LanguageModelItem>> = _availableLanguagesList
    val allLanguagesList: StateFlow<List<LanguageModelItem>> = _filteredLanguagesList



    init {
        getLocalAvailableLanguages()
    }

    private fun getLocalAvailableLanguages(){
        val languages = languageDataSource.getLanguages()
        if(languages.isEmpty()){
            printLogs("Local language is empty")
            getAvailableLanguages()
        } else {
            _availableLanguagesList.value = languages
            _filteredLanguagesList.value =languages
            _languageList.value = LanguageUiState.Success(languages)
        }
    }

    private fun getAvailableLanguages(){
        coroutineScope.launch {
            kotlin.runCatching {
                _languageList.value = LanguageUiState.Loading
                val result = codeRepository.getAvailableLanguages()

                when(result){
                    is NetworkResponse.Loading -> {
                        _languageList.value = LanguageUiState.Loading
                    }
                    is NetworkResponse.Success -> {
                        _availableLanguagesList.value = result.value
                        _filteredLanguagesList.value = result.value
                        _languageList.value = LanguageUiState.Success(result.value)
                        result.value.forEachIndexed{index, languageModelItem ->
                             languageDataSource.insertLanguage(languageModelItem, index.toLong())
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _languageList.value = LanguageUiState.Error(result.message)
                    }
                }
            }.onFailure {
                _languageList.value = LanguageUiState.Error(it.message.toString())
            }
        }
    }

    fun searchLanguage(searchTerm: String){
        val list = _filteredLanguagesList.value.filter {
            it.name.contains(searchTerm, ignoreCase = true)
        }
        _availableLanguagesList.value = if(searchTerm.isEmpty())  _filteredLanguagesList.value else list
    }

}