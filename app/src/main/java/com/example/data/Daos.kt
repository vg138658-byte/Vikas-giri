package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
  @Query("SELECT * FROM products ORDER BY id ASC")
  fun getAllProducts(): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE category = :category ORDER BY id ASC")
  fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
  suspend fun getProductById(id: Int): ProductEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProduct(product: ProductEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(products: List<ProductEntity>)

  @Update
  suspend fun updateProduct(product: ProductEntity)

  @Query("UPDATE products SET rate = :newRate WHERE category = :category")
  suspend fun updateCategoryRates(category: String, newRate: Double)

  @Query("DELETE FROM products WHERE id = :id")
  suspend fun deleteProductById(id: Int)
}

@Dao
interface DailyRateDao {
  @Query("SELECT * FROM daily_rates ORDER BY id ASC")
  fun getAllRates(): Flow<List<DailyRateEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(rates: List<DailyRateEntity>)

  @Update
  suspend fun updateRate(rate: DailyRateEntity)

  @Query("UPDATE daily_rates SET rateDisplay = :rateDisplay, lastUpdated = :lastUpdated WHERE productName = :productName")
  suspend fun updateRateByProduct(productName: String, rateDisplay: String, lastUpdated: String)
}

@Dao
interface OrderDao {
  @Query("SELECT * FROM orders ORDER BY timestamp DESC")
  fun getAllOrders(): Flow<List<OrderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity): Long

  @Query("UPDATE orders SET status = :status WHERE id = :orderId")
  suspend fun updateOrderStatus(orderId: Long, status: String)

  @Query("DELETE FROM orders WHERE id = :orderId")
  suspend fun deleteOrder(orderId: Long)
}

@Dao
interface EnquiryDao {
  @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
  fun getAllEnquiries(): Flow<List<EnquiryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEnquiry(enquiry: EnquiryEntity): Long

  @Query("UPDATE enquiries SET status = :status WHERE id = :enquiryId")
  suspend fun updateEnquiryStatus(enquiryId: Long, status: String)
}

@Dao
interface CompanyInfoDao {
  @Query("SELECT * FROM company_info WHERE id = 1 LIMIT 1")
  fun getCompanyInfo(): Flow<CompanyInfoEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(info: CompanyInfoEntity)
}

@Dao
interface CartDao {
  @Query("SELECT * FROM cart_items ORDER BY id DESC")
  fun getCartItems(): Flow<List<CartItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCartItem(item: CartItemEntity): Long

  @Query("DELETE FROM cart_items WHERE id = :id")
  suspend fun deleteCartItem(id: Long)

  @Query("DELETE FROM cart_items")
  suspend fun clearCart()
}
