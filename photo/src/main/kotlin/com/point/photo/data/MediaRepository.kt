package com.point.photo.data

import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<MediaEntity, Long>