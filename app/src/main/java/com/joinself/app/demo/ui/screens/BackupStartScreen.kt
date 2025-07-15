package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
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
fun BackupStartScreen(
    backupState: BackupRestoreState,
    onStartBackup: () -> Unit,
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
                HeroSection(
                    icon = Icons.Filled.Backup,
                    title = "Account Backup",
                    subtitle = "Create an encrypted backup of your account data."
                )
            }

            item {
                InfoCard(
                    icon = Icons.Filled.Info,
                    title = "Encrypted Backup",
                    message = "Your account data will be encrypted and backed up. You can restore it using your biometrics",
                    type = AlertType.Info
                )
            }

//            item {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "How Backup Works",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    ProcessStep(
//                        number = 1,
//                        title = "Prepare Data",
//                        description = "Your essential account data is prepared for backup."
//                    )
//
//                    ProcessStep(
//                        number = 2,
//                        title = "Encrypt & Secure",
//                        description = "The data is strongly encrypted, and the Self system sets up secure recovery protocols."
//                    )
//
//                    ProcessStep(
//                        number = 3,
//                        title = "Backup Complete",
//                        description = "Your encrypted data is backed up. You can restore it later through identity verification."
//                    )
//                }
//            }

//            item {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "Security & Peace of Mind",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Lock,
//                        title = "End-to-End Encryption",
//                        description = "Your backup data is encrypted before it leaves your device and remains encrypted at rest."
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.Security,
//                        title = "System-Managed Recovery",
//                        description = "The Self system handles the complexities of secure recovery, ensuring only you can restore after verification."
//                    )
//
//                    FeatureRow(
//                        icon = Icons.Filled.CheckCircle,
//                        title = "Easy Restoration",
//                        description = "Restore your account on any supported device through a guided identity verification process."
//                    )
//                }
//            }
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
                .fillMaxWidth()
        ) {
            PrimaryButton(
                title = "Start",
                isDisabled = backupState == BackupRestoreState.Processing,
                onClick = onStartBackup
            )
        }
    }
}

@Preview(showBackground = true, name = "Backup Start Screen (System Managed)")
@Composable
fun BackupStartScreenSystemManagedPreview() {
    BackupStartScreen(
        backupState = BackupRestoreState.Processing,
        onStartBackup = {}
    )
}