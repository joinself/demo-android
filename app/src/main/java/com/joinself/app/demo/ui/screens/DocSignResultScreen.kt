package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton
import com.joinself.sdk.models.ResponseStatus

@Composable
fun DocSignResultScreen(
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
                            title = "Document Signing Success",
                            subtitle = "Your digital signature has been added to the document."
                        )
                    } else {
                        HeroSection(
                            icon = Icons.Filled.CheckCircle,
                            title = "Document Signing Rejected",
                            subtitle = "Your digital signature has not been added to the document."
                        )
                    }
                } else {
                    HeroSection(
                        icon = Icons.Filled.Error,
                        title = "Document Signing Failure",
                        subtitle = "You rejected the document signing request."
                    )
                }
            }

            item {
                // Result details
                if (requestState is ServerRequestState.ResponseSent) {
                    if (requestState.status == ResponseStatus.accepted) {
                        InfoCard(
                            icon = Icons.Filled.Verified,
                            title = "Signature Complete",
                            message = "Your cryptographic signature has been successfully applied to the document. The signed document has been returned to the server.",
                            type = AlertType.Success
                        )
                    } else {
                        InfoCard(
                            icon = Icons.Filled.Verified,
                            title = "Signature Rejected",
                            message = "Your cryptographic signature has not been successfully applied to the document. The unsigned document has been returned to the server.",
                            type = AlertType.Success
                        )
                    }
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
fun DocSignResultScreenSuccessPreview() {
    DocSignResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.accepted),
        onContinue = {}
    )
}

@Preview(showBackground = true, name = " Rejected")
@Composable
fun DocSignResultScreenRejectedPreview() {
    DocSignResultScreen(
        requestState = ServerRequestState.ResponseSent(ResponseStatus.rejected),
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Failed")
@Composable
fun DocSignResultScreenFailedPreview() {
    DocSignResultScreen(
        requestState = ServerRequestState.RequestError("Signing failed"),
        onContinue = {}
    )
}