package com.point.profiles.contacts.errors.exceptions

import com.point.profiles.contacts.errors.codes.ContactsError

class SelfDeleteException : RuntimeException(ContactsError.SELF_DELETE.name)