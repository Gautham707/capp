package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Swiss Design Style Palette (International Typographic Style: High contrast, stark minimalism, bold Swiss Red, deep black, crisp white, structural grays)
val SwissBackgroundLight = Color(0xFFFCFCFC)       // Pure clean off-white
val SwissSurfaceLight = Color(0xFFFFFFFF)          // Crisp white card surface
val SwissSurfaceVariantLight = Color(0xFFF1F5F9)   // Structural light gray grid container
val SwissInkPrimary = Color(0xFF0F172A)            // Deep architectural black/charcoal
val SwissInkMuted = Color(0xFF64748B)              // Precise muted gray
val SwissBorderLight = Color(0xFFCBD5E1)           // Clean crisp grid border
val SwissBorderStrong = Color(0xFF0F172A)          // Bold black structural line

// Swiss Accent Colors (Iconic Swiss Red & Functional High-Contrast Accents)
val SwissRed = Color(0xFFE60000)                   // Iconic Swiss Red accent
val SwissRedDark = Color(0xFFB30000)               // Deep Swiss Red
val SwissGreen = Color(0xFF10B981)                 // Clean functional green (success)
val SwissBlue = Color(0xFF2563EB)                  // Clean structural blue
val SwissAmber = Color(0xFFD97706)                 // Precise amber (pending/warning)

// Swiss Dark Theme (Stark high-contrast modernist dark mode)
val SwissBackgroundDark = Color(0xFF0A0A0A)        // Deep modernist black
val SwissSurfaceDark = Color(0xFF141414)           // Dark architectural surface
val SwissSurfaceVariantDark = Color(0xFF1F2937)    // Dark structured container
val SwissTextPrimaryDark = Color(0xFFF8FAFC)       // Crisp white text
val SwissTextMutedDark = Color(0xFF94A3B8)         // Muted light gray
val SwissBorderDark = Color(0xFF334155)            // Precise dark grid border

// Backwards-compatible aliases for existing references
val BluePrimary = SwissRed
val BluePrimaryLight = SwissRedDark
val BlueAccent = SwissBlue
val TealSuccess = SwissGreen
val TealSuccessLight = Color(0xFF34D399)
val RoseDue = SwissRed
val RoseDueLight = Color(0xFFFF6B6B)
val AmberPending = SwissAmber

val BackgroundLight = SwissBackgroundLight
val SurfaceLight = SwissSurfaceLight
val SurfaceVariantLight = SwissSurfaceVariantLight
val TextPrimaryLight = SwissInkPrimary
val TextMutedLight = SwissInkMuted
val BorderLight = SwissBorderLight

val BackgroundDark = SwissBackgroundDark
val SurfaceDark = SwissSurfaceDark
val SurfaceVariantDark = SwissSurfaceVariantDark
val TextPrimaryDark = SwissTextPrimaryDark
val TextMutedDark = SwissTextMutedDark
val BorderDark = SwissBorderDark
