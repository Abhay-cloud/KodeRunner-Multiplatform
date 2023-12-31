package dev.abhaycloud.koderunner.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import dev.abhaycloud.koderunner.data.local.LanguageDataSource
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.screens.snippet.SnippetUiState
import dev.abhaycloud.koderunner.utils.NetworkResponse
import dev.abhaycloud.koderunner.utils.Utils.printLogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SnippetsViewModel(private val codeRepository: CodeRepository, private val languageDataSource: LanguageDataSource) : ScreenModel {
    private val _snippetsResponse = MutableStateFlow<SnippetUiState>(SnippetUiState.Loading)
    val snippetsResponse: StateFlow<SnippetUiState> = _snippetsResponse
    private var currentPage = 1
    private var language = "java"
    private var _currentSelectedChip = MutableStateFlow(0)
    var currentSelectedChip: StateFlow<Int> = _currentSelectedChip
    var isFirstTime = true
    private var _languageModelItem = MutableStateFlow(LanguageModelItem(-1, "", "", "", ""))
    val languageModelItem: StateFlow<LanguageModelItem> = _languageModelItem

//    init {
//        getSnippetList(language)
//    }

    private fun getSnippetList(language: String) {
        coroutineScope.launch {
            kotlin.runCatching {
//                _snippetsResponse.value = SnippetUiState.Loading
                val result = codeRepository.getSnippets(language, currentPage)

//                val currentList = (_snippetsResponse.value as? SnippetUiState.Success)?.data ?: emptyList()
//                val newList = currentList + result

                _snippetsResponse.value = SnippetUiState.Success(result)

                // pagination
//                currentPage++
//                print(currentPage)
//                result.let {
//                    _snippetsResponse.value = SnippetUiState.Success(it)
//                }
            }.onFailure {
                _snippetsResponse.value = SnippetUiState.Error(it.message.toString())
                print(it.message.toString())
            }
        }
    }

    private fun getPreFilledCode(language: String) {
        coroutineScope.launch {
            kotlin.runCatching {
                val result = codeRepository.fetchPreFilledCode(language)
                when (result) {
                    is NetworkResponse.Loading -> {

                    }

                    is NetworkResponse.Success -> {
                        findPreFilledCode(result.value).let {
                            val code = it.replaceHtmlEntities()
                            printLogs(code)
                            val languageItem = languageDataSource.getLanguageByName(language)
                            languageItem.snippet = code
                            languageItem.extension = "main.${languageItem.name.getLanguageExtension()}"
                            languageDataSource.insertLanguage(languageItem, languageItem.id!!)
                            _languageModelItem.value = languageItem
                        }
                    }

                    is NetworkResponse.Failure -> {

                    }
                }
            }
        }
    }

    private fun findPreFilledCode(html: String): String {
        val startIndex = html.indexOf("""<div class="editor" id="editor-1"""")
        val endIndex = html.indexOf("</div>", startIndex)
        val rawContent = if (startIndex != -1 && endIndex != -1) {
            html.substring(startIndex, endIndex + "</div>".length)
        } else {
            null
        }
        val startTag = "<div class=\"editor\" id=\"editor-1\">"
        val endTag = "</div>"

        val startIndex1 = rawContent!!.indexOf(startTag)
        val endIndex1 = rawContent.indexOf(endTag, startIndex1 + startTag.length)

        if (startIndex1 != -1 && endIndex1 != -1) {
            return rawContent.substring(startIndex1 + startTag.length, endIndex1)
        }
        return ""
    }

    fun changeLanguage(language: LanguageModelItem, position: Int) {
        this.language = language.name
        currentPage = 1
        _currentSelectedChip.value = position
        getSnippetList(language.name)
        if(language.snippet!!.isEmpty()){
            getPreFilledCode(language.name)
        } else {
            printLogs(language)
            _languageModelItem.value = language
        }
    }

    private fun String.getLanguageExtension(): String {
        return when(this.lowercase()){
            "assembly" -> "asm"
            "ats" -> "dats"
            "bash" -> "sh"
            "clisp" -> "lsp"
            "clojure" -> "clj"
            "cobol" -> "cob"
            "coffeescript" -> "coffee"
            "crystal" -> "cr"
            "csharp" -> "cs"
            "elixir" -> "ex"
            "erlang" -> "erl"
            "fsharp" -> "fs"
            "guile" -> "scm"
            "hare" -> "ha"
            "haskell" -> "hs"
            "idris" -> "idr"
            "javascript" -> "js"
            "julia" -> "jl"
            "kotlin" -> "kt"
            "mercury" -> "m"
            "ocaml" -> "ml"
            "pascal" -> "pp"
            "perl" -> "pl"
            "python" -> "py"
            "ruby" -> "rb"
            "rust" -> "rs"
            "typescript" -> "ts"
            else -> {
                this
            }
        }
    }

    private fun String.replaceHtmlEntities(): String {
        return this.replace("&quot;", "\"")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&nbsp;", " ")
            .replace("&copy;", "©")
            .replace("&#39;", "'")
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
    }

}
