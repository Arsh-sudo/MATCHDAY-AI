package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChatMessageEntity
import com.example.gemini.GeminiApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map

data class ChatMessage(
    val id: Int = 0,
    val text: String,
    val isUser: Boolean,
    val isError: Boolean = false,
    val isLoading: Boolean = false
)

data class DashboardState(
    val gate7Density: Int = 85,
    val volunteersActive: Int = 42,
    val surgeMinutes: Int = 12
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).chatMessageDao()

    val messages: StateFlow<List<ChatMessage>> = dao.getAllMessages()
        .map { entities ->
            entities.map { ChatMessage(id = it.id, text = it.text, isUser = it.isUser, isError = it.isError) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentRole = MutableStateFlow("Operations Staff")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow("English")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        viewModelScope.launch {
            // Check if we need to insert initial welcome message
            var isInitialized = false
            dao.getAllMessages().collect { msgs ->
                if (!isInitialized) {
                    if (msgs.isEmpty()) {
                        dao.insertMessage(ChatMessageEntity(text = "Welcome to MATCHDAY AI. I am the Unified GenAI Operations Brain for FIFA World Cup 2026. How can I assist you?", isUser = false))
                    }
                    isInitialized = true
                }
            }
        }
        
        // Feature 1: Simulate Live Data for Dashboard
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _dashboardState.value = DashboardState(
                    gate7Density = (75..95).random(),
                    volunteersActive = (38..45).random(),
                    surgeMinutes = maxOf(1, _dashboardState.value.surgeMinutes - if (Math.random() > 0.5) 1 else 0)
                )
            }
        }
    }

    fun setRole(role: String) {
        _currentRole.value = role
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return
        
        viewModelScope.launch {
            _isLoading.value = true
            
            // Save user message
            dao.insertMessage(ChatMessageEntity(text = text, isUser = true))

            val systemInstruction = """
                You are MATCHDAY AI, the Unified GenAI Operations Brain for FIFA World Cup 2026.
                You manage 16 host cities, 3 countries, and massive scale operations.
                Your current user role is: ${_currentRole.value}.
                You MUST respond in the following language: ${_currentLanguage.value}.
                Provide intelligent, proactive, and concise responses tailored to the role.
                If asked about congestion, you know Gate 7 has a projected halftime surge, and transit Hub A is flowing.
            """.trimIndent()
            
            // Build conversation history (capped to last 10 for Bug 5)
            val recentMessages = messages.value.takeLast(10)
            val history = recentMessages.joinToString("\n") { 
                if (it.isUser) "User: ${it.text}" else "AI: ${it.text}" 
            }
            val prompt = "Conversation History:\n$history\n\nNew User message: $text"

            val responseText = GeminiApi.generateContent(prompt, systemInstruction)
            
            val isError = responseText.startsWith("Error connecting to AI")
            
            // Save AI response
            dao.insertMessage(ChatMessageEntity(text = responseText, isUser = false, isError = isError))
            
            _isLoading.value = false
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            dao.clearMessages()
            dao.insertMessage(ChatMessageEntity(text = "Welcome to MATCHDAY AI. I am the Unified GenAI Operations Brain for FIFA World Cup 2026. How can I assist you?", isUser = false))
        }
    }
}
