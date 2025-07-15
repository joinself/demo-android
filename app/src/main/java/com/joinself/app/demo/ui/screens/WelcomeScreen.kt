package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    isSDKInitialized: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onGetStarted: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isRegistered: Boolean = false
) {
    var timeRemaining by remember { mutableStateOf(10) }
    var hasTimedOut by remember { mutableStateOf(false) }

    // Timer for SDK initialization timeout
    LaunchedEffect(isLoading, isSDKInitialized, errorMessage) {
        if (isLoading && !isSDKInitialized && errorMessage == null) {
            hasTimedOut = false
            timeRemaining = 10
            
            while (timeRemaining > 0 && isLoading && !isSDKInitialized && errorMessage == null) {
                delay(1000)
                timeRemaining--
            }
            
            if (timeRemaining <= 0) {
                hasTimedOut = true
            }
        }
    }

    val primaryButtonTitle = when {
        isRegistered -> "Account Dashboard"
        isSDKInitialized -> "Get Started"
        errorMessage != null -> "Retry"
        hasTimedOut && timeRemaining <= 0 -> "Retry"
        isLoading -> "Retry (${timeRemaining}s)"
        else -> "Retry"
    }

    val primaryButtonDisabled = when {
        isRegistered -> false
        isSDKInitialized -> false
        errorMessage != null -> false
        hasTimedOut -> false
        else -> true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Ensure white background
    ) {
        // DEBUG: Screen Name Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.primary.copy(alpha = 0.1f))
                .padding(8.dp)
        ) {
            androidx.compose.material3.Text(
                text = "DEBUG: WELCOME",
                style = AppFonts.caption,
                color = AppColors.primary,
                modifier = Modifier.padding(4.dp)
            )
        }
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section - different content for registered users
                if (isRegistered) {
                    HeroSection(
                        icon = Icons.Filled.AccountCircle,
                        title = "Welcome Back!",
                        subtitle = "Your Self account is active and ready for secure identity verification and authentication."
                    )
                } else {
                    HeroSection(
                        icon = Icons.Filled.AccountCircle,
                        title = "Welcome to Self Demo",
                        subtitle = "Experience secure, decentralized identity verification using cutting-edge biometric technology"
                    )
                }
            }

            if (isRegistered) {
                // Content for registered users
                item {
                    // Account Status Card
                    InfoCard(
                        icon = Icons.Filled.AccountCircle,
                        title = "Account Status",
                        message = "Your Self account is successfully registered and connected to the network.",
                        type = AlertType.Success
                    )
                }

                item {
                    // Available Features for registered users
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                    ) {
                        FeatureRow(
                            icon = Icons.Filled.Security,
                            title = "Authentication Ready",
                            description = "Use your account for secure authentication and verification"
                        )

                        FeatureRow(
                            icon = Icons.Filled.Face,
                            title = "Biometric Verification",
                            description = "Your biometric credentials are securely stored and ready to use"
                        )

                        FeatureRow(
                            icon = Icons.Filled.NetworkCheck,
                            title = "Network Connected",
                            description = "Connected to the Self network for secure communications"
                        )
                    }
                }
            } else {
                // Content for new users (original content)
                item {
                    // Feature List
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                    ) {
                        FeatureRow(
                            icon = Icons.Filled.Face,
                            title = "Biometric Authentication",
                            description = "Advanced liveness detection for secure identity verification"
                        )

                        FeatureRow(
                            icon = Icons.Filled.Security,
                            title = "Decentralized Security",
                            description = "Your identity data stays under your control"
                        )

                        FeatureRow(
                            icon = Icons.Filled.NetworkCheck,
                            title = "Secure Communication",
                            description = "Encrypted peer-to-peer connections with verified servers"
                        )
                    }
                }

                item {
                    // Information Card
                    InfoCard(
                        icon = Icons.Filled.Info,
                        title = "Demo Purpose",
                        message = "This app demonstrates Self SDK integration patterns for developers.",
                        type = AlertType.Info
                    )
                }
            }
        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
        ) {
            PrimaryButton(
                title = primaryButtonTitle,
                onClick = {
                    if (isRegistered) {
                        // For registered users, could navigate to dashboard or authentication demo
                        android.util.Log.d("SelfDemo", "Navigate to account dashboard")
                        // TODO: Navigate to account dashboard or authentication features
                    } else if (isSDKInitialized) {
                        onGetStarted()
                    } else {
                        onRetry()
                        timeRemaining = 10
                        hasTimedOut = false
                    }
                },
                isLoading = isLoading && !hasTimedOut,
                isDisabled = primaryButtonDisabled
            )
        }
    }
} 