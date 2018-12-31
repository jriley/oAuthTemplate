package dev.jriley

import java.util.regex.Pattern

private val pattern: Pattern by lazy { Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE) }

fun String.isValidEmail(): Boolean = this.isNotBlank() && pattern.matcher(this).matches()