package com.joinself.app.demo.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joinself.app.demo.ServerRequestState
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton


@Composable
fun SigningRequestStartScreen(
    requestState: ServerRequestState,
    onStartAuthentication: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heroTitle = if (requestState is ServerRequestState.RequestSent) "Waiting for a request from server..."
                    else if (requestState is ServerRequestState.RequestReceived) "The server has requested you to sign using your biometric credentials. Complete the liveness check to verify your identity."
                    else if (requestState is ServerRequestState.RequestError) "The request timed out. Please go back, check the server and try again."
                    else "Signing Request"
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
                    icon = Icons.Filled.Security,
                    title = "Signing Request",
                    subtitle = heroTitle
                )
            }

            item {
                // Information about the process
                InfoCard(
                    icon = Icons.Filled.FaceRetouchingNatural,
                    title = "Liveness Check Required",
                    message = "You will sign to the server using your biometric credentials. Look directly at the camera and follow the on-screen instructions.",
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
                isDisabled = requestState !is ServerRequestState.RequestReceived,
                onClick = onStartAuthentication
            )
        }
    }
}

@Preview(showBackground = true, name = "Auth Request Start - Waiting")
@Composable
fun SigningRequestStartScreenWaitingPreview() {
    SigningRequestStartScreen(
        requestState = ServerRequestState.RequestSent,
        onStartAuthentication = {}
    )
}

@Preview(showBackground = true, name = "Auth Request Start - Ready")
@Composable
fun SigningRequestStartScreenReadyPreview() {
    SigningRequestStartScreen(
        requestState = ServerRequestState.RequestReceived(),
        onStartAuthentication = {}
    )
}

@Preview(showBackground = true, name = "Auth Request Start - Error")
@Composable
fun SigningRequestStartScreenErrorPreview() {
    SigningRequestStartScreen(
        requestState = ServerRequestState.RequestError("Sample error message"),
        onStartAuthentication = {}
    )
}