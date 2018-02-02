package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@SinceVersion(162)
@DependsOn(LoginType::class)
class FriendSystem : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<LoginType>() } }

    class loginType : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<LoginType>() }
    }

    @DependsOn(FriendsList::class)
    class friendsList : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<FriendsList>() }
    }

    @DependsOn(IgnoreList::class)
    class ignoreList : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IgnoreList>() }
    }
}