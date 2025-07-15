package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState
import com.joinself.common.CredentialType
import com.joinself.sdk.models.ResponseStatus

@Composable
fun ShareCredentialResultScreen(
    requestState: ServerRequestState,
    credentialType: String, // "email" or "document"
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSuccess = requestState is ServerRequestState.ResponseSent
    val isStatusAccepted = requestState is ServerRequestState.ResponseSent && requestState.status == ResponseStatus.accepted
    val statusName = if (isStatusAccepted) "Shared" else "Rejected"
    val (successTitle, successMessage, failureTitle, failureMessage) = when (credentialType) {
        CredentialType.Email -> Tuple4(
            "Email Credential $statusName!",
            if (isStatusAccepted) "Your verified email credential has been successfully shared with the server. The server now has cryptographic proof of your email verification."
            else "Your verified email credential has been successfully rejected with the server. The server now does not have cryptographic proof of your email verification.",
            "Email Sharing Failed",
            "We were unable to share your email credential with the server. This could be due to network issues or if you don't have a verified email credential."
        )
        CredentialType.Document -> Tuple4(
            "Document Credential $statusName!",
            if (isStatusAccepted) "Your verified identity document credential has been successfully shared with the server. The server now has cryptographic proof of your document verification."
            else "Your verified identity document credential has been successfully rejected with the server. The server now does not have cryptographic proof of your document verification.",
            "Document Sharing Failed", 
            "We were unable to share your document credential with the server. This could be due to network issues or if you don't have a verified document credential."
        )
        else -> Tuple4(
            "Credential $statusName!",
            if (isStatusAccepted) "Your verified credential has been successfully shared with the server."
            else "Your verified credential has been successfully rejected with the server.",
            "Credential Sharing Failed",
            "We were unable to share your credential with the server."
        )
    }

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
                // Hero Section with Success/Error State
                HeroSection(
                    icon = if (isSuccess && isStatusAccepted) Icons.Filled.CheckCircle else Icons.Filled.Error,
                    title = if (isSuccess) successTitle else failureTitle,
                    subtitle = if (isSuccess) successMessage else failureMessage
                )
            }

            if (isSuccess) {
                item {
                    // Success information
                    InfoCard(
                        icon = Icons.Filled.Verified,
                        title = "Credential $statusName Successfully",
                        message = "Your ${credentialType} credential has been securely ${statusName.lowercase()} with the server.",
                        type = AlertType.Success
                    )
                }

//                item {
//                    // What was shared
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "What Was Shared",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Verification Proof",
//                            description = "Cryptographic proof that you have a verified ${credentialType}"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Share,
//                            title = "Privacy Protected",
//                            description = "Zero-knowledge proof - no personal data was exposed"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.CheckCircle,
//                            title = "Server Confirmation",
//                            description = "The server has confirmed receipt of your credential proof"
//                        )
//                    }
//                }
            } else {
                item {
                    // Error information
                    InfoCard(
                        icon = Icons.Filled.Error,
                        title = "Sharing Failed",
                        message = failureMessage,
                        type = AlertType.Error
                    )
                }

//                item {
//                    // Troubleshooting tips
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "Troubleshooting Tips",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.CheckCircle,
//                            title = "Check Credentials",
//                            description = "Ensure you have verified ${credentialType} credentials on your device"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Share,
//                            title = "Network Connection",
//                            description = "Verify you have a stable internet connection"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Server Connection",
//                            description = "Make sure you're still connected to the server"
//                        )
//                    }
//                }
            }

//            item {
//                // Status summary
//                StatusCard(
//                    title = "Credential Sharing",
//                    status = if (isSuccess) "Completed Successfully" else "Failed",
//                    statusColor = if (isSuccess) AppColors.success else AppColors.error,
//                    subtitle = if (isSuccess)
//                        "Your ${credentialType} credential proof has been delivered"
//                    else
//                        "You can try sharing your credentials again",
//                    icon = if (isSuccess) Icons.Filled.CheckCircle else Icons.Filled.Error
//                )
//            }
        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
        ) {
            PrimaryButton(
                title = if (isSuccess) "Continue" else "Continue",
                onClick = onContinue
            )
        }
    }
}

// Helper data class for multiple return values
private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview(showBackground = true, name = "Share Email Credential Result - Accepted")
@Composable
fun ShareEmailCredentialResultAcceptedPreview() {
    ShareCredentialResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        credentialType = CredentialType.Email,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Share Email Credential Result - Rejected")
@Composable
fun ShareEmailCredentialResultRejectedPreview() {
    ShareCredentialResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.rejected),
        credentialType = CredentialType.Email,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Share Document Credential Result - Accepted")
@Composable
fun ShareDocumentCredentialResultAcceptedPreview() {
    ShareCredentialResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        credentialType = CredentialType.Document,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Share Credential Result - Failed")
@Composable
fun ShareCredentialResultFailedPreview() {
    ShareCredentialResultScreen(
        requestState = ServerRequestState.RequestError("Sharing failed"),
        credentialType = CredentialType.Email,
        onContinue = {}
    )
}