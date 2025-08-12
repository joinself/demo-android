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
                    if (requestState.status == ResponseStatus.accepted) {
                        HeroSection(
                            icon = Icons.Filled.CheckCircle,
                            title = "Authentication Successful",
                            subtitle = "Your identity has been verified successfully. Your biometric credentials were validated by the server."
                        )
                    } else {
                        HeroSection(
                            icon = Icons.Filled.Error,
                            title = "Authentication Rejected",
                            subtitle = "Your identity has been rejected successfully. Your biometric credentials were not verified by the server."
                        )
                    }
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
                    if (requestState.status == ResponseStatus.accepted) {
                        InfoCard(
                            icon = Icons.Filled.Verified,
                            title = "Identity Verified",
                            message = "Your liveness check was completed successfully and your credentials have been validated by the server.\nYou can now continue with other actions.",
                            type = AlertType.Success
                        )
                    } else {
                        InfoCard(
                            icon = Icons.Filled.Verified,
                            title = "Identity Rejected",
                            message = "Your liveness check was rejected successfully and your credentials have not been validated by the server.\nYou can now continue with other actions.",
                            type = AlertType.Success
                        )
                    }
                } else {
                    AlertCard(
                        title = "Verification Failed",
                        message = "The liveness check could not be completed or the credentials were not validated. This could be due to poor lighting, camera issues, or network problems.",
                        type = AlertType.Error
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
                title = "Continue",
                onClick = onContinue
            )
        }
    }
}

@Preview(showBackground = true, name = "Success")
@Composable
fun AuthRequestResultScreenSuccessPreview() {
    AuthRequestResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Rejected")
@Composable
fun AuthRequestResultScreenRejectedPreview() {
    AuthRequestResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.rejected),
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Failure")
@Composable
fun AuthRequestResultScreenFailurePreview() {
    AuthRequestResultScreen(
        requestState = ServerRequestState.RequestError("Authentication failed"),
        onContinue = {}
    )
}