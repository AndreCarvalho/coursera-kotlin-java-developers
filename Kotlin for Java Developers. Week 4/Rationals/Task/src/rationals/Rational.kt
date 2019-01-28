package rationals

import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/-1098".toRational().toString() == "-13/122")

    println("-10".toRational().toString())

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

infix fun Int.divBy (denominator: Int): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())
infix fun BigInteger.divBy (denominator: BigInteger): Rational = Rational(this, denominator)
infix fun Long.divBy (denominator: Long): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())

fun String.toRational() : Rational {
    val components = this.split('/')
    val numerator = components[0].toBigInteger()
    val denominator = if (components.drop(1).any()) components[1].toBigInteger() else BigInteger.ONE
    return Rational(numerator, denominator)
}

class Rational(numerator: BigInteger, denominator: BigInteger = BigInteger.ONE) : Comparable<Rational> {
    private val numerator: BigInteger
    private val denominator: BigInteger

    init {
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException("Denominator can not be zero")
        }
        val gcd = numerator.gcd(denominator)
        var numerator = numerator.divide(gcd)
        var denominator = denominator.divide(gcd)

        val denominatorSignum = denominator.signum()

        if (denominatorSignum == -1){
            numerator = numerator.negate()
            denominator = denominator.negate()
        }

        this.numerator = numerator
        this.denominator = denominator
    }

    private fun reduce() : Double = numerator.toDouble() / denominator.toDouble()
    private fun invert() : Rational = Rational(denominator, numerator)

    operator fun unaryMinus() : Rational = Rational(-numerator, denominator)

    operator fun plus(other: Rational) : Rational {
        val lcmDenominator = other.denominator * denominator
        val otherNewNumerator = other.numerator * denominator
        val myNewNumerator = numerator * other.denominator

        return Rational(myNewNumerator + otherNewNumerator, lcmDenominator)
    }

    operator fun minus (other: Rational) : Rational = -other + this

    operator fun times(other: Rational) : Rational =
            Rational(numerator * other.numerator, denominator * other.denominator)

    operator fun div (other: Rational) : Rational = other.invert() * this

    override operator fun compareTo (other: Rational) = reduce().compareTo(other.reduce())

    override fun toString() = "$numerator" + ( if (denominator != BigInteger.ONE) "/$denominator" else "")

    override fun equals(other: Any?) =
            (other is Rational) && other.denominator == denominator && other.numerator == numerator
}
