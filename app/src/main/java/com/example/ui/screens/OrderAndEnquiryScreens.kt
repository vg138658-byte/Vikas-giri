package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.CustomerUser
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(
  user: CustomerUser,
  products: List<ProductEntity>,
  initialProduct: String,
  initialType: String,
  onBack: () -> Unit,
  onPlaceOrder: (
    name: String,
    mobile: String,
    address: String,
    product: String,
    materialType: String,
    quantity: String,
    deliveryDate: String,
    message: String
  ) -> Unit
) {
  var fullName by remember { mutableStateOf(user.name) }
  var mobileNumber by remember { mutableStateOf(user.mobile) }
  var deliveryAddress by remember { mutableStateOf(user.address) }

  val productOptions = remember(products) {
    if (products.isNotEmpty()) {
      products.map { it.name }
    } else {
      listOf("GITTI (Stone Aggregate)", "BALU (Construction Sand)", "SARIYA (TMT Steel Rebar)", "CEMENT (Quality Brands)", "EENT / BRICKS")
    }
  }

  var selectedProduct by remember {
    mutableStateOf(if (initialProduct.isNotEmpty()) initialProduct else productOptions.firstOrNull() ?: "")
  }

  var isProductDropdownExpanded by remember { mutableStateOf(false) }

  // Available types for the selected product
  val currentMatchedProduct = products.find { it.name.equals(selectedProduct, ignoreCase = true) || selectedProduct.contains(it.category, ignoreCase = true) }
  val typeOptions = remember(currentMatchedProduct) {
    currentMatchedProduct?.availableTypes?.split(",")?.map { it.trim() }
      ?: listOf("10 MM", "20 MM", "40 MM", "Standard Type")
  }

  var selectedMaterialType by remember {
    mutableStateOf(if (initialType.isNotEmpty()) initialType else typeOptions.firstOrNull() ?: "")
  }
  var isTypeDropdownExpanded by remember { mutableStateOf(false) }

  var quantity by remember { mutableStateOf("10 Ton") }
  var deliveryDate by remember { mutableStateOf("Immediate (Within 24 Hrs)") }
  var additionalMessage by remember { mutableStateOf("") }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "PLACE MATERIAL ORDER",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = SleekBlue900
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("order_back_button")
          ) {
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
      // Intro notice
      Surface(
        color = SleekBlue50,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Verified, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Direct booking from Umesh Enterprise. Best rate & fast delivery guaranteed.",
            fontSize = 12.sp,
            color = SleekBlue900
          )
        }
      }

      // Customer Details Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Customer & Delivery Details",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Slate800
          )

          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_full_name_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SleekBlue700) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_mobile_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = deliveryAddress,
            onValueChange = { deliveryAddress = it },
            label = { Text("Delivery Address (Site Location)") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_address_input"),
            maxLines = 3
          )
        }
      }

      // Material Selection Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Select Construction Material",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Slate800
          )

          // Select Product Dropdown
          ExposedDropdownMenuBox(
            expanded = isProductDropdownExpanded,
            onExpandedChange = { isProductDropdownExpanded = !isProductDropdownExpanded }
          ) {
            OutlinedTextField(
              value = selectedProduct,
              onValueChange = {},
              readOnly = true,
              label = { Text("Select Product") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProductDropdownExpanded) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .testTag("order_product_dropdown")
            )
            ExposedDropdownMenu(
              expanded = isProductDropdownExpanded,
              onDismissRequest = { isProductDropdownExpanded = false }
            ) {
              productOptions.forEach { productTitle ->
                DropdownMenuItem(
                  text = { Text(productTitle) },
                  onClick = {
                    selectedProduct = productTitle
                    isProductDropdownExpanded = false
                  }
                )
              }
            }
          }

          // Select Material Type Dropdown
          ExposedDropdownMenuBox(
            expanded = isTypeDropdownExpanded,
            onExpandedChange = { isTypeDropdownExpanded = !isTypeDropdownExpanded }
          ) {
            OutlinedTextField(
              value = selectedMaterialType,
              onValueChange = {},
              readOnly = true,
              label = { Text("Select Material Type / Size") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeDropdownExpanded) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .testTag("order_type_dropdown")
            )
            ExposedDropdownMenu(
              expanded = isTypeDropdownExpanded,
              onDismissRequest = { isTypeDropdownExpanded = false }
            ) {
              typeOptions.forEach { typeName ->
                DropdownMenuItem(
                  text = { Text(typeName) },
                  onClick = {
                    selectedMaterialType = typeName
                    isTypeDropdownExpanded = false
                  }
                )
              }
            }
          }

          // Quantity Field
          OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (e.g. 10 Ton / 1 Truck / 50 Bags)") },
            leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_quantity_input"),
            singleLine = true
          )

          // Delivery Date Field
          OutlinedTextField(
            value = deliveryDate,
            onValueChange = { deliveryDate = it },
            label = { Text("Delivery Date / Timeline") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_date_input"),
            singleLine = true
          )

          // Additional Message
          OutlinedTextField(
            value = additionalMessage,
            onValueChange = { additionalMessage = it },
            label = { Text("Additional Message / Instructions (Optional)") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_message_input"),
            maxLines = 3
          )
        }
      }

      // Submit Button
      Button(
        onClick = {
          onPlaceOrder(
            fullName.ifEmpty { "Customer" },
            mobileNumber.ifEmpty { "+91" },
            deliveryAddress.ifEmpty { "Site address" },
            selectedProduct,
            selectedMaterialType,
            quantity.ifEmpty { "1 Unit" },
            deliveryDate.ifEmpty { "As soon as possible" },
            additionalMessage
          )
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = SleekBlue700,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_submit_place_order")
      ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "PLACE ORDER",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialEnquiryScreen(
  user: CustomerUser,
  initialMaterial: String,
  onBack: () -> Unit,
  onSubmitEnquiry: (
    name: String,
    mobile: String,
    material: String,
    quantity: String,
    message: String
  ) -> Unit
) {
  var customerName by remember { mutableStateOf(user.name) }
  var mobileNumber by remember { mutableStateOf(user.mobile) }

  val materialList = listOf(
    "GITTI (Stone Aggregate)",
    "BALU (Construction Sand)",
    "SARIYA (TMT Steel Rebar)",
    "CEMENT (Quality Brands)",
    "EENT / BRICKS",
    "Stone Dust & Chips",
    "General Construction Enquiry"
  )

  var selectedMaterial by remember {
    mutableStateOf(if (initialMaterial.isNotEmpty()) initialMaterial else materialList.first())
  }
  var isMaterialDropdownExpanded by remember { mutableStateOf(false) }

  var quantity by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("") }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "MATERIAL ENQUIRY",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = SleekBlue900
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("enquiry_back_button")
          ) {
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
      Surface(
        color = SleekBlue50,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.HelpOutline, contentDescription = null, tint = SleekBlue700, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Customer kisi bhi material ke rate ya information ke liye enquiry bhej sakega.",
            fontSize = 12.sp,
            color = SleekBlue900
          )
        }
      }

      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enquiry_name_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SleekBlue700) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enquiry_mobile_input"),
            singleLine = true
          )

          // Select Material Dropdown
          ExposedDropdownMenuBox(
            expanded = isMaterialDropdownExpanded,
            onExpandedChange = { isMaterialDropdownExpanded = !isMaterialDropdownExpanded }
          ) {
            OutlinedTextField(
              value = selectedMaterial,
              onValueChange = {},
              readOnly = true,
              label = { Text("Select Material") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMaterialDropdownExpanded) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .testTag("enquiry_material_dropdown")
            )
            ExposedDropdownMenu(
              expanded = isMaterialDropdownExpanded,
              onDismissRequest = { isMaterialDropdownExpanded = false }
            ) {
              materialList.forEach { mat ->
                DropdownMenuItem(
                  text = { Text(mat) },
                  onClick = {
                    selectedMaterial = mat
                    isMaterialDropdownExpanded = false
                  }
                )
              }
            }
          }

          OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (Approx)") },
            leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null, tint = SleekBlue700) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enquiry_quantity_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message / Rate Query") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SleekBlue600,
              unfocusedBorderColor = Slate200,
              focusedLabelColor = SleekBlue700
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enquiry_message_input"),
            minLines = 3,
            maxLines = 5
          )
        }
      }

      Button(
        onClick = {
          onSubmitEnquiry(
            customerName.ifEmpty { "Customer" },
            mobileNumber.ifEmpty { "+91" },
            selectedMaterial,
            quantity.ifEmpty { "1 Unit" },
            message.ifEmpty { "Material rate query" }
          )
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = SleekBlue700,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_submit_send_enquiry")
      ) {
        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "SEND ENQUIRY",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
      }
    }
  }
}
