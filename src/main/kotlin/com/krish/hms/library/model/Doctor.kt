
package com.krish.hms.library.model

import com.krish.hms.library.helper.getBloodGroup
import com.krish.hms.library.helper.getDepartment
import com.krish.hms.library.helper.getGender
import com.krish.hms.library.helper.toInt
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import java.time.LocalDate
import java.time.LocalTime

class Doctor(
    name: String,
    age: Int,
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

    ) : Person(name, age, gender, dob, address, contact, bloodGroup, Ssn){

    constructor(fields: List<String>):
            this(
                fields[1],
                fields[2].toInt(),
                getGender(fields[3].toInt()),
                LocalDate.parse(fields[4]),
                fields[5],
                fields[6],
                getBloodGroup(fields[7].toInt()),
                fields[8].toInt(),
                fields[0],
                getDepartment(fields[9].toInt()),
                fields[10].toInt(),
                LocalTime.parse(fields[11]),
                LocalTime.parse(fields[12])
            )

    override fun toString(): String {
        return "$doctorId|$name|$age|${gender.ordinal}|$dob|$address|$contact|${bloodGroup.ordinal}|$Ssn|${department.ordinal}|" +
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