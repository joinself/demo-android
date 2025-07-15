package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.InitializationState

@Composable
fun InitializeSDKScreen(
    initialization: InitializationState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                if (initialization is InitializationState.Loading) {
                    HeroSection(
                        icon = Icons.Filled.CloudSync,
                        title = "Initializing Self SDK",
                        subtitle = "Setting up your Self environment. This may take a few moments on first launch."
                    )
                } else if (initialization is InitializationState.Error) {
                    HeroSection(
                        icon = Icons.Filled.Error,
                        title = "Initialization Failed",
                        subtitle = "We encountered an issue setting up the Self SDK. Please check your connection and try again."
                    )
                }
            }

            if (initialization is InitializationState.Loading) {
                // Loading content
                item {
                    // Progress steps
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                    ) {
                        androidx.compose.material3.Text(
                            text = "Initialization Steps",
                            style = AppFonts.heading,
                            color = AppColors.textPrimary
                        )

                        ProcessStep(
                            number = 1,
                            title = "Loading SDK Components",
                            description = "Initializing cryptographic libraries and security modules"
                        )

                        ProcessStep(
                            number = 2,
                            title = "Setting Up Storage",
                            description = "Creating secure storage for your account credentials"
                        )

                        ProcessStep(
                            number = 3,
                            title = "Establishing Environment",
                            description = "Connecting to Self network and configuring environment"
                        )
                    }
                }

                item {
                    // Information about Self
                    InfoCard(
                        icon = Icons.Filled.CloudSync,
                        title = "Self SDK",
                        message = "Self provides secure, decentralized identity verification.",
                        type = AlertType.Info
                    )
                }
            } else if (initialization is InitializationState.Error) {
                // Error content
                item {
                    AlertCard(
                        title = "Initialization Error",
                        message = initialization.message,
                        type = AlertType.Error
                    )
                }

                item {
                    // Troubleshooting steps
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                    ) {
                        androidx.compose.material3.Text(
                            text = "Troubleshooting",
                            style = AppFonts.heading,
                            color = AppColors.textPrimary
                        )

                        ProcessStep(
                            number = 1,
                            title = "Check Connection",
                            description = "Ensure you have a stable internet connection"
                        )

                        ProcessStep(
                            number = 2,
                            title = "Restart App",
                            description = "Close and reopen the application to retry initialization"
                        )

                        ProcessStep(
                            number = 3,
                            title = "Check Storage",
                            description = "Ensure the app has sufficient storage space available"
                        )
                    }
                }
            }
        }

        // Fixed Retry Button at Bottom (only show if error)
        if (initialization is InitializationState.Error && initialization.message.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(AppSpacing.screenPadding)
            ) {
                PrimaryButton(
                    title = "Retry",
                    onClick = onRetry
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Initialize SDK Screen - Loading")
@Composable
fun InitializeSDKScreenLoadingPreview() {
    InitializeSDKScreen(
        initialization = InitializationState.Loading,
        onRetry = {}
    )
}

@Preview(showBackground = true, name = "Initialize SDK Screen - Error")
@Composable
fun InitializeSDKScreenErrorPreview() {
    InitializeSDKScreen(
        initialization = InitializationState.Error("Sample error message"),
        onRetry = {}
    )
}