package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joinself.app.demo.ServerRequestState
import com.joinself.common.CredentialType

@Composable
fun ShareCredentialApprovalScreen(
    credentialType: String, // "email" or "document"
    requestState: ServerRequestState,
    onApprove: () -> Unit,
    onDeny: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subject = (requestState as? ServerRequestState.RequestReceived)?.subjects ?: listOf()
    val (title, description, icon) = when (credentialType) {
        CredentialType.Email -> Triple(
            "Share Email Credentials?",
            "The server is requesting your verified email address.",
            Icons.Filled.Security
        )
        CredentialType.Document -> Triple(
            "Share Document Credentials?",
            "The server is requesting your verified identity ${subject.joinToString(", ")}.",
            Icons.Filled.Security
        )
        else -> Triple(
            "Share Custom Credential?",
            "The server is requesting your custom credential.",
            Icons.Filled.Share
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
                // Hero Section
                HeroSection(
                    icon = icon,
                    title = title,
                    subtitle = description
                )
            }

            val infoTitle = if (requestState is ServerRequestState.RequestSent) "Waiting for a request from server..."
            else if (requestState is ServerRequestState.RequestReceived) "The server has requested access to your verified ${credentialType}."
            else if (requestState is ServerRequestState.RequestError) "The request timed out. Please go back, check the server and try again."
            else "Credential Request"
            item {
                // Request details
                InfoCard(
                    icon = Icons.Filled.Share,
                    title = "Server Request",
                    message = infoTitle,
                    type = AlertType.Info
                )
            }

//            item {
//                // What will be shared section
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "What Will Be Shared",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.CheckCircle,
//                        title = "Verification Status",
//                        description = "Proof that you have a verified ${credentialType} credential"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Security,
//                        title = "Cryptographic Proof",
//                        description = "Zero-knowledge proof of credential ownership"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Error,
//                        title = "Personal Data",
//                        description = "❌ Your actual ${if (credentialType == "email") "email address" else "ID number"} will NOT be shared"
//                    )
//                }
//            }

//            item {
//                // Privacy note
//                InfoCard(
//                    icon = Icons.Filled.Security,
//                    title = "Privacy Protected",
//                    message = "This credential sharing uses advanced cryptography to prove you have verified information without revealing the actual data. The server only learns that you have a valid ${credentialType} credential.",
//                    type = AlertType.Success
//                )
//            }
        }

        // Fixed action buttons at bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
        ) {
            PrimaryButton(
                title = "Approve",
                isDisabled = requestState !is ServerRequestState.RequestReceived,
                onClick = onApprove
            )
            
            SecondaryButton(
                title = "Reject",
                isDisabled = requestState !is ServerRequestState.RequestReceived,
                onClick = onDeny
            )
        }
    }
}

@Preview(showBackground = true, name = "Share Email Credential Approval - Ready")
@Composable
fun ShareEmailCredentialApprovalScreenReadyPreview() {
    ShareCredentialApprovalScreen(
        credentialType = CredentialType.Email,
        requestState = ServerRequestState.RequestReceived(),
        onApprove = {},
        onDeny = {}
    )
}

@Preview(showBackground = true, name = "Share Document Credential Approval - Ready")
@Composable
fun ShareDocumentCredentialApprovalScreenReadyPreview() {
    ShareCredentialApprovalScreen(
        credentialType = CredentialType.Document,
        requestState = ServerRequestState.RequestReceived(subjects = listOf("dateOfBirth")),
        onApprove = {},
        onDeny = {}
    )
}

@Preview(showBackground = true, name = "Share Credential Approval - Waiting")
@Composable
fun ShareCredentialApprovalScreenWaitingPreview() {
    ShareCredentialApprovalScreen(
        credentialType = CredentialType.Email,
        requestState = ServerRequestState.RequestSent,
        onApprove = {},
        onDeny = {}
    )
}

@Preview(showBackground = true, name = "Share Credential Approval - Error")
@Composable
fun ShareCredentialApprovalScreenErrorPreview() {
    ShareCredentialApprovalScreen(
        credentialType = CredentialType.Email,
        requestState = ServerRequestState.RequestError("Request timed out"),
        onApprove = {},
        onDeny = {}
    )
}