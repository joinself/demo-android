package com.joinself.app.demo.ui.screens

// Choose icons relevant to receiving credentials
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppColors
import com.joinself.app.demo.ui.theme.AppFonts
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.FeatureRow
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton


@Composable
fun GetCredentialStartScreen(
    onStartGettingCredentials: () -> Unit,
    modifier: Modifier = Modifier,
    credentialName: String = "Custom Credential" // Example: "Proof of Age", "Membership ID"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Or your app's background color
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
                    icon = Icons.Filled.AssignmentInd, // Or Icons.Filled.Verified
                    title = "Get $credentialName",
                    subtitle = "A new $credentialName will be delivered to your device."
                )
            }

            item {
                // Information about the process
                InfoCard(
                    icon = Icons.Filled.Settings, // Icon implying server interaction
                    title = "Credential Generation",
                    message = "The server will generate and securely sign a new $credentialName for you, then deliver it to your device.",
                    type = AlertType.Info
                )
            }

//            item {
//                // More details about what will happen or benefits
//                // This is similar to the "Email Verification Required" InfoCard in VerifyEmailStartScreen
//                // but adapted for credentials
//                InfoCard(
//                    icon = Icons.Filled.Security,
//                    title = "What to Expect",
//                    message = "The process is quick and secure. Once initiated, the server will prepare your $credentialName. No complex steps are required from your side during generation.",
//                    type = AlertType.Info // Or AlertType.Success if it's more of a benefit
//                )
//            }

//             item {
//                 Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)) {
//                     Text(
//                         text = "Benefits",
//                         style = AppFonts.heading,
//                         color = AppColors.textPrimary
//                     )
//                     FeatureRow(
//                         icon = Icons.Filled.Verified,
//                         title = "Authentic & Verified",
//                         description = "Receive a credential that is verifiably authentic."
//                     )
//                     FeatureRow(
//                         icon = Icons.Filled.Security,
//                         title = "Securely Delivered",
//                         description = "Your credential is created and delivered through a secure channel."
//                     )
//                 }
//             }
        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White) // Match LazyColumn background
                .padding(AppSpacing.screenPadding)
                .fillMaxWidth()
        ) {
            PrimaryButton(
                title = "Start",
                onClick = onStartGettingCredentials
            )
        }
    }
}

@Preview(showBackground = true, name = "Get Credentials Start Screen Default")
@Composable
fun GetCredentialsStartScreenDefaultPreview() {
    // Wrap with your AppTheme if it's not applied globally in previews
    // YourAppTheme {
    GetCredentialStartScreen(
        onStartGettingCredentials = {}
    )
    // }
}

@Preview(showBackground = true, name = "Get Credentials Start Screen Custom Name")
@Composable
fun GetCredentialsStartScreenCustomNamePreview() {
    // YourAppTheme {
    GetCredentialStartScreen(
        onStartGettingCredentials = {},
        credentialName = "Membership Pass"
    )
    // }
}