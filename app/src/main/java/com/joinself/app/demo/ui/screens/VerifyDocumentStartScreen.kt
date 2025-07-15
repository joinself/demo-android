package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerifyDocumentStartScreen(
    onStartVerification: () -> Unit,
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
                    icon = Icons.Filled.Badge,
                    title = "Identity Verification",
                    subtitle = "Verify your government-issued identity documents like passport, driver’s license, or national ID. This creates a secure, verifiable credential stored on your device."
                )
            }

            item {
                // Information about the process
                InfoCard(
                    icon = Icons.Filled.CameraAlt,
                    title = "Document Capture Required",
                    message = "You will be asked to capture images of your identity document. Ensure good lighting and that all text is clearly visible.",
                    type = AlertType.Info
                )
            }

//            item {
//                // Process steps
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "Verification Process",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    ProcessStep(
//                        number = 1,
//                        title = "Document Selection",
//                        description = "Choose the type of document you want to verify"
//                    )
//
//                    ProcessStep(
//                        number = 2,
//                        title = "Document Capture",
//                        description = "Take clear photos of the front and back of your document"
//                    )
//
//                    ProcessStep(
//                        number = 3,
//                        title = "Verification Processing",
//                        description = "Self SDK will verify the authenticity of your document"
//                    )
//
//                    ProcessStep(
//                        number = 4,
//                        title = "Credential Creation",
//                        description = "A verifiable credential will be created and stored securely"
//                    )
//                }
//            }
//
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
//                        title = "Document Authentication",
//                        description = "Advanced verification checks document authenticity and security features"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.CheckCircle,
//                        title = "Local Storage",
//                        description = "Your verified credentials are stored securely on your device only"
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Badge,
//                        title = "Tamper-Proof Credentials",
//                        description = "Verified documents create cryptographically secure credentials"
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
                onClick = onStartVerification
            )
        }
    }
}

@Preview(showBackground = true, name = "Verify Document Start Screen")
@Composable
fun VerifyDocumentStartScreenPreview() {
    VerifyDocumentStartScreen(
        onStartVerification = {}
    )
}