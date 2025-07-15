package com.joinself.app.demo.ui.screens
import com.joinself.app.demo.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerifyEmailStartScreen(
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
                    icon = Icons.Filled.Email,
                    title = "Email Verification",
                    subtitle = "Verify ownership of your email address to create a trusted credential."
                )
            }

            item {
                // Information about the process
                InfoCard(
                    icon = Icons.Filled.Verified,
                    title = "Email Verification Required",
                    message = "You will need to provide your email address and confirm it by entering a verification code sent to your inbox. Keep your email app handy during this process.",
                    type = AlertType.Info
                )
            }




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

@Preview(showBackground = true, name = "Verify Email Start Screen")
@Composable
fun VerifyEmailStartScreenPreview() {
    VerifyEmailStartScreen(
        onStartVerification = {}
    )
}