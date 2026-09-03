package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    ProductEntity::class,
    DailyRateEntity::class,
    OrderEntity::class,
    EnquiryEntity::class,
    CompanyInfoEntity::class,
    CartItemEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun productDao(): ProductDao
  abstract fun dailyRateDao(): DailyRateDao
  abstract fun orderDao(): OrderDao
  abstract fun enquiryDao(): EnquiryDao
  abstract fun companyInfoDao(): CompanyInfoDao
  abstract fun cartDao(): CartDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "umesh_enterprise_database"
        ).fallbackToDestructiveMigration()
         .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
