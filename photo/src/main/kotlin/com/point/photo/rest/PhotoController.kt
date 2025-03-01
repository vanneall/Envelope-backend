package com.point.photo.rest

import com.point.photo.service.PhotoService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/photos")
class PhotoController(private val photoService: PhotoService) {

    @PostMapping
    fun uploadPhoto(@RequestParam("photo") file: MultipartFile): ResponseEntity<Map<String, Any>> {
        val savedPhoto = photoService.savePhoto(file)
        return ResponseEntity.ok(mapOf("id" to savedPhoto.id))
    }

    @GetMapping("/{id}")
    fun getPhoto(@PathVariable id: Long): ResponseEntity<Any> {
        val photo = photoService.getPhoto(id) ?: return ResponseEntity.notFound().build()
        val headers = HttpHeaders().apply { add(HttpHeaders.CONTENT_TYPE, photo.contentType) }
        return ResponseEntity(photo.data, headers, HttpStatus.OK)
    }
}
