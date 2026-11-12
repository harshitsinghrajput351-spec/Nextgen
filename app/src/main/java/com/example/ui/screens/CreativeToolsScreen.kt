package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VfxGlassCard
import com.example.ui.components.VfxGlowingButton
import com.example.ui.theme.CreditGold
import com.example.ui.theme.DeepVfxBackground
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SecondaryVfxBackground
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun CreativeToolsScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTool by remember { mutableStateOf("CHAT") } // "CHAT", "TRANSLATE", "DOCS", "3D"

    var chatInput by remember { mutableStateOf("What are the key emotional beats for a Hindi cinematic drama?") }
    var chatResponse by remember { mutableStateOf<String?>(null) }
    var isChatLoading by remember { mutableStateOf(false) }

    var translateInput by remember { mutableStateOf("Dream big, work silently, let your success make the noise.") }
    var translatedResult by remember { mutableStateOf<String?>(null) }

    var docTitle by remember { mutableStateOf("Executive Film Production Pitch") }
    var generatedDoc by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CREATIVE SUITE UTILITIES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NeonCyan
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SecondaryVfxBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "MULTI-MODAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VfxGlowCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Tools selector chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryVfxBackground, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    Triple("CHAT", "AI Chat", Icons.Default.Chat),
                    Triple("TRANSLATE", "Translator", Icons.Default.Language),
                    Triple("DOCS", "Pitch / Doc", Icons.Default.Description),
                    Triple("3D", "3D Concept", Icons.Default.ViewInAr)
                ).forEach { (id, label, icon) ->
                    val isSel = id == selectedTool
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) ElectricBlue else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedTool = id }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSel) TextWhite else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) TextWhite else TextMuted
                            )
                        }
                    }
                }
            }
        }

        when (selectedTool) {
            "CHAT" -> {
                item {
                    VfxGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💬 AI Creative Co-Pilot",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Brainstorm plot twists, character backstories, marketing loglines, and visual styles.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                            )

                            OutlinedTextField(
                                value = chatInput,
                                onValueChange = { chatInput = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("creative_chat_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                    focusedContainerColor = DeepVfxBackground,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                ),
                                minLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            VfxGlowingButton(
                                text = "CONSULT AI CO-PILOT",
                                onClick = {
                                    isChatLoading = true
                                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                        chatResponse = "For a high-impact Hindi cinematic drama:\n1. The Anchor of Soil: Ground the protagonist in tangible family struggle (a mother's worn prayer bead or debt notice).\n2. The Mentor's Crucible: A mentor who refuses to hand over victory easily—demanding the hero build inner resilience.\n3. The Climax Match-Cut: Juxtapose childhood alley struggles directly with the modern boardroom victory.\n4. Musical Crescendo: Transition from an acoustic sitar melody into full modern brass synthesizers."
                                        isChatLoading = false
                                    }, 800)
                                },
                                isLoading = isChatLoading,
                                creditCost = 2,
                                testTag = "creative_chat_button"
                            )

                            if (chatResponse != null) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = chatResponse!!,
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        modifier = Modifier.padding(12.dp),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "TRANSLATE" -> {
                item {
                    VfxGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🌐 Multilingual Script & Dialogue Translator",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Translate cinematic scripts between Hindi, English, Spanish, Japanese, and German while preserving emotional subtext.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                            )

                            OutlinedTextField(
                                value = translateInput,
                                onValueChange = { translateInput = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                    focusedContainerColor = DeepVfxBackground,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                ),
                                minLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            VfxGlowingButton(
                                text = "TRANSLATE TO CINEMATIC HINDI",
                                onClick = {
                                    translatedResult = "बड़े सपने देखो, खामोशी से मेहनत करो, और अपनी कामयाबी को सारा शोर मचाने दो।"
                                },
                                creditCost = 2
                            )

                            if (translatedResult != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CreditGold.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Translated Dialogue (Hindi):", fontSize = 10.sp, color = CreditGold, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(translatedResult!!, fontSize = 14.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "DOCS" -> {
                item {
                    VfxGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📄 Pitch Deck & Document Generator",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Generate executive film pitches, investment summaries, and press releases.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                            )

                            OutlinedTextField(
                                value = docTitle,
                                onValueChange = { docTitle = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                    focusedContainerColor = DeepVfxBackground,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            VfxGlowingButton(
                                text = "GENERATE EXECUTIVE PITCH",
                                onClick = {
                                    generatedDoc = "TITLE: THE SLUMDOG EMPEROR\nFORMAT: 20-Minute Cinematic Feature\nTARGET AUDIENCE: Global Drama / Tech Innovators\nBUDGET ALLOCATION: 4K Neural VFX, Symphonic Score, Bilingual Cast\nLOGLINE: An impoverished youth in Mumbai deciphers quantum neural networks to rewrite his destiny and challenge global monopolies."
                                },
                                creditCost = 10
                            )

                            if (generatedDoc != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = DeepVfxBackground,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = generatedDoc!!,
                                        fontSize = 11.sp,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                        color = NeonCyan,
                                        modifier = Modifier.padding(12.dp),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "3D" -> {
                item {
                    VfxGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🧊 3D Holographic Concept Render",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Synthesize 3D spatial models, holographic props, and cyberpunk vehicles with depth maps.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DeepVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.ViewInAr,
                                            contentDescription = null,
                                            tint = NeonCyan,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Quantum Neural Core: OBJ / GLTF Ready",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = "Vertex Count: 142k • Normal Maps Enabled",
                                            fontSize = 10.sp,
                                            color = TextDim
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
}
