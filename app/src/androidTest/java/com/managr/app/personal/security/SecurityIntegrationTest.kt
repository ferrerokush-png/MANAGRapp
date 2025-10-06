package com.managr.app.personal.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.managr.app.core.data.security.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Security Integration Tests
 * Verifies all security features work correctly
 */
@RunWith(AndroidJUnit4::class)
class SecurityIntegrationTest {
    
    private lateinit var context: Context
    private lateinit var encryptionManager: EncryptionManager
    private lateinit var securePreferences: SecurePreferences
    private lateinit var inputValidator: InputValidator
    private lateinit var jwtTokenManager: JwtTokenManager
    private lateinit var oauth2Manager: OAuth2Manager
    private lateinit var runtimeSecurityManager: RuntimeSecurityManager
    
    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        encryptionManager = EncryptionManager()
        securePreferences = SecurePreferences(context)
        inputValidator = InputValidator()
        jwtTokenManager = JwtTokenManager(securePreferences)
        oauth2Manager = OAuth2Manager(securePreferences, jwtTokenManager)
        runtimeSecurityManager = RuntimeSecurityManager(context)
    }
    
    @Test
    fun testEncryptionDecryption() {
        // Arrange
        val originalData = "Sensitive project data: My New Album Release"
        val keyAlias = "test_key"
        
        // Act
        val encrypted = encryptionManager.encrypt(originalData, keyAlias)
        val decrypted = encryptionManager.decrypt(encrypted, keyAlias)
        
        // Assert
        assertEquals(originalData, decrypted)
        assertNotEquals(originalData, String(encrypted))
    }
    
    @Test
    fun testSecurePreferencesEncryption() {
        // Arrange
        val testKey = "test_token"
        val testValue = "secret_access_token_12345"
        
        // Act
        securePreferences.putString(testKey, testValue)
        val retrieved = securePreferences.getString(testKey)
        
        // Assert
        assertEquals(testValue, retrieved)
        
        // Cleanup
        securePreferences.remove(testKey)
        assertNull(securePreferences.getString(testKey))
    }
    
    @Test
    fun testSqlInjectionPrevention() {
        // Arrange
        val maliciousInput = "'; DROP TABLE projects; --"
        
        // Act
        val containsInjection = inputValidator.containsSqlInjection(maliciousInput)
        val isValid = inputValidator.isValidProjectTitle(maliciousInput)
        
        // Assert
        assertTrue("Should detect SQL injection", containsInjection)
        assertFalse("Should reject malicious input", isValid)
    }
    
    @Test
    fun testXssPrevention() {
        // Arrange
        val xssInput = "<script>alert('xss')</script>"
        
        // Act
        val containsXss = inputValidator.containsXSS(xssInput)
        val sanitized = inputValidator.sanitizeString(xssInput)
        
        // Assert
        assertTrue("Should detect XSS", containsXss)
        assertFalse("Sanitized output should not contain <script>", sanitized.contains("<script>"))
        assertEquals("&lt;script&gt;alert('xss')&lt;/script&gt;", sanitized)
    }
    
    @Test
    fun testEmailValidation() {
        // Valid emails
        assertTrue(inputValidator.isValidEmail("user@example.com"))
        assertTrue(inputValidator.isValidEmail("test.user+tag@domain.co.uk"))
        
        // Invalid emails
        assertFalse(inputValidator.isValidEmail("invalid"))
        assertFalse(inputValidator.isValidEmail("@example.com"))
        assertFalse(inputValidator.isValidEmail("user@"))
    }
    
    @Test
    fun testPasswordStrength() {
        // Strong password
        val strong = inputValidator.isValidPassword("MyP@ssw0rd123")
        assertTrue(strong.isValid)
        assertEquals(InputValidator.PasswordStrength.STRONG, strong.strength)
        
        // Weak password
        val weak = inputValidator.isValidPassword("password")
        assertFalse(weak.isValid)
        assertEquals(InputValidator.PasswordStrength.WEAK, weak.strength)
    }
    
    @Test
    fun testFileValidation() {
        // Valid file names
        assertTrue(inputValidator.isValidFileName("artwork.jpg"))
        assertTrue(inputValidator.isValidFileName("my_album_cover.png"))
        
        // Invalid file names (path traversal attempts)
        assertFalse(inputValidator.isValidFileName("../etc/passwd"))
        assertFalse(inputValidator.isValidFileName("../../secrets.txt"))
        assertFalse(inputValidator.isValidFileName(".hidden"))
        
        // Valid MIME types
        assertTrue(inputValidator.isValidImageMimeType("image/jpeg"))
        assertTrue(inputValidator.isValidImageMimeType("image/png"))
        
        // Invalid MIME types
        assertFalse(inputValidator.isValidImageMimeType("application/x-executable"))
        assertFalse(inputValidator.isValidImageMimeType("text/html"))
    }
    
    @Test
    fun testJwtTokenValidation() {
        // Valid JWT format (header.payload.signature)
        val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        assertTrue(jwtTokenManager.validateTokenFormat(validToken))
        
        // Invalid JWT format
        assertFalse(jwtTokenManager.validateTokenFormat("invalid.token"))
        assertFalse(jwtTokenManager.validateTokenFormat("only-one-part"))
    }
    
    @Test
    fun testOAuth2PkceGeneration() {
        // Act
        val codeVerifier = oauth2Manager.generateCodeVerifier()
        val codeChallenge = oauth2Manager.generateCodeChallenge(codeVerifier)
        
        // Assert
        assertNotNull(codeVerifier)
        assertNotNull(codeChallenge)
        assertTrue("Code verifier should be at least 43 chars", codeVerifier.length >= 43)
        assertTrue("Code challenge should be base64 encoded", codeChallenge.length >= 43)
        assertNotEquals("Challenge should differ from verifier", codeVerifier, codeChallenge)
    }
    
    @Test
    fun testRuntimeSecurityCheck() = runBlocking {
        // Act
        val result = runtimeSecurityManager.performSecurityCheck()
        
        // Assert
        assertNotNull(result)
        
        // Log results
        if (!result.isSecure) {
            println("Security threats detected: ${result.threats.size}")
            result.threats.forEach { threat ->
                println("  - ${threat.description}")
            }
        }
        
        // Test individual checks
        val isDebuggable = runtimeSecurityManager.isDebuggable()
        val isDebuggerAttached = runtimeSecurityManager.isDebuggerAttached()
        
        println("Is debuggable: $isDebuggable")
        println("Is debugger attached: $isDebuggerAttached")
        
        // In test builds, app is debuggable
        // In production, this should be false
    }
    
    @Test
    fun testKeyRotation() {
        // Arrange
        val oldKeyAlias = "old_test_key"
        val newKeyAlias = "new_test_key"
        val testData = "Test data for key rotation"
        
        // Act - Create old key and encrypt
        val encryptedWithOld = encryptionManager.encrypt(testData, oldKeyAlias)
        
        // Rotate key
        encryptionManager.rotateKey(oldKeyAlias, newKeyAlias)
        
        // Encrypt with new key
        val encryptedWithNew = encryptionManager.encrypt(testData, newKeyAlias)
        val decryptedWithNew = encryptionManager.decrypt(encryptedWithNew, newKeyAlias)
        
        // Assert
        assertFalse(encryptionManager.keyExists(oldKeyAlias))
        assertTrue(encryptionManager.keyExists(newKeyAlias))
        assertEquals(testData, decryptedWithNew)
        
        // Cleanup
        encryptionManager.deleteKey(newKeyAlias)
    }
    
    @Test
    fun testSessionTimeout() {
        // Arrange
        val mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZXhwIjo5OTk5OTk5OTk5fQ.fake"
        
        // Act
        jwtTokenManager.saveTokens(mockToken, "refresh_token")
        jwtTokenManager.updateActivity()
        
        // Assert
        assertTrue("Session should be valid immediately", jwtTokenManager.isSessionValid())
        
        // Cleanup
        jwtTokenManager.clearTokens()
        assertFalse("Session should be invalid after clearing", jwtTokenManager.isSessionValid())
    }
    
    @Test
    fun testInputSanitization() {
        // Test HTML injection
        val htmlInjection = "<img src=x onerror=alert('xss')>"
        val sanitized = inputValidator.sanitizeString(htmlInjection)
        assertFalse(sanitized.contains("<img"))
        assertTrue(sanitized.contains("&lt;img"))
        
        // Test script injection
        val scriptInjection = "<script>malicious()</script>"
        val sanitizedScript = inputValidator.sanitizeString(scriptInjection)
        assertFalse(sanitizedScript.contains("<script>"))
        
        // Test SQL comment removal
        val sqlComment = "SELECT * FROM users -- comment"
        val sanitizedSql = inputValidator.sanitizeSqlParameter(sqlComment)
        assertFalse(sanitizedSql.contains("--"))
    }
    
    @Test
    fun testSecureRandomness() {
        // Generate multiple code verifiers
        val verifiers = (1..10).map { oauth2Manager.generateCodeVerifier() }
        
        // Assert all are unique (extremely high probability)
        val uniqueVerifiers = verifiers.toSet()
        assertEquals("All verifiers should be unique", verifiers.size, uniqueVerifiers.size)
        
        // Assert all are properly formatted
        verifiers.forEach { verifier ->
            assertTrue("Verifier should be base64 URL-safe", verifier.matches(Regex("^[A-Za-z0-9_-]+$")))
        }
    }
}


