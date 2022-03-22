package com.example.hw16.data.local

enum class FileType {
    IMAGE_FILE {
        override val value = "images"
    },
    VOICE_FILE {
        override val value = "voice"
    };

    abstract val value: String
}