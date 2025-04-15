package com.hospitalmanagementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hospitalmanagementsystem.entity.Gender;
import com.hospitalmanagementsystem.entity.Patient;
import com.hospitalmanagementsystem.exceptions.DataAccessException;
import com.hospitalmanagementsystem.exceptions.InvalidPatientDataException;
import com.hospitalmanagementsystem.exceptions.PatientNotFoundException;
import com.hospitalmanagementsystem.util.DatabaseConnection;

public class PatientDaoImpl implements PatientDao {
    private Connection connection;

    public PatientDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createPatient(Patient patient) throws DataAccessException, InvalidPatientDataException {
    	if (patient.getFirstName() == null || patient.getLastName() == null || 
    		    patient.getDateOfBirth() == null || patient.getGender() == null || 
    		    patient.getContactNumber() == null || patient.getAddress() == null) {
    		    throw new InvalidPatientDataException("Invalid patient data");
    		}

        String query = "INSERT INTO patient (firstName, lastName, dateOfBirth, gender, contactNumber, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, new java.sql.Date(patient.getDateOfBirth().getTime()));
            stmt.setString(4, patient.getGender().name().replace('_', ' '));
            stmt.setString(5, patient.getContactNumber());
            stmt.setString(6, patient.getAddress());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create patient");
        }
    }

    @Override
    public Patient getPatientById(int patientId) throws PatientNotFoundException, DataAccessException {
        String query = "SELECT * FROM patient WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                    rs.getInt("patientID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getDate("dateOfBirth"),
                    Gender.valueOf(rs.getString("gender").toUpperCase()),
                    rs.getString("contactNumber"),
                    rs.getString("address")
                );
            } else {
                throw new PatientNotFoundException("Patient not found with ID: " + patientId);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve patient by ID");
        }
    }

    @Override
    public List<Patient> getAllPatients() throws DataAccessException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("patientID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getDate("dateOfBirth"),
                    Gender.valueOf(rs.getString("gender").toUpperCase()),
                    rs.getString("contactNumber"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all patients");
        }
        return patients;
    }

    @Override
    public boolean updatePatient(Patient patient) throws DataAccessException, InvalidPatientDataException, PatientNotFoundException {
        if (patient.getPatientId() == 0 || patient.getFirstName() == null || patient.getLastName() == null) {
            throw new InvalidPatientDataException("Invalid patient data");
        }

        String query = "UPDATE patient SET firstName = ?, lastName = ?, dateOfBirth = ?, gender = ?, contactNumber = ?, address = ? WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, new java.sql.Date(patient.getDateOfBirth().getTime()));
            stmt.setString(4, patient.getGender().name().replace('_', ' '));
            stmt.setString(5, patient.getContactNumber());
            stmt.setString(6, patient.getAddress());
            stmt.setInt(7, patient.getPatientId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new PatientNotFoundException("Patient not found with ID: " + patient.getPatientId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update patient");
        }
    }

    @Override
    public boolean deletePatient(int patientId) throws DataAccessException, PatientNotFoundException {
        String query = "DELETE FROM patient WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new PatientNotFoundException("Patient not found with ID: " + patientId);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete patient");
        }
    }

}