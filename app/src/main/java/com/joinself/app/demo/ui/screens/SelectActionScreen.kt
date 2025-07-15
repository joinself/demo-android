package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joinself.app.demo.ui.theme.AppColors
import com.joinself.app.demo.ui.theme.AppFonts
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection

@Composable
fun SelectActionScreen(
    onAuthenticate: () -> Unit,
    onVerifyCredentials: () -> Unit,
    onProvideCredentials: () -> Unit,
    onSignDocuments: () -> Unit,
    onBackup: () -> Unit,
    onConnectToServer: () -> Unit,
    modifier: Modifier = Modifier,
    isAuthenticating: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppSpacing.screenPadding, end = AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                HeroSection(
                    icon = Icons.Filled.VerifiedUser,
                    title = "Server Connected",
                    subtitle = "Your Self account is connected to the server and ready to use. Choose an action below to get started."
                )
            }

            item {
                // Available actions
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    ActionCard(
                        icon = Icons.Filled.Fingerprint,
                        title = "Authenticate",
                        description = "Use your biometric credentials to securely log in to services.",
                        onClick = onAuthenticate
                    )

                    ActionCard(
                        icon = Icons.Filled.VerifiedUser,
                        title = "Get Credentials",
                        description = "Get credentials about you such as email and government ID.",
                        onClick = onVerifyCredentials
                    )

                    ActionCard(
                        icon = Icons.Filled.Share,
                        title = "Share Credentials",
                        description = "Securely share credentials about you.",
                        onClick = onProvideCredentials
                    )

                    ActionCard(
                        icon = Icons.Filled.Edit,
                        title = "Digital Signatures",
                        description = "Sign a document with your digital signature.",
                        onClick = onSignDocuments
                    )

                    ActionCard(
                        icon = Icons.Filled.Backup,
                        title = "Backup",
                        description = "Create an encrypted backup of your account data.",
                        onClick = onBackup
                    )

                    ActionCard(
                        icon = Icons.Filled.CloudQueue,
                        title = "Connect to Server",
                        description = "Connect to another Self server to receive and send data",
                        onClick = onConnectToServer
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.componentSpacing))
                }
            }
        }
    }
    
    // Full-screen overlay with spinner when authenticating
    if (isAuthenticating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = AppColors.primary,
                strokeWidth = 4.dp
            )
        }
    }
}

@Preview
@Composable
fun SelectActionScreenPreview() {
    SelectActionScreen(
        onAuthenticate = {},
        onVerifyCredentials = {},
        onProvideCredentials = {},
        onSignDocuments = {},
        onBackup = {},
        onConnectToServer = {},
        isAuthenticating = false
    )
}

@Composable
fun ActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            AppColors.secondary.copy(alpha = 0.3f)
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
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
                tint = AppColors.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = AppFonts.subheading,
                    color = AppColors.textPrimary
                )
                androidx.compose.material3.Text(
                    text = description,
                    style = AppFonts.body,
                    color = AppColors.textSecondary
                )
            }

            androidx.compose.material3.Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Go to $title",
                tint = AppColors.textSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
} 