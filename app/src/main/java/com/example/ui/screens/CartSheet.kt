package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItemEntity
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
  cartItems: List<CartItemEntity>,
  onBack: () -> Unit,
  onRemoveItem: (Long) -> Unit,
  onCheckout: () -> Unit
) {
  val totalCost = cartItems.sumOf { it.pricePerUnit * it.quantity }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = { Text("My Material Cart (${cartItems.size})", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SleekBlue900) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("cart_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SleekBlue900)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    bottomBar = {
      if (cartItems.isNotEmpty()) {
        Surface(
          color = Color.White,
          tonalElevation = 1.dp,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
          modifier = Modifier.navigationBarsPadding()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Total Estimated Amount:", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Slate500)
              Text(
                text = "₹ ${"%,.0f".format(totalCost)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SleekBlue900
              )
            }

            Button(
              onClick = onCheckout,
              colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
              shape = RoundedCornerShape(24.dp),
              modifier = Modifier.fillMaxWidth().height(46.dp).testTag("cart_checkout_button")
            ) {
              Text("PROCEED TO ORDER", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }
          }
        }
      }
    }
  ) { innerPadding ->
    if (cartItems.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = SleekBlue300, modifier = Modifier.size(56.dp))
          Spacer(modifier = Modifier.height(14.dp))
          Text("Cart is Empty", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SleekBlue900)
          Spacer(modifier = Modifier.height(6.dp))
          Text("Aapne cart mein abhi tak koi material add nahi kiya hai.", fontSize = 13.sp, color = Slate500)
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(cartItems) { item ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                MaterialCategoryIcon(category = item.productName, iconSize = 20)
                Column {
                  Text(text = item.productName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate800)
                  Text(text = item.selectedType, fontSize = 12.sp, color = SleekBlue700, fontWeight = FontWeight.Medium)
                  Text(
                    text = "${item.quantity.toInt()} ${item.unit} x ₹${item.pricePerUnit.toInt()}",
                    fontSize = 11.5.sp,
                    color = Slate500
                  )
                }
              }

              IconButton(onClick = { onRemoveItem(item.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444))
              }
            }
          }
        }
      }
    }
  }
}
