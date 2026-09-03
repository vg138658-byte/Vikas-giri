package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.data.DailyRateEntity
import com.example.data.ProductEntity
import com.example.ui.components.launchDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.*

data class HomeCategoryItem(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val bgColor: Color,
  val iconColor: Color,
  val categoryKey: String
)

@Composable
fun HomeScreen(
  dailyRates: List<DailyRateEntity>,
  featuredProducts: List<ProductEntity>,
  onCategoryClick: (String) -> Unit,
  onCheckRatesClick: () -> Unit,
  onOrderNowClick: (String) -> Unit,
  onAskRateClick: (String) -> Unit,
  onOpenDeliveryClick: () -> Unit,
  onOpenAboutClick: () -> Unit,
  onOpenContactClick: () -> Unit,
  onOpenAdminClick: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(SleekBg)
      .testTag("home_screen_list"),
    contentPadding = PaddingValues(bottom = 88.dp)
  ) {
    // Top Hero Banner - Sleek Interface gradient card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  SleekBlue700,
                  SleekIndigo900
                )
              )
            )
            .padding(20.dp)
        ) {
          Column {
            // Greetings pill
            Surface(
              color = Color(0x25FFFFFF),
              shape = RoundedCornerShape(20.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x40FFFFFF))
            ) {
              Text(
                text = "Namaste! Swagat Hai.",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Ab ghar baithe mangwayein Quality Construction Materials.",
              color = Color.White,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Quality Material • Sahi Rate • Time Par Delivery",
              color = SleekBlue100,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick CTA Buttons in Banner
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { onOrderNowClick("") },
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color.White,
                  contentColor = SleekBlue900
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(40.dp)
                  .testTag("home_direct_order_button")
              ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ORDER NOW", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
              }

              OutlinedButton(
                onClick = { onAskRateClick("") },
                colors = ButtonDefaults.outlinedButtonColors(
                  contentColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(40.dp)
                  .testTag("home_rate_puchhe_button")
              ) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("RATE PUCHHE", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
              }
            }
          }
        }
      }
    }

    // Section Header: Categories
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Hamari Categories",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Slate900
          )
          Text(
            text = "Construction Materials & Services",
            fontSize = 11.5.sp,
            color = Slate500
          )
        }

        TextButton(
          onClick = { onCategoryClick("") },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
        ) {
          Text("View All", fontSize = 12.sp, color = SleekBlue600, fontWeight = FontWeight.SemiBold)
          Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp), tint = SleekBlue600)
        }
      }
    }

    // Categories Grid (8 items with sleek pastel badge styling)
    item {
      val categories = listOf(
        HomeCategoryItem("Gitti", "10mm, 20mm, 40mm", Icons.Default.Terrain, Color(0xFFFFEDD5), Color(0xFFEA580C), "GITTI"),
        HomeCategoryItem("Balu", "Nadi, Local, Plaster", Icons.Default.Grain, Color(0xFFFEF3C7), Color(0xFFD97706), "BALU"),
        HomeCategoryItem("Sariya", "Fe 550D TMT 6-25mm", Icons.Default.ViewWeek, Color(0xFFF1F5F9), Color(0xFF475569), "SARIYA"),
        HomeCategoryItem("Cement", "UltraTech, Ambuja", Icons.Default.Architecture, Color(0xFFEFF6FF), Color(0xFF2563EB), "CEMENT"),
        HomeCategoryItem("Eent", "1 No. Red & Fly Ash", Icons.Default.Square, Color(0xFFFFE4E6), Color(0xFFE11D48), "EENT"),
        HomeCategoryItem("Home Delivery", "Site Doorstep Supply", Icons.Default.LocalShipping, Color(0xFFDCFCE7), Color(0xFF16A34A), "DELIVERY"),
        HomeCategoryItem("About Us", "Trust & Experience", Icons.Default.Info, Color(0xFFF3E8FF), Color(0xFF7C3AED), "ABOUT"),
        HomeCategoryItem("Contact Us", "Call & WhatsApp", Icons.Default.ContactPhone, Color(0xFFE0F2FE), Color(0xFF0284C7), "CONTACT")
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        for (i in categories.indices step 2) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            CategoryCard(
              item = categories[i],
              modifier = Modifier.weight(1f),
              onClick = {
                when (categories[i].categoryKey) {
                  "DELIVERY" -> onOpenDeliveryClick()
                  "ABOUT" -> onOpenAboutClick()
                  "CONTACT" -> onOpenContactClick()
                  else -> onCategoryClick(categories[i].categoryKey)
                }
              }
            )

            if (i + 1 < categories.size) {
              CategoryCard(
                item = categories[i + 1],
                modifier = Modifier.weight(1f),
                onClick = {
                  when (categories[i + 1].categoryKey) {
                    "DELIVERY" -> onOpenDeliveryClick()
                    "ABOUT" -> onOpenAboutClick()
                    "CONTACT" -> onOpenContactClick()
                    else -> onCategoryClick(categories[i + 1].categoryKey)
                  }
                }
              )
            }
          }
        }
      }
    }

    // Aaj Ka Bazar Rate Snapshot Card
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .testTag("home_rate_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Card Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Slate50)
              .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(SleekBlue600)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "AAJ KA BAZAR RATE",
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                color = Slate600,
                letterSpacing = 0.5.sp
              )
            }

            Surface(
              color = Color(0xFFDCFCE7),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "Live Updated",
                color = Color(0xFF16A34A),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Divider(color = Slate100, thickness = 1.dp)

          // Rate rows with colored dots
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val sampleRates = if (dailyRates.isNotEmpty()) dailyRates.take(4) else emptyList()
            val dotColors = listOf(
              Color(0xFF3B82F6),
              Color(0xFFF59E0B),
              Color(0xFF64748B),
              Color(0xFFEF4444)
            )

            sampleRates.forEachIndexed { index, rate ->
              Surface(
                color = Slate50,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColors.getOrElse(index) { SleekBlue500 })
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = rate.productName,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Slate800
                    )
                  }
                  Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                      text = rate.rateDisplay,
                      fontSize = 13.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = Slate900
                    )
                    Text(
                      text = " /${rate.unit}",
                      fontSize = 10.5.sp,
                      color = Slate400,
                      modifier = Modifier.padding(start = 2.dp, bottom = 1.dp)
                    )
                  }
                }
              }
            }
          }

          Divider(color = Slate100, thickness = 1.dp)

          // Bottom Check Rates Action
          Box(modifier = Modifier.padding(12.dp)) {
            Button(
              onClick = onCheckRatesClick,
              colors = ButtonDefaults.buttonColors(
                containerColor = Slate100,
                contentColor = SleekBlue700
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("CHECK ALL RATES", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 0.5.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }

    // Owner Details & Contact Card
    item {
      Spacer(modifier = Modifier.height(6.dp))
      val context = LocalContext.current
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
        shadowElevation = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(SleekBlue50),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = SleekBlue700,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Owner: Amit Giri",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = SleekBlue900
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFDCFCE7)
                  ) {
                    Text(
                      text = "Proprietor",
                      color = Color(0xFF16A34A),
                      fontSize = 9.5.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                  }
                }
                Text(
                  text = "Khargsenpatti Mathetu, Bhadohi - 221404",
                  fontSize = 11.5.sp,
                  color = Slate500
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Divider(color = Slate100)
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { launchDialer(context, "9616410193") },
              colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
              shape = RoundedCornerShape(20.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .testTag("home_owner_call_btn")
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("9616410193", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { launchWhatsApp(context, "9616410193") },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
              shape = RoundedCornerShape(20.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .testTag("home_owner_whatsapp_btn")
            ) {
              Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Owner / Admin Quick Access card
    item {
      Spacer(modifier = Modifier.height(6.dp))
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        shadowElevation = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clickable { onOpenAdminClick() }
          .testTag("home_admin_panel_card")
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlue50),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = SleekBlue700,
                modifier = Modifier.size(22.dp)
              )
            }
            Column {
              Text(
                text = "Owner / Admin Panel",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Slate900
              )
              Text(
                text = "Manage daily rates, products & customer orders",
                fontSize = 11.sp,
                color = Slate500
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun CategoryCard(
  item: HomeCategoryItem,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("category_card_${item.title.lowercase().replace(" ", "_")}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(item.bgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = item.icon,
          contentDescription = item.title,
          tint = item.iconColor,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Text(
          text = item.title,
          fontWeight = FontWeight.Bold,
          fontSize = 13.5.sp,
          color = Slate800
        )
        Text(
          text = item.subtitle,
          fontSize = 10.5.sp,
          color = Slate500,
          maxLines = 1
        )
      }
    }
  }
}
