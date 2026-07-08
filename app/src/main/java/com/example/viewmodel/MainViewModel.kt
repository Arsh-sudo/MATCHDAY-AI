package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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

    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val _useRealSensors = MutableStateFlow(true)
    val useRealSensors: StateFlow<Boolean> = _useRealSensors.asStateFlow()

    private val _liveLightLux = MutableStateFlow(0f)
    val liveLightLux: StateFlow<Float> = _liveLightLux.asStateFlow()

    private val _livePressureHpa = MutableStateFlow(0f)
    val livePressureHpa: StateFlow<Float> = _livePressureHpa.asStateFlow()

    private val _liveVibrationMagnitude = MutableStateFlow(0f)
    val liveVibrationMagnitude: StateFlow<Float> = _liveVibrationMagnitude.asStateFlow()

    private val _hasLightSensor = MutableStateFlow(false)
    val hasLightSensor: StateFlow<Boolean> = _hasLightSensor.asStateFlow()

    private val _hasPressureSensor = MutableStateFlow(false)
    val hasPressureSensor: StateFlow<Boolean> = _hasPressureSensor.asStateFlow()

    private val _hasAccelerometer = MutableStateFlow(false)
    val hasAccelerometer: StateFlow<Boolean> = _hasAccelerometer.asStateFlow()

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
            when (event.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    _liveLightLux.value = event.values[0]
                }
                Sensor.TYPE_PRESSURE -> {
                    _livePressureHpa.value = event.values[0]
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val mag = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    _liveVibrationMagnitude.value = mag
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private fun registerSensors() {
        val manager = sensorManager ?: return
        
        val lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            _hasLightSensor.value = true
            manager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI)
        }
        
        val pressureSensor = manager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        if (pressureSensor != null) {
            _hasPressureSensor.value = true
            manager.registerListener(sensorListener, pressureSensor, SensorManager.SENSOR_DELAY_UI)
        }
        
        val accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accel != null) {
            _hasAccelerometer.value = true
            manager.registerListener(sensorListener, accel, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun unregisterSensors() {
        sensorManager?.unregisterListener(sensorListener)
    }

    fun setUseRealSensors(enabled: Boolean) {
        _useRealSensors.value = enabled
    }

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

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loggedInUserType = MutableStateFlow("") // "fan" or "staff"
    val loggedInUserType: StateFlow<String> = _loggedInUserType.asStateFlow()

    private val _fanName = MutableStateFlow("")
    val fanName: StateFlow<String> = _fanName.asStateFlow()

    private val _fanSeat = MutableStateFlow("")
    val fanSeat: StateFlow<String> = _fanSeat.asStateFlow()

    private val _fanTicketId = MutableStateFlow("")
    val fanTicketId: StateFlow<String> = _fanTicketId.asStateFlow()

    private val _staffId = MutableStateFlow("")
    val staffId: StateFlow<String> = _staffId.asStateFlow()

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
        registerSensors()
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
                if (nextMinutes >= 90) {
                    nextMinutes = 90
                    nextSeconds = 0
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

    fun loginAsFan(name: String, ticketId: String, seat: String) {
        _fanName.value = name
        _fanTicketId.value = ticketId
        _fanSeat.value = seat
        _loggedInUserType.value = "fan"
        _currentRole.value = "Fan Concierge"
        _isLoggedIn.value = true
        
        viewModelScope.launch {
            dao.clearMessages()
            dao.insertMessage(ChatMessageEntity(
                text = "Hello $name 👋\n\nWelcome to Estadio Azteca! I am your **FIFA 2026 Fan Concierge**, powered by Gemini.\n\n🎫 **Ticket Ref**: $ticketId\n💺 **Block**: $seat\n\nAsk me anything! For example:\n- 'How do I get to Block $seat?'\n- 'Which gate has the shortest queue?'\n- 'Where is the closest merchandise stand?'",
                isUser = false
            ))
        }
    }

    fun loginAsStaff(id: String, role: String) {
        _staffId.value = id
        _loggedInUserType.value = "staff"
        _currentRole.value = role
        _isLoggedIn.value = true
        
        viewModelScope.launch {
            dao.clearMessages()
            dao.insertMessage(ChatMessageEntity(
                text = "Access Authorized (Staff ID: $id) 👋\n\nMATCHDAY AI initialized as **$role**.\nConnected to:\n✓ HD CCTV Gate Cameras\n✓ Turnstile Flow Sensors\n✓ Local Transit Dispatch\n✓ Medical & Safety Radios\n\nI am ready to help you support safe and efficient stadium operations.",
                isUser = false
            ))
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _loggedInUserType.value = ""
        _fanName.value = ""
        _fanTicketId.value = ""
        _fanSeat.value = ""
        _staffId.value = ""
        viewModelScope.launch {
            dao.clearMessages()
        }
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
            
            val state = _dashboardState.value
            val systemInstruction = if (_loggedInUserType.value == "fan") {
                """
                    You are the FIFA WORLD CUP 2026 FAN CONCIERGE, a friendly, helpful, and highly intelligent AI assistant at Estadio Azteca.
                    You are assisting a fan named ${_fanName.value} who holds ticket ${_fanTicketId.value} and is seated in Block ${_fanSeat.value}.
                    Your goal is to provide a world-class, premium, and stress-free tournament experience.
                    You MUST respond in the following language: ${_currentLanguage.value}.
                    
                    REAL-TIME STADIUM SNAPSHOT:
                    - Match: Mexico vs Brazil, Current Score: Mex ${state.mexScore} - ${state.braScore} Bra, Minute: ${state.matchMinutes}'
                    - Stadium Weather: ${state.temperature}°C, ${state.humidity}% Humidity
                    - Gate 7 Exit Density: ${state.gate7Density}% (If > 75%, warn them to exit through Gate 3 or Gate 5 for rapid transit access)
                    - Bus/Metro Waiting Time: ${state.transitWaitMins} mins
                    - Food & Beverage Lines: Stand 4 is low wait; Mexican Taco bar near Gate 3 has 5 min wait.
                    
                    INSTRUCTIONS:
                    1. Address the fan by name (${_fanName.value}) when appropriate, maintaining a polite, excited, and warm tournament host tone. Do not be overly repetitive with greetings.
                    2. Provide practical assistance for seat location (Block ${_fanSeat.value}), nearby amenities, elevators, and accessibility options.
                    3. Proactively guide them on crowd navigation (e.g., recommend entering/exiting via Gate 3/5 instead of Gate 7 due to high density).
                    4. Keep answers concise (max 3 sentences) so the fan can read them quickly while walking or cheering in the stands.
                """.trimIndent()
            } else {
                """
                    You are MATCHDAY AI, the Unified GenAI Operations Brain for FIFA World Cup 2026.
                    You manage 16 host cities, 3 countries (Mexico, USA, Canada), and massive scale tournament operations.
                    Your current user role is: ${_currentRole.value}.
                    You MUST respond in the following language: ${_currentLanguage.value}.
                    
                    REAL-TIME STADIUM SNAPSHOT:
                    - Match: Mexico vs Brazil, Current Score: Mex ${state.mexScore} - ${state.braScore} Bra, Minute: ${state.matchMinutes}'
                    - Attendance: ${state.attendance} / 62,300 fans
                    - Overall Crowd Density: ${state.gate7Density}%
                    - Active Incidents: ${state.activeIncidentsCount} (If > 0, Gate 7 has High Density & an active critical alert requiring deployment)
                    - Active Volunteers: ${state.volunteersActive}
                    - Gate 7 Halftime Surge Projection: ${state.surgeMinutes} mins
                    - Transit Wait Time: ${state.transitWaitMins} mins
                    - Parking Fill: ${state.parkingFill}%
                    - Stadium Weather: ${state.temperature}°C, ${state.humidity}% Humidity
                    
                    INSTRUCTIONS:
                    1. Provide highly intelligent, proactive, and concise responses tailored directly to the user's role (${_currentRole.value}).
                    2. Answer queries by referencing the actual real-time numbers in the STADIUM SNAPSHOT above. Be realistic, helpful, and authoritative.
                    3. Keep answers concise (max 3-4 sentences) so they are easily readable on mobile screens by busy operators.
                    4. Suggest actionable next steps (e.g., dispatching volunteers, notifying transit hubs) when densities are high or incidents are active.
                """.trimIndent()
            }
            
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
            if (_loggedInUserType.value == "fan") {
                dao.insertMessage(ChatMessageEntity(
                    text = "Hello ${_fanName.value} 👋\n\nWelcome to Estadio Azteca! I am your **FIFA 2026 Fan Concierge**, powered by Gemini.\n\n🎫 **Ticket Ref**: ${_fanTicketId.value}\n💺 **Block**: ${_fanSeat.value}\n\nAsk me anything! For example:\n- 'How do I get to Block ${_fanSeat.value}?'\n- 'Which gate has the shortest queue?'\n- 'Where is the closest merchandise stand?'",
                    isUser = false
                ))
            } else {
                dao.insertMessage(ChatMessageEntity(
                    text = "Access Authorized (Staff ID: ${_staffId.value}) 👋\n\nMATCHDAY AI initialized as **${_currentRole.value}**.\nConnected to:\n✓ HD CCTV Gate Cameras\n✓ Turnstile Flow Sensors\n✓ Local Transit Dispatch\n✓ Medical & Safety Radios\n\nI am ready to help you support safe and efficient stadium operations.",
                    isUser = false
                ))
            }
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

    override fun onCleared() {
        super.onCleared()
        unregisterSensors()
    }
}
