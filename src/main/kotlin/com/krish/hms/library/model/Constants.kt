
package com.krish.hms.library.model

enum class Modules {
    ADDDOCTOR,
    ADDPATIENT,
    REGISTERCASE,
    HANDLECONSULTATION,
    LISTDOCTORS,
    LISTPATIENTS,
    LISTCASES,
    EXIT,
}

enum class Department {
    DERMATOLOGY,
    ENT,
    OPHTHALMOLOGY,
    GENERAL,
}

enum class Gender {
    MALE,
    FEMALE,
    OTHERS,
}

enum class IdHolder {
    DOCTOR,
    PATIENT,
    CASE,
    CONSULTATION,
    MEDICINE,
}

enum class BloodGroup{
    APOSITIVE,
    ANEGATIVE,
    BPOSITIVE,
    BNEGATIVE,
    OPOSITIVE,
    ONEGATIVE,
    ABPOSITIVE,
    ABNEGATIVE,
}

enum class MedicineType{
    TABLET,
    DROPS,
    SYRUP,
    INHALER,
    CREAM,
    OINTMENT,
    POWDER,
}

enum class Meridian{
    AM,
    PM,
}

enum class DoctorSelection{
    ALL,
    ID,
    DEPARTMENT,
}

enum class PatientSelection{
    ALL,
    ID,
    NAME,
}

