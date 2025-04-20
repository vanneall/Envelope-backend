package com.point.photo.errors.codes

enum class MediaError {
    // When multipart is empty
    FILE_EMPTY,
    // When file list is empty
    FILES_EMPTY,
    // When media content extension is not supported
    UNSUPPORTED_MEDIA_CONTENT,
    // When media is not found
    FILE_NOT_FOUND,
    // When file name is empty
    FILE_NAME_MISSING,
}