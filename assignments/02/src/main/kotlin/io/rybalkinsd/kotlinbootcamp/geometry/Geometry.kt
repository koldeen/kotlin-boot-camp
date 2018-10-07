package io.rybalkinsd.kotlinbootcamp.geometry

import kotlin.math.min
import kotlin.math.max
/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
}

/**
 * 2D point with integer coordinates
 */
data class Point(val x: Int,val y: Int) : Collider {
    override fun isColliding(other: Collider): Boolean =
            when (other){
                is Point -> this == other
                is Bar -> other.isColliding(this)
                else -> false
            }
}

/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
class Bar(firstCornerX: Int, firstCornerY: Int, secondCornerX: Int, secondCornerY: Int) : Collider {
    val bottomLeft = Point (min (firstCornerX, secondCornerX), min(firstCornerY,secondCornerY))
    val topRight = Point (max (firstCornerX, secondCornerX), max(firstCornerY,secondCornerY))

    val bottomRight
        get() = Point (topRight.x, bottomLeft.y)
    val topLeft
        get() = Point(bottomLeft.x, topRight.y)

    override fun isColliding(other: Collider): Boolean =
            when (other){
                is Point -> other.x in bottomLeft.x..topRight.x && other.y in bottomLeft.y..topRight.y
                is Bar -> listOf(bottomLeft, bottomRight, topLeft, topRight).any{other.isColliding(it)}||
                          listOf(other.bottomLeft, other.bottomRight, other.topLeft, other.topRight).any {this.isColliding(it)}
                else -> false
            }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bar

        if (bottomLeft != other.bottomLeft) return false
        if (topRight != other.topRight) return false

        return true
    }
}