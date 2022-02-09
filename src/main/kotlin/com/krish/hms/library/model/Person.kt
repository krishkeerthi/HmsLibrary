
package com.krish.hms.library.model

import com.krish.hms.library.helper.getToday
import java.time.LocalDate

sealed class Person(
    val name: String,
    val gender: Gender,
    val dob: LocalDate,
    var address: String,
    var contact: String,
    val bloodGroup: BloodGroup,
    val Ssn: Int,
    ){
    val age: Int
        get() = getToday().year - dob.year
}