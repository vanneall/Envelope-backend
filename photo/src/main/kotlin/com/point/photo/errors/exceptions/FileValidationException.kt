package com.point.photo.errors.exceptions

import com.point.photo.errors.codes.MediaError

class FileValidationException(mediaError: MediaError) : RuntimeException(mediaError.name)