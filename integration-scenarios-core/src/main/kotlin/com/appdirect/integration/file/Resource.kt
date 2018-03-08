package com.appdirect.integration.file


import com.appdirect.jackson.json.Json
import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.io.InputStreamReader
import java.util.Scanner

class Resource : Closeable {

    private var reader: BufferedReader? = null
    private var scan: Scanner? = null

    val text: String
        get() {
            val content = StringBuilder()
            while (scan!!.hasNext()) {
                content.append(scan!!.next())
            }
            return content.toString()
        }

    val flattenedText: String?
        get() {
            val content = StringBuilder()
            while (scan!!.hasNext()) {
                content.append(scan!!.next())
            }

            return content.toString().replace("\\s".toRegex(), "")

        }

    @Throws(IOException::class)
    constructor(filePath: String) {
        val f = Resource::class.java.classLoader.getResource(filePath)
        reader = BufferedReader(
                InputStreamReader(f!!.openStream()))
        scan = Scanner(reader!!)
        scan!!.useDelimiter("\\r\\n")
    }

    @Throws(IOException::class)
    constructor(filePath: String, delimiter: String) {
        val f = Resource::class.java.classLoader.getResource(filePath)
        reader = BufferedReader(
                InputStreamReader(f!!.openStream()))
        scan = Scanner(reader!!)
        scan!!.useDelimiter(delimiter)
    }

    inline fun <reified T> getJsonAsObject(): T {
        return Json.fromJson(text)
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
    }
}
