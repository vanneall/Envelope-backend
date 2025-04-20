package com.point.photo.rest.controllers

import com.point.photo.rest.responses.FileCreatedResponse
import com.point.photo.service.FileService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/media")
class PhotoController(private val fileService: FileService) {

    @GetMapping("/{id}")
    fun getPhoto(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val photo = fileService.getPhoto(id)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(photo.contentType))
            .body(photo.data)
    }

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileCreatedResponse> {
        val savedPhoto = fileService.saveFile(file)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/media/{id}")
            .buildAndExpand(savedPhoto)
            .toUri()

        return ResponseEntity.created(location)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(FileCreatedResponse(savedPhoto))
    }

    @DeleteMapping("/{id}")
    fun deletePhoto(@PathVariable id: Long): ResponseEntity<Unit> {
        fileService.deletePhoto(id)
        return ResponseEntity.noContent().build()
    }
}