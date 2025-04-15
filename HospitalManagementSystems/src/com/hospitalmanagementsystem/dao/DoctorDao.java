package com.hospitalmanagementsystem.dao;

import java.util.List;

import com.hospitalmanagementsystem.entity.Doctor;
import com.hospitalmanagementsystem.exceptions.DataAccessException;
import com.hospitalmanagementsystem.exceptions.DoctorNotFoundException;
import com.hospitalmanagementsystem.exceptions.InvalidDoctorDataException;

public interface DoctorDao {
    boolean createDoctor(Doctor doctor) throws DataAccessException, InvalidDoctorDataException;
    Doctor getDoctorById(int doctorId) throws DoctorNotFoundException, DataAccessException;
    List<Doctor> getAllDoctors() throws DataAccessException;
    boolean updateDoctor(Doctor doctor) throws DataAccessException, InvalidDoctorDataException, DoctorNotFoundException;
    boolean deleteDoctor(int doctorId) throws DataAccessException, DoctorNotFoundException;
}