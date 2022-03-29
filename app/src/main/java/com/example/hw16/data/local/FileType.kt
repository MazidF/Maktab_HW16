package com.example.hw16.data.local

enum class FileType {
    IMAGE_FILE {
        override val format = ".png"
        override val value = "images"
    },
    VOICE_FILE {
        override val format = ".mp3"
        override val value = "voice"
    };

    abstract val format: String
    abstract val value: String
}