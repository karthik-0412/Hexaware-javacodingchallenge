package com.hospitalmanagementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hospitalmanagementsystem.entity.Appointment;
import com.hospitalmanagementsystem.exceptions.AppointmentNotFoundException;
import com.hospitalmanagementsystem.exceptions.DataAccessException;
import com.hospitalmanagementsystem.util.DatabaseConnection;

public class AppointmentDaoImpl implements AppointmentDao {
    private Connection connection;

    public AppointmentDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) throws AppointmentNotFoundException, DataAccessException {
        String query = "SELECT * FROM appointment WHERE appointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Appointment(
                    rs.getInt("appointmentID"),
                    rs.getInt("patientID"),
                    rs.getInt("doctorID"),
                    rs.getDate("appointmentDate"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve appointment by ID"+ e);
        }
        throw new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId);
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(int patientId) throws DataAccessException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointmentID"),
                    rs.getInt("patientID"),
                    rs.getInt("doctorID"),
                    rs.getDate("appointmentDate"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve appointments for patient"+ e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(int doctorId) throws DataAccessException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE doctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointmentID"),
                    rs.getInt("patientID"),
                    rs.getInt("doctorID"),
                    rs.getDate("appointmentDate"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve appointments for doctor"+ e);
        }
        return appointments;
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) throws DataAccessException {
        String query = "INSERT INTO appointment (patientID, doctorID, appointmentDate, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            stmt.setString(4, appointment.getDescription());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to schedule appointment"+ e);
        }
    }

    @Override
    public boolean updateAppointment(Appointment appointment) throws DataAccessException {
        String query = "UPDATE appointment SET patientID = ?, doctorID = ?, appointmentDate = ?, description = ? WHERE appointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            stmt.setString(4, appointment.getDescription());
            stmt.setInt(5, appointment.getAppointmentId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update appointment"+ e);
        }
    }

    @Override
    public boolean cancelAppointment(int appointmentId) throws DataAccessException {
        String query = "DELETE FROM appointment WHERE appointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to cancel appointment"+ e);
        }
    }

	
}
