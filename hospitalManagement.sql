create database HospitalManagement;
use HospitalManagement;

create table Patient
(patientId int primary key auto_increment, 
firstName varchar(30),
lastName varchar(30),
dateOfBirth date,
gender ENUM('Male', 'Female', 'Other') NOT NULL, 
contactNumber varchar(20),
address varchar(255));

create table Doctor
(doctorId int primary key auto_increment,
firstName varchar(30),
lastName varchar(30),
specialization varchar(30),
contactNumber varchar(20));

create table Appointment
(appointmentId int primary key auto_increment,
patientId int,
doctorId int,
appointmentDate date,
description text,
FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE,
FOREIGN KEY (doctorId) REFERENCES Doctor(doctorId) ON DELETE CASCADE
);
