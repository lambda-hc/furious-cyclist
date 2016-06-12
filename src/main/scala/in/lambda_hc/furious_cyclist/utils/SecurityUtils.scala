package in.lambda_hc.furious_cyclist.utils

import java.security.{SecureRandom, MessageDigest}
import java.util.Date

import io.undertow.server.handlers.{CookieImpl, Cookie}

/**
  * Created by vishnu on 11/6/16.
  */
object SecurityUtils {

  private val TOKEN_LENGTH = 45

  private val TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

  private val charLen = TOKEN_CHARS.length()

  private val secureRandom = new SecureRandom()

  def hash(text: String): String = {
    val sha256: MessageDigest = MessageDigest.getInstance("SHA-256")
    sha256.update(text.getBytes("UTF-8"))
    val digest = sha256.digest()
    String.format("%064x", new java.math.BigInteger(1, digest))
  }

  private def toHex(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString("")

  private def sha(s: String): String = toHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8")))

  private def md5(s: String): String = toHex(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8")))

  def generateToken(tokenLength: Int): String = new String((0 until tokenLength).map(i => TOKEN_CHARS(secureRandom.nextInt(charLen))).toArray)

  def generateMD5Token(tokenPrefix: String): String = md5(tokenPrefix + System.nanoTime() + generateToken(TOKEN_LENGTH))

  def generateSHAToken(tokenPrefix: String): String = sha(tokenPrefix + System.nanoTime() + generateToken(TOKEN_LENGTH))


  def createCookie(key: String, value: String) = {
    val cookie: Cookie = new CookieImpl(key, value)

    cookie.setDomain(null)
    cookie.setPath("/")
    // Setting cookie to expire in one week
    cookie.setExpires(new Date(System.currentTimeMillis() * 60 * 60 * 24 * 7))
    cookie.setMaxAge(Int.MaxValue)
  }

}
