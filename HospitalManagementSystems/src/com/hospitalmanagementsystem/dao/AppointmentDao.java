package com.hospitalmanagementsystem.dao;

import java.util.List;

import com.hospitalmanagementsystem.entity.Appointment;
import com.hospitalmanagementsystem.exceptions.AppointmentNotFoundException;
import com.hospitalmanagementsystem.exceptions.DataAccessException;

public interface AppointmentDao {
    Appointment getAppointmentById(int appointmentId) throws AppointmentNotFoundException, DataAccessException;
    List<Appointment> getAppointmentsForPatient(int patientId) throws DataAccessException;
    List<Appointment> getAppointmentsForDoctor(int doctorId) throws DataAccessException;
    boolean scheduleAppointment(Appointment appointment) throws DataAccessException;
    boolean updateAppointment(Appointment appointment) throws DataAccessException;
    boolean cancelAppointment(int appointmentId) throws DataAccessException;
}