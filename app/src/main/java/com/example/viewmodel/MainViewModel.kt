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
    val gate7Density: Int = 88,
    val volunteersActive: Int = 42,
    val surgeMinutes: Int = 6,
    val transitWaitMins: Int = 5,
    val parkingFill: Int = 72,
    val attendance: Int = 58214,
    val temperature: Int = 28,
    val humidity: Int = 72,
    val aiScore: Int = 97
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
    
    private val _typingStatus = MutableStateFlow("")
    val typingStatus: StateFlow<String> = _typingStatus.asStateFlow()

    private val _currentRole = MutableStateFlow("Operations Staff")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow("English")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        viewModelScope.launch {
            val count = dao.getMessageCount()
            if (count == 0) {
                dao.insertMessage(ChatMessageEntity(text = "Hello Operations Staff 👋\n\nI am MATCHDAY AI.\nConnected to:\n✓ CCTV\n✓ Crowd Sensors\n✓ Transit APIs\n✓ Weather\n✓ Medical Teams\n\nAsk anything.", isUser = false))
            }
        }
        
        viewModelScope.launch {
            while (true) {
                delay(5000)
                val currentSurge = _dashboardState.value.surgeMinutes
                val nextSurge = if (currentSurge <= 1) (8..15).random() else maxOf(0, currentSurge - if (Math.random() > 0.5) 1 else 0)
                _dashboardState.value = DashboardState(
                    gate7Density = (75..95).random(),
                    volunteersActive = (38..45).random(),
                    surgeMinutes = nextSurge,
                    transitWaitMins = (1..10).random(),
                    parkingFill = (60..90).random(),
                    attendance = _dashboardState.value.attendance + (0..10).random(),
                    temperature = _dashboardState.value.temperature,
                    humidity = _dashboardState.value.humidity,
                    aiScore = (94..99).random()
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
            
            dao.insertMessage(ChatMessageEntity(text = text, isUser = true))

            _typingStatus.value = "Analyzing CCTV..."
            delay(800)
            _typingStatus.value = "Reading sensor feeds..."
            delay(800)
            _typingStatus.value = "Predicting congestion..."
            delay(800)
            _typingStatus.value = "Generating response..."
            
            val systemInstruction = """
                You are MATCHDAY AI, the Unified GenAI Operations Brain for FIFA World Cup 2026.
                You manage 16 host cities, 3 countries, and massive scale operations.
                Your current user role is: ${_currentRole.value}.
                You MUST respond in the following language: ${_currentLanguage.value}.
                Provide intelligent, proactive, and concise responses tailored to the role.
                If asked about congestion, you know Gate 7 has a projected halftime surge, and transit Hub A is flowing.
            """.trimIndent()
            
            val recentMessages = messages.value.takeLast(9)
            val history = recentMessages.joinToString("\n") { 
                if (it.isUser) "User: ${it.text}" else "AI: ${it.text}" 
            }
            val prompt = "Conversation History:\n$history\nUser: $text"

            val responseText = GeminiApi.generateContent(prompt, systemInstruction)
            
            val isError = responseText.startsWith("Error connecting to AI")
            
            dao.insertMessage(ChatMessageEntity(text = responseText, isUser = false, isError = isError))
            
            _isLoading.value = false
            _typingStatus.value = ""
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            dao.clearMessages()
            dao.insertMessage(ChatMessageEntity(text = "Hello Operations Staff 👋\n\nI am MATCHDAY AI.\nConnected to:\n✓ CCTV\n✓ Crowd Sensors\n✓ Transit APIs\n✓ Weather\n✓ Medical Teams\n\nAsk anything.", isUser = false))
        }
    }
    
    fun resolveEmergency() {
        val current = _dashboardState.value
        _dashboardState.value = current.copy(
            surgeMinutes = (15..20).random(),
            gate7Density = (50..65).random(),
            volunteersActive = current.volunteersActive + 3
        )
    }
}
