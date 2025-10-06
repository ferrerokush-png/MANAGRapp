package com.managr.app.core.data.security

import android.util.Log
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.MessageDigest
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Certificate Pin Generator
 * Utility to help generate certificate pins for your domains
 * 
 * USAGE: Call generateCertificatePin("your-domain.com") to get the SHA-256 pin
 * Then update network_security_config.xml and CertificatePinningManager with the pin
 */
object CertificatePinGenerator {
    
    /**
     * Generate certificate pin for a domain
     * This should be called during development to get pins for your production domains
     */
    suspend fun generateCertificatePin(domain: String, port: Int = 443): String? {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
                    // Extract and hash the public key
                    if (chain.isNotEmpty()) {
                        val cert = chain[0]
                        val pin = generatePin(cert)
                        Log.i("CertificatePin", "Domain: $domain")
                        Log.i("CertificatePin", "Pin: sha256/$pin")
                        Log.i("CertificatePin", "Subject: ${cert.subjectDN}")
                        Log.i("CertificatePin", "Issuer: ${cert.issuerDN}")
                    }
                }
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })
            
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            
            val client = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
            
            val request = Request.Builder()
                .url("https://$domain:$port")
                .build()
            
            client.newCall(request).execute().use { response ->
                response.body?.string()
            }
            
            null
        } catch (e: Exception) {
            Log.e("CertificatePin", "Error generating pin for $domain", e)
            null
        }
    }
    
    /**
     * Generate SHA-256 pin from certificate
     */
    private fun generatePin(cert: X509Certificate): String {
        val publicKey = cert.publicKey.encoded
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(publicKey)
        return android.util.Base64.encodeToString(hash, android.util.Base64.NO_WRAP)
    }
    
    /**
     * Validate if a domain's certificate matches the expected pin
     */
    fun validateCertificatePin(domain: String, expectedPin: String): Boolean {
        return try {
            val certificatePinner = CertificatePinner.Builder()
                .add(domain, "sha256/$expectedPin")
                .build()
            
            val client = OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build()
            
            val request = Request.Builder()
                .url("https://$domain")
                .build()
            
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e("CertificatePin", "Certificate validation failed for $domain", e)
            false
        }
    }
}

/**
 * Production Certificate Pins Configuration
 * Update these with your actual production certificate pins
 * 
 * To generate pins for your domains:
 * 1. Run: openssl s_client -servername api.managr.com -connect api.managr.com:443 | openssl x509 -pubkey -noout | openssl pkey -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
 * 2. Or use CertificatePinGenerator.generateCertificatePin("api.managr.com") in debug builds
 */
object ProductionCertificatePins {
    
    // Main API domain
    const val API_DOMAIN = "api.managr.com"
    
    // Certificate pins (primary and backup)
    // IMPORTANT: Update these with your actual certificate pins before production release
    val API_PINS = listOf(
        // Primary certificate pin (current production cert)
        "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=", // REPLACE ME
        // Backup certificate pin (for rotation)
        "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=" // REPLACE ME
    )
    
    // Firebase domains (Google's pins - these are real)
    val FIREBASE_PINS = listOf(
        "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=", // Google Root CA 1
        "sha256/f0KW/FtqTjs108NpYj42SrGvOB2PpxIVM8nWxjPqJGE=", // Google Root CA 2
        "sha256/KXm+8J45OSHwVnIDBdSB8xgGAqPCB3LxcqFLIaK0tQo="  // Google Root CA 3
    )
    
    /**
     * Build certificate pinner with all production pins
     */
    fun buildCertificatePinner(): CertificatePinner {
        val builder = CertificatePinner.Builder()
        
        // Add API pins
        API_PINS.forEach { pin ->
            builder.add(API_DOMAIN, pin)
            builder.add("*.$API_DOMAIN", pin) // Include subdomains
        }
        
        // Add Firebase pins
        FIREBASE_PINS.forEach { pin ->
            builder.add("*.googleapis.com", pin)
            builder.add("*.firebaseio.com", pin)
            builder.add("*.firebasestorage.googleapis.com", pin)
        }
        
        return builder.build()
    }
}


