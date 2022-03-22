package com.example.hw16.data.local

import android.content.Context
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class FileLocalDataSource {

    fun getRoot(context: Context, fileType: FileType): File {
        return File(context.filesDir, fileType.value)
    }

    fun getFile(
        context: Context,
        fileType: FileType,
        fileName: String
    ): File? {
        val root = getRoot(context, fileType)
        if (root.exists()) {
            return File(root, fileName)
        }
        return null
    }

    fun deleteFile(
        uri: String
    ): Boolean {
        val file = File(uri)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

    fun save(
        context: Context,
        fileType: FileType,
        fileName: String,
        bytes: ByteArray,
        override: Boolean = false, waitUntilFileIsReady: Boolean = false
    ): String? {
        val root = getRoot(context, fileType)
        if (root.exists().not()) {
            root.mkdirs()
        }
        return save(File(root, fileName), bytes, override, waitUntilFileIsReady)
    }

    private fun save(
        destinationFile: File,
        bytes: ByteArray,
        override: Boolean = false,
        waitUntilFileIsReady: Boolean = false
    ): String? {
        if (destinationFile.exists()) {
            if (override.not()) {
                return null
            } else {
                var succssed = destinationFile.delete()
                succssed = succssed and destinationFile.createNewFile()
                if (succssed.not()) {
                    return null
                }
            }
        } else if (destinationFile.createNewFile().not()) {
            return null
        }
        try {
            destinationFile.outputStream().use {
                it.write(bytes)
                it.flush()
            }
            if (waitUntilFileIsReady) {
                while (destinationFile.canRead().not()) { }
            }
        } catch (e: Exception) {
            return null
        }
        return destinationFile.absolutePath
    }

    fun load(
        context: Context,
        fileType: FileType,
        fileName: String,
    ): ByteArray? {
        val root = getRoot(context, fileType)
        if (root.exists()) {
            return load(File(root, fileName))
        }
        return null
    }

    private fun load(file: File): ByteArray? {
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

    fun <T : Serializable> writeObject(file: File, t: T): Boolean {
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

    fun <T : Serializable> readObject(file: File): T? {
        return try {
            ObjectInputStream(file.inputStream()).use {
                it.readUnshared() as T
            }
        } catch (e: Exception) {
            null
        }
    }
}

