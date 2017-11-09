package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.GlobalTile
import com.runesuite.client.game.api.Scene
import com.runesuite.client.game.api.SceneTile
import com.runesuite.client.game.raw.Client.accessor

object LiveScene : Scene {

    override fun getCollisionFlags(sceneTile: SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return accessor.collisionMaps[sceneTile.plane].flags[sceneTile.x][sceneTile.y]
    }

    override val collisionFlags get() = accessor.collisionMaps.map { it.flags }.toTypedArray()

    override val renderFlags get() = accessor.tileRenderFlags

    override val heights get() = accessor.tileHeights

    override val base get() = GlobalTile(accessor.baseX, accessor.baseY, 0)

    override fun toString(): String {
        return "Scene.Live(raw=${base})"
    }
}