package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerifyEmailResultScreen(
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
                        "Your email address has been successfully verified and a secure credential has been created on your device."
                    else 
                        "Your email address could not be verified. Please try again."
                )
            }

            if (isSuccess) {
                item {
                    // Success information
                    InfoCard(
                        icon = Icons.Filled.Verified,
                        title = "Verification Complete",
                        message = "Your email has been verified and a verifiable credential has been securely stored on your device. You can now use this credential to prove email ownership.",
                        type = AlertType.Success
                    )
                }


            } else {
                item {
                    // Error information
                    InfoCard(
                        icon = Icons.Filled.Error,
                        title = "Verification Failed",
                        message = "The email verification process was unsuccessful. Please check your email address, internet connection, and ensure you clicked the verification link.",
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
//                            title = "Check Email Address",
//                            description = "Ensure you entered a valid, accessible email address"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Email,
//                            title = "Check Your Inbox",
//                            description = "Look in spam/junk folders for the verification email"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Network Connection",
//                            description = "Ensure you have a stable internet connection during verification"
//                        )
//                    }
//                }
            }


        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
        ) {
            PrimaryButton(
                title = if (isSuccess) "Continue" else "Try Again",
                onClick = onContinue
            )
        }
    }
}

@Preview(showBackground = true, name = "Verify Email Result - Success")
@Composable
fun VerifyEmailResultScreenSuccessPreview() {
    VerifyEmailResultScreen(
        isSuccess = true,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Verify Email Result - Failure")
@Composable
fun VerifyEmailResultScreenFailurePreview() {
    VerifyEmailResultScreen(
        isSuccess = false,
        onContinue = {}
    )
}