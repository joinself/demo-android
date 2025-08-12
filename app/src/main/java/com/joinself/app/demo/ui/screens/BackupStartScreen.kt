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
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.BackupRestoreState
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton


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