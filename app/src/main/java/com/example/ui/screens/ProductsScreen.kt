package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.theme.*

@Composable
fun ProductsScreen(
  products: List<ProductEntity>,
  selectedCategory: String,
  onSelectCategory: (String) -> Unit,
  onProductClick: (ProductEntity) -> Unit,
  onRatePuchheClick: (ProductEntity) -> Unit,
  onOrderNowClick: (ProductEntity) -> Unit
) {
  val categories = listOf("ALL", "GITTI", "BALU", "SARIYA", "CEMENT", "EENT")

  val filteredProducts = if (selectedCategory == "ALL") {
    products
  } else {
    products.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(SleekBg)
      .testTag("products_screen_container")
  ) {
    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { category ->
        val isSelected = selectedCategory == category
        FilterChip(
          selected = isSelected,
          onClick = { onSelectCategory(category) },
          label = {
            Text(
              text = if (category == "ALL") "All Materials" else category,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 12.sp
            )
          },
          shape = RoundedCornerShape(20.dp),
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SleekBlue700,
            selectedLabelColor = Color.White,
            containerColor = SleekBlue50,
            labelColor = SleekBlue900
          ),
          border = FilterChipDefaults.filterChipBorder(
            borderColor = if (isSelected) SleekBlue700 else SleekBlue100,
            enabled = true,
            selected = isSelected
          ),
          modifier = Modifier.testTag("filter_chip_${category.lowercase()}")
        )
      }
    }

    Divider(color = Slate100, thickness = 1.dp)

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(filteredProducts) { product ->
        ProductCard(
          product = product,
          onClick = { onProductClick(product) },
          onRatePuchhe = { onRatePuchheClick(product) },
          onOrderNow = { onOrderNowClick(product) }
        )
      }

      if (filteredProducts.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(48.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No products found in this category.",
              color = Slate600,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProductCard(
  product: ProductEntity,
  onClick: () -> Unit,
  onRatePuchhe: () -> Unit,
  onOrderNow: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("product_card_${product.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MaterialCategoryIcon(category = product.category, iconSize = 22)
          Column {
            Text(
              text = product.name,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Slate900
            )
            Text(
              text = product.category,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              color = SleekBlue600
            )
          }
        }

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

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = product.description,
        fontSize = 12.5.sp,
        color = Slate600,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Available Types Chips
      Text(
        text = "Available Types / Sizes:",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate500
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        product.availableTypes.split(",").map { it.trim() }.forEach { typeName ->
          Surface(
            color = Slate50,
            shape = RoundedCornerShape(6.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
          ) {
            Text(
              text = typeName,
              fontSize = 11.sp,
              color = Slate700,
              fontWeight = FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Price Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Rate / Price", fontSize = 10.5.sp, color = Slate400)
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "₹ ${"%,.0f".format(product.rate)}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = SleekBlue900
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = product.unit,
              fontSize = 11.5.sp,
              color = Slate500
            )
          }
        }

        Surface(
          color = SleekBlue50,
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100)
        ) {
          Text(
            text = product.highlightBadge,
            color = SleekBlue700,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Buttons: RATE PUCHHE and ORDER NOW (as requested in prompt)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onRatePuchhe,
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("btn_rate_puchhe_${product.id}"),
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = SleekBlue700),
          border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue600)
        ) {
          Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "RATE PUCHHE", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }

        Button(
          onClick = onOrderNow,
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("btn_order_now_${product.id}"),
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = SleekBlue700,
            contentColor = Color.White
          )
        ) {
          Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "ORDER NOW", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
      }
    }
  }
}
