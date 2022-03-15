package com.example.hw16.data.local

import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Exception

class FileLocalDataSource {

    fun save(uri: String, bytes: ByteArray, override: Boolean = false) : Boolean {
        val file = File(uri)
        return save(file, bytes, override)
    }

    fun save(destinationFile: File, bytes: ByteArray,
             override: Boolean = false, waitUntilFileIsReady: Boolean = false) : Boolean {
        if (destinationFile.exists()) {
            if (override.not()) {
                return false
            } else {
                var succssed = destinationFile.delete()
                succssed = succssed and destinationFile.createNewFile()
                if (succssed.not()) {
                    return false
                }
            }
        } else if (destinationFile.createNewFile().not()) {
            return false
        }
        try {
            destinationFile.outputStream().use {
                it.write(bytes)
                it.flush()
            }
            if (waitUntilFileIsReady) {
                while (destinationFile.canRead().not()) {}
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun load(uri: String) : ByteArray? {
        val file = File(uri)
        return load(file)
    }

    fun load(file: File): ByteArray? {
        if (file.exists().not()) {
            return null
        }
        return try {
            file.inputStream().use {
                it.readBytes()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun <T : Serializable> writeObject(file: File, t: T) : Boolean {
        return try {
            ObjectOutputStream(file.outputStream()).use {
                it.writeUnshared(t)
                it.flush()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun <T : Serializable> readObject(file: File) : T? {
        return try {
            ObjectInputStream(file.inputStream()).use {
                it.readUnshared() as T
            }
        } catch (e: Exception) {
            null
        }
    }
}
