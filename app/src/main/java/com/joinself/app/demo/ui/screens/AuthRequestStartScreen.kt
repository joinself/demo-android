package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState


@Composable
fun AuthRequestStartScreen(
    requestState: ServerRequestState,
    onStartAuthentication: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heroTitle = if (requestState is ServerRequestState.RequestSent) "Waiting for a request from server..."
                    else if (requestState is ServerRequestState.RequestReceived) "The server has requested you to authenticate using your biometric credentials. Complete the liveness check to verify your identity."
                    else if (requestState is ServerRequestState.RequestError) "The request timed out. Please go back, check the server and try again."
                    else "Authentication Request"
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
                HeroSection(
                    icon = Icons.Filled.Security,
                    title = "Authentication Request",
                    subtitle = heroTitle
                )
            }

            item {
                // Information about the process
                InfoCard(
                    icon = Icons.Filled.FaceRetouchingNatural,
                    title = "Liveness Check Required",
                    message = "You will authenticate to the server using your biometric credentials. Look directly at the camera and follow the on-screen instructions.",
                    type = AlertType.Info
                )
            }

//            item {
//                // Process steps
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "Authentication Process",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    ProcessStep(
//                        number = 1,
//                        title = "Camera Access",
//                        description = "Grant camera permission for biometric capture"
//                    )
//
//                    ProcessStep(
//                        number = 2,
//                        title = "Liveness Check",
//                        description = "Follow on-screen instructions for facial verification"
//                    )
//
//                    ProcessStep(
//                        number = 3,
//                        title = "Credential Generation",
//                        description = "Secure credentials will be generated and sent to the server"
//                    )
//                }
//            }

//            item {
//                // Security information
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "Security & Privacy",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Security,
//                        title = "Zero-Knowledge Verification",
//                        description = "Your biometric data never leaves your device"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.CheckCircle,
//                        title = "Encrypted Communication",
//                        description = "All data is encrypted end-to-end during transmission"
//                    )
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
                title = "Start",
                isDisabled = requestState != ServerRequestState.RequestReceived,
                onClick = onStartAuthentication
            )
        }
    }
}

@Preview(showBackground = true, name = "Auth Request Start - Waiting")
@Composable
fun AuthRequestStartScreenWaitingPreview() {
    AuthRequestStartScreen(
        requestState = ServerRequestState.RequestSent,
        onStartAuthentication = {}
    )
}

@Preview(showBackground = true, name = "Auth Request Start - Ready")
@Composable
fun AuthRequestStartScreenReadyPreview() {
    AuthRequestStartScreen(
        requestState = ServerRequestState.RequestReceived,
        onStartAuthentication = {}
    )
}

@Preview(showBackground = true, name = "Auth Request Start - Error")
@Composable
fun AuthRequestStartScreenErrorPreview() {
    AuthRequestStartScreen(
        requestState = ServerRequestState.RequestError("Sample error message"),
        onStartAuthentication = {}
    )
}