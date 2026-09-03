package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UmeshRepository(
  private val productDao: ProductDao,
  private val dailyRateDao: DailyRateDao,
  private val orderDao: OrderDao,
  private val enquiryDao: EnquiryDao,
  private val companyInfoDao: CompanyInfoDao,
  private val cartDao: CartDao,
  scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
  val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
  val allDailyRates: Flow<List<DailyRateEntity>> = dailyRateDao.getAllRates()
  val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
  val allEnquiries: Flow<List<EnquiryEntity>> = enquiryDao.getAllEnquiries()
  val companyInfo: Flow<CompanyInfoEntity?> = companyInfoDao.getCompanyInfo()
  val cartItems: Flow<List<CartItemEntity>> = cartDao.getCartItems()

  init {
    scope.launch {
      seedInitialDataIfNeeded()
    }
  }

  fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
    return productDao.getProductsByCategory(category)
  }

  suspend fun getProductById(id: Int): ProductEntity? {
    return productDao.getProductById(id)
  }

  suspend fun addProduct(product: ProductEntity): Long {
    return productDao.insertProduct(product)
  }

  suspend fun updateProduct(product: ProductEntity) {
    productDao.updateProduct(product)
  }

  suspend fun deleteProduct(id: Int) {
    productDao.deleteProductById(id)
  }

  suspend fun updateDailyRate(rate: DailyRateEntity) {
    dailyRateDao.updateRate(rate)
  }

  suspend fun updateRateByProduct(productName: String, rateDisplay: String) {
    val currentTime = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
    dailyRateDao.updateRateByProduct(productName, rateDisplay, currentTime)
  }

  suspend fun placeOrder(
    name: String,
    mobile: String,
    address: String,
    product: String,
    materialType: String,
    quantity: String,
    deliveryDate: String,
    message: String = ""
  ): Long {
    val order = OrderEntity(
      customerName = name,
      mobileNumber = mobile,
      deliveryAddress = address,
      productName = product,
      materialType = materialType,
      quantity = quantity,
      deliveryDate = deliveryDate,
      additionalMessage = message,
      status = "Pending",
      timestamp = System.currentTimeMillis()
    )
    return orderDao.insertOrder(order)
  }

  suspend fun updateOrderStatus(orderId: Long, status: String) {
    orderDao.updateOrderStatus(orderId, status)
  }

  suspend fun deleteOrder(orderId: Long) {
    orderDao.deleteOrder(orderId)
  }

  suspend fun submitEnquiry(
    name: String,
    mobile: String,
    material: String,
    quantity: String,
    message: String
  ): Long {
    val enquiry = EnquiryEntity(
      customerName = name,
      mobileNumber = mobile,
      material = material,
      quantity = quantity,
      message = message,
      status = "New",
      timestamp = System.currentTimeMillis()
    )
    return enquiryDao.insertEnquiry(enquiry)
  }

  suspend fun updateEnquiryStatus(enquiryId: Long, status: String) {
    enquiryDao.updateEnquiryStatus(enquiryId, status)
  }

  suspend fun updateCompanyInfo(info: CompanyInfoEntity) {
    companyInfoDao.insertOrUpdate(info)
  }

  suspend fun addToCart(product: ProductEntity, variant: String, quantity: Double) {
    cartDao.insertCartItem(
      CartItemEntity(
        productId = product.id,
        productName = product.name,
        selectedType = variant,
        unit = product.unit,
        pricePerUnit = product.rate,
        quantity = quantity
      )
    )
  }

  suspend fun removeCartItem(id: Long) {
    cartDao.deleteCartItem(id)
  }

  suspend fun clearCart() {
    cartDao.clearCart()
  }

  private suspend fun seedInitialDataIfNeeded() {
    val existingProducts = productDao.getAllProducts().first()
    if (existingProducts.isEmpty()) {
      val defaultProducts = listOf(
        // 1. GITTI
        ProductEntity(
          category = "GITTI",
          name = "GITTI (Stone Aggregate)",
          description = "Construction aur RCC work ke liye high-quality gitti available. Crushed blue-grey stone chips with high compressive strength for pillars, beams, slab casting & road construction.",
          availableTypes = "10 MM Gitti, 20 MM Gitti, 40 MM Gitti, Stone Dust, Stone Chips",
          rate = 1850.0,
          unit = "Per Ton",
          stockStatus = "In Stock",
          highlightBadge = "RCC Special"
        ),
        // 2. BALU
        ProductEntity(
          category = "BALU",
          name = "BALU (Construction Sand)",
          description = "Construction work ke liye different types ki balu available. Washed river sand with low silt content, ideal for masonry, foundation, brickwork and smooth wall plastering.",
          availableTypes = "Nadi Balu, Local Balu, Plaster Balu, Construction Balu",
          rate = 7500.0,
          unit = "Per Truck",
          stockStatus = "In Stock",
          highlightBadge = "High Silt Free"
        ),
        // 3. SARIYA
        ProductEntity(
          category = "SARIYA",
          name = "SARIYA (TMT Steel Rebar)",
          description = "Strong aur quality steel sariya available. High ductility and earthquake-resistant Fe 550D grade steel rebar for multi-story buildings and durable structures.",
          availableTypes = "6 MM, 8 MM, 10 MM, 12 MM, 16 MM, 20 MM, 25 MM",
          rate = 68.0,
          unit = "Per Kg",
          stockStatus = "In Stock",
          highlightBadge = "Fe 550D Grade"
        ),
        // 4. CEMENT
        ProductEntity(
          category = "CEMENT",
          name = "CEMENT (Quality Brands)",
          description = "Different brands aur types ka quality cement available. Fresh factory stock of UltraTech, Ambuja, ACC, Dalmia, and Bangur for superior strength & quick setting time.",
          availableTypes = "OPC Cement, PPC Cement, Premium Cement",
          rate = 380.0,
          unit = "Per Bag",
          stockStatus = "In Stock",
          highlightBadge = "Fresh Mill Stock"
        ),
        // 5. EENT
        ProductEntity(
          category = "EENT",
          name = "EENT / BRICKS (Red & Fly Ash)",
          description = "Construction ke liye quality bricks available. Kiln-fired red clay bricks with sharp edges and uniform baking, plus lightweight high-compressive Fly Ash blocks.",
          availableTypes = "1 Number Red Brick, 2 Number Red Brick, Fly Ash Bricks, Solid Concrete Blocks",
          rate = 9.5,
          unit = "Per Piece",
          stockStatus = "In Stock",
          highlightBadge = "Kiln Fired"
        )
      )
      productDao.insertAll(defaultProducts)
    }

    val existingRates = dailyRateDao.getAllRates().first()
    if (existingRates.isEmpty()) {
      val defaultRates = listOf(
        DailyRateEntity(
          productName = "Gitti",
          rateDisplay = "₹ 1,850",
          unit = "Per Ton",
          note = "Market ke according rates change ho sakte hain.",
          lastUpdated = "Today, 9:00 AM"
        ),
        DailyRateEntity(
          productName = "Balu",
          rateDisplay = "₹ 7,500",
          unit = "Per Truck",
          note = "Market ke according rates change ho sakte hain.",
          lastUpdated = "Today, 9:00 AM"
        ),
        DailyRateEntity(
          productName = "Sariya",
          rateDisplay = "₹ 68",
          unit = "Per Kg",
          note = "Market ke according rates change ho sakte hain.",
          lastUpdated = "Today, 9:00 AM"
        ),
        DailyRateEntity(
          productName = "Cement",
          rateDisplay = "₹ 380",
          unit = "Per Bag",
          note = "Market ke according rates change ho sakte hain.",
          lastUpdated = "Today, 9:00 AM"
        ),
        DailyRateEntity(
          productName = "Eent",
          rateDisplay = "₹ 9.50",
          unit = "Per Piece",
          note = "Market ke according rates change ho sakte hain.",
          lastUpdated = "Today, 9:00 AM"
        )
      )
      dailyRateDao.insertAll(defaultRates)
    }

    val currentCompany = companyInfoDao.getCompanyInfo().first()
    if (currentCompany == null || currentCompany.ownerName != "Amit Giri" || currentCompany.phone.contains("98765")) {
      companyInfoDao.insertOrUpdate(
        CompanyInfoEntity(
          id = 1,
          name = "UMESH ENTERPRISE",
          tagline = "Aapke Har Nirman Ka Bharosemand Saathi",
          ownerName = "Amit Giri",
          phone = "+91 96164 10193",
          whatsapp = "+91 96164 10193",
          email = "umeshenterprise@email.com",
          address = "Khargsenpatti Mathetu, Bhadohi - 221404",
          servicePincodes = "221404, 221401, 221409, 221304, 221001"
        )
      )
    }

    val existingOrders = orderDao.getAllOrders().first()
    if (existingOrders.isEmpty()) {
      // Seed sample initial orders so customer sees realistic order history
      orderDao.insertOrder(
        OrderEntity(
          customerName = "Rajesh Kumar",
          mobileNumber = "+91 98765 12345",
          deliveryAddress = "Station Road, Near Market, Bhadohi - 221401",
          productName = "GITTI (Stone Aggregate)",
          materialType = "20 MM Gitti",
          quantity = "10 Ton",
          deliveryDate = "Immediate (Within 24 Hrs)",
          additionalMessage = "RCC slab casting on Friday morning. Please deliver early.",
          status = "Accepted",
          timestamp = System.currentTimeMillis() - 86400000L
        )
      )
      orderDao.insertOrder(
        OrderEntity(
          customerName = "Rajesh Kumar",
          mobileNumber = "+91 98765 12345",
          deliveryAddress = "Station Road, Near Market, Bhadohi - 221401",
          productName = "BALU (Construction Sand)",
          materialType = "Nadi Balu",
          quantity = "1 Truck (Dala)",
          deliveryDate = "Tomorrow",
          additionalMessage = "Please ensure red river sand with zero clay.",
          status = "Delivered",
          timestamp = System.currentTimeMillis() - 172800000L
        )
      )
    }
  }
}
