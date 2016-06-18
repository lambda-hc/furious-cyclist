package in.lambda_hc.furious_cyclist.utils

/**
  * Created by vishnu on 18/6/16.
  */
object NthPrimeNumber {

  val primes = sieve(Stream.from(2)).take(49048).toArray

  def main(args: Array[String]): Unit = {
    println(primes(48959)+" "+primes(48960)+" "+primes(48961))
  }

  // Sieving integral numbers
  def sieve(s: Stream[Int]): Stream[Int] = {
    s.head #:: sieve(s.tail.filter(_ % s.head != 0))
  }

  // All primes as a lazy sequence


  // Dumping the first five primes
  print(primes.take(5).toList) // List(2, 3, 5, 7, 11)

}
