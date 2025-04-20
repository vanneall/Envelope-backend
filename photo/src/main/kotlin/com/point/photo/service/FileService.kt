package com.point.photo.service

import com.point.photo.data.MediaEntity
import com.point.photo.data.MediaRepository
import com.point.photo.errors.codes.MediaError
import com.point.photo.errors.exceptions.FileNotFoundException
import com.point.photo.errors.exceptions.FileValidationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(private val mediaRepository: MediaRepository) {

    @Transactional(readOnly = true)
    fun getPhoto(id: Long): MediaEntity = mediaRepository.findById(id).orElseThrow { FileNotFoundException() }

    @Transactional
    fun saveFile(file: MultipartFile): Long {

        if (file.isEmpty) throw FileValidationException(MediaError.FILE_EMPTY)

        val originalFileName = file.originalFilename ?: throw FileValidationException(MediaError.FILE_NAME_MISSING)
        validateFileExtension(originalFileName)
        return mediaRepository.save(
            MediaEntity(
                fileName = originalFileName,
                contentType = file.contentType ?: "application/octet-stream",
                size = file.size,
                data = file.bytes
            )
        ).id
    }

    private fun validateFileExtension(fileName: String) {
        fileName
            .substringAfterLast('.', "")
            .lowercase()
            .let { extension ->
                if (extension !in ALLOWED_PHOTO_EXTENSION) throw FileValidationException(MediaError.UNSUPPORTED_MEDIA_CONTENT)
            }
    }

    @Transactional
    fun deletePhoto(id: Long) {
        if (!mediaRepository.existsById(id)) throw FileNotFoundException()
        mediaRepository.deleteById(id)
    }

    companion object {
        private val ALLOWED_PHOTO_EXTENSION = listOf("jpg", "jpeg", "png")
    }
}
