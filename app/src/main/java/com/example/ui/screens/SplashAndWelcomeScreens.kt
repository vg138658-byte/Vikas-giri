package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
  val scale = remember { Animatable(0.7f) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = 1.0f,
      animationSpec = tween(durationMillis = 800)
    )
    delay(1200)
    onSplashFinished()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            SleekBlue900,
            SleekIndigo900,
            Slate900
          )
        )
      )
      .testTag("splash_screen_container"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .scale(scale.value)
        .padding(32.dp)
    ) {
      // Construction Icon Badge
      Box(
        modifier = Modifier
          .size(108.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              colors = listOf(SleekBlue500, SleekBlue700)
            )
          )
          .padding(4.dp),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(Slate900),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Construction,
            contentDescription = "Logo",
            tint = SleekBlue100,
            modifier = Modifier.size(56.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "UMESH ENTERPRISE",
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.2.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Aapke Har Nirman Ka Bharosemand Saathi",
        color = SleekBlue100,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(24.dp))

      Surface(
        color = Color(0x25FFFFFF),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x40FFFFFF))
      ) {
        Text(
          text = "Gitti  |  Balu  |  Sariya  |  Cement",
          color = SleekBlue100,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(48.dp))

      CircularProgressIndicator(
        color = SleekBlue500,
        strokeWidth = 3.dp,
        modifier = Modifier.size(32.dp)
      )
    }
  }
}

@Composable
fun WelcomeScreen(onExploreClick: () -> Unit) {
  var visible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    visible = true
  }

  Scaffold(
    containerColor = SleekBg,
    bottomBar = {
      Surface(
        color = Color.White,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        modifier = Modifier.navigationBarsPadding()
      ) {
        Button(
          onClick = onExploreClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = SleekBlue700,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .testTag("welcome_explore_button")
        ) {
          Text(
            text = "EXPLORE NOW",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -40 }
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // Decorative Construction Badge
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(RoundedCornerShape(22.dp))
              .background(SleekBlue50)
              .border(1.dp, SleekBlue100, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Apartment,
              contentDescription = null,
              tint = SleekBlue700,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = "WELCOME TO",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = SleekBlue600,
            letterSpacing = 2.sp
          )

          Text(
            text = "UMESH ENTERPRISE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SleekBlue900,
            letterSpacing = (-0.3).sp,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Aapke ghar, building aur har construction kaam ke liye quality building materials ek hi jagah.",
            fontSize = 14.sp,
            color = Slate600,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // What we provide card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Hum provide karte hain:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SleekBlue900
          )

          Spacer(modifier = Modifier.height(14.dp))

          val materials = listOf(
            Triple("Gitti", "10mm, 20mm, 40mm RCC Stone Aggregate", Icons.Default.Terrain),
            Triple("Balu", "Nadi Balu, Plaster Balu, Construction Sand", Icons.Default.Grain),
            Triple("Sariya", "Fe 550D TMT Steel Rebar (6mm to 25mm)", Icons.Default.ViewWeek),
            Triple("Cement", "UltraTech, Ambuja, ACC, PPC & OPC Brands", Icons.Default.Architecture),
            Triple("Eent", "1 Number Red Clay Kiln Bricks & Fly Ash Blocks", Icons.Default.Square),
            Triple("Aur anya Materials", "Stone dust, chips aur complete building supply", Icons.Default.Category)
          )

          materials.forEach { (name, desc, icon) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(SleekBlue50),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = SleekBlue700,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Slate800
                )
                Text(
                  text = desc,
                  fontSize = 12.sp,
                  color = Slate500
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Core Guarantees Banner
      Surface(
        color = Color(0xFFEFF6FF),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          GuaranteeItem(icon = Icons.Default.Verified, title = "Achhi Quality")
          GuaranteeItem(icon = Icons.Default.CurrencyRupee, title = "Sahi Rate")
          GuaranteeItem(icon = Icons.Default.LocalShipping, title = "Fast Delivery")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun GuaranteeItem(icon: ImageVector, title: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = SteelBlue,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = title,
      fontSize = 11.5.sp,
      fontWeight = FontWeight.SemiBold,
      color = Slate800
    )
  }
}
