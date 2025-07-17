package com.joinself.app.demo

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joinself.app.demo.ui.screens.AuthRequestResultScreen
import com.joinself.app.demo.ui.screens.AuthRequestStartScreen
import com.joinself.app.demo.ui.screens.BackupResultScreen
import com.joinself.app.demo.ui.screens.BackupStartScreen
import com.joinself.app.demo.ui.screens.DocSignResultScreen
import com.joinself.app.demo.ui.screens.DocSignStartScreen
import com.joinself.app.demo.ui.screens.GetCredentialResultScreen
import com.joinself.app.demo.ui.screens.GetCredentialStartScreen
import com.joinself.app.demo.ui.screens.InitializeSDKScreen
import com.joinself.app.demo.ui.screens.RegistrationIntroScreen
import com.joinself.app.demo.ui.screens.RestoreResultScreen
import com.joinself.app.demo.ui.screens.RestoreStartScreen
import com.joinself.app.demo.ui.screens.SelectActionScreen
import com.joinself.app.demo.ui.screens.ServerConnectResultScreen
import com.joinself.app.demo.ui.screens.ServerConnectSelectionScreen
import com.joinself.app.demo.ui.screens.ServerConnectStartScreen
import com.joinself.app.demo.ui.screens.ShareCredentialApprovalScreen
import com.joinself.app.demo.ui.screens.ShareCredentialResultScreen
import com.joinself.app.demo.ui.screens.ShareCredentialSelectionScreen
import com.joinself.app.demo.ui.screens.VerifyDocumentResultScreen
import com.joinself.app.demo.ui.screens.VerifyDocumentStartScreen
import com.joinself.app.demo.ui.screens.VerifyEmailResultScreen
import com.joinself.app.demo.ui.screens.VerifyEmailStartScreen
import com.joinself.app.demo.ui.screens.VerifySelectionScreen
import com.joinself.common.Constants
import com.joinself.common.CredentialType
import com.joinself.common.exception.InvalidCredentialException
import com.joinself.sdk.models.Account
import com.joinself.sdk.models.ResponseStatus
import com.joinself.sdk.ui.adQRCodeRoute
import com.joinself.sdk.ui.addDocumentVerificationRoute
import com.joinself.sdk.ui.addEmailRoute
import com.joinself.sdk.ui.addLivenessCheckRoute
import com.joinself.sdk.utils.popAllBackStacks
import com.joinself.ui.component.LoadingDialog
import com.joinself.ui.theme.SelfModifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlin.collections.isNotEmpty


private const val TAG = "SelfDemoApp"

sealed class MainRoute {
    @Serializable object Initializing
    @Serializable object Registration
    @Serializable object ConnectToServerSelection
    @Serializable object ConnectToServerAddress
    @Serializable object ConnectingToServer
    @Serializable object ServerConnectionReady
    @Serializable object AuthRequestStart
    @Serializable object AuthResultResult
    @Serializable object VerifySelection
    @Serializable object VerifyEmailStart
    @Serializable object VerifyEmailResult
    @Serializable object VerifyDocumentStart
    @Serializable object VerifyDocumentResult
    @Serializable object GetCustomCredentialStart
    @Serializable object GetCustomCredentialResult

    @Serializable object ShareCredentialSelection
    @Serializable object ShareCredentialApproval
    @Serializable object ShareCredentialResult
    @Serializable object DocumentSignStart
    @Serializable object DocumentSignResult

    @Serializable object BackupStart
    @Serializable object BackupResult
    @Serializable object RestoreStart
    @Serializable object RestoreResult

    companion object {
        val LivenessRoute = "livenessRoute"
        val EmailRoute = "emailRoute"
        val DocumentRoute = "documentRoute"
        val QRCodeRoute = "qrCodeRoute"
    }
}

