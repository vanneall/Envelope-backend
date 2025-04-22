package com.point.profiles.profile.errors.exceptions

import com.point.profiles.profile.errors.codes.ProfileCode

class UserAlreadyExistsException : RuntimeException(ProfileCode.USER_ALREADY_EXISTS.name)