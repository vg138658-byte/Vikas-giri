package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CartItemEntity
import com.example.data.CompanyInfoEntity
import com.example.data.DailyRateEntity
import com.example.data.EnquiryEntity
import com.example.data.OrderEntity
import com.example.data.ProductEntity
import com.example.data.UmeshRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppPhase {
  SPLASH,
  WELCOME,
  MAIN_APP
}

enum class BottomTab {
  HOME,
  PRODUCTS,
  RATES,
  ORDERS,
  PROFILE
}

enum class ActiveSubscreen {
  NONE,
  PRODUCT_DETAIL,
  PLACE_ORDER,
  MATERIAL_ENQUIRY,
  HOME_DELIVERY_CHECK,
  ABOUT_US,
  CONTACT_US,
  ADMIN_PANEL,
  CART,
  LOGIN_REGISTER
}

data class CustomerUser(
  val name: String = "Rajesh Kumar",
  val mobile: String = "+91 98765 12345",
  val email: String = "rajesh.kumar@gmail.com",
  val address: String = "Mathetu, Bhadohi - 221404",
  val isLoggedIn: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: UmeshRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = UmeshRepository(
      productDao = db.productDao(),
      dailyRateDao = db.dailyRateDao(),
      orderDao = db.orderDao(),
      enquiryDao = db.enquiryDao(),
      companyInfoDao = db.companyInfoDao(),
      cartDao = db.cartDao(),
      scope = viewModelScope
    )
  }

  // Navigation State
  private val _appPhase = MutableStateFlow(AppPhase.SPLASH)
  val appPhase: StateFlow<AppPhase> = _appPhase.asStateFlow()

  private val _currentTab = MutableStateFlow(BottomTab.HOME)
  val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

  private val _activeSubscreen = MutableStateFlow(ActiveSubscreen.NONE)
  val activeSubscreen: StateFlow<ActiveSubscreen> = _activeSubscreen.asStateFlow()

  // Selected Product for Details
  private val _selectedProduct = MutableStateFlow<ProductEntity?>(null)
  val selectedProduct: StateFlow<ProductEntity?> = _selectedProduct.asStateFlow()

  // Preselection for Order / Enquiry forms
  private val _preselectedProductForOrder = MutableStateFlow("")
  val preselectedProductForOrder: StateFlow<String> = _preselectedProductForOrder.asStateFlow()

  private val _preselectedTypeForOrder = MutableStateFlow("")
  val preselectedTypeForOrder: StateFlow<String> = _preselectedTypeForOrder.asStateFlow()

  private val _preselectedMaterialForEnquiry = MutableFlowSafe("")
  val preselectedMaterialForEnquiry: StateFlow<String> = _preselectedMaterialForEnquiry.asStateFlow()

  // Active Category Filter on Products Screen
  private val _selectedCategoryFilter = MutableStateFlow("ALL")
  val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

  // Customer Profile
  private val _customerUser = MutableStateFlow(CustomerUser())
  val customerUser: StateFlow<CustomerUser> = _customerUser.asStateFlow()

  // Alerts & Messages
  private val _alertMessage = MutableStateFlow<String?>(null)
  val alertMessage: StateFlow<String?> = _alertMessage.asStateFlow()

  private val _isOrderSuccess = MutableStateFlow(false)
  val isOrderSuccess: StateFlow<Boolean> = _isOrderSuccess.asStateFlow()

  private val _isEnquirySuccess = MutableStateFlow(false)
  val isEnquirySuccess: StateFlow<Boolean> = _isEnquirySuccess.asStateFlow()

  // Reactive Data from Repository
  val products: StateFlow<List<ProductEntity>> = repository.allProducts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val dailyRates: StateFlow<List<DailyRateEntity>> = repository.allDailyRates
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val orders: StateFlow<List<OrderEntity>> = repository.allOrders
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val enquiries: StateFlow<List<EnquiryEntity>> = repository.allEnquiries
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val companyInfo: StateFlow<CompanyInfoEntity?> = repository.companyInfo
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun setAppPhase(phase: AppPhase) {
    _appPhase.value = phase
  }

  fun selectTab(tab: BottomTab) {
    _currentTab.value = tab
    _activeSubscreen.value = ActiveSubscreen.NONE
  }

  fun openSubscreen(subscreen: ActiveSubscreen) {
    _activeSubscreen.value = subscreen
  }

  fun closeSubscreen() {
    _activeSubscreen.value = ActiveSubscreen.NONE
  }

  fun setSelectedCategoryFilter(category: String) {
    _selectedCategoryFilter.value = category
  }

  fun openProductDetails(product: ProductEntity) {
    _selectedProduct.value = product
    _activeSubscreen.value = ActiveSubscreen.PRODUCT_DETAIL
  }

  fun openOrderScreen(productName: String = "", materialType: String = "") {
    _preselectedProductForOrder.value = productName
    _preselectedTypeForOrder.value = materialType
    _activeSubscreen.value = ActiveSubscreen.PLACE_ORDER
  }

  fun openEnquiryScreen(materialName: String = "") {
    _preselectedMaterialForEnquiry.value = materialName
    _activeSubscreen.value = ActiveSubscreen.MATERIAL_ENQUIRY
  }

  fun submitOrder(
    name: String,
    mobile: String,
    address: String,
    product: String,
    materialType: String,
    quantity: String,
    deliveryDate: String,
    message: String = ""
  ) {
    viewModelScope.launch {
      repository.placeOrder(name, mobile, address, product, materialType, quantity, deliveryDate, message)
      _isOrderSuccess.value = true
    }
  }

  fun dismissOrderSuccess() {
    _isOrderSuccess.value = false
    _activeSubscreen.value = ActiveSubscreen.NONE
    _currentTab.value = BottomTab.ORDERS
  }

  fun submitEnquiry(
    name: String,
    mobile: String,
    material: String,
    quantity: String,
    message: String
  ) {
    viewModelScope.launch {
      repository.submitEnquiry(name, mobile, material, quantity, message)
      _isEnquirySuccess.value = true
    }
  }

  fun dismissEnquirySuccess() {
    _isEnquirySuccess.value = false
    _activeSubscreen.value = ActiveSubscreen.NONE
  }

  fun addToCart(product: ProductEntity, variant: String, quantity: Double) {
    viewModelScope.launch {
      repository.addToCart(product, variant, quantity)
      _alertMessage.value = "${product.name} added to cart!"
    }
  }

  fun removeCartItem(id: Long) {
    viewModelScope.launch {
      repository.removeCartItem(id)
    }
  }

  fun clearCart() {
    viewModelScope.launch {
      repository.clearCart()
    }
  }

  fun clearAlert() {
    _alertMessage.value = null
  }

  // Admin Actions
  fun updateRateByProduct(productName: String, newRateDisplay: String) {
    viewModelScope.launch {
      repository.updateRateByProduct(productName, newRateDisplay)
      _alertMessage.value = "Updated $productName rate to $newRateDisplay"
    }
  }

  fun updateDailyRateEntity(rate: DailyRateEntity) {
    viewModelScope.launch {
      repository.updateDailyRate(rate)
      _alertMessage.value = "Rate updated successfully!"
    }
  }

  fun addOrUpdateProduct(product: ProductEntity) {
    viewModelScope.launch {
      if (product.id == 0) {
        repository.addProduct(product)
        _alertMessage.value = "Product added successfully!"
      } else {
        repository.updateProduct(product)
        _alertMessage.value = "Product updated successfully!"
      }
    }
  }

  fun deleteProduct(id: Int) {
    viewModelScope.launch {
      repository.deleteProduct(id)
      _alertMessage.value = "Product removed."
    }
  }

  fun updateOrderStatus(orderId: Long, status: String) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, status)
      _alertMessage.value = "Order status updated to $status"
    }
  }

  fun updateEnquiryStatus(enquiryId: Long, status: String) {
    viewModelScope.launch {
      repository.updateEnquiryStatus(enquiryId, status)
      _alertMessage.value = "Enquiry marked as $status"
    }
  }

  fun updateCompanyInfo(info: CompanyInfoEntity) {
    viewModelScope.launch {
      repository.updateCompanyInfo(info)
      _alertMessage.value = "Company details updated successfully!"
    }
  }

  fun updateCustomerProfile(name: String, mobile: String, email: String, address: String) {
    _customerUser.value = CustomerUser(
      name = name,
      mobile = mobile,
      email = email,
      address = address,
      isLoggedIn = true
    )
    _alertMessage.value = "Profile updated!"
  }

  fun logoutCustomer() {
    _customerUser.value = CustomerUser(
      name = "Guest User",
      mobile = "",
      email = "",
      address = "",
      isLoggedIn = false
    )
  }
}

// Helper wrapper
private fun MutableFlowSafe(initial: String) = MutableStateFlow(initial)
