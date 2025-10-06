package com.managr.app.core.data.security

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure Database Factory with SQLCipher
 * Creates encrypted Room databases using SQLCipher
 */
@Singleton
class SecureDatabaseFactory @Inject constructor(
    private val securePreferences: SecurePreferences
) {
    
    /**
     * Create encrypted database with SQLCipher
     * 
     * @param context Application context
     * @param databaseClass Room database class
     * @param databaseName Name of the database
     * @return Configured database builder
     */
    fun <T : RoomDatabase> createEncryptedDatabase(
        context: Context,
        databaseClass: Class<T>,
        databaseName: String
    ): RoomDatabase.Builder<T> {
        // Get or generate database passphrase
        val passphrase = getDatabasePassphrase()
        
        // Create SQLCipher factory with passphrase
        val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))
        
        return Room.databaseBuilder(
            context.applicationContext,
            databaseClass,
            databaseName
        )
            .openHelperFactory(factory)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Database created for the first time
                }
                
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // Enable foreign key constraints
                    db.execSQL("PRAGMA foreign_keys=ON;")
                }
            })
    }
    
    /**
     * Get or generate secure database passphrase
     */
    private fun getDatabasePassphrase(): String {
        var passphrase = securePreferences.getString(KEY_DATABASE_PASSPHRASE)
        
        if (passphrase.isNullOrBlank()) {
            // Generate new secure passphrase
            passphrase = generateSecurePassphrase()
            securePreferences.putString(KEY_DATABASE_PASSPHRASE, passphrase)
        }
        
        return passphrase
    }
    
    /**
     * Generate cryptographically secure passphrase
     */
    private fun generateSecurePassphrase(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32) // 256-bit passphrase
        random.nextBytes(bytes)
        return android.util.Base64.encodeToString(
            bytes,
            android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
        )
    }
    
    /**
     * Change database passphrase (for key rotation)
     * 
     * WARNING: This requires migrating the entire database
     * Should be done carefully and with proper backup
     */
    fun rotatePassphrase(
        context: Context,
        databaseName: String,
        oldPassphrase: String,
        newPassphrase: String
    ) {
        val databaseFile = context.getDatabasePath(databaseName)
        
        if (databaseFile.exists()) {
            val db = SQLiteDatabase.openDatabase(
                databaseFile.absolutePath,
                oldPassphrase,
                null,
                SQLiteDatabase.OPEN_READWRITE
            )
            
            db.rawExecSQL("PRAGMA rekey = '$newPassphrase';")
            db.close()
            
            // Update stored passphrase
            securePreferences.putString(KEY_DATABASE_PASSPHRASE, newPassphrase)
        }
    }
    
    companion object {
        private const val KEY_DATABASE_PASSPHRASE = "database_passphrase"
    }
}

/**
 * Extension function to create encrypted database easily
 */
inline fun <reified T : RoomDatabase> SecureDatabaseFactory.buildEncryptedDatabase(
    context: Context,
    databaseName: String
): RoomDatabase.Builder<T> {
    return createEncryptedDatabase(context, T::class.java, databaseName)
}

