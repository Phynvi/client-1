package com.runesuite.client.updater.deob.jagex

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


object MethodOrigClassFinder : Deobfuscator {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = KotlinLogging.logger { }

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val dupFile = source.resolveSibling(source.fileName.toString() + ".static-methods-dup.json")
        check(Files.exists(dupFile))

        val dupMethods = mapper.readValue<List<SortedSet<String>>>(dupFile.toFile())
        val map = TreeMap<String, String>()

        classNodes.forEach { c ->
            for (m in c.methods) {
                val name = c.name + "." + m.name + m.desc
                val set = dupMethods.firstOrNull { it.contains(name) } ?: continue
                val classNames = set.minus(name).map { it.split(".").first() }.distinct()
                check(classNames.size == 1) { "name: $name, set: $set" }
                val realClassName = classNames.first()
                if (c.name == realClassName) continue
                map[name] = realClassName
            }
        }

        val classFile = source.resolveSibling(source.fileName.toString() + ".methods-orig-class.json")
        mapper.writeValue(classFile.toFile(), map)

        logger.debug { "Static method original classes found: ${map.size}" }

        writeJar(classNodes, destination)
    }
}