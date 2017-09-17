package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(BitBuffer.isaacCipher::class)
class IsaacCipher : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type == field<BitBuffer.isaacCipher>().type }
}