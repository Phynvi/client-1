package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2

// name length ??
class LoginType : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == String::class.type } == 1 }
            .and { it.instanceMethods.size == 1 }
}