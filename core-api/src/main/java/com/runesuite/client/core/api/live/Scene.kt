package com.runesuite.client.core.api.live

import com.hunterwb.kxtra.collections.array.deepCopyOf
import com.runesuite.client.core.raw.Client.accessor

/**
 * The loaded area that follows the local player.
 */
interface Scene {

    companion object {
        const val SIZE = 104
        const val PLANE_SIZE = 4
        val CENTER = com.runesuite.client.core.api.SceneTile(SIZE / 2, SIZE / 2, 0)
        val BASE = com.runesuite.client.core.api.SceneTile(0, 0, 0)
        val CORNERS = BASE.run { listOf(
                this,
                copy(x = SIZE - 1),
                copy(x = SIZE - 1, y = SIZE - 1),
                copy(y = SIZE - 1)) }
    }

    val base: com.runesuite.client.core.api.GlobalTile

    fun getHeight(sceneTile: com.runesuite.client.core.api.SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return heights[sceneTile.plane][sceneTile.x][sceneTile.y]
    }

    val heights: Array<Array<IntArray>>

    fun getTileHeight(position: com.runesuite.client.core.api.Position): Int {
        require(position.isLoaded) { position }
        var p = position.plane
        if (p < 3 && 0 != (getRenderFlags(com.runesuite.client.core.api.SceneTile(position.x, position.y, 1)).toInt() and 2)) {
            p++
        }
        val o = getHeight(com.runesuite.client.core.api.SceneTile(position.x, position.y, p))
        val ne = if (position.x != SIZE - 1 && position.y != SIZE - 1) getHeight(com.runesuite.client.core.api.SceneTile(1 + position.x, 1 + position.y, p)) else o
        val n = if (position.y != SIZE - 1) getHeight(com.runesuite.client.core.api.SceneTile(position.x, 1 + position.y, p)) else o
        val e = if (position.x != SIZE - 1) getHeight(com.runesuite.client.core.api.SceneTile(1 + position.x, position.y, p)) else o
        return position.subY * (ne * position.subX + n * (128 - position.subX) shr 7) +
                (128 - position.subY) * (position.subX * e + o * (128 - position.subX) shr 7) shr 7
    }

    fun getRenderFlags(sceneTile: com.runesuite.client.core.api.SceneTile): Byte {
        require(sceneTile.isLoaded) { sceneTile }
        return renderFlags[sceneTile.plane][sceneTile.x][sceneTile.y]
    }

    val renderFlags: Array<Array<ByteArray>>

    fun getCollisionFlags(sceneTile: com.runesuite.client.core.api.SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return collisionFlags[sceneTile.plane][sceneTile.x][sceneTile.y]
    }

    val collisionFlags: Array<Array<IntArray>>

    object Live : Scene {

        override fun getCollisionFlags(sceneTile: com.runesuite.client.core.api.SceneTile): Int {
            require(sceneTile.isLoaded) { sceneTile }
            return accessor.collisionMaps[sceneTile.plane].flags[sceneTile.x][sceneTile.y]
        }

        override val collisionFlags get() = accessor.collisionMaps.map { it.flags }.toTypedArray()

        override val renderFlags get() = accessor.tileRenderFlags

        override val heights get() = accessor.tileHeights

        override val base get() = com.runesuite.client.core.api.GlobalTile(accessor.baseX, accessor.baseY, 0)

        override fun toString(): String {
            return "Scene.Live(raw=$base)"
        }
    }

    fun copyOf(): Copy {
        return Copy(base, renderFlags.deepCopyOf(), heights.deepCopyOf(), collisionFlags.deepCopyOf())
    }

    class Copy(
            override val base: com.runesuite.client.core.api.GlobalTile,
            override val renderFlags: Array<Array<ByteArray>>,
            override val heights: Array<Array<IntArray>>,
            override val collisionFlags: Array<Array<IntArray>>
    ) : Scene {

        override fun toString(): String {
            return "Scene.Copy(raw=$base)"
        }
    }
}