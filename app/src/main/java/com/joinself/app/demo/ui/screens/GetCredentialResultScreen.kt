package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState
import com.joinself.app.demo.ui.theme.AlertCard
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton
import com.joinself.sdk.models.ResponseStatus


@Composable
fun GetCredentialResultScreen(
    requestState: ServerRequestState,
    credentialName: String = "Custom Credential",
    onContinue: () -> Unit, // Navigate to next screen (e.g., wallet, home)
    onRetry: (() -> Unit)? = null,
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
                if (requestState is ServerRequestState.ResponseSent) {
                    if (requestState.status == ResponseStatus.accepted) {
                        HeroSection(
                            icon = Icons.Filled.AssignmentTurnedIn,
                            title = "Get $credentialName Success",
                            subtitle = "Your $credentialName has been delivered and stored on your device."
                        )
                    } else {
                        HeroSection(
                            icon = Icons.Filled.Error,
                            title = "Get $credentialName Rejected",
                            subtitle = "Your $credentialName has not been stored. Please try again."
                        )
                    }
                } else {
                    HeroSection(
                        icon = Icons.Filled.Error,
                        title = "Get $credentialName Failure",
                        subtitle = "Your $credentialName has not been delivered. Please try again."
                    )
                }
            }

            item {
                if (requestState is ServerRequestState.ResponseSent) {
                    if (requestState.status == ResponseStatus.accepted) {
                        InfoCard(
                            icon = Icons.Filled.VerifiedUser, // Or specific success icon
                            title = "Credential Delivered",
                            message = "Your $credentialName has been generated and signed by the server, and securely stored on your device. You can now use this credential to prove your information about yourself.",
                            type = AlertType.Success
                        )
                    } else {
                        InfoCard(
                            icon = Icons.Filled.VerifiedUser, // Or specific success icon
                            title = "Credential Not Stored",
                            message = "We couldn't store your $credentialName at this time. Please try again.",
                            type = AlertType.Error
                        )
                    }
                } else {
                    AlertCard( // Assuming AlertCard for errors
                        title = "Credential Not Delivered",
                        message = "We couldn't generate and sign your $credentialName at this time. Please check your connection or try again.",
                        type = AlertType.Error
                    )
                }
            }
        }

        // Fixed Buttons at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
        ) {
            PrimaryButton(
                title = "Continue", // Or "View Wallet", "Close"
                onClick = onContinue
            )
//            if (!isSuccess && onRetry != null) {
//                SecondaryButton( // Assuming SecondaryButton or equivalent
//                    title = "Retry Getting $credentialName",
//                    onClick = onRetry
//                )
//            }
        }
    }
}

@Preview(showBackground = true, name = "Get Credential Result Success")
@Composable
fun GetCredentialResultScreenSuccessPreview() {
    GetCredentialResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        credentialName = "Custom Credential",
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Get Credential Result Failure")
@Composable
fun GetCredentialResultScreenFailurePreview() {
    GetCredentialResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.rejected),
        credentialName = "Proof of Age",
        onContinue = {},
        onRetry = {}
    )
}

@Preview(showBackground = true, name = "Get Credential Result Failure No Retry")
@Composable
fun GetCredentialResultScreenFailureNoRetryPreview() {
    GetCredentialResultScreen(
        requestState = ServerRequestState.RequestError("error"),
        credentialName = "Access Pass",
        onContinue = {}
        // onRetry is null
    )
}