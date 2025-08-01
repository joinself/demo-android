package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState
import com.joinself.app.demo.ui.theme.*
import com.joinself.sdk.models.ResponseStatus


@Composable
fun AuthRequestResultScreen(
    requestState: ServerRequestState,
    onContinue: () -> Unit,
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
                // Hero Section - Success or Failure
                if (requestState is ServerRequestState.ResponseSent) {
                    HeroSection(
                        icon = Icons.Filled.CheckCircle,
                        title = "Authentication Successful",
                        subtitle = "Your identity has been verified successfully. Your biometric credentials were validated by the server."
                    )
                } else {
                    HeroSection(
                        icon = Icons.Filled.Error,
                        title = "Authentication Failed",
                        subtitle = "Unable to verify your identity. The authentication process was not completed successfully."
                    )
                }
            }

            item {
                // Result details
                if (requestState is ServerRequestState.ResponseSent) {
                    InfoCard(
                        icon = Icons.Filled.Verified,
                        title = "Identity Verified",
                        message = "Your liveness check was completed successfully and your credentials have been validated by the server. You can now continue with other actions.",
                        type = AlertType.Success
                    )
                } else {
                    AlertCard(
                        title = "Verification Failed",
                        message = "The liveness check could not be completed or the credentials were not validated. This could be due to poor lighting, camera issues, or network problems.",
                        type = AlertType.Error
                    )
                }
            }

//            if (requestState is ServerRequestState.ResponseSent) {
//                item {
//                    // What happened during authentication
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "Authentication Details",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        ProcessStep(
//                            number = 1,
//                            title = "Liveness Check Completed",
//                            description = "Biometric verification was successful"
//                        )
//
//                        ProcessStep(
//                            number = 2,
//                            title = "Credentials Generated",
//                            description = "Secure credentials were created from your biometric data"
//                        )
//
//                        ProcessStep(
//                            number = 3,
//                            title = "Server Validation",
//                            description = "Your credentials were verified by the server"
//                        )
//                    }
//                }
//
//                item {
//                    // Security confirmation
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "Security Confirmation",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Data Privacy Maintained",
//                            description = "Your biometric data remained on your device throughout the process"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.CheckCircle,
//                            title = "Secure Transmission",
//                            description = "All credentials were transmitted using end-to-end encryption"
//                        )
//                    }
//                }
//            } else {
//                item {
//                    // Troubleshooting for failed authentication
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "Troubleshooting",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        ProcessStep(
//                            number = 1,
//                            title = "Check Lighting",
//                            description = "Ensure you're in a well-lit area for the camera"
//                        )
//
//                        ProcessStep(
//                            number = 2,
//                            title = "Camera Position",
//                            description = "Hold your device at eye level and look directly at the camera"
//                        )
//
//                        ProcessStep(
//                            number = 3,
//                            title = "Network Connection",
//                            description = "Verify you have a stable internet connection"
//                        )
//                    }
//                }
//            }
        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
        ) {
            PrimaryButton(
                title = "Continue",
                onClick = onContinue
            )
        }
    }
}

@Preview(showBackground = true, name = "Auth Request Result - Success")
@Composable
fun AuthRequestResultScreenSuccessPreview() {
    AuthRequestResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Auth Request Result - Failure")
@Composable
fun AuthRequestResultScreenFailurePreview() {
    AuthRequestResultScreen(
        requestState = ServerRequestState.RequestError("Authentication failed"),
        onContinue = {}
    )
}