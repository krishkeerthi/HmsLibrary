
package com.krish.hms.library.repository

import com.krish.hms.library.helper.readFile
import com.krish.hms.library.helper.writeFile
import com.krish.hms.library.model.Consultation

internal class ConsultationRepository() {
    val consultations = mutableMapOf<String, Consultation>()

    init {
        loadConsultations()
    }

    private fun loadConsultations(){
        val consultationFile = readFile("Consultations.txt")
        for(line in consultationFile){
            val consultation = Consultation(line.split('|'))
            consultations[consultation.consultationId] = consultation
        }
    }

    fun addConsultation(consultation: Consultation){
        consultations[consultation.consultationId] = consultation
    }

    fun isConsultationIdExists(consultationId: String) = consultations.containsKey(consultationId)

    fun getConsultation(consultationId: String) = consultations[consultationId]

    fun addAssessment(consultationId: String, assessment: String){
        val consultation = consultations[consultationId] ?: return
//        return uiHandler.writeData("Consultation id does not exists".changeColor(LogLevel.ERROR))
        consultation.assessment = assessment

        writeFile("Consultations.txt", consultation.toString())
    }

}