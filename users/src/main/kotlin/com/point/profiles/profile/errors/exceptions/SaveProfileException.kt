package com.point.profiles.profile.errors.exceptions

import com.point.profiles.profile.errors.codes.ProfileCode

class SaveProfileException : RuntimeException(ProfileCode.SAVE_PROFILE_ERROR.name)