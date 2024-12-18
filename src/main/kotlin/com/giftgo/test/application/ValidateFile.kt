package com.giftgo.test.application

import org.springframework.web.multipart.MultipartFile

interface ValidateFile {
    fun validate(file: MultipartFile)
}