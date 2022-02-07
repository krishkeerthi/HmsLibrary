
package com.krish.hms.library.model

import com.krish.hms.library.helper.getBloodGroup
import com.krish.hms.library.helper.getGender
import com.krish.hms.library.helper.toInt
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import java.time.LocalDate

class Patient(
    name: String,
    age: Int,
    gender: Gender,
    dob: LocalDate,
    address: String,
    contact: String,
    bloodGroup: BloodGroup,
    Ssn: Int,
    val patientId: String,
    val firstRegistered: LocalDate,
    var lastRegistered: LocalDate
    ) : Person(name, age, gender, dob, address, contact, bloodGroup, Ssn){

    constructor(fields: List<String>) :
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
                LocalDate.parse(fields[9]),
                LocalDate.parse(fields[10]))

    override fun toString() = "$patientId|$name|$age|${gender.ordinal}|$dob|$address|$contact|${bloodGroup.ordinal}|" +
            "$Ssn|$firstRegistered|$lastRegistered\n"

    companion object{
        val validatePatientClass = Validation<Patient>{
            Patient::name {
                minLength(2)
                maxLength(100)
            }

            Patient::age ifPresent {
                minimum(0)
                maximum(150)
            }
        }
    }
}