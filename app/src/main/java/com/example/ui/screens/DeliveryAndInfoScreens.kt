package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CompanyInfoEntity
import com.example.ui.components.launchDialer
import com.example.ui.components.launchEmail
import com.example.ui.components.launchLocation
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDeliveryScreen(
  companyInfo: CompanyInfoEntity?,
  onBack: () -> Unit,
  onOrderNow: () -> Unit
) {
  var addressInput by remember { mutableStateOf("") }
  var checkResult by remember { mutableStateOf<String?>(null) }
  var isEligible by remember { mutableStateOf(false) }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Text("HOME DELIVERY SERVICE", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SleekBlue900)
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("delivery_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SleekBlue900)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Hero Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SleekBlue900),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(SleekBlue900, SleekBlue800)
              )
            )
            .padding(20.dp)
        ) {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Color(0x3338BDF8)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.LocalShipping,
                  contentDescription = null,
                  tint = SleekBlue300,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Text(
                  text = "UMESH ENTERPRISE",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
                Text(
                  text = "Doorstep Site Supply Fleet",
                  color = SleekBlue200,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "UMESH ENTERPRISE selected areas mein construction materials ki home delivery bhi provide karega.",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 13.5.sp,
              lineHeight = 20.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Available Materials for Delivery
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Delivery ke liye available materials:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = SleekBlue900
          )

          Spacer(modifier = Modifier.height(10.dp))

          val materials = listOf(
            "Gitti (10mm, 20mm, 40mm) by Tipper / Truck",
            "Balu (Nadi & Local sand) by Dala / Truck",
            "Sariya (TMT Rebars bundle) safe tied transport",
            "Cement (Bags) covered truck dispatch",
            "Eent (Bricks) unloading at your site",
            "Aur anya building materials"
          )

          materials.forEach { item ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item,
                fontSize = 13.sp,
                color = Slate800
              )
            }
          }
        }
      }

      // Address & Pincode Checker Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Check Delivery In Your Area",
            fontWeight = FontWeight.Bold,
            fontSize = 14.5.sp,
            color = SleekBlue900
          )

          Text(
            text = "Customer apna address ya pincode enter karke delivery enquiry kar sakega.",
            fontSize = 12.sp,
            color = Slate500
          )

          OutlinedTextField(
            value = addressInput,
            onValueChange = {
              addressInput = it
              checkResult = null
            },
            label = { Text("Enter Site Address or Pincode") },
            leadingIcon = { Icon(Icons.Default.LocationSearching, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("delivery_address_input")
          )

          Button(
            onClick = {
              val p = addressInput.trim()
              if (p.isNotEmpty()) {
                isEligible = true
                checkResult = "Haan! Umesh Enterprise aapke area ($p) mein full truck & tractor trolley delivery provide karta hai. Estimated delivery time: 2 se 6 ghante ke andar."
              } else {
                checkResult = "Kripya apna address ya pincode enter karein."
              }
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = SleekBlue700,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("btn_check_delivery")
          ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("CHECK DELIVERY", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
          }

          if (checkResult != null) {
            Surface(
              color = if (isEligible) Color(0xFFECFDF5) else Color(0xFFFEF3C7),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isEligible) Color(0xFFA7F3D0) else Color(0xFFFDE68A)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (isEligible) Icons.Default.CheckCircle else Icons.Default.Info,
                    contentDescription = null,
                    tint = if (isEligible) Color(0xFF059669) else Color(0xFFD97706),
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isEligible) "Delivery Available!" else "Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isEligible) Color(0xFF059669) else Color(0xFFD97706)
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = checkResult ?: "",
                  fontSize = 12.5.sp,
                  color = Slate800,
                  lineHeight = 18.sp
                )

                if (isEligible) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Button(
                    onClick = onOrderNow,
                    colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                  ) {
                    Text("Order for Delivery Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(onBack: () -> Unit) {
  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = { Text("ABOUT US", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SleekBlue900) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("about_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SleekBlue900)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Brand Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SleekBlue900),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(SleekBlue900, SleekBlue800)
              )
            )
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(Color(0x3338BDF8)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Construction, contentDescription = null, tint = SleekBlue300, modifier = Modifier.size(36.dp))
          }
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "UMESH ENTERPRISE",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 1.sp
          )
          Text(
            text = "Aapke Har Nirman Ka Bharosemand Saathi",
            color = SleekBlue200,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // About Text Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Hamara Parichay",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SleekBlue900
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Umesh Enterprise ek trusted building materials supplier hai. Hum construction ke liye zaroori materials jaise Gitti, Balu, Sariya, Cement, Eent aur anya building materials achhi quality aur reasonable price par provide karte hain.",
            fontSize = 13.5.sp,
            color = Slate700,
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Hamari koshish hai ki har customer ko construction material ek hi jagah par aasani se available ho.",
            fontSize = 13.5.sp,
            color = Slate600,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Owner & Business Details Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Owner & Business Information",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SleekBlue900
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SleekBlue50),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(text = "Proprietor / Owner", fontSize = 11.5.sp, color = Slate500)
              Text(text = "Amit Giri", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SleekBlue900)
            }
          }

          Divider(color = Slate100)

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SleekBlue50),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(text = "Mobile & WhatsApp", fontSize = 11.5.sp, color = Slate500)
              Text(text = "+91 96164 10193", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekBlue900)
            }
          }

          Divider(color = Slate100)

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SleekBlue50),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Place, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(text = "Address & Depot Location", fontSize = 11.5.sp, color = Slate500)
              Text(text = "Khargsenpatti Mathetu, Bhadohi - 221404", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp, color = SleekBlue900)
            }
          }
        }
      }

      // Priorities Section
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Hamari Priority Hai:",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SleekBlue900
          )

          Spacer(modifier = Modifier.height(12.dp))

          val priorities = listOf(
            Pair("Achhi Quality", "Strict testing standard aggregate & steel"),
            Pair("Sahi Rate", "Transparent mandi rates with zero hidden charges"),
            Pair("Time Par Delivery", "Prompt truck dispatch to keep site workers on schedule"),
            Pair("Customer Satisfaction", "Long term relations with home owners & contractors"),
            Pair("Bharosemand Service", "Accurate weight measurement & certified brands")
          )

          priorities.forEach { (title, desc) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(SleekBlue50),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(16.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekBlue900)
                Text(text = desc, fontSize = 11.5.sp, color = Slate500)
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(
  companyInfo: CompanyInfoEntity?,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val owner = companyInfo?.ownerName ?: "Amit Giri"
  val phone = companyInfo?.phone ?: "+91 96164 10193"
  val whatsapp = companyInfo?.whatsapp ?: "+91 96164 10193"
  val email = companyInfo?.email ?: "umeshenterprise@email.com"
  val address = companyInfo?.address ?: "Khargsenpatti Mathetu, Bhadohi - 221404"

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = { Text("CONTACT US", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SleekBlue900) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("contact_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SleekBlue900)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Brand Header Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SleekBlue900),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(SleekBlue900, SleekBlue800)
              )
            )
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "UMESH ENTERPRISE",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Aapke Har Nirman Ka Bharosemand Saathi",
            color = SleekBlue200,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Contact Details Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          ContactRowItem(
            icon = Icons.Default.Person,
            label = "Owner / Proprietor",
            value = owner,
            iconTint = SleekBlue700
          )

          Divider(color = Slate100)

          ContactRowItem(
            icon = Icons.Default.Phone,
            label = "Mobile Number",
            value = phone,
            iconTint = SleekBlue700
          )

          Divider(color = Slate100)

          ContactRowItem(
            icon = Icons.Default.Chat,
            label = "WhatsApp Number",
            value = whatsapp,
            iconTint = Color(0xFF25D366)
          )

          Divider(color = Slate100)

          ContactRowItem(
            icon = Icons.Default.Place,
            label = "Shop Address",
            value = address,
            iconTint = SleekBlue600
          )

          Divider(color = Slate100)

          ContactRowItem(
            icon = Icons.Default.Email,
            label = "Email",
            value = email,
            iconTint = SleekIndigo
          )
        }
      }

      // Action Buttons
      Text(
        text = "Quick Actions",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = SleekBlue900
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { launchDialer(context, phone) },
          colors = ButtonDefaults.buttonColors(containerColor = SleekBlue900, contentColor = Color.White),
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("contact_call_now_button")
        ) {
          Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("CALL NOW", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
        }

        Button(
          onClick = { launchWhatsApp(context, whatsapp) },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("contact_whatsapp_now_button")
        ) {
          Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("WHATSAPP", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = { launchEmail(context, email) },
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("contact_email_us_button"),
          border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue200)
        ) {
          Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = SleekBlue700)
          Spacer(modifier = Modifier.width(6.dp))
          Text("EMAIL US", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = SleekBlue700)
        }

        OutlinedButton(
          onClick = { launchLocation(context, address) },
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("contact_get_location_button"),
          border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue200)
        ) {
          Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp), tint = SleekBlue700)
          Spacer(modifier = Modifier.width(6.dp))
          Text("GET LOCATION", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = SleekBlue700)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun ContactRowItem(
  icon: ImageVector,
  label: String,
  value: String,
  iconTint: Color
) {
  Row(
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(iconTint.copy(alpha = 0.12f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
    }
    Column {
      Text(text = label, fontSize = 11.5.sp, color = Slate500, fontWeight = FontWeight.Medium)
      Text(text = value, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = SleekBlue900)
    }
  }
}
