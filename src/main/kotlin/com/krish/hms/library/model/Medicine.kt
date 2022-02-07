
package com.krish.hms.library.model

import com.krish.hms.library.helper.getBoolean
import com.krish.hms.library.helper.getMedicineType
import com.krish.hms.library.helper.toInt
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength

class Medicine(
    val medicineId: String,
    val consultationId: String,
    val medicineName: String,
    val medicineType: MedicineType,
    val count: Int,
    val days: Int,
    val morning: Boolean,
    val afternoon: Boolean,
    val night: Boolean,
){

    constructor(fields: List<String>) :
            this(
                fields[0],
                fields[1],
                fields[2],
                getMedicineType(fields[3].toInt()),
                fields[4].toInt(),
                fields[5].toInt(),
                fields[6].toInt().getBoolean(),
                fields[7].toInt().getBoolean(),
                fields[8].toInt().getBoolean()
            )

    override fun toString(): String {
        return "$medicineId|$consultationId|$medicineName|${medicineType.ordinal}|$count|$days|" +
                "${morning.toInt}|${afternoon.toInt}|${night.toInt}\n"
    }

    companion object{
        val validateMedicineClass = Validation<Medicine>{
            Medicine::medicineName {
                minLength(2)
                maxLength(100)
            }

        }
    }
}