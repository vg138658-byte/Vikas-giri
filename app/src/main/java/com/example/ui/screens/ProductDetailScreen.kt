package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.components.launchDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
  product: ProductEntity,
  companyPhone: String,
  onBack: () -> Unit,
  onAddToCart: (selectedType: String, quantity: Double) -> Unit,
  onOrderNow: (productName: String, selectedType: String, quantity: String) -> Unit
) {
  val context = LocalContext.current
  val types = remember(product) {
    product.availableTypes.split(",").map { it.trim() }.filter { it.isNotEmpty() }
  }

  var selectedType by remember(product) {
    mutableStateOf(types.firstOrNull() ?: product.name)
  }

  var quantity by remember { mutableStateOf(1.0) }

  val unitLabel = remember(product) {
    product.unit.replace("Per ", "")
  }

  val estimatedTotal = remember(quantity, product.rate) {
    quantity * product.rate
  }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = SleekBlue900,
            maxLines = 1
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("detail_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SleekBlue900)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      // Bottom Action Bar: ADD TO CART & ORDER NOW
      Surface(
        color = Color.White,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        modifier = Modifier.navigationBarsPadding()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Live price summary
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Total Estimate", fontSize = 11.sp, color = Slate500)
              Text(
                text = "₹ ${"%,.0f".format(estimatedTotal)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SleekBlue900
              )
            }
            Surface(
              color = SleekBlue50,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100)
            ) {
              Text(
                text = "${quantity.toInt()} $unitLabel",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SleekBlue700,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          // Two Primary Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = {
                onAddToCart(selectedType, quantity)
              },
              shape = RoundedCornerShape(24.dp),
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("detail_add_to_cart_button"),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = SleekBlue700),
              border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue600)
            ) {
              Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("ADD TO CART", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
            }

            Button(
              onClick = {
                onOrderNow(product.name, selectedType, "${quantity.toInt()} $unitLabel")
              },
              shape = RoundedCornerShape(24.dp),
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("detail_order_now_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = SleekBlue700,
                contentColor = Color.White
              )
            ) {
              Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("ORDER NOW", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Product Image / Visual Showcase Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .height(170.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  SleekBlue700,
                  SleekIndigo900
                )
              )
            )
            .padding(20.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            MaterialCategoryIcon(category = product.category, iconSize = 44)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = product.name,
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = product.category,
              color = SleekBlue100,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Product Title, Badge & Price Section
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
            Text(
              text = product.name,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Slate900
            )

            Surface(
              color = Color(0xFFDCFCE7),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = product.stockStatus,
                color = Color(0xFF15803D),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Price row
          Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "Price: ₹ ${"%,.0f".format(product.rate)}",
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold,
              color = SleekBlue900
            )
            Text(
              text = product.unit,
              fontSize = 12.5.sp,
              color = Slate500,
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))
          Divider(color = Slate100)
          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Product Description",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = Slate800
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = product.description,
            fontSize = 13.sp,
            color = Slate600,
            lineHeight = 19.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Available Size / Type Selector
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Select Available Size / Type",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Slate800
          )
          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            types.forEach { type ->
              val isSelected = selectedType == type
              Surface(
                color = if (isSelected) SleekBlue50 else Color.White,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) SleekBlue600 else Slate200
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { selectedType = type }
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                      selected = isSelected,
                      onClick = { selectedType = type },
                      colors = RadioButtonDefaults.colors(selectedColor = SleekBlue700)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = type,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 13.5.sp,
                      color = if (isSelected) SleekBlue900 else Slate800
                    )
                  }
                  if (isSelected) {
                    Text(
                      text = "Selected",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = SleekBlue700
                    )
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Quantity Selector
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Quantity ($unitLabel)",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Slate800
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${quantity.toInt()} $unitLabel",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Slate900
            )

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              FilledTonalIconButton(
                onClick = { if (quantity > 1) quantity -= 1 },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                  containerColor = SleekBlue50,
                  contentColor = SleekBlue700
                ),
                modifier = Modifier.size(38.dp)
              ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
              }

              Surface(
                color = Slate50,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
              ) {
                Text(
                  text = "${quantity.toInt()}",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = SleekBlue900,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
              }

              FilledTonalIconButton(
                onClick = { quantity += 1 },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                  containerColor = SleekBlue50,
                  contentColor = SleekBlue700
                ),
                modifier = Modifier.size(38.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Direct Contact Buttons (CALL NOW & WHATSAPP NOW as requested in prompt)
      Text(
        text = "Instant Contact with Umesh Enterprise",
        fontSize = 12.5.sp,
        fontWeight = FontWeight.Bold,
        color = Slate700
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { launchDialer(context, companyPhone) },
          colors = ButtonDefaults.buttonColors(
            containerColor = SleekBlue900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(22.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("detail_call_now_button")
        ) {
          Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("CALL NOW", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
        }

        Button(
          onClick = {
            launchWhatsApp(
              context,
              companyPhone,
              "Namaste! Mujhe Umesh Enterprise se $selectedType ($quantity $unitLabel) ki rate aur delivery chahiye."
            )
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF25D366),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(22.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("detail_whatsapp_now_button")
        ) {
          Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("WHATSAPP NOW", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
