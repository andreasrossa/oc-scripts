import sun.security.ec.point.ProjectivePoint
import kotlin.math.abs

// Nodes are Integers

typealias Node = Int
typealias Cost = Int
typealias Path = List<Node>
typealias Coordinates = Pair<Int, Int>
typealias Layout = Map<Node, Coordinates>

typealias Neighbours = Map<Node, List<Node>> // direct neighbours (edges)
typealias Nodes = Layout // Layout (Node -> X,Y)

// We boutta 'calc the manhattan dist
fun cost(a: Node, b: Node, nodes: Layout): Cost {
    val nA = nodes[a] ?: error("Node doesnt exist")
    val nB = nodes[b] ?: error("Node doesnt exist")
    val dist = abs(nA.first - nB.first) + abs(nA.second - nB.second)
    return dist//"Manhattan" Distance
}

fun russianMan(
    start: Node,
    end: Node,
    neighbours: Neighbours,
    nodes: Layout,
    pathList: MutableList<Path>? = mutableListOf()
): Path? {
    val finished: MutableSet<Node> = mutableSetOf()
    val costs: MutableMap<Node, Cost> = mutableMapOf(start to 0) // Cost from Starting point to Key
    val paths: MutableMap<Node, Path> = mutableMapOf(start to listOf(start)) // Path from Starting point to Key

    while (true) {
        val current = costs.filter { it.key !in finished }.minBy { it.value }?.key
            ?: break // The closest unfinished node
        println("Current Node: $current")
        if (end == current) {
            return paths[end]
        }

        val n = neighbours[current]
        if (n == null || n.isEmpty()) error("Separated Nodes or Ill-Defined")

        // Check costs for all neighbours
        n.forEach {
            println("|  Processing from $current to $it")
            val edgeCost = cost(current, it, nodes)
            val newCost = edgeCost + costs[current]!! // Cost up to this point
            val oldCost = costs[it] // Cost up to this point (old)
            val newPath = paths[current]!! + it

            println("|  |  Cost of $current to $it = $edgeCost")

            if (oldCost == null) { // Hasnt been visited yet
                costs[it] = newCost //
                paths[it] = newPath
                println("|  |  Encountered $it for first time (cost=$newCost, path=$newPath)")
            } else {
                if (newCost < oldCost) { // Cheaper?
                    println("|  |  Updating $it with (cost=$newCost, path=$newPath)")
                    costs[it] = newCost // Update with cheaper route
                    paths[it] = newPath
                } else {
                    println("|  |  Nothing to do")
                }
            }
        }
        finished.add(current)
        println("Marking $current as finished")
        pathList?.removeAll { true }
        pathList?.addAll(paths.map { it.value })
    }

    return null
}

fun main() {
    val neighbours = mapOf(
        0 to listOf(1, 4, 7),
        1 to listOf(2, 0),
        2 to listOf(3,1,9),
        3 to listOf(2),
        4 to listOf(0,5),
        5 to listOf(4, 6, 12),
        6 to listOf(5),
        7 to listOf(8,11,0),
        8 to listOf(7,9),
        9 to listOf(10,8,2),
        10 to listOf(9),
        11 to listOf(7,12),
        12 to listOf(11,5,13),
        13 to listOf(12),
        14 to listOf(7,15,18),
        15 to listOf(16,14),
        16 to listOf(17,15),
        17 to listOf(17),
        18 to listOf(14,19),
        19 to listOf(18,20),
        20 to listOf(20)
    )
    val nodes = mapOf(
        0 to (0 to 0),
        1 to (-1 to 0),
        2 to (-3 to 0),
        3 to (-4 to 0),
        4 to (1 to 0),
        5 to (3 to 0),
        6 to (4 to 0),
        7 to (0 to 3),
        8 to (-1 to 3),
        9 to (-3 to 3),
        10 to (-4 to 3),
        11 to (1 to 3),
        12 to (3 to 3),
        13 to (4 to 3),
        14 to (0 to 7),
        15 to (-1 to 7),
        16 to (-3 to 7),
        17 to (-4 to 7),
        18 to (1 to 7),
        20 to (3 to 7),
        20 to (4 to 7)
    )
    val path = russianMan(3, 13, neighbours, nodes)

    if (path == null) {
        println("No Path Found")
    } else {
        println("Path found: $path")
    }
}
