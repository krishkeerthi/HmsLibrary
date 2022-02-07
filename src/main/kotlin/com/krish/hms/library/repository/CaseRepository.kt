
package com.krish.hms.library.repository

import com.krish.hms.library.helper.getToday
import com.krish.hms.library.helper.readFile
import com.krish.hms.library.helper.writeFile
import com.krish.hms.library.model.Case


internal class CaseRepository() {
    val cases = mutableMapOf<String, Case>()

    init {
        loadCases()
    }

    private fun loadCases(){
        val caseFile = readFile("Cases.txt")
        for(line in caseFile){
            val case = Case(line.split('|'))
            cases[case.caseId] = case
        }
    }

    fun addCase(case: Case){
        cases[case.caseId] = case
        writeFile("Cases.txt", case.toString())
    }

    fun isCaseIdExists(caseId: String) = cases.containsKey(caseId)

    fun getCase(caseId: String) = cases[caseId]

    fun updateCaseLastVisit(caseId: String){
        if(isCaseIdExists(caseId))
            cases[caseId]!!.lastVisit = getToday()
    }

}