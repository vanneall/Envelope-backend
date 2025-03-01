package com.point.profiles.errors.exceptions

class UserNotFoundException(id: String) : RuntimeException("User with id: $id not found")