@Composable
fun SelfDemoApp(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val selfModifier = SelfModifier.sdk()

    val viewModel: MainViewModel = viewModel {
        MainViewModel(context)
    }
    val appState by viewModel.appStateFlow.collectAsState()

    var credentialType by remember { mutableStateOf("") }

    // picker to save backup file to local storage
    var backupByteArray by remember { mutableStateOf(byteArrayOf()) }
    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(backupByteArray)
            }
        }
    }

    // picker to select backup file from local storage
    var isRestoreFlow by remember { mutableStateOf(false) }
    var selfieByteArray by remember { mutableStateOf(byteArrayOf()) }
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val backupBytes = context.contentResolver.openInputStream(it)?.use { input ->
                input.readBytes()
            }
            if (backupBytes != null && backupBytes.isNotEmpty() && selfieByteArray.isNotEmpty()) {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        viewModel.restore(backupBytes, selfieByteArray)
                    } catch (ex: Exception) {
                        Log.e("SelfSDK", "restore error", ex)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Restore failed: ${ex.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                    withContext(Dispatchers.Main) {
                        navController.navigate(MainRoute.RestoreResult)
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute.Initializing,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {

        composable<MainRoute.Initializing> {
            InitializeSDKScreen(
                initialization = appState.initialization,
                onRetry = {

                }
            )

            LaunchedEffect(appState.initialization) {
                when (val status = appState.initialization) {
                    is InitializationState.Success -> {
                        val route = if (viewModel.isRegistered()) MainRoute.ConnectToServerSelection else MainRoute.Registration
                        navController.navigate(route)
                    }
                    is InitializationState.Error -> {

                    }
                    else -> {}
                }
            }
        }

        composable<MainRoute.Registration> {
            RegistrationIntroScreen( selfModifier = selfModifier,
                onStartRegistration = {
                    navController.navigate(MainRoute.LivenessRoute)
                },
                onStartRestore = {
                    navController.navigate(MainRoute.RestoreStart)
                },
                onOpenSettings = onOpenSettings
            )
        }
        composable<MainRoute.ConnectToServerSelection> {
            ServerConnectSelectionScreen(
                onAddress = {
                    navController.navigate(MainRoute.ConnectToServerAddress)
                },
                onQRCode = {
                    navController.navigate(MainRoute.QRCodeRoute)
                }
            )
        }
        composable<MainRoute.ConnectToServerAddress> {
            ServerConnectStartScreen(
                onContinue = { address ->
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.connect(inboxAddress = address)
                    }
                    navController.navigate(MainRoute.ConnectingToServer)
                }
            )
        }
        composable<MainRoute.ConnectingToServer> {
            ServerConnectResultScreen(
                serverAddress = viewModel.serverInboxAddress,
                serverState = appState.serverState,
                onContinue = {
                    navController.popAllBackStacks()
                    navController.navigate(MainRoute.ServerConnectionReady)
                },
                onRetry = {
                    navController.popBackStack()
                },
                onTimeout = {

                }
            )
        }

        composable<MainRoute.ServerConnectionReady> {
            SelectActionScreen(
                onAuthenticate = {
                    navController.navigate(MainRoute.AuthRequestStart)
                },
                onVerifyCredentials = {
                    navController.navigate(MainRoute.VerifySelection)
                },
                onProvideCredentials = {
                    navController.navigate(MainRoute.ShareCredentialSelection)
                },
                onSignDocuments = {
                    navController.navigate(MainRoute.DocumentSignStart)
                },
                onBackup = {
                    navController.navigate(MainRoute.BackupStart)
                },
                onConnectToServer = {
                    navController.navigate(MainRoute.ConnectToServerSelection)
                }
            )
            LaunchedEffect(Unit) {
                delay(500)
                viewModel.resetState(ServerRequestState.None)
            }

            BackHandler {}
        }

        composable<MainRoute.AuthRequestStart> {
            AuthRequestStartScreen(
                requestState = appState.requestState,
                onStartAuthentication = {
                    navController.navigate(MainRoute.LivenessRoute)
                }
            )

            LaunchedEffect(appState.requestState) {
                Log.d(TAG, "auth request state: ${appState.requestState}")
                when (appState.requestState) {
                    is ServerRequestState.None -> {
                        withContext(Dispatchers.IO){
                            viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_CREDENTIAL_AUTH)
                        }
                    }
                    is ServerRequestState.ResponseSent -> {
                        withContext(Dispatchers.Main){
                            navController.navigate(MainRoute.AuthResultResult)
                        }
                    }
                    else -> {}
                }
            }
        }
        composable<MainRoute.AuthResultResult> {
            AuthRequestResultScreen(
                requestState = appState.requestState,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                }
            )
        }

        composable<MainRoute.VerifySelection> {
            VerifySelectionScreen(
                onVerifyIdentityDocument = {
                    navController.navigate(MainRoute.VerifyDocumentStart)
                },
                onVerifyEmail = {
                    navController.navigate(MainRoute.VerifyEmailStart)
                },
                onGetCredentials = {
                    navController.navigate(MainRoute.GetCustomCredentialStart)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<MainRoute.VerifyEmailStart> {
            VerifyEmailStartScreen(
                onStartVerification = {
                    navController.navigate(MainRoute.EmailRoute)
                }
            )
        }
        composable<MainRoute.VerifyEmailResult> {
            VerifyEmailResultScreen(
                isSuccess = true,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                }
            )
        }
        composable<MainRoute.VerifyDocumentStart> {
            VerifyDocumentStartScreen(
                onStartVerification = {
                    navController.navigate(MainRoute.DocumentRoute)
                }
            )
        }
        composable<MainRoute.VerifyDocumentResult> {
            VerifyDocumentResultScreen(
                isSuccess = true,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                }
            )
        }
        composable<MainRoute.GetCustomCredentialStart> {
            GetCredentialStartScreen(
                onStartGettingCredentials = {
                    coroutineScope.launch {
                        viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_GET_CUSTOM_CREDENTIAL)
                    }
                }
            )
            LaunchedEffect(appState.requestState) {
                Log.d(TAG, "custom credential state: ${appState.requestState}")
                when (appState.requestState) {
                    is ServerRequestState.RequestReceived -> {
                        withContext(Dispatchers.Main){
                            navController.navigate(MainRoute.GetCustomCredentialResult)
                        }
                    }
                    else -> {}
                }
            }
        }
        composable<MainRoute.GetCustomCredentialResult> {
            GetCredentialResultScreen(
                isSuccess = true,
                credentialName = "Custom Credentials",
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                    viewModel.storeCredentials()
                },
                onRetry = {
                    navController.popBackStack()
                },
            )
        }

        composable<MainRoute.ShareCredentialSelection> {
            ShareCredentialSelectionScreen(
                onProvideEmail = {
                    credentialType = CredentialType.Email
                    navController.navigate(MainRoute.ShareCredentialApproval)
                },
                onProvideDocument = {
                    credentialType = CredentialType.Document
                    navController.navigate(MainRoute.ShareCredentialApproval)
                },
                onProvideCustomCredential = {
                    credentialType = CredentialType.Custom
                    navController.navigate(MainRoute.ShareCredentialApproval)

                },
                onBack = {

                }
            )
        }
        composable<MainRoute.ShareCredentialApproval> {
            ShareCredentialApprovalScreen(
                credentialType = credentialType,
                requestState = appState.requestState,
                onApprove = {
                    viewModel.shareCredential(status = ResponseStatus.accepted)
                },
                onDeny = {
                    viewModel.shareCredential(status = ResponseStatus.rejected)
                }
            )
            LaunchedEffect(appState.requestState) {
                Log.d(TAG, "credential request state: ${appState.requestState}")
                when (appState.requestState) {
                    is ServerRequestState.None -> {
                        withContext(Dispatchers.IO) {
                            if (credentialType == CredentialType.Email) viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_CREDENTIAL_EMAIL)
                            else if (credentialType == CredentialType.Document) viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_CREDENTIAL_DOCUMENT)
                            else if (credentialType == CredentialType.Custom) viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_CREDENTIAL_CUSTOM)
                        }
                    }
                    is ServerRequestState.ResponseSent -> {
                        withContext(Dispatchers.Main) {
                            navController.navigate(MainRoute.ShareCredentialResult)
                        }
                    }
                    else -> {}
                }
            }
            DisposableEffect(true) {
                onDispose {
                    Log.d(TAG, "disposable request state: ${appState.requestState}")
                    if (appState.requestState !is ServerRequestState.ResponseSent) {
                        viewModel.resetState(ServerRequestState.None)
                    }
                }
            }
        }
        composable<MainRoute.ShareCredentialResult> {
            ShareCredentialResultScreen(
                requestState = appState.requestState,
                credentialType = credentialType,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                }
            )
        }
        composable<MainRoute.DocumentSignStart> {
            DocSignStartScreen(
                requestState = appState.requestState,
                onSign = {
                    viewModel.sendDocSignResponse(status = ResponseStatus.accepted)
                },
                onReject = {
                    viewModel.sendDocSignResponse(status = ResponseStatus.rejected)
                }
            )
            LaunchedEffect(appState.requestState) {
                Log.d(TAG, "docsign request state: ${appState.requestState}")
                when (appState.requestState) {
                    is ServerRequestState.None -> {
                        withContext(Dispatchers.IO){
                            viewModel.notifyServerForRequest(SERVER_REQUESTS.REQUEST_DOCUMENT_SIGNING)
                        }
                    }
                    is ServerRequestState.ResponseSent -> {
                        withContext(Dispatchers.Main){
                            navController.navigate(MainRoute.DocumentSignResult)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
        composable<MainRoute.DocumentSignResult> {
            DocSignResultScreen(
                requestState = appState.requestState,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                }
            )
        }

        composable<MainRoute.BackupStart> {
            BackupStartScreen(
                backupState = appState.backupRestoreState,
                onStartBackup = {
                    coroutineScope.launch(Dispatchers.IO) {
                        backupByteArray = viewModel.backup()
                        withContext(Dispatchers.Main) {
                            navController.navigate(MainRoute.BackupResult)
                        }
                    }
                }
            )
        }
        composable<MainRoute.BackupResult> {
            BackupResultScreen(
                backupState = appState.backupRestoreState,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                    saveLauncher.launch("self_sdk.backup")
                },
                onRetry = {}
            )
        }
        composable<MainRoute.RestoreStart> {
            RestoreStartScreen(
                restoreState = appState.backupRestoreState,
                onStartRestore = {
                    isRestoreFlow = true
                    navController.navigate(MainRoute.LivenessRoute)
                }
            )
            if (appState.backupRestoreState is BackupRestoreState.Processing) {
                LoadingDialog(selfModifier)
            }
        }
        composable<MainRoute.RestoreResult> {
            RestoreResultScreen(
                restoreState = appState.backupRestoreState,
                onContinue = {
                    isRestoreFlow = false
                    navController.popAllBackStacks()
                    navController.navigate(MainRoute.ConnectToServerSelection)
                },
                onRetry = {
                    navController.popBackStack()
                }
            )
        }

        // add liveness check to main navigation
        addLivenessCheckRoute(navController, route = MainRoute.LivenessRoute, selfModifier = selfModifier,
            account = { viewModel.account },
            withCredential = true,
            onFinish = { selfie, credentials ->
                if (isRestoreFlow) {
                    selfieByteArray = selfie
                    pickerLauncher.launch("application/octet-stream")
                } else if (!viewModel.isRegistered()) {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            if (selfie.isNotEmpty() && credentials.isNotEmpty()) {
                                val success = viewModel.register(selfie = selfie, credentials = credentials)
                                if (success) {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        navController.popBackStack()
                                        navController.navigate(MainRoute.ConnectToServerSelection)
                                    }
                                }
                            }
                        } catch (_: InvalidCredentialException) { }
                    }
                } else {
                    viewModel.sendCredentialResponse(credentials, status = ResponseStatus.accepted)
                }
                // nav back to main
                coroutineScope.launch(Dispatchers.Main) {
                    navController.popBackStack(MainRoute.LivenessRoute, true)
                }
            }
        )

        // add email flow into main navigation
        addEmailRoute(navController, route = MainRoute.EmailRoute, selfModifier = selfModifier,
            account = { viewModel.account },
            onFinish = { isSuccess, error ->
                if (isSuccess) {
                    coroutineScope.launch(Dispatchers.Main) {
                        navController.navigate(MainRoute.VerifyEmailResult)
                    }
                }
            }
        )

        // integrate passport, idcard verification flow
        addDocumentVerificationRoute(navController, route = MainRoute.DocumentRoute, selfModifier = selfModifier,account = { viewModel.account },
            isDevMode = { false }, // true for testing only
            onFinish = { isSuccess, error ->
                if (isSuccess) {
                    coroutineScope.launch(Dispatchers.Main) {
                        navController.navigate(MainRoute.VerifyDocumentResult)
                    }
                }
            }
        )

        // integrate qr code flow
        adQRCodeRoute(navController, MainRoute.QRCodeRoute, selfModifier = selfModifier,
            onFinish = { qrCodeBytes, _ ->
                coroutineScope.launch(Dispatchers.IO) {
                    // parse qrcode first and check the correct environment
                    val discoveryData = Account.qrCode(qrCodeBytes)
                    if (discoveryData == null || !discoveryData.sandbox) {
                        return@launch
                    }

                    // then connect with the connection in the qrcode
                    viewModel.connect(inboxAddress = discoveryData.address, qrCode = qrCodeBytes)

                    coroutineScope.launch(Dispatchers.Main) {
                        navController.popBackStack(MainRoute.QRCodeRoute, true)
                        if (appState.serverState is ServerState.Success) {
                            navController.navigate(MainRoute.ServerConnectionReady)
                        }
                    }
                }
            },
            onExit = {
                coroutineScope.launch(Dispatchers.Main) {
                    navController.popBackStack(MainRoute.QRCodeRoute, true)
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Version: ${BuildConfig.VERSION_NAME}")
    }
    LaunchedEffect(appState.requestState) {
        Log.d(TAG, "credential request state: ${appState.requestState}")
        when (appState.requestState) {
            is ServerRequestState.RequestReceived -> {
                val subjects = (appState.requestState as ServerRequestState.RequestReceived).subjects
                val types = (appState.requestState as ServerRequestState.RequestReceived).types
                if (navController.currentDestination?.route?.contains(MainRoute.ServerConnectionReady::class.simpleName.toString()) == true) {
                    if (types.contains(CredentialType.Liveness)) {
                        navController.navigate(MainRoute.AuthRequestStart)
                    } else if (types.contains(CredentialType.Passport)) {
                        credentialType = CredentialType.Document
                        navController.navigate(MainRoute.ShareCredentialApproval)
                    }
                }
            }
            else -> {}
        }
    }
}