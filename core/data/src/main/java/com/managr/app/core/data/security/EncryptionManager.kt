package com.managr.app.core.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encryption Manager using Android Keystore System
 * Implements AES-256-GCM encryption for sensitive data
 */
@Singleton
class EncryptionManager @Inject constructor() {
    
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }
    
    private val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
    
    /**
     * Encrypt data using AES-256-GCM
     * @param data Plain text data to encrypt
     * @param keyAlias Alias for the encryption key in Keystore
     * @return Encrypted data with IV prepended
     */
    fun encrypt(data: String, keyAlias: String = DEFAULT_KEY_ALIAS): ByteArray {
        val key = getOrCreateKey(keyAlias)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryption = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        
        // Prepend IV to encrypted data
        return iv + encryption
    }
    
    /**
     * Decrypt data using AES-256-GCM
     * @param encryptedData Encrypted data with IV prepended
     * @param keyAlias Alias for the decryption key in Keystore
     * @return Decrypted plain text
     */
    fun decrypt(encryptedData: ByteArray, keyAlias: String = DEFAULT_KEY_ALIAS): String {
        // Extract IV (first 12 bytes for GCM)
        val iv = encryptedData.sliceArray(0 until IV_SIZE)
        val cipherText = encryptedData.sliceArray(IV_SIZE until encryptedData.size)
        
        val key = getOrCreateKey(keyAlias)
        val spec = GCMParameterSpec(AUTH_TAG_SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        
        val decrypted = cipher.doFinal(cipherText)
        return String(decrypted, Charsets.UTF_8)
    }
    
    /**
     * Get or create a secret key in Android Keystore
     */
    private fun getOrCreateKey(keyAlias: String): SecretKey {
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(KEY_SIZE)
                .setUserAuthenticationRequired(false) // Set to true for biometric-protected keys
                .setRandomizedEncryptionRequired(true)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
        
        return keyStore.getKey(keyAlias, null) as SecretKey
    }
    
    /**
     * Delete a key from the keystore
     */
    fun deleteKey(keyAlias: String) {
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias)
        }
    }
    
    /**
     * Check if a key exists in the keystore
     */
    fun keyExists(keyAlias: String): Boolean {
        return keyStore.containsAlias(keyAlias)
    }
    
    /**
     * Rotate encryption key (delete old key and create new one)
     */
    fun rotateKey(oldKeyAlias: String, newKeyAlias: String) {
        deleteKey(oldKeyAlias)
        getOrCreateKey(newKeyAlias)
    }
    
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val DEFAULT_KEY_ALIAS = "managr_master_key"
        private const val KEY_SIZE = 256
        private const val IV_SIZE = 12 // GCM standard IV size
        private const val AUTH_TAG_SIZE = 128 // GCM standard auth tag size in bits
    }
}


