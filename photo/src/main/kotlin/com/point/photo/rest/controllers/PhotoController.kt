package com.point.photo.rest.controllers

import com.point.photo.rest.requests.DeleteFilesRequest
import com.point.photo.rest.responses.FileCreatedResponse
import com.point.photo.rest.responses.FilesCreatedResponse
import com.point.photo.service.FileService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
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
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadFile(@RequestPart("file") file: MultipartFile): ResponseEntity<FileCreatedResponse> {
        val savedPhoto = fileService.saveFile(file)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/media/{id}")
            .buildAndExpand(savedPhoto)
            .toUri()

        return ResponseEntity.created(location)
            .contentType(MediaType.APPLICATION_JSON)
            .body(FileCreatedResponse(savedPhoto))
    }

    @PostMapping(
        "/batch",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun uploadFiles(@RequestPart("files") files: List<MultipartFile>): ResponseEntity<FilesCreatedResponse> {
        val ids = fileService.saveFiles(files)
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(FilesCreatedResponse(ids))
    }

    @DeleteMapping("/{id}")
    fun deletePhoto(@PathVariable id: Long): ResponseEntity<Unit> {
        fileService.deleteFile(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun deleteFiles(@RequestBody request: DeleteFilesRequest): ResponseEntity<Unit> {
        fileService.deleteFiles(request.ids)
        return ResponseEntity.noContent().build()
    }

}