
package com.krish.hms.library.model

import com.krish.hms.library.helper.*
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import java.time.LocalDate
import java.time.LocalTime

class Doctor(
    name: String,
    gender: Gender,
    dob: LocalDate,
    address: String,
    contact: String,
    bloodGroup: BloodGroup,
    Ssn: Int,
    val doctorId: String,
    val department: Department,
    var yearsOfExperience: Int,
    var startTime: LocalTime,
    var endTime: LocalTime,

    ) : Person(name, gender, dob, address, contact, bloodGroup, Ssn){

    constructor(fields: List<String>):
            this(
                fields[1],
                getGender(fields[2].toInt()),
                LocalDate.parse(fields[3]),
                fields[4],
                fields[5],
                getBloodGroup(fields[6].toInt()),
                fields[7].toInt(),
                fields[0],
                getDepartment(fields[8].toInt()),
                fields[9].toInt(),
                LocalTime.parse(fields[10]),
                LocalTime.parse(fields[11]),
                )

    override fun toString(): String {
        return "$doctorId|$name|${gender.ordinal}|$dob|$address|$contact|${bloodGroup.ordinal}|$Ssn|${department.ordinal}|" +
                "$yearsOfExperience|$startTime|$endTime\n"
    }

    companion object{
        val validateDoctorClass = Validation<Doctor>{
            Doctor::name {
                minLength(2)
                maxLength(100)
            }

            Doctor::age ifPresent {
                minimum(0)
                maximum(150)
            }
        }
    }
}