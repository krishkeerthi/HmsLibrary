
package com.krish.hms.library.model

import com.krish.hms.library.model.BloodGroup
import com.krish.hms.library.model.Gender
import java.time.LocalDate

sealed class Person(
    var name: String,
    val age: Int,
    val gender: Gender,
    val dob: LocalDate,
    var address: String,
    var contact: String,
    val bloodGroup: BloodGroup,
    val Ssn: Int
    )