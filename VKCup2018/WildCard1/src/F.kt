import java.io.InputStream
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun gcd(a : Int, b : Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}

fun main(args: Array<String>) {
    val reader = Scanner(System.`in`)
    val n = reader.nextInt()
    val points = ArrayList<Int>(n)
    for (i in 1..n) {
        points.add(reader.nextInt())
    }
    Collections.sort(points)

    val dists = ArrayList<Int>(n)
    var g = -1
    for (i in 0..n-2) {
        val d = points.get(i+1) - points.get(i)
        dists.add(d)
        if (g == -1) {
            g = d
        } else {
            g = gcd(g, d)
        }
    }
    var res = 0
    for (d in dists) {
        res += (d / g) - 1
    }
    println(res)
}