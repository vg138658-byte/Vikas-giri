package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItemEntity
import com.example.data.EnquiryEntity
import com.example.data.OrderEntity
import com.example.ui.CustomerUser
import com.example.ui.components.MaterialCategoryIcon
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
  orders: List<OrderEntity>,
  onOrderNow: () -> Unit
) {
  if (orders.isEmpty()) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.ShoppingCart,
          contentDescription = null,
          tint = Slate400,
          modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "No Orders Yet",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Slate800
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Aapne abhi tak koi construction material order nahi kiya hai.",
          fontSize = 13.sp,
          color = Slate600
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onOrderNow,
          colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
          shape = RoundedCornerShape(24.dp)
        ) {
          Text("Order Construction Material")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(SleekBg)
        .testTag("orders_screen_list"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Text(
          text = "My Construction Orders (${orders.size})",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = SleekBlue900
        )
      }

      items(orders) { order ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth().testTag("order_card_${order.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                MaterialCategoryIcon(category = order.productName, iconSize = 20)
                Column {
                  Text(
                    text = order.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Slate800
                  )
                  Text(
                    text = "Type: ${order.materialType}",
                    fontSize = 12.sp,
                    color = SleekBlue700,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
              StatusBadge(status = order.status)
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Quantity", fontSize = 11.sp, color = Slate500)
                Text(text = order.quantity, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = SleekBlue900)
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(text = "Delivery Date", fontSize = 11.sp, color = Slate500)
                Text(text = order.deliveryDate, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
              Icon(Icons.Default.Place, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = order.deliveryAddress, fontSize = 12.sp, color = Slate600)
            }

            if (order.additionalMessage.isNotEmpty()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Note: ${order.additionalMessage}",
                fontSize = 11.5.sp,
                color = Slate500,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
              )
            }
          }
        }
      }
      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
fun ProfileScreen(
  user: CustomerUser,
  ordersCount: Int,
  enquiries: List<EnquiryEntity>,
  onOpenMyOrders: () -> Unit,
  onOpenEnquiries: () -> Unit,
  onOpenAdminPanel: () -> Unit,
  onOpenLogin: () -> Unit,
  onLogout: () -> Unit
) {
  var showEnquiriesDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(SleekBg)
      .testTag("profile_screen_list"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // User Profile Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SleekBlue900),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (user.name.isNotEmpty()) user.name.take(1).uppercase() else "U",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = SleekBlue900
              )
              Text(
                text = user.mobile,
                fontSize = 13.sp,
                color = Slate500
              )
              Text(
                text = user.email,
                fontSize = 12.sp,
                color = Slate500
              )
            }

            if (!user.isLoggedIn) {
              TextButton(onClick = onOpenLogin) {
                Text("Login", color = SleekBlue700, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = Slate100)
          Spacer(modifier = Modifier.height(10.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(text = "Saved Delivery Address", fontSize = 11.5.sp, color = Slate500)
              Text(text = user.address.ifEmpty { "Mathetu, Bhadohi - 221404" }, fontSize = 13.sp, color = Slate800)
            }
          }
        }
      }
    }

    // Customer Profile Navigation options
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
          ProfileMenuItem(
            icon = Icons.Default.ShoppingCart,
            title = "My Orders",
            badge = "$ordersCount orders",
            onClick = onOpenMyOrders
          )

          Divider(color = Slate100, modifier = Modifier.padding(horizontal = 16.dp))

          ProfileMenuItem(
            icon = Icons.Default.HelpOutline,
            title = "My Enquiries",
            badge = "${enquiries.size} enquiries",
            onClick = { showEnquiriesDialog = true }
          )

          Divider(color = Slate100, modifier = Modifier.padding(horizontal = 16.dp))

          ProfileMenuItem(
            icon = Icons.Default.AdminPanelSettings,
            title = "Admin Panel",
            badge = "Owner Access",
            onClick = onOpenAdminPanel
          )
        }
      }
    }

    // Account Actions
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
          ProfileMenuItem(
            icon = Icons.Default.PersonAdd,
            title = "Create New Account / Switch Account",
            onClick = onOpenLogin
          )

          Divider(color = Slate100, modifier = Modifier.padding(horizontal = 16.dp))

          ProfileMenuItem(
            icon = Icons.Default.Logout,
            title = "Logout",
            tint = Color(0xFFEF4444),
            onClick = onLogout
          )
        }
      }
      Spacer(modifier = Modifier.height(80.dp))
    }
  }

  // My Enquiries Dialog
  if (showEnquiriesDialog) {
    AlertDialog(
      onDismissRequest = { showEnquiriesDialog = false },
      title = { Text("My Enquiries History", fontWeight = FontWeight.Bold, color = SleekBlue900) },
      text = {
        if (enquiries.isEmpty()) {
          Text("Aapki abhi tak koi enquiry nahi hai.", color = Slate600)
        } else {
          LazyColumn(
            modifier = Modifier.heightIn(max = 350.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(enquiries) { enq ->
              Surface(
                color = SleekBg,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = enq.material, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SleekBlue900)
                    StatusBadge(status = enq.status)
                  }
                  Text(text = "Qty: ${enq.quantity}", fontSize = 12.sp, color = Slate700)
                  Text(text = enq.message, fontSize = 11.5.sp, color = Slate500)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showEnquiriesDialog = false }) {
          Text("Close", color = SleekBlue700, fontWeight = FontWeight.Bold)
        }
      }
    )
  }
}

