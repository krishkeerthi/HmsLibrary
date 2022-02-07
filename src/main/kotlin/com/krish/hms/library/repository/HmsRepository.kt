
package com.krish.hms.library.repository

import com.krish.hms.library.model.*
import java.time.LocalTime
import java.util.*

sealed class Result<out Success, out Failure>

data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()

object HmsRepository {
    //All repositories
    private val doctorRepository = DoctorRepository()
    private val patientRepository = PatientRepository()
    private val caseRepository = CaseRepository()
    private val consultationRepository = ConsultationRepository()
    private val medicineRepository = MedicineRepository()

    private val patientsCases = mutableMapOf<String, MutableList<String>>() // <patientId, ListOf<caseId>> patient's history of cases
    private val doctorsConsultations = mutableMapOf<String, MutableList<String>>() // <doctorId, ListOf<consultationId>> doctor's history of cases
    private val casesConsultations = mutableMapOf<String, MutableList<String>>() // caseId, ListOf<consultationId>> case's list of consultations
    private val doctorsPendingConsultations = mutableMapOf<String, Queue<String>>() // <doctorId, ListOf(consultationId)> doctor's current or pending consultations
    private val consultationsMedicines = mutableMapOf<String, MutableList<String>>() // <consultationId, ListOf(MedicationId) consultation's medications

    init {
        loadPatientsCases()
        loadConsultationsCasesDoctors()
        loadConsultationsMedicines()
    }

    //Public methods

    //For adding doctor
    fun checkDoctorExistence(ssn: Int) = doctorRepository.checkDoctorExistence(ssn)

    fun addDoctor(doctor: Doctor): Result<String, Exception> {
        if(checkDoctorExistence(doctor.Ssn))
            return Failure(Exception("SSN is already registered"))
        val validationResult = Doctor.validateDoctorClass(doctor)

        if(validationResult.errors.isNotEmpty())
            return Failure(Exception(validationResult.errors.toString()))

        doctorRepository.addDoctor(doctor)
        return Success("Doctor added successfully")
    }

    //For adding patient
    fun checkPatientExistence(ssn: Int) = patientRepository.checkPatientExistence(ssn)

    fun addPatient(patient: Patient): Result<String, Exception> {
        if(checkPatientExistence(patient.Ssn))
            return Failure(Exception("SSN is already registered"))
        val validationResult = Patient.validatePatientClass(patient)

        if(validationResult.errors.isNotEmpty())
            return Failure(Exception(validationResult.errors.toString()))

        patientRepository.addPatient(patient)
        return Success("Patient added successfully")
    }

    // For case registration

    fun getCase(caseId: String) = caseRepository.getCase(caseId)

    fun createNewCase(ssn: Int): Case?{
        if(!checkPatientExistence(ssn))
            return null

        val patientId = patientRepository.getPatientId(ssn)!! // We ensure that patient id exists by after condition

        val case = Case.createCase(patientId)
        caseRepository.addCase(case)

        addOrCreate(patientsCases, patientId, case.caseId)
        return case
    }


    fun assignDoctor(ssn: Int, issue: String, time: LocalTime, caseId: String): Result<String, Exception> {
        if(!caseRepository.isCaseIdExists(caseId)) //caseId == ""
            return Failure(Exception("Case id does not exists"))

        val case = caseRepository.getCase(caseId)!!

        val department = findDepartment(issue)
        val departmentDoctors = doctorRepository.getDepartmentDoctors(department)

        if(departmentDoctors.isEmpty())
            return Failure(Exception("Department has no doctors"))
        else{
            var minNoOfConsultations = Int.MAX_VALUE
            var assignedDoctorId: String? = null

            for(doctor in departmentDoctors){
                val pendingConsultations = getPendingConsultations(doctor.doctorId)
                val consultationsHandlingTime = pendingConsultations * 15  // 15 minutes is considered as an average time for handling case
                val (consultationsHours, consultationsMinutes) = getHoursAndMinutes(consultationsHandlingTime)

                if(time.plusHours(consultationsHours.toLong()).plusMinutes(consultationsMinutes.toLong()).
                    isBefore(doctor.endTime.minusMinutes(14))){

                    if(pendingConsultations < minNoOfConsultations){
                        minNoOfConsultations = pendingConsultations
                        assignedDoctorId = doctor.doctorId
                    }
                }
            }

            if(assignedDoctorId != null)
                manageConsultationsAndDoctors(assignedDoctorId, issue, case.caseId, department, ssn)
            else
                return Failure(Exception("Doctor engaged with other cases, unavailable to treat patients"))
        }
        return Success("Doctor assigned successfully")
    }

    //For handling consultation
    fun checkConsultationExistence(consultationId: String) = consultationRepository.isConsultationIdExists(consultationId)

    private fun isDoctorsPendingConsultationExists(doctorId: String) = doctorsPendingConsultations.containsKey(doctorId)

    fun getDoctorsFirstConsultation(doctorId: String): Result<Consultation, Exception> {
        if(isDoctorsPendingConsultationExists(doctorId)){
            val consultationId = doctorsPendingConsultations[doctorId]!!.peek() ?:
            return Failure(Exception("No consultation available"))
            if(consultationRepository.isConsultationIdExists(consultationId))
                return Success(consultationRepository.getConsultation(consultationId)!!) // consultation
            return Failure(Exception("Sorry data missing from record"))
        }
        return Failure(Exception("Doctor does not exist"))
    }

