package com.point.photo.data

import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<PhotoEntity, Long>