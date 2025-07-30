package com.joinself.app.demo

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joinself.common.CredentialType
import com.joinself.common.Environment
import com.joinself.sdk.SelfSDK
import com.joinself.sdk.models.Account
import com.joinself.sdk.models.ChatMessage
import com.joinself.sdk.models.CredentialMessage
import com.joinself.sdk.models.CredentialRequest
import com.joinself.sdk.models.Message
import com.joinself.sdk.models.PublicKey
import com.joinself.sdk.models.ResponseStatus
import com.joinself.sdk.models.VerificationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "MainViewModel"

sealed class InitializationState {
    data object None: ServerState()
    data object Loading: InitializationState()
    data object Success: InitializationState()
    data class  Error(val message: String): InitializationState()
}
sealed class ServerState {
    data object None: ServerState()
    data object Connecting: ServerState()
    data object Success: ServerState()
    data class  Error(val message: String): ServerState()
}
sealed class ServerRequestState {
    data object None: ServerRequestState()
    data object RequestSent: ServerRequestState()
    data class RequestReceived(val request: Any? = null): ServerRequestState()
    data class  RequestError(val message: String): ServerRequestState()
    data class  ResponseSent(val status: ResponseStatus): ServerRequestState()
}
sealed class BackupRestoreState {
    data object None: BackupRestoreState()
    data object Processing: BackupRestoreState()
    data object Success: BackupRestoreState()
    data class  Error(val message: String): BackupRestoreState()
    data object VerificationFailed: BackupRestoreState()
    data object DataRecoveryFailed: BackupRestoreState()
}

sealed class SERVER_REQUESTS {
    companion object {
        const val REQUEST_CREDENTIAL_AUTH: String = "REQUEST_CREDENTIAL_AUTH"
        const val REQUEST_CREDENTIAL_EMAIL: String = "PROVIDE_CREDENTIAL_EMAIL"
        const val REQUEST_CREDENTIAL_DOCUMENT: String = "PROVIDE_CREDENTIAL_DOCUMENT"
        const val REQUEST_CREDENTIAL_CUSTOM: String = "PROVIDE_CREDENTIAL_CUSTOM"
        const val REQUEST_DOCUMENT_SIGNING: String = "REQUEST_DOCUMENT_SIGNING"
        const val REQUEST_GET_CUSTOM_CREDENTIAL: String = "REQUEST_GET_CUSTOM_CREDENTIAL"
    }
}

// the main states of the app
data class AppUiState(
    var isRegistered: Boolean = false,
    var initialization: InitializationState = InitializationState.Loading,
    var serverState: ServerState = ServerState.None,
    var requestState: ServerRequestState = ServerRequestState.None,
    var backupRestoreState: BackupRestoreState = BackupRestoreState.None
)

class MainViewModel(context: Context): ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appStateFlow: StateFlow<AppUiState> = _appUiState.asStateFlow()

    val account: Account
    var serverInboxAddress: PublicKey? = null
    private var groupAddress: PublicKey? = null
    private var credentialRequest: CredentialRequest? = null
    private var verificationRequest: VerificationRequest? = null
    private var requestTimeoutJob: Job? = null
