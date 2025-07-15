package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

//                item {
//                    // What happens next
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "What You Can Do Now",
//                            style = AppFonts.heading,
//                            color = AppColors.textPrimary
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Share Your Credentials",
//                            description = "Use your verified credentials to authenticate with services"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Badge,
//                            title = "Prove Your Identity",
//                            description = "Your verified document can be used as proof of identity"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.CheckCircle,
//                            title = "Access Protected Services",
//                            description = "Many services accept verified credentials for enhanced security"
//                        )
//                    }
//                }
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
//                            title = "Check Image Quality",
//                            description = "Ensure photos are clear, well-lit, and all text is readable"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Badge,
//                            title = "Verify Document Type",
//                            description = "Make sure your document type is supported by the verification system"
//                        )
//
//                        FeatureRow(
//                            icon = Icons.Filled.Security,
//                            title = "Document Condition",
//                            description = "Ensure your document is not damaged, expired, or tampered with"
//                        )
//                    }
//                }
            }

//            item {
//                // Additional information
//                StatusCard(
//                    title = "Document Verification",
//                    status = if (isSuccess) "Completed Successfully" else "Failed",
//                    statusColor = if (isSuccess) AppColors.success else AppColors.error,
//                    subtitle = if (isSuccess)
//                        "Your credential is now available for use"
//                    else
//                        "You can try the verification process again",
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

@Preview(showBackground = true, name = "Verify Document Result - Success")
@Composable
fun VerifyDocumentResultScreenSuccessPreview() {
    VerifyDocumentResultScreen(
        isSuccess = true,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Verify Document Result - Failure")
@Composable
fun VerifyDocumentResultScreenFailurePreview() {
    VerifyDocumentResultScreen(
        isSuccess = false,
        onContinue = {}
    )
}