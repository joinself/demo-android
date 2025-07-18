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
    data class RequestReceived(val types: List<String> = listOf(), val subjects: List<String> = listOf()): ServerRequestState()
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
    var serverInboxAddress: String = ""
    private var groupAddress: String = ""
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
                        is CredentialRequest -> {
                            credentialRequest = message
                            _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived())}
                        }
                        is VerificationRequest -> {
                            // check the request is agreement, this example will respond automatically to the request
                            // users need to handle msg.proofs() which contains agreement content, to display to user
                            if (message.types().contains(CredentialType.Agreement)) {
                                verificationRequest = message
                                _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived()) }
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

        // setup listeners which receive events from the server.
//        account.setOnMessageListener { msg ->
//            when (msg) {
//                // receive custom credentials messages
//                is CredentialMessage -> {
//                    Log.d("Self", "received credential message")
//                    receivedCredentials.clear()
//                    receivedCredentials.addAll(msg.credentials())
//
//                    _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived()) }
//
//                    cancelRequestTimeout()
//                }
//            }
//        }
//        account.setOnRequestListener { msg ->
//            when (msg) {
//                is CredentialRequest -> {
//                    credentialRequest = msg
//                    _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived(types = msg.details().flatMap { d -> d.types() }, subjects = msg.details().map { d -> d.subject() })) }
//                }
//                is VerificationRequest -> {
//                    // check the request is agreement, this example will respond automatically to the request
//                    // users need to handle msg.proofs() which contains agreement content, to display to user
//                    if (msg.types().contains(CredentialType.Agreement)) {
//                        verificationRequest = msg
//                        _appUiState.update { it.copy(requestState = ServerRequestState.RequestReceived()) }
//                    }
//                }
//            }
//
//            cancelRequestTimeout()
//        }
    }


    fun isRegistered() : Boolean {
        return account.registered()
    }

    // connect with server using an inbox address
    suspend fun connect(inboxAddress: String) {
        try {
            _appUiState.update { it.copy(serverState = ServerState.Connecting) }
            serverInboxAddress = inboxAddress

            groupAddress = account.connectWith(PublicKey(serverInboxAddress), info = mapOf())
            if (groupAddress.isNotEmpty()) {
                _appUiState.update { it.copy(serverState = ServerState.Success) }
            } else {
                _appUiState.update { it.copy(serverState = ServerState.Error("failed to connect to server")) }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message, ex)
            _appUiState.update { it.copy(serverState = ServerState.Error(ex.message ?: "failed to connect to server")) }
        }
    }

    suspend fun connect(inboxAddress: String, qrCode: ByteArray) {
        try {
            groupAddress = account.connectWith(qrCode)
            serverInboxAddress = inboxAddress

            if (groupAddress.isNotEmpty()) {
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


    /**
     * Notifies the server about a request by sending a chat message.
     * After server receive a chat message, it will check the content and send a request message.
     */
    suspend fun notifyServerForRequest(message: String) {
        val chat = ChatMessage.Builder()
            .setMessage(message)
            .build()

        // send chat to server
        val messageId = account.send(toAddress = PublicKey(serverInboxAddress), chat)
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

    /**
     * Sends a response to a credential request.
     * This function constructs and sends a [CredentialResponse] based on the received [CredentialRequest].
     */
//    fun sendCredentialResponse(credentials: List<Credential>, status: ResponseStatus) {
//        if (credentialRequest == null) return
//
//        val credentialResponse = CredentialResponse.Builder()
//            .setRequestId(credentialRequest!!.id())
//            .setTypes(credentialRequest!!.types())
//            .setToIdentifier(credentialRequest!!.toIdentifier())
//            .setFromIdentifier(credentialRequest!!.fromIdentifier())
//            .setStatus(status)
//            .setCredentials(credentials)
//            .build()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            account.send(credentialResponse) { messageId, _ ->
//                _appUiState.update { it.copy(requestState = ServerRequestState.ResponseSent(status)) }
//            }
//        }
//    }

    /**
     * Looks up credentials based on the details of the credential request and sends a response.
     * The credentials are store in the account after you verify email, document or get custom credentials.
     *
     * If there is no pending credential request, the function returns without doing anything.
     *
     * @param status The status of the response to be sent (e.g., accepted, rejected).
     */
//    fun shareCredential(status: ResponseStatus) {
//        if (credentialRequest == null) return
//
//        val details = credentialRequest!!.details().map {
//            Claim.Builder()
//                .setTypes(it.types())
//                .setSubject(it.subject())
//                .setComparisonOperator(it.comparisonOperator())
//                .setValue(it.value())
//                .build()
//        }
//        val storedCredentials = account.lookUpCredentials(details)
//
//        sendCredentialResponse(storedCredentials, status)
//    }

    /**
     * Stores the received custom credentials in the account.
     */
//    fun storeCredentials() {
//        account.storeCredentials(receivedCredentials)
//        receivedCredentials.clear()
//    }

    /**
     * Sends a verification response to the server.
     */
//    fun sendDocSignResponse(status: ResponseStatus) {
//        if (verificationRequest == null) return
//
//        val verificationResponse = VerificationResponse.Builder()
//            .setRequestId(verificationRequest!!.id())
//            .setTypes(verificationRequest!!.types())
//            .setToIdentifier(verificationRequest!!.toIdentifier())
//            .setFromIdentifier(verificationRequest!!.fromIdentifier())
//            .setStatus(status)
//            .build()
//        viewModelScope.launch(Dispatchers.IO) {
//            account.send(verificationResponse) { messageId, _ ->
//                _appUiState.update { it.copy(requestState = ServerRequestState.ResponseSent(status)) }
//            }
//        }
//    }

    /**
     * Backs up the account data.
     * @return A [ByteArray] containing the backup data.
     */
//    suspend fun backup(): ByteArray {
//        _appUiState.update { it.copy(backupRestoreState = BackupRestoreState.Processing) }
//        val backupBytes = account.backup()
//        _appUiState.update { it.copy(backupRestoreState = BackupRestoreState.Success) }
//        return backupBytes
//    }


    /**
     * Restores the account from a backup. This function attempts to restore the user's account using the provided backup data and selfie image.
     * @param backupBytes A ByteArray containing the account backup data.
     * @param selfieBytes A ByteArray containing the user's selfie image for verification.
     */
//    suspend fun restore(backupBytes: ByteArray, selfieBytes: ByteArray) {
//        try {
//            _appUiState.update { it.copy(backupRestoreState = BackupRestoreState.Processing) }
//            account.restore(backupBytes, selfieBytes)
//            _appUiState.update { it.copy(backupRestoreState = BackupRestoreState.Success) }
//        } catch (ex: Exception) {
//            _appUiState.update { it.copy(backupRestoreState = BackupRestoreState.Error(ex.message ?: "restore failed")) }
//        }
//    }
}