package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XEntity

abstract class Entity(override val accessor: XEntity) : Wrapper(accessor) {

    abstract val modelPosition: Position

    abstract val orientation: Angle

    val model: Model? get() = accessor.model?.let { Model(it, modelPosition, orientation) }
}