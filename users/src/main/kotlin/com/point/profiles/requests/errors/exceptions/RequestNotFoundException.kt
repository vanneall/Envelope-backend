package com.point.profiles.requests.errors.exceptions

import com.point.profiles.requests.errors.codes.RequestCode

class RequestNotFoundException : RuntimeException(RequestCode.REQUEST_NOT_FOUND.name)