
package com.krish.hms.library.helper

import com.krish.hms.library.model.*
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun String.toInt() = this.toIntOrNull() ?: -1

val Boolean.toInt : Int
    get() = if(this) 1 else 0

fun Int.getBoolean(): Boolean{
    return when(this){
        0 -> false
        1 -> true
        else -> false
    }
}

fun String.toDateOrNull(): LocalDate?{
    return try {
        LocalDate.parse(this, formatter)
    }
    catch (e: DateTimeParseException){
        null
    }
}

fun getGender(value: Int) : Gender {
    return when(value){
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        2 -> Gender.OTHERS
        else -> Gender.OTHERS
    }
}

fun getDepartment(value: Int) : Department {
    return when(value){
        0 -> Department.DERMATOLOGY
        1 -> Department.ENT
        2 -> Department.OPHTHALMOLOGY
        3 -> Department.GENERAL
        else -> Department.GENERAL
    }
}

fun getBloodGroup(value: Int) : BloodGroup {
    return when(value){
        0 -> BloodGroup.APOSITIVE
        1 -> BloodGroup.ANEGATIVE
        2 -> BloodGroup.BPOSITIVE
        3 -> BloodGroup.BNEGATIVE
        4 -> BloodGroup.OPOSITIVE
        5 -> BloodGroup.ONEGATIVE
        6 -> BloodGroup.ABPOSITIVE
        7 -> BloodGroup.ABNEGATIVE
        else -> BloodGroup.OPOSITIVE
    }
}

fun getMedicineType(value: Int) : MedicineType {
    return when(value){
        0 -> MedicineType.TABLET
        1 -> MedicineType.DROPS
        2 -> MedicineType.SYRUP
        3 -> MedicineType.INHALER
        4 -> MedicineType.CREAM
        else -> MedicineType.TABLET
    }
}

fun getToday(): LocalDate = LocalDate.now()

fun generateId(holder: IdHolder): String{
    val prefix = when(holder){
        IdHolder.DOCTOR -> "DO"
        IdHolder.PATIENT -> "PA"
        IdHolder.CASE ->"CA"
        IdHolder.CONSULTATION ->"CO"
        IdHolder.MEDICINE -> "MD"
    }

    return prefix + UUID.randomUUID().toString().replace("-","")
}

fun getMeridian(value: Int) : Meridian {
    return when(value){
        0 -> Meridian.AM
        1 -> Meridian.PM
        else -> Meridian.PM
    }
}

fun getModule(value: Int): Modules{
    return when(value){
        0 -> Modules.ADDDOCTOR
        1 -> Modules.ADDPATIENT
        2 -> Modules.REGISTERCASE
        3 -> Modules.`HANDLE CONSULTATION`
        4 -> Modules.LISTDOCTORS
        5 -> Modules.LISTPATIENTS
        6 -> Modules.LISTCASES
        else -> Modules.EXIT
    }
}

fun getDoctorSelection(value: Int) : DoctorSelection{
    return when(value){
        0 -> DoctorSelection.ALL
        1 -> DoctorSelection.ID
        2 -> DoctorSelection.DEPARTMENT
        else -> DoctorSelection.ALL
    }
}

fun getPatientSelection(value: Int) : PatientSelection{
    return when(value){
        0 -> PatientSelection.ALL
        1 -> PatientSelection.ID
        2 -> PatientSelection.NAME
        else ->PatientSelection.ALL
    }
}


fun readFile(fileName: String) : List<String>{
    val file = "C:\\Users\\KRISH\\IdeaProjects\\HmsLibrary\\src\\main\\kotlin\\com\\krish\\hms\\library\\data\\$fileName"
    //Replace the path with your absolute path to data directory
    return File(file).readLines().drop(1)
}

fun writeFile(fileName: String, line: String){
    val file = "C:\\Users\\KRISH\\IdeaProjects\\HmsLibrary\\src\\main\\kotlin\\com\\krish\\hms\\library\\data\\$fileName"
    //Replace the path with your absolute path to data directory
    File(file).appendText(line)

}


