package com.point.photo.service

import com.point.photo.data.PhotoEntity
import com.point.photo.data.PhotoRepository
import com.point.photo.exceptions.FileSizeExceededException
import com.point.photo.exceptions.FileValidationException
import com.point.photo.exceptions.InvalidFileExtensionException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PhotoService(private val photoRepository: PhotoRepository) {

    fun savePhoto(file: MultipartFile): PhotoEntity {
        if (file.isEmpty) {
            throw FileValidationException("File is empty")
        }

        val originalFileName = file.originalFilename ?: ""
        val extension = originalFileName.substringAfterLast('.', "").lowercase()
        if (extension !in ALLOWED_PHOTO_EXTENSION) {
            throw InvalidFileExtensionException(
                "Invalid file extension. Allowed extensions: ${ALLOWED_PHOTO_EXTENSION.joinToString { ".$it" }}"
            )
        }

        if (file.size > MAX_FILE_SIZE) {
            throw FileSizeExceededException("File size exceeds the maximum allowed size of ${MAX_FILE_SIZE_MB}MB")
        }

        val bytes = file.bytes
        val photo = PhotoEntity(
            fileName = originalFileName,
            contentType = file.contentType ?: "application/octet-stream",
            size = file.size,
            data = bytes
        )
        return photoRepository.save(photo)
    }

    fun getPhoto(id: Long): PhotoEntity? {
        return photoRepository.findById(id).orElse(null)
    }

    companion object {
        private const val MAX_FILE_SIZE_MB = 100
        private const val MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024

        private val ALLOWED_PHOTO_EXTENSION = listOf("jpg", "jpeg", "png")
    }
}
