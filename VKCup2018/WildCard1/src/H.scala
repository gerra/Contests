import java.io.FileInputStream

object task extends App {
  val sc = new java.util.Scanner(System.in)
//  val sc = new java.util.Scanner(new FileInputStream("input.txt"))
  val n:Int = sc.nextInt
  val k:Int = sc.nextInt
  val a:List[Int] = (0 until n).map(_ => sc.nextInt()).toList
  val c:List[Char] = sc.next.toList
  val ac:List[(Int, Char)] = a zip c

  val r:List[Int] = ac.filter(x => x._2 == 'R').map(_._1).sorted.reverse
  val w:List[Int] = ac.filter(x => x._2 == 'W').map(_._1).sorted.reverse
  val o:List[Int] = ac.filter(x => x._2 == 'O').map(_._1).sorted.reverse

  var res:Int = -1
  if (o.nonEmpty && k >= 2) {
    if (r.nonEmpty && r.size + o.size >= k) {
      res = calc(r, o, k)
    }
    if (w.nonEmpty && w.size + o.size >= k) {
      res = math.max(res, calc(w, o, k))
    }
  }

  println(res)

  def calc(a: List[Int], b: List[Int], k:Int): Int = {
    var itA:Int = 1
    var itB:Int = 1
    var result = a.head + b.head
    while (itA + itB < k) {
      if (itB == b.size || itA < a.size && a(itA) >= b(itB)) {
        result += a(itA)
        itA += 1
      } else {
        result += b(itB)
        itB += 1
      }
    }
    result
  }
}