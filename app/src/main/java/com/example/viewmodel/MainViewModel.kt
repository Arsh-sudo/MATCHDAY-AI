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

data class GateState(
    val name: String,
    val status: String,
    val isWarning: Boolean,
    val expectedPeak: String,
    val confidence: String,
    val people: String,
    val waitTime: String,
    val volunteers: String,
    val exits: String,
    val trendPoints: List<Float>
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
    val aiScore: Int = 97,
    val matchMinutes: Int = 72,
    val matchSeconds: Int = 18,
    val mexScore: Int = 2,
    val braScore: Int = 1,
    val activeIncidentsCount: Int = 1,
    val gates: Map<String, GateState> = emptyMap()
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

    private val _dashboardState = MutableStateFlow(
        DashboardState(
            gate7Density = 88,
            volunteersActive = 42,
            surgeMinutes = 6,
            transitWaitMins = 5,
            parkingFill = 72,
            attendance = 58214,
            temperature = 28,
            humidity = 72,
            aiScore = 97,
            matchMinutes = 72,
            matchSeconds = 18,
            mexScore = 2,
            braScore = 1,
            activeIncidentsCount = 1,
            gates = emptyMap() // populated in init
        )
    )
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    private fun generateDefaultGates(
        surgeMinutes: Int, 
        gate7Density: Int, 
        volunteersActive: Int,
        activeIncidentsCount: Int
    ): Map<String, GateState> {
        val gate7Trend = listOf(0.1f, 0.2f, 0.15f, 0.5f, 0.9f, (gate7Density / 100f).coerceIn(0f, 1f))
        
        return mapOf(
            "GATE 7" to GateState(
                name = "GATE 7",
                status = if (activeIncidentsCount > 0) "HIGH CONGESTION" else if (gate7Density >= 50) "MODERATE CONGESTION" else "NORMAL DENSITY",
                isWarning = activeIncidentsCount > 0,
                expectedPeak = if (activeIncidentsCount > 0) "Expected peak in $surgeMinutes mins" else "Flow is stabilizing",
                confidence = "Confidence 96%",
                people = String.format("%,d", gate7Density * 50 + (100..300).random()),
                waitTime = if (activeIncidentsCount > 0) "${(gate7Density * 0.2).toInt() + 1} min" else "${(4..8).random()} min",
                volunteers = "${volunteersActive / 12}",
                exits = if (activeIncidentsCount > 0) "2/4" else "4/4",
                trendPoints = gate7Trend
            ),
            "GATE 1" to GateState(
                name = "GATE 1",
                status = "NORMAL DENSITY",
                isWarning = false,
                expectedPeak = "Stable flow expected",
                confidence = "Confidence 98%",
                people = String.format("%,d", (1100..1300).random()),
                waitTime = "${(3..5).random()} min",
                volunteers = "${(6..9).random()}",
                exits = "4/4",
                trendPoints = listOf(0.2f, 0.25f, 0.2f, 0.3f, 0.22f, 0.18f)
            ),
            "GATE 2" to GateState(
                name = "GATE 2",
                status = "NORMAL DENSITY",
                isWarning = false,
                expectedPeak = "Stable flow expected",
                confidence = "Confidence 95%",
                people = String.format("%,d", (1700..1900).random()),
                waitTime = "${(4..6).random()} min",
                volunteers = "${(5..8).random()}",
                exits = "3/4",
                trendPoints = listOf(0.15f, 0.22f, 0.3f, 0.25f, 0.28f, 0.2f)
            ),
            "GATE 3" to GateState(
                name = "GATE 3",
                status = "MODERATE CONGESTION",
                isWarning = false,
                expectedPeak = "Peak expected in ${(12..18).random()} mins",
                confidence = "Confidence 91%",
                people = String.format("%,d", (2900..3200).random()),
                waitTime = "${(10..13).random()} min",
                volunteers = "${(3..5).random()}",
                exits = "2/4",
                trendPoints = listOf(0.3f, 0.45f, 0.65f, 0.6f, 0.5f, 0.42f)
            ),
            "GATE 4" to GateState(
                name = "GATE 4",
                status = "NORMAL DENSITY",
                isWarning = false,
                expectedPeak = "Stable flow expected",
                confidence = "Confidence 97%",
                people = String.format("%,d", (850..1050).random()),
                waitTime = "${(2..4).random()} min",
                volunteers = "${(8..11).random()}",
                exits = "4/4",
                trendPoints = listOf(0.12f, 0.15f, 0.1f, 0.14f, 0.12f, 0.1f)
            ),
            "GATE 5" to GateState(
                name = "GATE 5",
                status = "NORMAL DENSITY",
                isWarning = false,
                expectedPeak = "Stable flow expected",
                confidence = "Confidence 94%",
                people = String.format("%,d", (1050..1250).random()),
                waitTime = "${(3..5).random()} min",
                volunteers = "${(6..8).random()}",
                exits = "4/4",
                trendPoints = listOf(0.22f, 0.18f, 0.2f, 0.15f, 0.25f, 0.18f)
            ),
            "GATE 6" to GateState(
                name = "GATE 6",
                status = "MODERATE CONGESTION",
                isWarning = false,
                expectedPeak = "Peak expected in ${(18..24).random()} mins",
                confidence = "Confidence 89%",
                people = String.format("%,d", (2200..2500).random()),
                waitTime = "${(8..11).random()} min",
                volunteers = "${(4..6).random()}",
                exits = "3/4",
                trendPoints = listOf(0.25f, 0.35f, 0.5f, 0.48f, 0.4f, 0.35f)
            )
        )
    }

    init {
        // Initialize gates
        val initVal = _dashboardState.value
        _dashboardState.value = initVal.copy(
            gates = generateDefaultGates(
                surgeMinutes = initVal.surgeMinutes,
                gate7Density = initVal.gate7Density,
                volunteersActive = initVal.volunteersActive,
                activeIncidentsCount = initVal.activeIncidentsCount
            )
        )

        viewModelScope.launch {
            val count = dao.getMessageCount()
            if (count == 0) {
                dao.insertMessage(ChatMessageEntity(text = "Hello Operations Staff 👋\n\nI am MATCHDAY AI.\nConnected to:\n✓ CCTV\n✓ Crowd Sensors\n✓ Transit APIs\n✓ Weather\n✓ Medical Teams\n\nAsk anything.", isUser = false))
            }
        }
        
        viewModelScope.launch {
            while (true) {
                delay(5000)
                val current = _dashboardState.value
                val nextSurge = if (current.surgeMinutes <= 1) (8..15).random() else maxOf(0, current.surgeMinutes - if (Math.random() > 0.4) 1 else 0)
                val nextGate7Density = if (current.activeIncidentsCount == 0) (40..60).random() else (75..95).random()
                val nextVolunteers = (38..45).random() + (if (current.activeIncidentsCount == 0) 5 else 0)
                
                // Increment match clock (5 seconds simulated step)
                var nextSeconds = current.matchSeconds + 5
                var nextMinutes = current.matchMinutes
                if (nextSeconds >= 60) {
                    nextSeconds -= 60
                    nextMinutes += 1
                }
                
                // Keep score updated
                var nextMex = current.mexScore
                var nextBra = current.braScore
                // 1% chance of random goal during second half up to 90'
                if (Math.random() > 0.99 && nextMinutes < 90) {
                    if (Math.random() > 0.5) nextMex += 1 else nextBra += 1
                }

                val nextAttendance = current.attendance + (0..10).random()
                val nextParking = (60..90).random()
                val nextTransit = (1..10).random()
                val nextAiScore = if (current.activeIncidentsCount > 0) (94..98).random() else (97..100).random()
                
                _dashboardState.value = DashboardState(
                    gate7Density = nextGate7Density,
                    volunteersActive = nextVolunteers,
                    surgeMinutes = nextSurge,
                    transitWaitMins = nextTransit,
                    parkingFill = nextParking,
                    attendance = if (nextAttendance < 62300) nextAttendance else 62300,
                    temperature = current.temperature,
                    humidity = current.humidity,
                    aiScore = nextAiScore,
                    matchMinutes = nextMinutes,
                    matchSeconds = nextSeconds,
                    mexScore = nextMex,
                    braScore = nextBra,
                    activeIncidentsCount = current.activeIncidentsCount,
                    gates = generateDefaultGates(nextSurge, nextGate7Density, nextVolunteers, current.activeIncidentsCount)
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
        val resolvedGate7Density = (45..55).random()
        val resolvedVolunteers = current.volunteersActive + 3
        val resolvedSurge = (15..20).random()
        _dashboardState.value = current.copy(
            surgeMinutes = resolvedSurge,
            gate7Density = resolvedGate7Density,
            volunteersActive = resolvedVolunteers,
            activeIncidentsCount = 0,
            gates = generateDefaultGates(resolvedSurge, resolvedGate7Density, resolvedVolunteers, 0)
        )
    }
}
