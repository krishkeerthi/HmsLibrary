
package com.krish.hms.library.repository

import com.krish.hms.library.helper.readFile
import com.krish.hms.library.helper.writeFile
import com.krish.hms.library.model.Department
import com.krish.hms.library.model.Doctor

internal class DoctorRepository {
    private val doctors = mutableMapOf<String, Doctor>()

    init {
        loadDoctors()
    }

    private fun loadDoctors(){
        val doctorFile = readFile("Doctors.txt")
        for(line in doctorFile){
            val doctor = Doctor(line.split('|'))
            doctors[doctor.doctorId] = doctor
        }
    }

    fun isDoctorIdExists(doctorId: String) = doctors.containsKey(doctorId)

    fun addDoctor(doctor: Doctor){

        doctors[doctor.doctorId] = doctor

        writeFile("Doctors.txt", doctor.toString())
    }

    fun checkDoctorExistence(ssn: Int) : Boolean{
        return doctors.values.find { it.Ssn == ssn } != null
    }

    fun getDepartmentDoctors(department: Department): List<Doctor>{
        return doctors.values.filter { it.department == department }
    }

    fun getDoctorById(id: String): List<Doctor> {
        val doctor = doctors.values.find { it.doctorId == id }
        return if(doctor != null)
            listOf(doctor)
        else listOf()
    }

    fun getDoctorsByDepartment(department: Department): List<Doctor>{
        return doctors.values.filter { it.department == department}
    }

    fun getAllDoctors() = doctors.values.distinct()

}