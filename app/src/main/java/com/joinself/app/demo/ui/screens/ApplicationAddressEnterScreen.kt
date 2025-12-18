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
import androidx.compose.material.icons.filled.PrivateConnectivity
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
import com.joinself.app.demo.ui.theme.AppColors
import com.joinself.app.demo.ui.theme.AppFonts
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.PrimaryButton

@Composable
fun ApplicationAddressEnterScreen(
    onContinue: (String) -> Unit,
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
                .fillMaxSize()
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                HeroSection(
                    icon = Icons.Filled.PrivateConnectivity,
                    title = "Register by Address",
                    subtitle = "Enter the application address to register your Self account."
                )
            }

            item {
                Spacer(modifier = Modifier.height(AppSpacing.heroTopPadding))
            }


            item {
                // Server address input
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
                ) {
                    Text(
                        text = "Application Address",
                        style = AppFonts.heading,
                        color = AppColors.textPrimary
                    )
                    
                    Text(
                        text = "Enter the 66-character hexadecimal application address/ID.",
                        style = AppFonts.body,
                        color = AppColors.textSecondary
                    )
                    
                    OutlinedTextField(
                        value = serverAddress,
                        onValueChange = { newValue ->
                            // Only allow hex characters (0-9, a-f, A-F) and limit to 66 chars
                            if (newValue.length <= 66 && newValue.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
                                serverAddress = newValue
                            }
                        },
                        label = { Text("Application Address/ID") },
                        placeholder = { Text("Enter 66-character hex address...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = if (isValidAddress) ImeAction.Done else ImeAction.Default
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (isValidAddress) {
                                    focusManager.clearFocus()
                                    onContinue(serverAddress)
                                }
                            }
                        ),
                        supportingText = {
                            Text(
                                text = "${serverAddress.length}/66 characters",
                                color = if (isValidAddress) AppColors.success else AppColors.textSecondary
                            )
                        },
                        isError = serverAddress.isNotEmpty() && !isValidAddress
                    )
                }
            }
            
            item {
                // Primary Button - now part of scrollable content
                Spacer(modifier = Modifier.height(AppSpacing.buttonTopSpacing))
                PrimaryButton(
                    title = "Register",
                    onClick = { 
                        focusManager.clearFocus()
                        onContinue(serverAddress) 
                    },
                    isDisabled = !isValidAddress
                )
                Spacer(modifier = Modifier.height(AppSpacing.buttonBottomPadding))
            }
        }
    }
}

private fun isValidHexAddress(address: String): Boolean {
    return address.length == 66 && address.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }
}

@Preview
@Composable
fun ApplicationAddressEnterScreenPreview() {
    ApplicationAddressEnterScreen(onContinue = {})
}
