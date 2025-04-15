package com.hospitalmanagementsystem.main;

import com.hospitalmanagementsystem.dao.PatientDao;
import com.hospitalmanagementsystem.dao.PatientDaoImpl;
import com.hospitalmanagementsystem.dao.DoctorDao;
import com.hospitalmanagementsystem.dao.DoctorDaoImpl;
import com.hospitalmanagementsystem.entity.Gender;
import com.hospitalmanagementsystem.dao.AppointmentDao;
import com.hospitalmanagementsystem.dao.AppointmentDaoImpl;
import com.hospitalmanagementsystem.entity.Patient;
import com.hospitalmanagementsystem.entity.Doctor;
import com.hospitalmanagementsystem.entity.Appointment;
import com.hospitalmanagementsystem.exceptions.*;
import com.hospitalmanagementsystem.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HospitalApp {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            PatientDao patientDao = new PatientDaoImpl(connection);
            DoctorDao doctorDao = new DoctorDaoImpl(connection);
            AppointmentDao appointmentDao = new AppointmentDaoImpl(connection);

            boolean running = true;

            while (running) {
                System.out.println("\n--- Hospital Management System ---");
                System.out.println("1. Manage Patients");
                System.out.println("2. Manage Doctors");
                System.out.println("3. Manage Appointments");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        managePatients(scanner, patientDao);
                        break;
                    case 2:
                        manageDoctors(scanner, doctorDao);
                        break;
                    case 3:
                        manageAppointments(scanner, appointmentDao);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException | ParseException | InvalidPatientDataException e) {
            e.printStackTrace();
        }
    }

    private static void managePatients(Scanner scanner, PatientDao patientDao) throws ParseException, InvalidPatientDataException {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Get Patient by ID");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addPatient(scanner, patientDao);
                    break;
                case 2:
                    getPatientById(scanner, patientDao);
                    break;
                case 3:
                    updatePatient(scanner, patientDao);
                    break;
                case 4:
                    deletePatient(scanner, patientDao);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPatient(Scanner scanner, PatientDao patientDao) throws ParseException, InvalidPatientDataException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
        String dob = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = sdf.parse(dob);
        Date dateOfBirth = new Date(parsedDate.getTime());
        System.out.print("Enter Gender: ");
        String genderInput = scanner.nextLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase().replace(" ", "_"));
        System.out.print("Enter Contact Number: ");
        String contactNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            contactNumber = scanner.nextLine();
            if (contactNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Patient patient = new Patient(firstName, lastName, dateOfBirth, gender, contactNumber, address);
        try {
            if (patientDao.createPatient(patient)) {
                System.out.println("Patient added successfully.");
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (DataAccessException  e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }

    private static void getPatientById(Scanner scanner, PatientDao patientDao) {
        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Patient patient = patientDao.getPatientById(id);
            System.out.println(patient);
        } catch (PatientNotFoundException | DataAccessException e) {
            System.out.println("Error retrieving patient: " + e.getMessage());
        }
    }

    private static void updatePatient(Scanner scanner, PatientDao patientDao) throws ParseException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
        String dob = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = sdf.parse(dob);
        Date dateOfBirth = new Date(parsedDate.getTime());
        System.out.print("Enter Gender: ");
        String genderInput = scanner.nextLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase().replace(" ", "_"));
        System.out.print("Enter Contact Number: ");
        String contactNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            contactNumber = scanner.nextLine();
            if (contactNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Patient patient = new Patient(patientId, firstName, lastName, dateOfBirth, gender, contactNumber, address);
        try {
            if (patientDao.updatePatient(patient)) {
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("Failed to update patient.");
            }
        } catch (DataAccessException | InvalidPatientDataException | PatientNotFoundException e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    private static void deletePatient(Scanner scanner, PatientDao patientDao) {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        try {
            if (patientDao.deletePatient(patientId)) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("Failed to delete patient.");
            }
        } catch (DataAccessException | PatientNotFoundException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }

    private static void manageDoctors(Scanner scanner, DoctorDao doctorDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Doctor Management ---");
            System.out.println("1. Add Doctor");
            System.out.println("2. Get Doctor by ID");
            System.out.println("3. Update Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addDoctor(scanner, doctorDao);
                    break;
                case 2:
                    getDoctorById(scanner, doctorDao);
                    break;
                case 3:
                    updateDoctor(scanner, doctorDao);
                    break;
                case 4:
                    deleteDoctor(scanner, doctorDao);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addDoctor(Scanner scanner, DoctorDao doctorDao) {
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contactNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            contactNumber = scanner.nextLine();
            if (contactNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }

        Doctor doctor = new Doctor(firstName, lastName, specialization, contactNumber);
        try {
            if (doctorDao.createDoctor(doctor)) {
                System.out.println("Doctor added successfully.");
            } else {
                System.out.println("Failed to add doctor.");
            }
        } catch (DataAccessException | InvalidDoctorDataException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
        }
    }

    private static void getDoctorById(Scanner scanner, DoctorDao doctorDao) {
        System.out.print("Enter Doctor ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Doctor doctor = doctorDao.getDoctorById(id);
            System.out.println(doctor);
        } catch (DoctorNotFoundException | DataAccessException e) {
            System.out.println("Error retrieving doctor: " + e.getMessage());
        }
    }

    private static void updateDoctor(Scanner scanner, DoctorDao doctorDao) {
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contactNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            contactNumber = scanner.nextLine();
            if (contactNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }

        Doctor doctor = new Doctor(doctorId, firstName, lastName, specialization, contactNumber);
        try {
            if (doctorDao.updateDoctor(doctor)) {
                System.out.println("Doctor updated successfully.");
            } else {
                System.out.println("Failed to update doctor.");
            }
        } catch (DataAccessException | InvalidDoctorDataException | DoctorNotFoundException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
        }
    }

    private static void deleteDoctor(Scanner scanner, DoctorDao doctorDao) {
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (doctorDao.deleteDoctor(doctorId)) {
                System.out.println("Doctor deleted successfully.");
            } else {
                System.out.println("Failed to delete doctor.");
            }
        } catch (DataAccessException | DoctorNotFoundException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }

    private static void manageAppointments(Scanner scanner, AppointmentDao appointmentDao) throws ParseException {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Appointment Management ---");
            System.out.println("1. Schedule Appointment");
            System.out.println("2. Get Appointment by ID");
            System.out.println("3. Get Appointments for Patient");
            System.out.println("4. Get Appointments for Doctor");
            System.out.println("5. Update Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    scheduleAppointment(scanner, appointmentDao);
                    break;
                case 2:
                    getAppointmentById(scanner, appointmentDao);
                    break;
                case 3:
                    getAppointmentsForPatient(scanner, appointmentDao);
                    break;
                case 4:
                    getAppointmentsForDoctor(scanner, appointmentDao);
                    break;
                case 5:
                    updateAppointment(scanner, appointmentDao);
                    break;
                case 6:
                    cancelAppointment(scanner, appointmentDao);
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void scheduleAppointment(Scanner scanner, AppointmentDao appointmentDao) throws ParseException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String appDate = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = sdf.parse(appDate);
        Date appointmentDate  = new Date(parsedDate.getTime());
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        Appointment appointment = new Appointment(patientId, doctorId, appointmentDate, description);
        try {
            if (appointmentDao.scheduleAppointment(appointment)) {
                System.out.println("Appointment scheduled successfully.");
            } else {
                System.out.println("Failed to schedule appointment.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    private static void getAppointmentById(Scanner scanner, AppointmentDao appointmentDao) {
        System.out.print("Enter Appointment ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Appointment appointment = appointmentDao.getAppointmentById(id);
            System.out.println(appointment);
        } catch (AppointmentNotFoundException | DataAccessException e) {
            System.out.println("Error retrieving appointment: " + e.getMessage());
        }
    }

    private static void getAppointmentsForPatient(Scanner scanner, AppointmentDao appointmentDao) {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            List<Appointment> appointments = appointmentDao.getAppointmentsForPatient(patientId);
            appointments.forEach(System.out::println);
        } catch (DataAccessException e) {
            System.out.println("Error retrieving appointments for patient: " + e.getMessage());
        }
    }

    private static void getAppointmentsForDoctor(Scanner scanner, AppointmentDao appointmentDao) {
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            List<Appointment> appointments = appointmentDao.getAppointmentsForDoctor(doctorId);
            appointments.forEach(System.out::println);
        } catch (DataAccessException e) {
            System.out.println("Error retrieving appointments for doctor: " + e.getMessage());
        }
    }

    private static void updateAppointment(Scanner scanner, AppointmentDao appointmentDao) throws ParseException {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String appDate = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = sdf.parse(appDate);
        Date appointmentDate  = new Date(parsedDate.getTime());
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDate, description);
        try {
            if (appointmentDao.updateAppointment(appointment)) {
                System.out.println("Appointment updated successfully.");
            } else {
                System.out.println("Failed to update appointment.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

    private static void cancelAppointment(Scanner scanner, AppointmentDao appointmentDao) {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (appointmentDao.cancelAppointment(appointmentId)) {
                System.out.println("Appointment canceled successfully.");
            } else {
                System.out.println("Failed to cancel appointment.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error canceling appointment: " + e.getMessage());
        }
    }
}
