package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.*
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

enum class AdminTab {
  RATES,
  PRODUCTS,
  ORDERS,
  ENQUIRIES,
  COMPANY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
  dailyRates: List<DailyRateEntity>,
  products: List<ProductEntity>,
  orders: List<OrderEntity>,
  enquiries: List<EnquiryEntity>,
  companyInfo: CompanyInfoEntity?,
  onBack: () -> Unit,
  onUpdateRate: (DailyRateEntity) -> Unit,
  onAddOrUpdateProduct: (ProductEntity) -> Unit,
  onDeleteProduct: (Int) -> Unit,
  onUpdateOrderStatus: (orderId: Long, status: String) -> Unit,
  onUpdateEnquiryStatus: (enquiryId: Long, status: String) -> Unit,
  onUpdateCompanyInfo: (CompanyInfoEntity) -> Unit
) {
  var currentAdminTab by remember { mutableStateOf(AdminTab.RATES) }

  // Rate Edit Dialog state
  var editingRate by remember { mutableStateOf<DailyRateEntity?>(null) }
  var newRateDisplay by remember { mutableStateOf("") }

  // Product Add/Edit Dialog state
  var showProductDialog by remember { mutableStateOf(false) }
  var editProductId by remember { mutableStateOf(0) }
  var prodCategory by remember { mutableStateOf("GITTI") }
  var prodName by remember { mutableStateOf("") }
  var prodDesc by remember { mutableStateOf("") }
  var prodTypes by remember { mutableStateOf("") }
  var prodRate by remember { mutableStateOf("") }
  var prodUnit by remember { mutableStateOf("Per Ton") }

  // Company Contact Update Dialog state
  var showCompanyDialog by remember { mutableStateOf(false) }
  var compOwner by remember(companyInfo) { mutableStateOf(companyInfo?.ownerName ?: "Amit Giri") }
  var compPhone by remember(companyInfo) { mutableStateOf(companyInfo?.phone ?: "+91 96164 10193") }
  var compWhatsapp by remember(companyInfo) { mutableStateOf(companyInfo?.whatsapp ?: "+91 96164 10193") }
  var compEmail by remember(companyInfo) { mutableStateOf(companyInfo?.email ?: "umeshenterprise@email.com") }
  var compAddress by remember(companyInfo) { mutableStateOf(companyInfo?.address ?: "Khargsenpatti Mathetu, Bhadohi - 221404") }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("UMESH ENTERPRISE - ADMIN PANEL", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SleekBlue900)
            Text("Owner & Material Management", fontSize = 11.sp, color = Slate500)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
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
    ) {
      // Admin Tabs Row
      ScrollableTabRow(
        selectedTabIndex = currentAdminTab.ordinal,
        edgePadding = 16.dp,
        containerColor = Color.White,
        contentColor = SleekBlue700
      ) {
        Tab(
          selected = currentAdminTab == AdminTab.RATES,
          onClick = { currentAdminTab = AdminTab.RATES },
          text = { Text("Update Rates", fontWeight = FontWeight.Bold, color = if (currentAdminTab == AdminTab.RATES) SleekBlue700 else Slate500) }
        )
        Tab(
          selected = currentAdminTab == AdminTab.PRODUCTS,
          onClick = { currentAdminTab = AdminTab.PRODUCTS },
          text = { Text("Products (${products.size})", fontWeight = FontWeight.Bold, color = if (currentAdminTab == AdminTab.PRODUCTS) SleekBlue700 else Slate500) }
        )
        Tab(
          selected = currentAdminTab == AdminTab.ORDERS,
          onClick = { currentAdminTab = AdminTab.ORDERS },
          text = { Text("Orders (${orders.size})", fontWeight = FontWeight.Bold, color = if (currentAdminTab == AdminTab.ORDERS) SleekBlue700 else Slate500) }
        )
        Tab(
          selected = currentAdminTab == AdminTab.ENQUIRIES,
          onClick = { currentAdminTab = AdminTab.ENQUIRIES },
          text = { Text("Enquiries (${enquiries.size})", fontWeight = FontWeight.Bold, color = if (currentAdminTab == AdminTab.ENQUIRIES) SleekBlue700 else Slate500) }
        )
        Tab(
          selected = currentAdminTab == AdminTab.COMPANY,
          onClick = { currentAdminTab = AdminTab.COMPANY },
          text = { Text("Company Info", fontWeight = FontWeight.Bold, color = if (currentAdminTab == AdminTab.COMPANY) SleekBlue700 else Slate500) }
        )
      }

      when (currentAdminTab) {
        AdminTab.RATES -> {
          // RATES TAB
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            item {
              Surface(
                color = SleekBlue50,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "Owner yahan se Gitti, Balu, Sariya, Cement aur Eent ke latest daily rates update kar sakta hai.",
                  fontSize = 12.sp,
                  color = SleekBlue900,
                  modifier = Modifier.padding(12.dp)
                )
              }
            }

            items(dailyRates) { rate ->
              Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    MaterialCategoryIcon(category = rate.productName, iconSize = 20)
                    Column {
                      Text(text = rate.productName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SleekBlue900)
                      Text(text = "${rate.rateDisplay} / ${rate.unit}", color = SleekBlue700, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                      Text(text = "Updated: ${rate.lastUpdated}", fontSize = 11.sp, color = Slate500)
                    }
                  }

                  Button(
                    onClick = {
                      editingRate = rate
                      newRateDisplay = rate.rateDisplay
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_edit_rate_${rate.productName.lowercase()}")
                  ) {
                    Text("Edit Rate", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }

        AdminTab.PRODUCTS -> {
          // PRODUCTS TAB
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            item {
              Button(
                onClick = {
                  editProductId = 0
                  prodName = ""
                  prodCategory = "GITTI"
                  prodDesc = ""
                  prodTypes = ""
                  prodRate = ""
                  prodUnit = "Per Ton"
                  showProductDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp).testTag("admin_add_product_btn")
              ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add New Construction Product", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }

            items(products) { prod ->
              Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      MaterialCategoryIcon(category = prod.category, iconSize = 18)
                      Text(text = prod.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SleekBlue900)
                    }
                    Text(text = "₹ ${prod.rate} / ${prod.unit}", fontWeight = FontWeight.Bold, color = SleekBlue700, fontSize = 13.sp)
                  }

                  Spacer(modifier = Modifier.height(6.dp))
                  Text(text = prod.description, fontSize = 12.sp, color = Slate600, maxLines = 2)
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(text = "Types: ${prod.availableTypes}", fontSize = 11.5.sp, color = Slate500)

                  Spacer(modifier = Modifier.height(10.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    TextButton(
                      onClick = {
                        editProductId = prod.id
                        prodName = prod.name
                        prodCategory = prod.category
                        prodDesc = prod.description
                        prodTypes = prod.availableTypes
                        prodRate = prod.rate.toString()
                        prodUnit = prod.unit
                        showProductDialog = true
                      }
                    ) {
                      Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = SleekBlue700)
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Edit", fontSize = 12.sp, color = SleekBlue700)
                    }

                    TextButton(
                      onClick = { onDeleteProduct(prod.id) },
                      colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                      Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Delete", fontSize = 12.sp)
                    }
                  }
                }
              }
            }
          }
        }

        AdminTab.ORDERS -> {
          // ORDERS TAB
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            if (orders.isEmpty()) {
              item {
                Text("No orders to manage.", color = Slate500, fontSize = 14.sp)
              }
            }

            items(orders) { order ->
              Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekBlue900)
                    StatusBadge(status = order.status)
                  }

                  Spacer(modifier = Modifier.height(6.dp))
                  Text(text = "Customer: ${order.customerName} (${order.mobileNumber})", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Slate800)
                  Text(text = "Item: ${order.productName} - ${order.materialType}", fontSize = 12.5.sp, color = Slate700)
                  Text(text = "Quantity: ${order.quantity} | Delivery: ${order.deliveryDate}", fontSize = 12.sp, color = SleekBlue700, fontWeight = FontWeight.SemiBold)
                  Text(text = "Address: ${order.deliveryAddress}", fontSize = 12.sp, color = Slate500)

                  Spacer(modifier = Modifier.height(10.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Button(
                      onClick = { onUpdateOrderStatus(order.id, "Accepted") },
                      colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                      shape = RoundedCornerShape(16.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("Accept", fontSize = 11.sp)
                    }

                    Button(
                      onClick = { onUpdateOrderStatus(order.id, "Out for Delivery") },
                      colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo, contentColor = Color.White),
                      shape = RoundedCornerShape(16.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("Out for Delivery", fontSize = 11.sp)
                    }

                    Button(
                      onClick = { onUpdateOrderStatus(order.id, "Delivered") },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White),
                      shape = RoundedCornerShape(16.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("Delivered", fontSize = 11.sp)
                    }

                    OutlinedButton(
                      onClick = { onUpdateOrderStatus(order.id, "Rejected") },
                      shape = RoundedCornerShape(16.dp),
                      contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                      colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                      Text("Reject", fontSize = 11.sp)
                    }
                  }
                }
              }
            }
          }
        }

        AdminTab.ENQUIRIES -> {
          // ENQUIRIES TAB
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            if (enquiries.isEmpty()) {
              item { Text("No enquiries received yet.", color = Slate500, fontSize = 14.sp) }
            }
            items(enquiries) { enq ->
              Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = enq.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekBlue900)
                    StatusBadge(status = enq.status)
                  }
                  Text(text = "Phone: ${enq.mobileNumber}", fontSize = 12.sp, color = Slate600)
                  Text(text = "Material: ${enq.material} (${enq.quantity})", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = SleekBlue700)
                  Text(text = "Query: ${enq.message}", fontSize = 12.sp, color = Slate700)

                  Spacer(modifier = Modifier.height(8.dp))
                  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                      onClick = { onUpdateEnquiryStatus(enq.id, "Contacted") },
                      shape = RoundedCornerShape(16.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("Mark Contacted", fontSize = 11.sp)
                    }
                    Button(
                      onClick = { onUpdateEnquiryStatus(enq.id, "Resolved") },
                      shape = RoundedCornerShape(16.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("Mark Resolved", fontSize = 11.sp)
                    }
                  }
                }
              }
            }
          }
        }

        AdminTab.COMPANY -> {
          // COMPANY DETAILS TAB
          Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Company Contact & Shop Details", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SleekBlue900)
                Text(text = "Owner / Proprietor: ${companyInfo?.ownerName ?: compOwner}", fontSize = 13.5.sp, color = Slate800, fontWeight = FontWeight.SemiBold)
                Text(text = "Mobile Number: ${companyInfo?.phone ?: compPhone}", fontSize = 13.sp, color = Slate700)
                Text(text = "WhatsApp Number: ${companyInfo?.whatsapp ?: compWhatsapp}", fontSize = 13.sp, color = Slate700)
                Text(text = "Email: ${companyInfo?.email ?: compEmail}", fontSize = 13.sp, color = Slate700)
                Text(text = "Shop Address: ${companyInfo?.address ?: compAddress}", fontSize = 13.sp, color = Slate700)

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                  onClick = { showCompanyDialog = true },
                  colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
                  shape = RoundedCornerShape(24.dp),
                  modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                  Text("Update Contact Details", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }

  // Rate Edit Dialog
  editingRate?.let { rate ->
    AlertDialog(
      onDismissRequest = { editingRate = null },
      title = { Text("Update Rate for ${rate.productName}", fontWeight = FontWeight.Bold, color = SleekBlue900) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Current Unit: ${rate.unit}", fontSize = 12.sp, color = Slate500)
          OutlinedTextField(
            value = newRateDisplay,
            onValueChange = { newRateDisplay = it },
            label = { Text("New Rate Display (e.g. ₹ 1,900)") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("admin_rate_input_dialog")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val updated = rate.copy(
              rateDisplay = newRateDisplay,
              lastUpdated = "Updated just now"
            )
            onUpdateRate(updated)
            editingRate = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
          shape = RoundedCornerShape(20.dp)
        ) {
          Text("Save Rate")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingRate = null }) { Text("Cancel", color = Slate600) }
      }
    )
  }

  // Product Add / Edit Dialog
  if (showProductDialog) {
    AlertDialog(
      onDismissRequest = { showProductDialog = false },
      title = { Text(if (editProductId == 0) "Add New Product" else "Edit Product", fontWeight = FontWeight.Bold, color = SleekBlue900) },
      text = {
        Column(
          modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = prodName,
            onValueChange = { prodName = it },
            label = { Text("Product Name") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
          OutlinedTextField(
            value = prodCategory,
            onValueChange = { prodCategory = it },
            label = { Text("Category (GITTI, BALU, SARIYA, CEMENT, EENT)") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
          OutlinedTextField(
            value = prodDesc,
            onValueChange = { prodDesc = it },
            label = { Text("Description") },
            shape = RoundedCornerShape(12.dp),
            maxLines = 2
          )
          OutlinedTextField(
            value = prodTypes,
            onValueChange = { prodTypes = it },
            label = { Text("Available Types (comma-separated)") },
            shape = RoundedCornerShape(12.dp)
          )
          OutlinedTextField(
            value = prodRate,
            onValueChange = { prodRate = it },
            label = { Text("Price (numeric)") },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
          )
          OutlinedTextField(
            value = prodUnit,
            onValueChange = { prodUnit = it },
            label = { Text("Unit (e.g. Per Ton, Per Bag)") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val p = ProductEntity(
              id = editProductId,
              category = prodCategory.uppercase().trim(),
              name = prodName.trim(),
              description = prodDesc.trim(),
              availableTypes = prodTypes.trim(),
              rate = prodRate.toDoubleOrNull() ?: 100.0,
              unit = prodUnit.trim()
            )
            onAddOrUpdateProduct(p)
            showProductDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
          shape = RoundedCornerShape(20.dp)
        ) {
          Text("Save Product")
        }
      },
      dismissButton = {
        TextButton(onClick = { showProductDialog = false }) { Text("Cancel", color = Slate600) }
      }
    )
  }

  // Company Details Edit Dialog
  if (showCompanyDialog) {
    AlertDialog(
      onDismissRequest = { showCompanyDialog = false },
      title = { Text("Update Company Information", fontWeight = FontWeight.Bold, color = SleekBlue900) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = compOwner, onValueChange = { compOwner = it }, label = { Text("Owner Name") }, shape = RoundedCornerShape(12.dp))
          OutlinedTextField(value = compPhone, onValueChange = { compPhone = it }, label = { Text("Mobile Number") }, shape = RoundedCornerShape(12.dp))
          OutlinedTextField(value = compWhatsapp, onValueChange = { compWhatsapp = it }, label = { Text("WhatsApp Number") }, shape = RoundedCornerShape(12.dp))
          OutlinedTextField(value = compEmail, onValueChange = { compEmail = it }, label = { Text("Email Address") }, shape = RoundedCornerShape(12.dp))
          OutlinedTextField(value = compAddress, onValueChange = { compAddress = it }, label = { Text("Shop Address") }, shape = RoundedCornerShape(12.dp))
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val info = CompanyInfoEntity(
              id = 1,
              name = "UMESH ENTERPRISE",
              tagline = "Aapke Har Nirman Ka Bharosemand Saathi",
              ownerName = compOwner,
              phone = compPhone,
              whatsapp = compWhatsapp,
              email = compEmail,
              address = compAddress,
              servicePincodes = companyInfo?.servicePincodes ?: "221404, 221401, 221409, 221304, 221001"
            )
            onUpdateCompanyInfo(info)
            showCompanyDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
          shape = RoundedCornerShape(20.dp)
        ) {
          Text("Save Details")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCompanyDialog = false }) { Text("Cancel", color = Slate600) }
      }
    )
  }
}
