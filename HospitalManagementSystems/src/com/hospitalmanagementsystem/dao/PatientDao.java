package com.hospitalmanagementsystem.dao;

import java.util.List;

import com.hospitalmanagementsystem.entity.Patient;
import com.hospitalmanagementsystem.exceptions.DataAccessException;
import com.hospitalmanagementsystem.exceptions.InvalidPatientDataException;
import com.hospitalmanagementsystem.exceptions.PatientNotFoundException;

public interface PatientDao {
    boolean createPatient(Patient patient) throws DataAccessException, InvalidPatientDataException;
    Patient getPatientById(int patientId) throws PatientNotFoundException, DataAccessException;
    List<Patient> getAllPatients() throws DataAccessException;
    boolean updatePatient(Patient patient) throws DataAccessException, InvalidPatientDataException, PatientNotFoundException;
    boolean deletePatient(int patientId) throws DataAccessException, PatientNotFoundException;
}