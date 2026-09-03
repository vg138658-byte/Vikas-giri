package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val category: String, // GITTI, BALU, SARIYA, CEMENT, EENT
  val name: String,
  val description: String,
  val availableTypes: String, // e.g., "10 MM, 20 MM, 40 MM, Stone Dust"
  val rate: Double,
  val unit: String, // "Per Ton", "Per Truck", "Per Kg", "Per Bag", "Per Piece"
  val stockStatus: String = "In Stock",
  val highlightBadge: String = "Quality Assured"
)

@Entity(tableName = "daily_rates")
data class DailyRateEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val productName: String,
  val rateDisplay: String,
  val unit: String,
  val note: String = "Market ke according rates change ho sakte hain.",
  val lastUpdated: String = "Today, 9:00 AM"
)

@Entity(tableName = "orders")
data class OrderEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val customerName: String,
  val mobileNumber: String,
  val deliveryAddress: String,
  val productName: String,
  val materialType: String,
  val quantity: String,
  val deliveryDate: String,
  val additionalMessage: String = "",
  val status: String = "Pending", // Pending, Accepted, Out for Delivery, Delivered, Rejected
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "enquiries")
data class EnquiryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val customerName: String,
  val mobileNumber: String,
  val material: String,
  val quantity: String,
  val message: String,
  val status: String = "New", // New, Contacted, Resolved
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "company_info")
data class CompanyInfoEntity(
  @PrimaryKey val id: Int = 1,
  val name: String = "UMESH ENTERPRISE",
  val tagline: String = "Aapke Har Nirman Ka Bharosemand Saathi",
  val ownerName: String = "Amit Giri",
  val phone: String = "+91 96164 10193",
  val whatsapp: String = "+91 96164 10193",
  val email: String = "umeshenterprise@email.com",
  val address: String = "Khargsenpatti Mathetu, Bhadohi - 221404",
  val servicePincodes: String = "221404, 221401, 221409, 221304, 221001"
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val productId: Int,
  val productName: String,
  val selectedType: String,
  val unit: String,
  val pricePerUnit: Double,
  val quantity: Double
)
