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
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton

@Composable
fun VerifyDocumentResultScreen(
    isSuccess: Boolean,
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
                // Hero Section with Success/Error State
                HeroSection(
                    icon = if (isSuccess) Icons.Filled.CheckCircle else Icons.Filled.Error,
                    title = if (isSuccess) "Verification Success" else "Verification Failure",
                    subtitle = if (isSuccess) 
                        "Your identity document has been successfully verified and a secure credential has been created on your device."
                    else 
                        "Your identity document could not be verified. Please try again."
                )
            }

            if (isSuccess) {
                item {
                    // Success information
                    InfoCard(
                        icon = Icons.Filled.Verified,
                        title = "Verification Complete",
                        message = "Your document has been authenticated and a verifiable credential has been securely stored on your device. You can now use this credential to prove your identity.",
                        type = AlertType.Success
                    )
                }
            } else {
                item {
                    // Error information
                    InfoCard(
                        icon = Icons.Filled.Error,
                        title = "Verification Failed",
                        message = "The document verification process was unsuccessful. Please check that your document is supported, images are clear, and try again.",
                        type = AlertType.Error
                    )
                }
            }
        }

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

@Preview(showBackground = true, name = "Success")
@Composable
fun VerifyDocumentResultScreenSuccessPreview() {
    VerifyDocumentResultScreen(
        isSuccess = true,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Failure")
@Composable
fun VerifyDocumentResultScreenFailurePreview() {
    VerifyDocumentResultScreen(
        isSuccess = false,
        onContinue = {}
    )
}