@Composable
private fun ProfileMenuItem(
  icon: ImageVector,
  title: String,
  badge: String? = null,
  tint: Color = Slate800,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = if (tint == Slate800) SleekBlue700 else tint, modifier = Modifier.size(22.dp))
      Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = tint)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (badge != null) {
        Surface(
          color = SleekBlue50,
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = badge,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SleekBlue700,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLoginRegisterScreen(
  onBack: () -> Unit,
  onLoginSuccess: (name: String, mobile: String, email: String, address: String) -> Unit
) {
  var isRegisterMode by remember { mutableStateOf(false) }

  // Login Fields
  var loginIdentifier by remember { mutableStateOf("") }
  var loginPassword by remember { mutableStateOf("") }

  // Registration Fields
  var regFullName by remember { mutableStateOf("") }
  var regMobile by remember { mutableStateOf("") }
  var regEmail by remember { mutableStateOf("") }
  var regAddress by remember { mutableStateOf("") }
  var regPassword by remember { mutableStateOf("") }
  var regConfirmPassword by remember { mutableStateOf("") }

  Scaffold(
    containerColor = SleekBg,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isRegisterMode) "CUSTOMER REGISTRATION" else "CUSTOMER LOGIN",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = SleekBlue900
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
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
      if (!isRegisterMode) {
        // LOGIN MODE
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Login to Umesh Enterprise",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SleekBlue900
            )

            OutlinedTextField(
              value = loginIdentifier,
              onValueChange = { loginIdentifier = it },
              label = { Text("Mobile Number / Email") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SleekBlue700) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("login_identifier_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = loginPassword,
              onValueChange = { loginPassword = it },
              label = { Text("Password") },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SleekBlue700) },
              visualTransformation = PasswordVisualTransformation(),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
              singleLine = true
            )

            Button(
              onClick = {
                val name = if (loginIdentifier.contains("@")) loginIdentifier.substringBefore("@") else "Customer"
                val mobile = if (loginIdentifier.all { it.isDigit() }) loginIdentifier else "+91 98765 12345"
                onLoginSuccess(name, mobile, "$name@email.com", "Main Market Site Address")
              },
              colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
              shape = RoundedCornerShape(24.dp),
              modifier = Modifier.fillMaxWidth().height(46.dp).testTag("login_submit_button")
            ) {
              Text("LOGIN", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              TextButton(onClick = { /* Forgot password note */ }) {
                Text("FORGOT PASSWORD", fontSize = 11.5.sp, color = Slate500)
              }

              TextButton(onClick = { isRegisterMode = true }) {
                Text("CREATE NEW ACCOUNT", fontSize = 11.5.sp, color = SleekBlue700, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      } else {
        // REGISTRATION MODE
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Create New Customer Account",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SleekBlue900
            )

            OutlinedTextField(
              value = regFullName,
              onValueChange = { regFullName = it },
              label = { Text("Full Name") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SleekBlue700) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = regMobile,
              onValueChange = { regMobile = it },
              label = { Text("Mobile Number") },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SleekBlue700) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_mobile_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = regEmail,
              onValueChange = { regEmail = it },
              label = { Text("Email Address") },
              leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SleekBlue700) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = regAddress,
              onValueChange = { regAddress = it },
              label = { Text("Address") },
              leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = SleekBlue700) },
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_address_input"),
              maxLines = 2
            )

            OutlinedTextField(
              value = regPassword,
              onValueChange = { regPassword = it },
              label = { Text("Password") },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SleekBlue700) },
              visualTransformation = PasswordVisualTransformation(),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_password_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = regConfirmPassword,
              onValueChange = { regConfirmPassword = it },
              label = { Text("Confirm Password") },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SleekBlue700) },
              visualTransformation = PasswordVisualTransformation(),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekBlue600,
                unfocusedBorderColor = Slate200,
                focusedLabelColor = SleekBlue700
              ),
              modifier = Modifier.fillMaxWidth().testTag("reg_confirm_password_input"),
              singleLine = true
            )

            Button(
              onClick = {
                onLoginSuccess(
                  regFullName.ifEmpty { "Customer" },
                  regMobile.ifEmpty { "+91 96164 10193" },
                  regEmail.ifEmpty { "customer@email.com" },
                  regAddress.ifEmpty { "Construction Site" }
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = SleekBlue700, contentColor = Color.White),
              shape = RoundedCornerShape(24.dp),
              modifier = Modifier.fillMaxWidth().height(46.dp).testTag("reg_create_account_button")
            ) {
              Text("CREATE ACCOUNT", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }

            TextButton(
              onClick = { isRegisterMode = false },
              modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
              Text("Already have an account? Login", fontSize = 12.sp, color = SleekBlue700, fontWeight = FontWeight.Medium)
            }
          }
        }
      }
    }
  }
}
