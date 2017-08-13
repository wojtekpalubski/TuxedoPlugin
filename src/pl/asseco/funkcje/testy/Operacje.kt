package pl.asseco.funkcje.testy

fun operation(x: Int, y: Int, op: (Int, Int) -> Int): Int {
    return op(x, y)
}

fun sum(x: Int, y: Int) = x + y
fun mul(x: Int, y: Int) = x * y
fun main(args: Array<String>) {
    println(operation(3, 2, ::sum))
    println(operation(3, 2, ::mul))
    println(operation(3, 2, {x,y->x+y}))
}