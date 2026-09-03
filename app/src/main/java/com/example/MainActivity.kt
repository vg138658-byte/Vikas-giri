package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.components.launchDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val viewModel: MainViewModel = viewModel()

      UmeshEnterpriseTheme {
        UmeshEnterpriseApp(viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmeshEnterpriseApp(viewModel: MainViewModel) {
  val context = LocalContext.current

  val appPhase by viewModel.appPhase.collectAsStateWithLifecycle()
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val activeSubscreen by viewModel.activeSubscreen.collectAsStateWithLifecycle()

  val products by viewModel.products.collectAsStateWithLifecycle()
  val dailyRates by viewModel.dailyRates.collectAsStateWithLifecycle()
  val orders by viewModel.orders.collectAsStateWithLifecycle()
  val enquiries by viewModel.enquiries.collectAsStateWithLifecycle()
  val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
  val companyInfo by viewModel.companyInfo.collectAsStateWithLifecycle()
  val customerUser by viewModel.customerUser.collectAsStateWithLifecycle()

  val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
  val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
  val preselectedProductForOrder by viewModel.preselectedProductForOrder.collectAsStateWithLifecycle()
  val preselectedTypeForOrder by viewModel.preselectedTypeForOrder.collectAsStateWithLifecycle()
  val preselectedMaterialForEnquiry by viewModel.preselectedMaterialForEnquiry.collectAsStateWithLifecycle()

  val isOrderSuccess by viewModel.isOrderSuccess.collectAsStateWithLifecycle()
  val isEnquirySuccess by viewModel.isEnquirySuccess.collectAsStateWithLifecycle()
  val alertMessage by viewModel.alertMessage.collectAsStateWithLifecycle()

  // Handle system back navigation
  BackHandler(enabled = activeSubscreen != ActiveSubscreen.NONE) {
    viewModel.closeSubscreen()
  }

  when (appPhase) {
    AppPhase.SPLASH -> {
      SplashScreen(
        onSplashFinished = { viewModel.setAppPhase(AppPhase.WELCOME) }
      )
    }

    AppPhase.WELCOME -> {
      WelcomeScreen(
        onExploreClick = { viewModel.setAppPhase(AppPhase.MAIN_APP) }
      )
    }

    AppPhase.MAIN_APP -> {
      // Main Application with Top Bar, Bottom Bar, and FAB
      Scaffold(
        modifier = Modifier
          .fillMaxSize()
          .testTag("main_app_scaffold"),
        topBar = {
          if (activeSubscreen == ActiveSubscreen.NONE) {
            TopAppBar(
              title = {
                Column {
                  Text(
                    text = "UMESH ENTERPRISE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = (-0.2).sp,
                    color = SleekBlue900
                  )
                  Text(
                    text = "AAPKE HAR NIRMAN KA SATHI",
                    fontSize = 10.sp,
                    color = SleekBlue600,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    maxLines = 1
                  )
                }
              },
              actions = {
                // Call quick button
                IconButton(
                  onClick = {
                    launchDialer(context, companyInfo?.phone ?: "+91 96164 10193")
                  },
                  modifier = Modifier
                    .padding(end = 4.dp)
                    .size(36.dp)
                    .testTag("top_bar_call_btn")
                ) {
                  Surface(
                    shape = CircleShape,
                    color = SleekBlue50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
                    modifier = Modifier.fillMaxSize()
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(Icons.Default.Phone, contentDescription = "Call", tint = SleekBlue700, modifier = Modifier.size(18.dp))
                    }
                  }
                }

                // WhatsApp quick button
                IconButton(
                  onClick = {
                    launchWhatsApp(context, companyInfo?.whatsapp ?: "+91 96164 10193")
                  },
                  modifier = Modifier
                    .padding(end = 4.dp)
                    .size(36.dp)
                    .testTag("top_bar_whatsapp_btn")
                ) {
                  Surface(
                    shape = CircleShape,
                    color = SleekBlue50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
                    modifier = Modifier.fillMaxSize()
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF25D366), modifier = Modifier.size(18.dp))
                    }
                  }
                }

                // Cart badge button
                IconButton(
                  onClick = { viewModel.openSubscreen(ActiveSubscreen.CART) },
                  modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .testTag("top_bar_cart_btn")
                ) {
                  Surface(
                    shape = CircleShape,
                    color = SleekBlue50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBlue100),
                    modifier = Modifier.fillMaxSize()
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      BadgedBox(
                        badge = {
                          if (cartItems.isNotEmpty()) {
                            Badge(
                              containerColor = SleekBlue700,
                              contentColor = Color.White
                            ) {
                              Text("${cartItems.size}")
                            }
                          }
                        }
                      ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = SleekBlue700, modifier = Modifier.size(18.dp))
                      }
                    }
                  }
                }
              },
              colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
              )
            )
          }
        },
        bottomBar = {
          if (activeSubscreen == ActiveSubscreen.NONE) {
            NavigationBar(
              containerColor = Color.White,
              tonalElevation = 1.dp,
              modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
              NavigationBarItem(
                selected = currentTab == BottomTab.HOME,
                onClick = { viewModel.selectTab(BottomTab.HOME) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 10.sp, fontWeight = if (currentTab == BottomTab.HOME) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SleekBlue700,
                  selectedTextColor = SleekBlue700,
                  indicatorColor = SleekBlue100,
                  unselectedIconColor = Slate400,
                  unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("tab_home")
              )

              NavigationBarItem(
                selected = currentTab == BottomTab.PRODUCTS,
                onClick = { viewModel.selectTab(BottomTab.PRODUCTS) },
                icon = { Icon(Icons.Default.Apartment, contentDescription = "Materials") },
                label = { Text("Products", fontSize = 10.sp, fontWeight = if (currentTab == BottomTab.PRODUCTS) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SleekBlue700,
                  selectedTextColor = SleekBlue700,
                  indicatorColor = SleekBlue100,
                  unselectedIconColor = Slate400,
                  unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("tab_products")
              )

              NavigationBarItem(
                selected = currentTab == BottomTab.RATES,
                onClick = { viewModel.selectTab(BottomTab.RATES) },
                icon = { Icon(Icons.Default.CurrencyRupee, contentDescription = "Rates") },
                label = { Text("Rates", fontSize = 10.sp, fontWeight = if (currentTab == BottomTab.RATES) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SleekBlue700,
                  selectedTextColor = SleekBlue700,
                  indicatorColor = SleekBlue100,
                  unselectedIconColor = Slate400,
                  unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("tab_rates")
              )

              NavigationBarItem(
                selected = currentTab == BottomTab.ORDERS,
                onClick = { viewModel.selectTab(BottomTab.ORDERS) },
                icon = {
                  BadgedBox(
                    badge = {
                      if (orders.isNotEmpty()) {
                        Badge(containerColor = SleekBlue700) { Text("${orders.size}") }
                      }
                    }
                  ) {
                    Icon(Icons.Default.Assignment, contentDescription = "Orders")
                  }
                },
                label = { Text("My Orders", fontSize = 10.sp, fontWeight = if (currentTab == BottomTab.ORDERS) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SleekBlue700,
                  selectedTextColor = SleekBlue700,
                  indicatorColor = SleekBlue100,
                  unselectedIconColor = Slate400,
                  unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("tab_orders")
              )

              NavigationBarItem(
                selected = currentTab == BottomTab.PROFILE,
                onClick = { viewModel.selectTab(BottomTab.PROFILE) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile", fontSize = 10.sp, fontWeight = if (currentTab == BottomTab.PROFILE) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SleekBlue700,
                  selectedTextColor = SleekBlue700,
                  indicatorColor = SleekBlue100,
                  unselectedIconColor = Slate400,
                  unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("tab_profile")
              )
            }
          }
        },
        floatingActionButton = {
          if (activeSubscreen == ActiveSubscreen.NONE && currentTab != BottomTab.ORDERS && currentTab != BottomTab.PROFILE) {
            ExtendedFloatingActionButton(
              onClick = { viewModel.openOrderScreen("", "") },
              containerColor = SleekBlue700,
              contentColor = Color.White,
              shape = RoundedCornerShape(28.dp),
              elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
              modifier = Modifier.testTag("fab_order_material")
            ) {
              Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("ORDER NOW", fontWeight = FontWeight.Bold, fontSize = 12.5.sp, letterSpacing = 0.5.sp)
            }
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          // Main tab contents or active subscreen
          when (activeSubscreen) {
            ActiveSubscreen.PRODUCT_DETAIL -> {
              selectedProduct?.let { prod ->
                ProductDetailScreen(
                  product = prod,
                  companyPhone = companyInfo?.phone ?: "+91 96164 10193",
                  onBack = { viewModel.closeSubscreen() },
                  onAddToCart = { selectedType, qty ->
                    viewModel.addToCart(prod, selectedType, qty)
                  },
                  onOrderNow = { prodName, type, qtyStr ->
                    viewModel.openOrderScreen(prodName, type)
                  }
                )
              } ?: viewModel.closeSubscreen()
            }

            ActiveSubscreen.PLACE_ORDER -> {
              PlaceOrderScreen(
                user = customerUser,
                products = products,
                initialProduct = preselectedProductForOrder,
                initialType = preselectedTypeForOrder,
                onBack = { viewModel.closeSubscreen() },
                onPlaceOrder = { name, mobile, address, product, type, qty, date, msg ->
                  viewModel.submitOrder(name, mobile, address, product, type, qty, date, msg)
                }
              )
            }

            ActiveSubscreen.MATERIAL_ENQUIRY -> {
              MaterialEnquiryScreen(
                user = customerUser,
                initialMaterial = preselectedMaterialForEnquiry,
                onBack = { viewModel.closeSubscreen() },
                onSubmitEnquiry = { name, mobile, mat, qty, msg ->
                  viewModel.submitEnquiry(name, mobile, mat, qty, msg)
                }
              )
            }

            ActiveSubscreen.HOME_DELIVERY_CHECK -> {
              HomeDeliveryScreen(
                companyInfo = companyInfo,
                onBack = { viewModel.closeSubscreen() },
                onOrderNow = { viewModel.openOrderScreen("", "") }
              )
            }

            ActiveSubscreen.ABOUT_US -> {
              AboutUsScreen(onBack = { viewModel.closeSubscreen() })
            }

            ActiveSubscreen.CONTACT_US -> {
              ContactUsScreen(companyInfo = companyInfo, onBack = { viewModel.closeSubscreen() })
            }

            ActiveSubscreen.ADMIN_PANEL -> {
              AdminPanelScreen(
                dailyRates = dailyRates,
                products = products,
                orders = orders,
                enquiries = enquiries,
                companyInfo = companyInfo,
                onBack = { viewModel.closeSubscreen() },
                onUpdateRate = { updatedRate -> viewModel.updateDailyRateEntity(updatedRate) },
                onAddOrUpdateProduct = { prod -> viewModel.addOrUpdateProduct(prod) },
                onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                onUpdateOrderStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                onUpdateEnquiryStatus = { id, status -> viewModel.updateEnquiryStatus(id, status) },
                onUpdateCompanyInfo = { info -> viewModel.updateCompanyInfo(info) }
              )
            }

            ActiveSubscreen.CART -> {
              CartScreen(
                cartItems = cartItems,
                onBack = { viewModel.closeSubscreen() },
                onRemoveItem = { id -> viewModel.removeCartItem(id) },
                onCheckout = {
                  val firstItem = cartItems.firstOrNull()
                  viewModel.openOrderScreen(firstItem?.productName ?: "", firstItem?.selectedType ?: "")
                }
              )
            }

            ActiveSubscreen.LOGIN_REGISTER -> {
              CustomerLoginRegisterScreen(
                onBack = { viewModel.closeSubscreen() },
                onLoginSuccess = { name, mobile, email, address ->
                  viewModel.updateCustomerProfile(name, mobile, email, address)
                  viewModel.closeSubscreen()
                }
              )
            }

            ActiveSubscreen.NONE -> {
              // Primary 5 Bottom Tabs
              when (currentTab) {
                BottomTab.HOME -> {
                  HomeScreen(
                    dailyRates = dailyRates,
                    featuredProducts = products,
                    onCategoryClick = { cat ->
                      viewModel.setSelectedCategoryFilter(cat)
                      viewModel.selectTab(BottomTab.PRODUCTS)
                    },
                    onCheckRatesClick = { viewModel.selectTab(BottomTab.RATES) },
                    onOrderNowClick = { p -> viewModel.openOrderScreen(p, "") },
                    onAskRateClick = { p -> viewModel.openEnquiryScreen(p) },
                    onOpenDeliveryClick = { viewModel.openSubscreen(ActiveSubscreen.HOME_DELIVERY_CHECK) },
                    onOpenAboutClick = { viewModel.openSubscreen(ActiveSubscreen.ABOUT_US) },
                    onOpenContactClick = { viewModel.openSubscreen(ActiveSubscreen.CONTACT_US) },
                    onOpenAdminClick = { viewModel.openSubscreen(ActiveSubscreen.ADMIN_PANEL) }
                  )
                }

                BottomTab.PRODUCTS -> {
                  ProductsScreen(
                    products = products,
                    selectedCategory = selectedCategoryFilter,
                    onSelectCategory = { viewModel.setSelectedCategoryFilter(it) },
                    onProductClick = { prod -> viewModel.openProductDetails(prod) },
                    onRatePuchheClick = { prod -> viewModel.openEnquiryScreen(prod.name) },
                    onOrderNowClick = { prod -> viewModel.openOrderScreen(prod.name, "") }
                  )
                }

                BottomTab.RATES -> {
                  RatesScreen(
                    rates = dailyRates,
                    onLatestRatePuchhe = { prodName -> viewModel.openEnquiryScreen(prodName) },
                    onOrderProduct = { prodName -> viewModel.openOrderScreen(prodName, "") }
                  )
                }

                BottomTab.ORDERS -> {
                  OrdersScreen(
                    orders = orders,
                    onOrderNow = { viewModel.openOrderScreen("", "") }
                  )
                }

                BottomTab.PROFILE -> {
                  ProfileScreen(
                    user = customerUser,
                    ordersCount = orders.size,
                    enquiries = enquiries,
                    onOpenMyOrders = { viewModel.selectTab(BottomTab.ORDERS) },
                    onOpenEnquiries = { /* Handled in profile screen dialog */ },
                    onOpenAdminPanel = { viewModel.openSubscreen(ActiveSubscreen.ADMIN_PANEL) },
                    onOpenLogin = { viewModel.openSubscreen(ActiveSubscreen.LOGIN_REGISTER) },
                    onLogout = { viewModel.logoutCustomer() }
                  )
                }
              }
            }
          }
        }
      }

      // Order Success Dialog (Verbatim as required in prompt)
      if (isOrderSuccess) {
        AlertDialog(
          onDismissRequest = { viewModel.dismissOrderSuccess() },
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Thank You!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
            }
          },
          text = {
            Column {
              Text(
                text = "Aapka order successfully receive ho gaya hai.\nHamari team jald hi aapse contact karegi.",
                fontSize = 14.5.sp,
                color = Slate800,
                lineHeight = 22.sp
              )
            }
          },
          confirmButton = {
            Button(
              onClick = { viewModel.dismissOrderSuccess() },
              colors = ButtonDefaults.buttonColors(containerColor = IndustrialAmber),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("View Orders", fontWeight = FontWeight.Bold)
            }
          }
        )
      }

      // Enquiry Success Dialog (Verbatim as required in prompt)
      if (isEnquirySuccess) {
        AlertDialog(
          onDismissRequest = { viewModel.dismissEnquirySuccess() },
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Thank You!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
            }
          },
          text = {
            Column {
              Text(
                text = "Aapki enquiry successfully submit ho gayi hai. Hamari team jald hi aapse contact karegi.",
                fontSize = 14.5.sp,
                color = Slate800,
                lineHeight = 22.sp
              )
            }
          },
          confirmButton = {
            Button(
              onClick = { viewModel.dismissEnquirySuccess() },
              colors = ButtonDefaults.buttonColors(containerColor = IndustrialAmber),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Theek Hai", fontWeight = FontWeight.Bold)
            }
          }
        )
      }

      // Snackbar / toast for general alert messages (e.g. cart added, product saved)
      alertMessage?.let { msg ->
        LaunchedEffect(msg) {
          kotlinx.coroutines.delay(2500)
          viewModel.clearAlert()
        }
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.BottomCenter
        ) {
          Surface(
            color = Slate900,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 6.dp
          ) {
            Text(
              text = msg,
              color = Color.White,
              fontSize = 13.sp,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
          }
        }
      }
    }
  }
}
