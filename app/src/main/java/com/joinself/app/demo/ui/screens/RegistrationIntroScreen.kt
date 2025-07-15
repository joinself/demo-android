package com.joinself.app.demo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.RememberMe
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joinself.app.demo.ui.theme.AlertType
import com.joinself.app.demo.ui.theme.AppSpacing
import com.joinself.app.demo.ui.theme.HeroSection
import com.joinself.app.demo.ui.theme.InfoCard
import com.joinself.app.demo.ui.theme.PrimaryButton
import com.joinself.ui.component.Switch
import com.joinself.ui.theme.SelfModifier

@Composable
fun RegistrationIntroScreen(
    selfModifier: SelfModifier,
    onStartRegistration: () -> Unit,
    onStartRestore: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checked = remember { mutableStateOf(false) }
    val textStyle = selfModifier.textStyle()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(AppSpacing.screenPadding)
                .clickable {
                    onStartRestore.invoke()
                },
            text = "Restore Account"
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionSpacing)
        ) {
            item {
                // Hero Section
                HeroSection(
                    icon = Icons.Filled.AccountCircle,
                    title = "Register Your Account",
                    subtitle = "Complete a quick liveness check to securely register your Self account."
                )
            }

//            item {
//                // Process Steps Section
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(20.dp)
//                ) {
//                    Text(
//                        text = "What to Expect",
//                        style = AppFonts.heading,
//                        color = AppColors.textPrimary
//                    )
//
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(AppSpacing.componentSpacing)
//                    ) {
//                        ProcessStep(
//                            number = 1,
//                            title = "Camera Access",
//                            description = "We'll ask for camera permission when you start"
//                        )
//
//                        ProcessStep(
//                            number = 2,
//                            title = "Position Your Face",
//                            description = "Look directly at the camera and follow on-screen instructions"
//                        )
//                    }
//                }
//            }

            item {
                // Privacy & Security Info
                InfoCard(
                    icon = Icons.Filled.RememberMe,
                    title = "Liveness Check Required",
                    message = "You will be asked for camera permission when you start. Look directly at the camera and follow the on-screen instructions.",
                    type = AlertType.Info
                )
            }
        }

        // Fixed Primary Button at Bottom
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(AppSpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = selfModifier.color.textPrimary)) {
                    append("To use Self, please agree to our")
                }
                append("\n ")
                withLink(LinkAnnotation.Url("https://docs.joinself.com/agreements/consumer_terms_and_conditions/", TextLinkStyles(SpanStyle(color = selfModifier.color.info, textDecoration = TextDecoration.Underline)))) {
                    append("terms & conditions")
                }
                withStyle(style = SpanStyle(color = selfModifier.color.textPrimary)) {
                    append(" & ")
                }
                withLink(LinkAnnotation.Url("https://docs.joinself.com/agreements/app_privacy_notice/", TextLinkStyles(SpanStyle(color = selfModifier.color.info, textDecoration = TextDecoration.Underline)))) {
                    append("privacy policy.")
                }
            }

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = selfModifier.dimension.padding.normal),
                text = annotatedString, style = textStyle.body1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = selfModifier.dimension.padding.normal).height(20.dp)
            ) {
                Switch(selfModifier = selfModifier, enabled = true, checked = checked.value) {
                    checked.value = it
                }
                Text(
                    modifier = Modifier.padding(start = selfModifier.dimension.padding.normal),
                    text = "I agree",
                    style = textStyle.body1,
                    color = selfModifier.color.textPrimary
                )
            }

            PrimaryButton(
                title = "Start",
                onClick = {
                    onStartRegistration()
                },
                isDisabled = !checked.value
            )
        }
    }
}

@Preview(showBackground = true, name = "Registration Intro Screen")
@Composable
fun RegistrationIntroScreenPreview() {
    RegistrationIntroScreen(
        selfModifier = SelfModifier.sdk(),
        onStartRegistration = {},
        onStartRestore = {},
        onOpenSettings = {}
    )
}