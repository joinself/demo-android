package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState

@Composable
fun DocSignStartScreen(
    requestState: ServerRequestState,
    onSign: () -> Unit,
    onReject: () -> Unit,
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
                HeroSection(
                    icon = Icons.Filled.PictureAsPdf,
                    title = "Document Signing",
                    subtitle = if(requestState !is ServerRequestState.RequestReceived) { "Waiting for a request from server..."} else {"The server has requested you sign a document. Review the details below and choose whether to sign or reject."}
                )
            }

            item {
                // Information about the document
                InfoCard(
                    icon = Icons.Filled.PictureAsPdf,
                    title = "Server Request",
                    message = "You are being asked to sign a document. This creates a verifiable digital signature using your cryptographic credentials.",
                    type = AlertType.Info
                )
            }

//            item {
//                // Security information
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    Text(
//                        text = "Security & Legal Notice",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Security,
//                        title = "Cryptographic Authentication",
//                        description = "Your signature is created using secure cryptographic keys unique to you"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.CheckCircle,
//                        title = "Non-Repudiation",
//                        description = "The signature provides legal proof of your agreement to the terms of the document."
//                    )
//                }
//            }
        }

        // Fixed Action Buttons at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
        ) {
            PrimaryButton(
                title = "Approve",
                isDisabled = requestState !is ServerRequestState.RequestReceived,
                onClick = onSign
            )
            
            SecondaryButton(
                title = "Reject",
                isDisabled = requestState !is ServerRequestState.RequestReceived,
                onClick = onReject
            )
        }
    }
}

@Preview(showBackground = true, name = "Doc Sign Start Screen - Ready")
@Composable
fun DocSignStartScreenReadyPreview() {
    DocSignStartScreen(
        requestState = ServerRequestState.RequestReceived(null),
        onSign = {},
        onReject = {}
    )
}

@Preview(showBackground = true, name = "Doc Sign Start Screen - Waiting")
@Composable
fun DocSignStartScreenWaitingPreview() {
    DocSignStartScreen(
        requestState = ServerRequestState.RequestSent,
        onSign = {},
        onReject = {}
    )
}

@Preview(showBackground = true, name = "Doc Sign Start Screen - Error")
@Composable
fun DocSignStartScreenErrorPreview() {
    DocSignStartScreen(
        requestState = ServerRequestState.RequestError("Request timed out"),
        onSign = {},
        onReject = {}
    )
}