package com.point.photo.errors.exceptions

import com.point.photo.errors.codes.MediaError

class FileNotFoundException : RuntimeException(MediaError.FILE_NOT_FOUND.name)