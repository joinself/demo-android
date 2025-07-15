package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerifySelectionScreen(
    onVerifyIdentityDocument: () -> Unit,
    onVerifyEmail: () -> Unit,
    onGetCredentials: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth().height(48.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                androidx.compose.material3.IconButton(
                    onClick = onBack
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AppColors.primary
                    )
                }
            }
        }
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                HeroSection(
                    icon = Icons.Filled.Security,
                    title = "Get Credentials",
                    subtitle = "Choose the credential you want to get. These credentials help you establish trust and prove aspects of your identity."
                )
            }

            item {
                // Available verification options
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    androidx.compose.material3.Text(
                        text = "Available Verifications",
                        style = AppFonts.heading,
                        color = AppColors.textPrimary
                    )

                    // Verification option cards
                    VerificationCard(
                        icon = Icons.Filled.Email,
                        title = "Verify Email Address",
                        description = "Verify ownership of your email address.",
                        isEnabled = true,
                        onClick = onVerifyEmail
                    )

                    VerificationCard(
                        icon = Icons.Filled.Badge,
                        title = "Verify Identity Document",
                        description = "Verify your government-issued identity documents.",
                        isEnabled = true,
                        onClick = onVerifyIdentityDocument
                    )

                    VerificationCard(
                        icon = Icons.Filled.AssignmentInd,
                        title = "Get Custom Credential",
                        description = "Get a credential created by the server you are connected to.",
                        isEnabled = true,
                        onClick = onGetCredentials
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.componentSpacing))
                }
            }
        }
    }
}

@Composable
private fun VerificationCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        onClick = { if (isEnabled) onClick() },
        modifier = modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isEnabled) Color.White else AppColors.disabledBackground.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            if (isEnabled) AppColors.secondary.copy(alpha = 0.3f) else AppColors.disabledBackground
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = if (isEnabled) 2.dp else 0.dp
        ),
        enabled = isEnabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.cardPadding),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isEnabled) AppColors.primary else AppColors.disabledText,
                modifier = Modifier.size(32.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = AppFonts.subheading,
                    color = if (isEnabled) AppColors.textPrimary else AppColors.disabledText
                )
                androidx.compose.material3.Text(
                    text = description,
                    style = AppFonts.body,
                    color = if (isEnabled) AppColors.textSecondary else AppColors.disabledText
                )
            }

            if (isEnabled) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Go to $title",
                    tint = AppColors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Verify Selection Screen")
@Composable
fun VerifySelectionScreenPreview() {
    VerifySelectionScreen(
        onVerifyIdentityDocument = {},
        onVerifyEmail = {},
        onGetCredentials = {},
        onBack = {}
    )
}