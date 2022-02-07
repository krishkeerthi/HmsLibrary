
package com.krish.hms.library.model

import com.krish.hms.library.helper.generateId
import com.krish.hms.library.helper.getToday
import java.time.LocalDate

class Case(
    val caseId: String,
    val patientId: String,
    val firstVisit: LocalDate,
    var lastVisit: LocalDate,
) {

    constructor(fields: List<String>) :
            this(
                fields[0],
                fields[1],
                LocalDate.parse(fields[2]),
                LocalDate.parse(fields[3]))

    override fun toString(): String {
        return "$caseId|$patientId|$firstVisit|$lastVisit\n"
    }

    companion object{
        fun createCase(patientId: String): Case {
            return Case(generateId(IdHolder.CASE), patientId, getToday(), getToday())
        }
    }
}