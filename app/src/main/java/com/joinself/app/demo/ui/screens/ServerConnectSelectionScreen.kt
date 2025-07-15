package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.PrivateConnectivity
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppColors
import com.joinself.app.demo.ui.theme.AppFonts
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton

@Composable
fun ServerConnectSelectionScreen(
    onAddress: () -> Unit,
    onQRCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var serverAddress by remember { mutableStateOf("") }
    val isValidAddress = isValidHexAddress(serverAddress)
    val focusManager = LocalFocusManager.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Main content with LazyColumn for better keyboard handling
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppSpacing.screenPadding, end = AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                HeroSection(
                    icon = Icons.Filled.CloudQueue,
                    title = "Connect to Server",
                    subtitle = "Connect to a server using one of the methods below."
                )
            }


            item {
                // Available actions
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    
                    ActionCard(
                        icon = Icons.Filled.PrivateConnectivity,
                        title = "Server Address",
                        description = "Connect to the server using the server address.",
                        onClick = onAddress
                    )

                    ActionCard(
                        icon = Icons.Filled.QrCode,
                        title = "Server QR Code",
                        description = "Scan the servers QR code to connect.",
                        onClick = onQRCode
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.componentSpacing))
                }
            }

        }
    }
}

private fun isValidHexAddress(address: String): Boolean {
    return address.length == 66 && address.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }
}

@Preview
@Composable
fun ServerConnectSelectionScreenPreview() {
    ServerConnectSelectionScreen(onAddress = {}, onQRCode = {})
}
