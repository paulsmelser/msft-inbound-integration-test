package com.appdirect.integration.file


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.psmelser.jackson.json.Json
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.Scanner

class Resource : Closeable {
    private val filePath: String
    private var reader: BufferedReader? = null
    private lateinit var scan: Scanner

    val text: String
        get() {
            if (!scan.hasNext()) {
                initialize(filePath)
            }
            val content = StringBuilder()
            while (scan.hasNext()) {
                content.append(scan.next())
            }
            return content.toString()
        }

    val flattenedText: String?
        get() {
            val content = StringBuilder()
            while (scan.hasNext()) {
                content.append(scan!!.next())
            }

            return content.toString().replace("\\s".toRegex(), "")

        }

    @Throws(IOException::class)
    constructor(filePath: String) {
        this.filePath = filePath
        initialize(filePath)
    }

    private fun initialize(filePath: String) {
        val f = Resource::class.java.classLoader.getResource(filePath)
        reader = BufferedReader(
                InputStreamReader(f!!.openStream()))
        scan = Scanner(reader!!)
        scan.useDelimiter("\\r\\n")
    }

    @Throws(IOException::class)
    constructor(filePath: String, delimiter: String) {
        this.filePath = filePath
        val f = Resource::class.java.classLoader.getResource(filePath)
        reader = BufferedReader(
                InputStreamReader(f!!.openStream()))
        scan = Scanner(reader!!)
        scan.useDelimiter(delimiter)
    }

    inline fun <reified T> getJsonAsObject(): T {
        return Json.fromJson(text)
    }

    fun getAsJsonNode() : JsonNode {
        return Json.toJsonNode(text)
    }

    fun getAsObjectNode() : ObjectNode {
        return getAsJsonNode() as ObjectNode
    }

    @Throws(IOException::class)
    override fun close() {
        reader!!.close()
    }

    companion object {

        @Throws(ResourceReadException::class)
        fun parseFile(path: String): Resource {
            try {
                return Resource(path)
            } catch (e: IOException) {
                throw ResourceReadException(e)
            }

        }

        fun saveToNewFile(filename: String, text: String) {
            val file = File(filename)
            file.createNewFile()
            file.writeText(text)
        }
    }
}
