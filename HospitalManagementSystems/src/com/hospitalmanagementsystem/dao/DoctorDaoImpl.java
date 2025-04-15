package com.hospitalmanagementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hospitalmanagementsystem.entity.Doctor;
import com.hospitalmanagementsystem.exceptions.DataAccessException;
import com.hospitalmanagementsystem.exceptions.DoctorNotFoundException;
import com.hospitalmanagementsystem.exceptions.InvalidDoctorDataException;
import com.hospitalmanagementsystem.util.DatabaseConnection;

public class DoctorDaoImpl implements DoctorDao {
    private Connection connection;

    public DoctorDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createDoctor(Doctor doctor) throws DataAccessException, InvalidDoctorDataException {
    	if (doctor.getFirstName() == null || doctor.getLastName() == null || 
    		    doctor.getSpecialization() == null || doctor.getContactNumber() == null) {
    		    throw new InvalidDoctorDataException("Invalid doctor data");
    		}

        String query = "INSERT INTO doctor (firstName, lastName, specialization, contactNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getContactNumber());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create doctor"+ e);
        }
    }

    @Override
    public Doctor getDoctorById(int doctorId) throws DoctorNotFoundException, DataAccessException {
        String query = "SELECT * FROM doctor WHERE doctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Doctor(
                    rs.getInt("doctorID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("specialization"),
                    rs.getString("contactNumber")
                );
            } else {
                throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve doctor by ID"+e);
        }
    }

    @Override
    public List<Doctor> getAllDoctors() throws DataAccessException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctor";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("doctorID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("specialization"),
                    rs.getString("contactNumber")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all doctors"+e);
        }
        return doctors;
    }

    @Override
    public boolean updateDoctor(Doctor doctor) throws DataAccessException, InvalidDoctorDataException, DoctorNotFoundException {
        if (doctor.getDoctorId() == 0 || doctor.getFirstName() == null || doctor.getLastName() == null) {
            throw new InvalidDoctorDataException("Invalid doctor data");
        }

        String query = "UPDATE doctor SET firstName = ?, lastName = ?, specialization = ?, contactNumber = ? WHERE doctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getContactNumber());
            stmt.setInt(5, doctor.getDoctorId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DoctorNotFoundException("Doctor not found with ID: " + doctor.getDoctorId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update doctor"+e);
        }
    }

    @Override
    public boolean deleteDoctor(int doctorId) throws DataAccessException, DoctorNotFoundException {
        String query = "DELETE FROM doctor WHERE doctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete doctor"+e);
        }
    }
}