//    private val receivedCredentials = mutableListOf<Credential>()

    init {
        // init the sdk
        SelfSDK.initialize(
            context,
            pushToken = null,
            log = { Log.d("SelfSDK", it) }
        )

        // the sdk will store data in this directory, make sure it exists.
        val storagePath = File(context.filesDir.absolutePath + "/account1")
        if (!storagePath.exists()) storagePath.mkdirs()

        account = Account.Builder()
            .setContext(context)
            .setEnvironment(Environment.production)
            .setSandbox(true)
            .setStoragePath(storagePath.absolutePath)
            .setCallbacks(object : Account.Callbacks {
                override fun onMessage(message: Message) {
                    Log.d("Self", "onMessage: ${message.id()}")

                    when (message) {
                        is CredentialMessage -> {
                            _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived(message))}
                        }
                        is CredentialRequest -> {
                            credentialRequest = message
                            _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived(message))}
                        }
                        is VerificationRequest -> {
                            // check the request is agreement, this example will respond automatically to the request
                            // users need to handle msg.proofs() which contains agreement content, to display to user
                            if (message.types().contains(CredentialType.Agreement)) {
                                verificationRequest = message
                                _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived(message)) }
                            }
                        }
                    }

                    cancelRequestTimeout()
                }
                override fun onConnect() {
                    Log.d("Self", "onConnect")
                    _appUiState.update {
                        it.copy(
                            initialization = InitializationState.Success
                        )
                    }
                }
                override fun onDisconnect(errorMessage: String?) {
                    Log.d("Self", "onDisconnect: $errorMessage")
                }
                override fun onAcknowledgement(id: String) {
                    Log.d("Self", "onAcknowledgement: $id")
                }
                override fun onError(id: String, errorMessage: String?) {
                    Log.d("Self", "onError: $errorMessage")
                }
            })
            .build()

        _appUiState.update {
            it.copy(
                isRegistered = account.registered(),
            )
        }
    }


    fun isRegistered() : Boolean {
        return account.registered()
    }

    // connect with server using an inbox address
    suspend fun connect(inboxAddress: String) {
        try {
            _appUiState.update { it.copy(serverState = ServerState.Connecting) }
            serverInboxAddress = PublicKey(inboxAddress)

            groupAddress = account.connectWith(serverInboxAddress!!, info = mapOf())
            if (groupAddress != null) {
                _appUiState.update { it.copy(serverState = ServerState.Success) }
            } else {
                _appUiState.update { it.copy(serverState = ServerState.Error("failed to connect to server")) }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message, ex)
            _appUiState.update { it.copy(serverState = ServerState.Error(ex.message ?: "failed to connect to server")) }
        }
    }

    suspend fun connect(inboxAddress: PublicKey, qrCode: ByteArray) {
        try {
            groupAddress = account.connectWith(qrCode)
            serverInboxAddress = inboxAddress

            if (groupAddress != null) {
                _appUiState.update { it.copy(serverState = ServerState.Success) }
            } else {
                _appUiState.update { it.copy(serverState = ServerState.Error("failed to connect to server")) }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message, ex)
            _appUiState.update { it.copy(serverState = ServerState.Error(ex.message ?: "failed to connect to server")) }
        }
    }

    // reset local variables and states
    fun resetState(requestState: ServerRequestState) {
        Log.d(TAG, "reset states")
        _appUiState.update { it.copy(requestState = requestState) }
        credentialRequest = null
        verificationRequest = null
    }

    fun setBackupRestoreState(state: BackupRestoreState) {
        _appUiState.update { it.copy(backupRestoreState = state) }
    }


    /**
     * Notifies the server about a request by sending a chat message.
     * After server receive a chat message, it will check the content and send a request message.
     */
    suspend fun notifyServerForRequest(message: String) {
        requireNotNull(serverInboxAddress)

        val chat = ChatMessage.Builder()
            .setMessage(message)
            .build()

        // send chat to server
        val messageId = account.send(toAddress = serverInboxAddress!!, chat)
        _appUiState.update { it.copy(requestState = ServerRequestState.RequestSent) }

        startRequestTimeout()
    }

    /**
     * Starts a timeout for a request.
     * If the request is not received within 20 seconds, the request state is updated to [ServerRequestState.RequestError].
     */
    private fun startRequestTimeout() {
        requestTimeoutJob = viewModelScope.launch(Dispatchers.IO) {
            delay(20000)
            if (_appUiState.value.requestState == ServerRequestState.RequestSent)
                _appUiState.update { it.copy(requestState = ServerRequestState.RequestError("request timed out")) }
        }
    }
    private fun cancelRequestTimeout() {
        requestTimeoutJob?.cancel()
    }
}