package com.point.profiles.contacts.errors.exceptions

import com.point.profiles.contacts.errors.codes.ContactsError

class UserNotInContactsException : RuntimeException(ContactsError.USER_NOT_IN_CONTACTS.name)