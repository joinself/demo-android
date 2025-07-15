package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SecurityUpdateGood
import androidx.compose.material.icons.filled.SyncLock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.BackupRestoreState
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppColors
import com.joinself.app.demo.ui.theme.AppFonts
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.FeatureRow
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton
import com.joinself.app.demo.ui.theme.ProcessStep

@Composable
fun RestoreStartScreen(
    restoreState: BackupRestoreState,
    onStartRestore: () -> Unit,
    modifier: Modifier = Modifier
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
                HeroSection(
                    icon = Icons.Filled.Restore,
                    title = "Restore Your Account",
                    subtitle = "Verify your identity through a liveness check and selfie to securely restore your account data."
                )
            }

            item {
                InfoCard(
                    icon = Icons.Filled.Info,
                    title = "How Account Restoration Works",
                    message = "To ensure security, you'll first complete a liveness check and verify your identity with a selfie. Once confirmed, the Self system will automatically retrieve your encrypted backup.",
                    type = AlertType.Info
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    androidx.compose.material3.Text(
                        text = "Restoration Steps",
                        style = AppFonts.heading,
                        color = AppColors.textPrimary
                    )

                    ProcessStep(
                        number = 1,
                        title = "Liveness Check",
                        description = "Perform a quick liveness check to confirm you're a real person."
                    )

                    ProcessStep(
                        number = 2,
                        title = "Automatic Data Recovery",
                        description = "After successful verification, your encrypted account data will be securely restored by the Self system."
                    )
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    androidx.compose.material3.Text(
                        text = "What You'll Need",
                        style = AppFonts.heading,
                        color = AppColors.textPrimary
                    )
                    ProcessStep(
                        number = 1,
                        title = "This Device & Camera",
                        description = "You'll need access to this device's camera for the liveness check and selfie."
                    )
                    // Removed "Your Recovery Phrase" as user doesn't enter it.
                    ProcessStep(
                        number = 2,
                        title = "Stable Internet Connection",
                        description = "A good connection is needed for verification and to download your data."
                    )
                }
            }


            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    androidx.compose.material3.Text(
                        text = "Secure & Reliable",
                        style = AppFonts.heading,
                        color = AppColors.textPrimary
                    )

                    FeatureRow(
                        icon = Icons.Filled.SecurityUpdateGood,
                        title = "Enhanced Identity Verification",
                        description = "Liveness and selfie checks ensure only you can access your account."
                    )

                    FeatureRow(
                        icon = Icons.Filled.SyncLock,
                        title = "Automated & Encrypted",
                        description = "Your data remains encrypted and is automatically restored by the Self system after verification."
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
                .fillMaxWidth()
        ) {
            PrimaryButton(
                title = "Start Restore Process",
                isDisabled = restoreState == BackupRestoreState.Processing,
                onClick = onStartRestore
            )
        }
    }
}


@Preview(showBackground = true, name = "Restore Start Screen (Automated Recovery)")
@Composable
fun RestoreStartScreenAutomatedRecoveryPreview() {
    RestoreStartScreen(
        restoreState = BackupRestoreState.Processing,
        onStartRestore = {}
    )
}