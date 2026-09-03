package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DailyRateEntity
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.theme.*

@Composable
fun RatesScreen(
  rates: List<DailyRateEntity>,
  onLatestRatePuchhe: (String) -> Unit,
  onOrderProduct: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(SleekBg)
      .testTag("rates_screen_list"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(SleekBlue700, SleekIndigo900)
              )
            )
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Surface(
                color = Color(0x25FFFFFF),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x40FFFFFF))
              ) {
                Text(
                  text = "Live Market Rates",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "TODAY'S RATE LIST",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Daily Construction Material Prices",
                color = SleekBlue100,
                fontSize = 12.sp
              )
            }

            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CurrencyRupee,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        }
      }
    }

    // Rate items
    items(rates) { rate ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("rate_item_${rate.productName.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              MaterialCategoryIcon(category = rate.productName, iconSize = 22)
              Column {
                Text(
                  text = rate.productName,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Slate800
                )
                Text(
                  text = "Unit: ${rate.unit}",
                  fontSize = 11.5.sp,
                  color = Slate500
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = rate.rateDisplay,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SleekBlue900
              )
              Text(
                text = rate.unit,
                fontSize = 11.sp,
                color = Slate400
              )
            }
          }

          Divider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = Slate400,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = rate.lastUpdated,
                fontSize = 11.sp,
                color = Slate500
              )
            }

            TextButton(
              onClick = { onOrderProduct(rate.productName) },
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
              Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(14.dp), tint = SleekBlue700)
              Spacer(modifier = Modifier.width(4.dp))
              Text("Order This", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SleekBlue700)
            }
          }
        }
      }
    }

    // Market Note & Master CTA Button
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SleekBlue50),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = SleekBlue700,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Important Note:",
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp,
              color = SleekBlue700
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Market ke according rates change ho sakte hain. Wholesale ya bulk booking ke liye special discount milta hai.",
            fontSize = 12.5.sp,
            color = Slate700,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = { onLatestRatePuchhe("") },
            colors = ButtonDefaults.buttonColors(
              containerColor = SleekBlue700,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("rates_latest_rate_puchhe_button")
          ) {
            Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "LATEST RATE PUCHHE",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(72.dp))
    }
  }
}
