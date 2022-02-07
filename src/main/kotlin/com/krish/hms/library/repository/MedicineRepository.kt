
package com.krish.hms.library.repository

import com.krish.hms.library.helper.readFile
import com.krish.hms.library.helper.writeFile
import com.krish.hms.library.model.Medicine

internal class MedicineRepository() {
    val medicines = mutableMapOf<String, Medicine>()

    init {
        loadMedicines()
    }

    private fun loadMedicines(){
        val medicineFile = readFile("Medicines.txt")
        for(line in medicineFile){
            val medicine = Medicine(line.split("|"))
            medicines[medicine.medicineId] = medicine
        }
    }

    fun getMedicine(medicineId: String) = medicines[medicineId]

    fun addMedicine(medicine: Medicine){
        medicines[medicine.medicineId] = medicine
        writeFile("Medicines.txt", medicine.toString())
    }
}