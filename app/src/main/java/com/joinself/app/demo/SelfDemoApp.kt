package com.joinself.app.demo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.joinself.common.CredentialType
import com.joinself.sdk.SelfSDK
import com.joinself.sdk.models.CredentialMessage
import com.joinself.sdk.models.CredentialRequest
import com.joinself.sdk.models.VerificationRequest
import com.joinself.sdk.ui.DisplayRequestUI
import com.joinself.sdk.ui.integrateUIFlows
import com.joinself.sdk.ui.openBackupFlow
import com.joinself.sdk.ui.openDocumentVerificationFlow
import com.joinself.sdk.ui.openEmailVerificationFlow
import com.joinself.sdk.ui.openQRCodeFlow
import com.joinself.sdk.ui.openRegistrationFlow
import com.joinself.sdk.ui.openRestoreFlow
import com.joinself.sdk.utils.popAllBackStacks
import com.joinself.ui.component.LoadingDialog
import com.joinself.ui.theme.SelfModifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable


private const val TAG = "SelfSDKDemoApp"

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

    NavHost(
        navController = navController,
        startDestination = MainRoute.Initializing,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        SelfSDK.integrateUIFlows(this, navController, selfModifier = selfModifier)
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
                    viewModel.account.openRegistrationFlow { isSuccess, error ->
                        coroutineScope.launch(Dispatchers.Main) {
                            if (isSuccess) navController.navigate(MainRoute.ConnectToServerSelection)
                        }
                    }
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
                    viewModel.account.openQRCodeFlow(
                        onFinish = { qrCode, discoveryData ->
                            if (discoveryData == null || !discoveryData.sandbox) {
                                return@openQRCodeFlow
                            }

                            coroutineScope.launch(Dispatchers.IO) {
                                // then connect with the connection in the qrcode
                                viewModel.connect(inboxAddress = discoveryData.address, qrCode = qrCode)

                                withContext(Dispatchers.Main) {
                                    if (appState.serverState is ServerState.Success) {
                                        navController.navigate(MainRoute.ServerConnectionReady)
                                    }
                                }
                            }
                        },
                        onExit = {
                            navController.popBackStack()
                        }
                    )
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

                }
            )
            if (appState.requestState is ServerRequestState.RequestReceived) {
                val request = (appState.requestState as ServerRequestState.RequestReceived).request
                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        usePlatformDefaultWidth = false
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(request) {
                            is CredentialRequest -> {
                                viewModel.account.DisplayRequestUI(selfModifier, request, onFinish = { isSent, status ->
                                    viewModel.resetState(requestState = if (isSent) ServerRequestState.ResponseSent(status) else ServerRequestState.RequestError("failed to respond"))
                                })
                            }
                        }
                    }
                }
            }
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
                    viewModel.account.openEmailVerificationFlow(onFinish = { isSuccess, error ->
                        navController.navigate(MainRoute.VerifyEmailResult)
                    })
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
                    viewModel.account.openDocumentVerificationFlow(isDevMode = false,
                        onFinish = { isSuccess, error ->
                            navController.navigate(MainRoute.VerifyDocumentResult)
                        }
                    )
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
            if (appState.requestState is ServerRequestState.RequestReceived) {
                val request = (appState.requestState as ServerRequestState.RequestReceived).request
                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(request) {
                            is CredentialMessage -> {
                                viewModel.account.DisplayRequestUI(selfModifier, request, onFinish = { isSent, status ->
                                    viewModel.resetState(requestState = if (isSent) ServerRequestState.ResponseSent(status) else ServerRequestState.RequestError("failed to respond"))
                                })
                            }
                        }
                    }
                }
            }
            LaunchedEffect(appState.requestState) {
                Log.d(TAG, "custom credential state: ${appState.requestState}")
                when (appState.requestState) {
                    is ServerRequestState.ResponseSent -> {
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
            LaunchedEffect(Unit) {
                delay(500)
                viewModel.resetState(ServerRequestState.None)
            }
        }
        composable<MainRoute.ShareCredentialApproval> {
            ShareCredentialApprovalScreen(
                credentialType = credentialType,
                requestState = appState.requestState,
                onApprove = {

                },
                onDeny = {

                }
            )
            if (appState.requestState is ServerRequestState.RequestReceived) {
                val request = (appState.requestState as ServerRequestState.RequestReceived).request
                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        usePlatformDefaultWidth = false
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(request) {
                            is CredentialRequest -> {
                                viewModel.account.DisplayRequestUI(selfModifier, request, onFinish = { isSent, status ->
                                    viewModel.resetState(requestState = if (isSent) ServerRequestState.ResponseSent(status) else ServerRequestState.RequestError("failed to respond"))
                                })
                            }
                        }
                    }
                }
            }
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

                },
                onReject = {

                }
            )
            if (appState.requestState is ServerRequestState.RequestReceived) {
                val request = (appState.requestState as ServerRequestState.RequestReceived).request
                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(request) {
                            is VerificationRequest -> {
                                viewModel.account.DisplayRequestUI(selfModifier, request, onFinish = { isSent, status ->
                                    viewModel.resetState(requestState = if (isSent) ServerRequestState.ResponseSent(status) else ServerRequestState.RequestError("failed to respond"))
                                })
                            }
                        }
                    }
                }
            }
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
                    coroutineScope.launch(Dispatchers.Main) {
                        viewModel.account.openBackupFlow(onFinish = { isSuccess, error ->
                            if (isSuccess) {
                                viewModel.setBackupRestoreState(state = BackupRestoreState.Success)
                            } else {
                                viewModel.setBackupRestoreState(state = BackupRestoreState.Error("failed to backup"))
                            }
                            coroutineScope.launch(Dispatchers.Main) {
                                navController.navigate(MainRoute.BackupResult)
                            }
                        })
                    }
                }
            )
        }
        composable<MainRoute.BackupResult> {
            BackupResultScreen(
                backupState = appState.backupRestoreState,
                onContinue = {
                    navController.popBackStack(MainRoute.ServerConnectionReady, inclusive = false)
                },
                onRetry = {}
            )
        }
        composable<MainRoute.RestoreStart> {
            RestoreStartScreen(
                restoreState = appState.backupRestoreState,
                onStartRestore = {
                    coroutineScope.launch(Dispatchers.Main) {
                        viewModel.account.openRestoreFlow(onFinish = { isSuccess, error ->
                            if (isSuccess) {
                                viewModel.setBackupRestoreState(state = BackupRestoreState.Success)
                            } else {
                                viewModel.setBackupRestoreState(state = BackupRestoreState.Error("failed to restore"))
                            }
                            coroutineScope.launch(Dispatchers.Main) {
                                navController.navigate(MainRoute.RestoreResult)
                            }
                        })
                    }
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
                    navController.popAllBackStacks()
                    navController.navigate(MainRoute.ConnectToServerSelection)
                },
                onRetry = {
                    navController.popBackStack()
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Version: ${BuildConfig.VERSION_NAME}")
    }
    LaunchedEffect(appState.requestState) {
        Log.d(TAG, "credential request state: ${appState.requestState}")
        when (appState.requestState) {
            is ServerRequestState.RequestReceived -> {
                if (navController.currentDestination?.route?.contains(MainRoute.ServerConnectionReady::class.simpleName.toString()) == true) {
                    val request = (appState.requestState as ServerRequestState.RequestReceived).request
                    when (request) {
                        is CredentialRequest -> {
                            navController.navigate(MainRoute.ShareCredentialApproval)
                        }
                        is VerificationRequest -> {
                            navController.navigate(MainRoute.DocumentSignStart)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}