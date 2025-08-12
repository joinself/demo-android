package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.BackupRestoreState
import com.joinself.app.demo.ui.theme.AlertCard
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton

@Composable
fun BackupResultScreen(
    backupState: BackupRestoreState,
    onContinue: () -> Unit,
    onRetry: (() -> Unit)? = null,
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
                // Hero Section - Success or Failure
                if (backupState is BackupRestoreState.Success) {
                    HeroSection(
                        icon = Icons.Filled.CloudDone,
                        title = "Backup Successful",
                        subtitle = "Your account data has been securely backed up by the Self system."
                    )
                } else {
                    HeroSection(
                        icon = Icons.Filled.CloudOff,
                        title = "Backup Failed",
                        subtitle = "We encountered an issue while trying to back up your account."
                    )
                }
            }

            item {
                // Result details card
                if (backupState is BackupRestoreState.Success) {
                    InfoCard(
                        icon = Icons.Filled.CheckCircle,
                        title = "Backup Complete",
                        message = "Your data has been backed up in an encrypted file. You can restore the data using your biometrics.",
                        type = AlertType.Success
                    )
                } else {
                    AlertCard(
                        title = "Backup Failed",
                        message = "The backup process could not be completed. Please check your internet connection and try again.",
                        type = AlertType.Error
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
        ) {
            PrimaryButton(
                title = if (backupState is BackupRestoreState.Success) "Continue" else "Continue",
                onClick = onContinue
            )
//            if (backupState is BackupRestoreState.Error && onRetry != null) {
//                SecondaryButton(
//                    title = "Retry Backup",
//                    onClick = onRetry
//                )
//            }
        }
    }
}


@Preview(showBackground = true, name = "Backup Result Success (System Managed)")
@Composable
fun BackupResultScreenSuccessSystemManagedPreview() {
    BackupResultScreen(
        backupState = BackupRestoreState.Success,
        onContinue = {}
    )
}

@Preview(showBackground = true, name = "Backup Result Failure (System Managed)")
@Composable
fun BackupResultScreenFailureSystemManagedPreview() {
    BackupResultScreen(
        backupState = BackupRestoreState.Error("failed"),
        onContinue = {},
        onRetry = {}
    )
}