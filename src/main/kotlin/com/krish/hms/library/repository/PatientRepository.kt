
package com.krish.hms.library.repository

import com.krish.hms.library.helper.getToday
import com.krish.hms.library.helper.readFile
import com.krish.hms.library.helper.writeFile
import com.krish.hms.library.model.Patient

internal class PatientRepository() {
    private val patients = mutableMapOf<String, Patient>()

    init {
        loadPatients()
    }

    private fun loadPatients(){
        val patientFile = readFile("Patients.txt")
        for(line in patientFile){
            val patient = Patient(line.split('|'))
            patients[patient.patientId] = patient
        }
    }

    fun addPatient(patient: Patient){
        patients[patient.patientId] = patient
        writeFile("Patients.txt", patient.toString())
    }

    fun checkPatientExistence(ssn: Int) : Boolean{
        return patients.values.find { it.Ssn == ssn } != null
    }

    fun getPatientId(ssn: Int): String?{
        return patients.values.find { it.Ssn == ssn }?.patientId
    }

    fun isPatientIdExists(caseId: String) = patients.containsKey(caseId)

    fun updatePatientLastRegistered(patientId: String){
        if(isPatientIdExists(patientId))
            patients[patientId]!!.lastRegistered = getToday()
    }

    fun getPatientsByName(name: String): List<Patient>{
        return patients.values.filter { it.name.lowercase() == name.lowercase()}
    }

    fun getAllPatients() = patients.values.distinct()

    fun getPatientById(id: String): List<Patient> {
        val patient = patients.values.find { it.patientId == id }
        return if(patient != null)
            listOf(patient)
        else listOf()
    }
}