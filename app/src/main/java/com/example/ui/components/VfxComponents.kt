package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreditGold
import com.example.ui.theme.DeepVfxBackground
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SecondaryVfxBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VfxBorderGlow
import com.example.ui.theme.VfxCardSurface
import com.example.ui.theme.VfxGlowCyan
import kotlin.random.Random

private data class Particle(
    val xRatio: Float,
    val yRatio: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float,
    val color: Color
)

@Composable
fun VfxParticleBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val particles = remember {
        val rand = Random(42)
        List(35) {
            val isCyan = rand.nextBoolean()
            Particle(
                xRatio = rand.nextFloat(),
                yRatio = rand.nextFloat(),
                radius = rand.nextFloat() * 3f + 1.2f,
                speed = rand.nextFloat() * 0.8f + 0.3f,
                alpha = rand.nextFloat() * 0.6f + 0.2f,
                color = if (isCyan) NeonCyan else ElectricBlue
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVfxBackground)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Subtle cyber grid
            val gridStep = 80.dp.toPx()
            var xPos = 0f
            while (xPos <= w) {
                drawLine(
                    color = ElectricBlue.copy(alpha = 0.04f),
                    start = Offset(xPos, 0f),
                    end = Offset(xPos, h),
                    strokeWidth = 1f
                )
                xPos += gridStep
            }

            var yPos = 0f
            while (yPos <= h) {
                drawLine(
                    color = ElectricBlue.copy(alpha = 0.04f),
                    start = Offset(0f, yPos),
                    end = Offset(w, yPos),
                    strokeWidth = 1f
                )
                yPos += gridStep
            }

            // Top ambient glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(w * 0.5f, -60f),
                    radius = w * 0.9f
                ),
                radius = w * 0.9f,
                center = Offset(w * 0.5f, -60f)
            )

            // Draw floating particles
            particles.forEach { p ->
                val currentY = ((p.yRatio - (phase * p.speed)) % 1f + 1f) % 1f * h
                val currentX = p.xRatio * w

                drawCircle(
                    color = p.color.copy(alpha = p.alpha),
                    radius = p.radius.dp.toPx(),
                    center = Offset(currentX, currentY)
                )

                // Particle halo
                drawCircle(
                    color = p.color.copy(alpha = p.alpha * 0.25f),
                    radius = (p.radius * 2.8f).dp.toPx(),
                    center = Offset(currentX, currentY)
                )
            }
        }

        content()
    }
}

@Composable
fun VfxGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderGlowAlpha: Float = 0.35f,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = borderGlowAlpha),
                        ElectricBlue.copy(alpha = borderGlowAlpha * 0.5f),
                        VfxBorderGlow.copy(alpha = borderGlowAlpha * 0.8f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(clickableModifier),
        shape = RoundedCornerShape(cornerRadius),
        color = VfxCardSurface.copy(alpha = 0.88f),
        tonalElevation = 6.dp
    ) {
        content()
    }
}

@Composable
fun VfxGlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
    creditCost: Int? = null,
    testTag: String = "vfx_glow_button"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_alpha"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(14.dp))
            .drawBehind {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(
                            ElectricBlue.copy(alpha = glowAlpha),
                            NeonCyan.copy(alpha = glowAlpha * 0.8f)
                        )
                    ),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    ) {
        Button(
            onClick = onClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = TextWhite
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(ElectricBlue, Color(0xFF0052CC), NeonCyan.copy(alpha = 0.7f))
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = NeonCyan,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Synthesizing Neural Matrix...",
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (icon != null) {
                            icon()
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = text,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                        if (creditCost != null) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = DeepVfxBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Credits",
                                        tint = CreditGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "$creditCost",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CreditGold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreditBalanceChip(
    balance: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(CreditGold.copy(alpha = 0.6f), NeonCyan.copy(alpha = 0.4f))),
                RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = SecondaryVfxBackground.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Available Credits",
                tint = CreditGold,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$balance",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "⚡",
                fontSize = 11.sp
            )
        }
    }
}
