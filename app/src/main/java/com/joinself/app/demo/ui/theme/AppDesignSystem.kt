package com.joinself.app.demo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// MARK: - App Colors
object AppColors {
    val primary = Color(0xFF007AFF)
    val secondary = Color(0xFF8E8E93)
    val success = Color(0xFF34C759)
    val warning = Color(0xFFFF9500)
    val error = Color(0xFFFF3B30)
    val background = Color.White
    val secondaryBackground = Color(0xFFF8F9FA)
    val cardBackground = Color(0xFFF8F9FA)
    val textPrimary = Color(0xFF1D1D1F)
    val textSecondary = Color(0xFF86868B)
    
    // Standard disabled button colors
    val disabledBackground = Color(0xFFE0E0E0)
    val disabledText = Color(0xFF757575)
}

// MARK: - App Typography
object AppFonts {
    val title = androidx.compose.ui.text.TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
    val heading = androidx.compose.ui.text.TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
    val subheading = androidx.compose.ui.text.TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )
    val body = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
    val caption = androidx.compose.ui.text.TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
}

// MARK: - App Spacing
object AppSpacing {
    val screenPadding = 20.dp
    val sectionSpacing = 30.dp
    val componentSpacing = 16.dp
    val heroTopPadding = 30.dp
    val buttonBottomPadding = 20.dp
    val buttonTopSpacing = 40.dp
    val cardPadding = 16.dp
    val navHeaderPadding = 16.dp
}

// MARK: - Primary Button Component
@Composable
fun PrimaryButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isDisabled: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = !isDisabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.primary,
            contentColor = Color.White,
            disabledContainerColor = AppColors.disabledBackground,
            disabledContentColor = AppColors.disabledText
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = title,
                style = AppFonts.body.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

// MARK: - Secondary Button Component
@Composable
fun SecondaryButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDisabled: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = !isDisabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isDisabled) AppColors.disabledText else AppColors.primary
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDisabled) AppColors.disabledText else AppColors.primary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = title,
            style = AppFonts.body.copy(fontWeight = FontWeight.Medium)
        )
    }
}

// MARK: - Status Card Component
@Composable
fun StatusCard(
    title: String,
    status: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                Text(
                    text = title,
                    style = AppFonts.heading,
                    color = AppColors.textPrimary
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = status,
                    style = AppFonts.body.copy(fontWeight = FontWeight.Medium),
                    color = AppColors.textPrimary
                )
            }
            
            subtitle?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = AppFonts.caption,
                    color = AppColors.textSecondary
                )
            }
        }
    }
}

// MARK: - Alert Card Component
@Composable
fun AlertCard(
    title: String,
    message: String,
    type: AlertType,
    modifier: Modifier = Modifier,
    action: (() -> Unit)? = null,
    actionTitle: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                type.color.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = null,
                    tint = type.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = AppFonts.subheading,
                        color = AppColors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message,
                        style = AppFonts.body,
                        color = AppColors.textSecondary
                    )
                }
            }
            
            if (action != null && actionTitle != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = action,
                        colors = ButtonDefaults.textButtonColors(contentColor = type.color)
                    ) {
                        Text(
                            text = actionTitle,
                            style = AppFonts.body.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}

// MARK: - Alert Type Enum
enum class AlertType(val color: Color, val icon: ImageVector) {
    Info(AppColors.primary, Icons.Filled.Info),
    Warning(AppColors.warning, Icons.Filled.Warning),
    Error(AppColors.error, Icons.Filled.Close),
    Success(AppColors.success, Icons.Filled.Check)
}

// MARK: - Hero Section Component
@Composable
fun HeroSection(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconSize: Int = 60,
    topPadding: Int = 30
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(topPadding.dp))
        
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(iconSize.dp)
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = title,
            style = AppFonts.title,
            color = AppColors.textPrimary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = subtitle,
            style = AppFonts.body,
            color = AppColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

// MARK: - Process Step Component
@Composable
fun ProcessStep(
    number: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Step Number Circle
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(AppColors.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = AppFonts.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        
        Spacer(modifier = Modifier.width(AppSpacing.componentSpacing))
        
        // Step Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = AppFonts.body.copy(fontWeight = FontWeight.SemiBold),
                color = AppColors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = AppFonts.caption,
                color = AppColors.textSecondary
            )
        }
    }
}

// MARK: - Feature Row Component
@Composable
fun FeatureRow(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(30.dp)
        )
        
        Spacer(modifier = Modifier.width(AppSpacing.componentSpacing))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = AppFonts.body.copy(fontWeight = FontWeight.SemiBold),
                color = AppColors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = AppFonts.caption,
                color = AppColors.textSecondary
            )
        }
    }
}

// MARK: - Info Card Component
@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    message: String,
    type: AlertType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                type.color.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.cardPadding),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = type.color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(AppSpacing.componentSpacing))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = AppFonts.body.copy(fontWeight = FontWeight.SemiBold),
                    color = AppColors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = AppFonts.caption,
                    color = AppColors.textSecondary
                )
            }
        }
    }
} 