package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

fun launchDialer(context: Context, phone: String) {
  try {
    val cleanPhone = phone.replace(" ", "").replace("-", "")
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
  }
}

fun launchWhatsApp(context: Context, phone: String, message: String = "Namaste Amit ji (Umesh Enterprise)! Mujhe construction material ki jaankari chahiye.") {
  try {
    val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
    val fullPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
    val encoded = Uri.encode(message)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$fullPhone&text=$encoded"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "WhatsApp not installed. Calling phone instead.", Toast.LENGTH_SHORT).show()
    launchDialer(context, phone)
  }
}

fun launchEmail(context: Context, email: String, subject: String = "Construction Material Enquiry - Umesh Enterprise") {
  try {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
      putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open email app.", Toast.LENGTH_SHORT).show()
  }
}

fun launchLocation(context: Context, address: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(address)}"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open map app.", Toast.LENGTH_SHORT).show()
  }
}

@Composable
fun MaterialCategoryIcon(category: String, modifier: Modifier = Modifier, iconSize: Int = 24) {
  val (icon, bgColor, tintColor) = when (category.uppercase()) {
    "GITTI" -> Triple(Icons.Default.Terrain, SleekBlue50, SleekBlue700)
    "BALU" -> Triple(Icons.Default.Grain, Color(0xFFFEF3C7), Color(0xFFD97706))
    "SARIYA" -> Triple(Icons.Default.ViewWeek, SleekBlue100, SleekBlue800)
    "CEMENT" -> Triple(Icons.Default.Architecture, Slate100, SleekBlue900)
    "EENT" -> Triple(Icons.Default.Square, Color(0xFFFFEDD5), Color(0xFFEA580C))
    else -> Triple(Icons.Default.HomeRepairService, SleekBlue50, SleekBlue700)
  }

  Box(
    modifier = modifier
      .size((iconSize + 16).dp)
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = category,
      tint = tintColor,
      modifier = Modifier.size(iconSize.dp)
    )
  }
}

@Composable
fun StatusBadge(status: String) {
  val (bg, text) = when (status.lowercase()) {
    "pending", "new" -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
    "accepted", "contacted" -> Pair(SleekBlue50, SleekBlue700)
    "out for delivery" -> Pair(Color(0xFFF3E8FF), Color(0xFF7E22CE))
    "delivered", "resolved" -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
    "rejected", "cancelled" -> Pair(Color(0xFFFEE2E2), Color(0xFFB91C1C))
    else -> Pair(Slate100, Slate700)
  }

  Surface(
    color = bg,
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
  ) {
    Text(
      text = status,
      color = text,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Composable
fun TopBrandHeader(
  onCallClick: () -> Unit,
  onWhatsAppClick: () -> Unit,
  onAdminClick: () -> Unit,
  cartCount: Int = 0,
  onCartClick: () -> Unit
) {
  Surface(
    color = Color.White,
    tonalElevation = 1.dp,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SleekBlue900),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Construction,
            contentDescription = "Logo",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
        Column {
          Text(
            text = "UMESH ENTERPRISE",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = SleekBlue900,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "Aapke Har Nirman Ka Bharosemand Saathi",
            fontSize = 10.5.sp,
            color = SleekBlue700,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // WhatsApp button
        IconButton(
          onClick = onWhatsAppClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("header_whatsapp_button")
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "WhatsApp",
            tint = Color(0xFF25D366),
            modifier = Modifier.size(20.dp)
          )
        }

        // Call button
        IconButton(
          onClick = onCallClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("header_call_button")
        ) {
          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "Call",
            tint = SleekBlue700,
            modifier = Modifier.size(20.dp)
          )
        }

        // Admin Icon
        IconButton(
          onClick = onAdminClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("header_admin_button")
        ) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = "Admin Panel",
            tint = Slate500,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