    fun addAssessment(consultationId: String, doctorId: String, assessment: String) : Result<String, Exception> {
        if(!consultationRepository.isConsultationIdExists(consultationId))
            return Failure(Exception("Consultation Id does not exist"))

        if(!doctorRepository.isDoctorIdExists(doctorId))
            return Failure(Exception("Doctor Id does not exist"))

        consultationRepository.addAssessment(consultationId, assessment)
        removeConsultation(doctorId)

        return Success("Assessment added successfully")
    }

    fun addMedicine(consultationId: String, medicine: Medicine): Result<String, Exception> {
        if(!consultationRepository.isConsultationIdExists(consultationId))
            return Failure(Exception("Consultation Id does not exist"))

        val validationResult = Medicine.validateMedicineClass(medicine)

        if(validationResult.errors.isNotEmpty())
            return Failure(Exception(validationResult.errors.toString()))

        medicineRepository.addMedicine(medicine)
        addOrCreate(consultationsMedicines, consultationId, medicine.medicineId)
        return Success("Medicine added successfully")
    }

    // For doctors list
    fun getAllDoctors() = doctorRepository.getAllDoctors()
    fun getDoctorById(doctorId: String) = doctorRepository.getDoctorById(doctorId)
    fun getDoctorByDepartment(department: Department) = doctorRepository.getDoctorsByDepartment(department)

    // For patients list
    fun getAllPatients() = patientRepository.getAllPatients()
    fun getPatientById(patientId: String) = patientRepository.getPatientById(patientId)
    fun getPatientByName(name: String) = patientRepository.getPatientsByName(name)

    // For case list
    fun getConsultations(caseId: String): Result<List<String>, Exception> {
        if(!casesConsultations.containsKey(caseId))
            return Failure(Exception("Case Id does not exist"))
        val consultations = casesConsultations[caseId]!!

        if(consultations.isEmpty())
            return Failure(Exception("No consultation registered for the case"))

        return Success(consultations)
    }

    fun getConsultation(consultationId: String) = consultationRepository.getConsultation(consultationId)

    fun getMedicines(consultationId: String) : Result<List<String>, Exception> {
        if(!consultationsMedicines.containsKey(consultationId))
            return Failure(Exception("Consultation Id does not exist"))
        val medicines = consultationsMedicines[consultationId]!!

        if(medicines.isEmpty())
            return Failure(Exception("No medicines prescribed for the consultation"))

        return Success(medicines)
    }

    fun getMedicine(medicineId: String) = medicineRepository.getMedicine(medicineId)

    // Private methods
    private fun getPendingConsultations(doctorId: String): Int{
        return doctorsPendingConsultations[doctorId]?.size ?: 0
    }

    private fun removeConsultation(doctorId: String){
        doctorsPendingConsultations[doctorId]!!.remove()
    }

    // No validation needed for this method, it is private and also called after the validations
    private fun manageConsultationsAndDoctors(doctorId: String, issue: String, caseId: String, department: Department, ssn: Int){
        //Create consultation
        val consultation = Consultation.createConsultation(caseId, doctorId, issue, department)
        consultationRepository.addConsultation(consultation)

        //Update case last visit date
        caseRepository.updateCaseLastVisit(caseId)

        //Update patient last visit date
        val patientId = patientRepository.getPatientId(ssn)!!
        patientRepository.updatePatientLastRegistered(patientId)

        addOrCreate(doctorsConsultations, doctorId, consultation.consultationId)
        addOrCreateQueue(doctorsPendingConsultations, doctorId, consultation.consultationId)
        addOrCreate(casesConsultations, caseId, consultation.consultationId)
    }

    private fun findDepartment(issue: String) : Department {
        issue.split(" ").forEach { word ->
            when(word){
                in mutableListOf("skin", "rashes", "spot") -> return Department.DERMATOLOGY
                in mutableListOf("eye", "vision", "sight") -> return Department.OPHTHALMOLOGY
                in mutableListOf("ear", "nose", "throat") -> return Department.ENT
            }
        }
        return Department.GENERAL
    }

    private fun getHoursAndMinutes(totalMinutes: Int): Pair<Int, Int>{
        val hours = totalMinutes/60
        val minutes = totalMinutes%60
        return Pair(hours, minutes)
    }


    private fun addOrCreate(map: MutableMap<String, MutableList<String>>, key: String, value: String){
        if(map.containsKey(key))
            map[key]?.add(value)
        else
            map[key] = mutableListOf(value)
    }

    private fun addOrCreateQueue(map: MutableMap<String, Queue<String>>, key: String, value: String){
        if(map.containsKey(key))
            map[key]?.add(value)
        else {
            map[key] = ArrayDeque()
            map[key]?.add(value)
        }
    }

    // Loading for map
    private fun loadPatientsCases(){
        for(case in caseRepository.cases.values)
            addOrCreate(patientsCases, case.patientId, case.caseId)
    }

    private fun loadConsultationsMedicines(){
        for(medicine in medicineRepository.medicines.values)
            addOrCreate(consultationsMedicines, medicine.consultationId, medicine.medicineId)
    }

    private fun loadConsultationsCasesDoctors(){
        for(consultation in consultationRepository.consultations.values){
            addOrCreate(casesConsultations, consultation.caseId, consultation.consultationId)
            addOrCreate(doctorsConsultations, consultation.doctorId, consultation.consultationId)
        }
    }


}