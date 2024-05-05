package com.example.sweprojects2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GlamBook.db";
    private static final int DATABASE_VERSION = 1;

    // Tables names
    private static final String Client = "client";
    private static final String Appointment  = "appointment";
    private static final String Staff  = "staff";
    private static final String Service  = "service";

    // Client table column names
    private static final String ClientID = "userID";
    private static final String ClientName = "clientName";
    private static final String Email = "email";
    private static final String PhoneNumber = "phoneNumber";
    private static final String Birthday = "birthday";
    private static final String Password = "password";

    // Appointment table column names
    private static final String AppointmentID = "appointmentID";
    private static final String Date = "date";
    private static final String Time = "time";

    // Staff table column names
    private static final String StaffID = "staffID";
    private static final String StaffName = "staffName";
    private static final String Rating = "rating";
    private static final String Specialty = "specialty";

    // Service table column names
    private static final String ServiceID = "serviceID";
    private static final String ServiceName = "serviceName";
    private static final String Description = "description";
    private static final String Price = "price";

    //constructor
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create client table
        String createClientTableQuery = "CREATE TABLE " + Client + "("
                + ClientID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ClientName + " TEXT,"
                + Email + " TEXT,"
                + PhoneNumber + " TEXT,"
                + Birthday + " TEXT,"
                + Password + " TEXT" + ")";
        db.execSQL(createClientTableQuery);

        // Create appointment table
        String createAppointmentTableQuery = "CREATE TABLE " + Appointment + "("
                + AppointmentID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ClientID + " INTEGER,"
                + Date + " TEXT,"
                + Time + " TEXT,"
                + StaffID + " INTEGER,"
                + ServiceID + " INTEGER,"
                + StaffName + " TEXT,"
                + ServiceName + " TEXT,"
                + "FOREIGN KEY(" + ClientID + ") REFERENCES " + Client + "(" + ClientID + "),"
                + "FOREIGN KEY(" + StaffID + ") REFERENCES " + Staff + "(" + StaffID + "),"
                + "FOREIGN KEY(" + ServiceID + ") REFERENCES " + Service + "(" + ServiceID + ")" + ")";
        db.execSQL(createAppointmentTableQuery);

        // Create staff table
        String createStaffTableQuery = "CREATE TABLE " + Staff + "("
                + StaffID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StaffName + " TEXT,"
                + Rating + " REAL,"
                + Specialty + " TEXT" + ")";
        db.execSQL(createStaffTableQuery);

        // Create service table
        String createServiceTableQuery = "CREATE TABLE " + Service + "("
                + ServiceID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ServiceName + " TEXT,"
                + Description + " TEXT,"
                + Price + " REAL" + ")";
        db.execSQL(createServiceTableQuery);

        addStaff(db, "Aisha", 4.5f, "Haircut");
        addStaff(db, "Fatima", 4.8f, "Hair Dye");
        addStaff(db, "Zainab", 4.2f, "Makeup");

        addService(db, "Haircut", "Professional haircut and styling", 69.0f);
        addService(db, "Hair Dye", "Hair coloring and highlighting", 49.0f);
        addService(db, "Makeup", "Full face makeup application", 99.0f);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Client);
        db.execSQL("DROP TABLE IF EXISTS " + Appointment);
        db.execSQL("DROP TABLE IF EXISTS " + Staff);
        db.execSQL("DROP TABLE IF EXISTS " + Service);
        // Create tables again
        onCreate(db);
    }
    public boolean addClient(String clientName, String email, String phoneNumber, String birthday, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClientName, clientName);
        values.put(Email, email);
        values.put(PhoneNumber, phoneNumber);
        values.put(Birthday, birthday);
        values.put(Password, password);

        long result = db.insert(Client, null, values);
        if (result == -1) {
            db.close(); // Close the database connection
            return false;
        } else {
            int clientId = (int) result;

            ContentValues idValues = new ContentValues();
            idValues.put(ClientID, clientId);

            db.update(Client, idValues, ClientID + " = ?", new String[]{String.valueOf(clientId)});

            db.close(); // Close the database connection
            return true;
        }
    }
    public boolean addAppointment(bookO appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Since appointmentID is auto-incremented, we don't need to put it into ContentValues
        values.put(ClientID, appointment.getClientID());
        values.put(Date, appointment.getDate() != null ? appointment.getDate() : null);
        values.put(Time, appointment.getTime() != null ? appointment.getTime() : null);
        values.put(StaffID, appointment.getStaffID());
        values.put(StaffName, appointment.getStaffName() != null ? appointment.getStaffName() : null);
        values.put(ServiceName, appointment.getServiceName() != null ? appointment.getServiceName() : null);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert(Appointment, null, values);
        db.close(); // Close database connection

        // Check if insertion was successful
        return result != -1;
    }
    public boolean deleteOneAppointment(bookO appointment) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause
        String whereClause = AppointmentID + " = ?";
        String[] whereArgs = {String.valueOf(appointment.getAppointmentID())};

        // Execute the deletion and get the number of rows affected
        int rowsDeleted = db.delete(Appointment, whereClause, whereArgs);

        db.close(); // Close the database connection

        // Return true if at least one row was deleted, false otherwise
        return rowsDeleted > 0;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + Client + " WHERE " + Email + "=?";
            cursor = db.rawQuery(query, new String[]{email});

            return cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();

            db.close();
        }
    }
    public boolean loginClient(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + Client + " WHERE " + Email + "=? AND " + Password + "=?";
            cursor = db.rawQuery(query, new String[]{email, password});

            return cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();

            db.close();
        }
    }
    public int getClientId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int clientId = -1;
        try {
            String query = "SELECT " + ClientID + " FROM " + Client + " WHERE " + Email + "=?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                clientId = cursor.getInt(cursor.getColumnIndex(ClientID));
            }

            return clientId;
        } finally {
            if (cursor != null)
                cursor.close();

            db.close();
        }
    }
    public List<bookO> getAllAppointments(int clientID) {
        List<bookO> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {AppointmentID, ClientID, StaffID, Date, Time, StaffName, ServiceName};

        String selection = ClientID + " = ?";
        String[] selectionArgs = {String.valueOf(clientID)};

        Cursor cursor = db.query(Appointment, columns, selection, selectionArgs, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                int appointmentIDIndex = cursor.getColumnIndex(AppointmentID);
                int clientIDIndex = cursor.getColumnIndex(ClientID);
                int staffIDIndex = cursor.getColumnIndex(StaffID);
                int dateIndex = cursor.getColumnIndex(Date);
                int timeIndex = cursor.getColumnIndex(Time);
                int staffNameIndex = cursor.getColumnIndex(StaffName);
                int serviceNameIndex = cursor.getColumnIndex(ServiceName);

                do {
                    int appointmentID = cursor.getInt(appointmentIDIndex);
                    int retrievedClientID = cursor.getInt(clientIDIndex);
                    int staffID = cursor.getInt(staffIDIndex);
                    String date = cursor.getString(dateIndex);
                    String time = cursor.getString(timeIndex);
                    String staffName = cursor.getString(staffNameIndex);
                    String serviceName = cursor.getString(serviceNameIndex);

                    bookO newAppointment = new bookO(appointmentID, retrievedClientID, staffID, date, time, staffName, serviceName);
                    returnList.add(newAppointment);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return returnList;
    }

    private void addStaff(SQLiteDatabase db, String staffName, float rating, String specialty) {
        ContentValues values = new ContentValues();
        values.put(StaffName, staffName);
        values.put(Rating, rating);
        values.put(Specialty, specialty);
        db.insert(Staff, null, values);
    }
    private void addService(SQLiteDatabase db, String serviceName, String description, float price) {
        ContentValues values = new ContentValues();
        values.put(ServiceName, serviceName);
        values.put(Description, description);
        values.put(Price, price);
        db.insert(Service, null, values);
    }

    public String getClientName(int clientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String clientName = null;
        try {
            String query = "SELECT " + ClientName + " FROM " + Client + " WHERE " + ClientID + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(clientId)});
            if (cursor.moveToFirst()) {
                clientName = cursor.getString(cursor.getColumnIndex(ClientName));
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return clientName;
    }

    public List<Staff> getStaffBySpecialty(String specialty) {
        List<Staff> staffList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Staff + " WHERE " + Specialty + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{specialty});

        if (cursor.moveToFirst()) {
            do {
                int staffId = cursor.getInt(cursor.getColumnIndex(StaffID));
                String staffName = cursor.getString(cursor.getColumnIndex(StaffName));
                float rating = cursor.getFloat(cursor.getColumnIndex(Rating));
                String staffSpecialty = cursor.getString(cursor.getColumnIndex(Specialty));

                staffList.add(new Staff(staffId, staffName, rating, staffSpecialty));
            } while (cursor.moveToNext());
        } else {
            // No staff found with the specified specialty, so add them
            addStaff(db, "Aisha", 4.5f, "Haircut");
            addStaff(db, "Fatima", 4.8f, "Hair Dye");
            addStaff(db, "Zainab", 4.2f, "Makeup");

            // Retrieve the staff again after adding them
            cursor = db.rawQuery(query, new String[]{specialty});
            if (cursor.moveToFirst()) {
                do {
                    int staffId = cursor.getInt(cursor.getColumnIndex(StaffID));
                    String staffName = cursor.getString(cursor.getColumnIndex(StaffName));
                    float rating = cursor.getFloat(cursor.getColumnIndex(Rating));
                    String staffSpecialty = cursor.getString(cursor.getColumnIndex(Specialty));

                    staffList.add(new Staff(staffId, staffName, rating, staffSpecialty));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return staffList;
    }

    public boolean updateAppointment(bookO appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClientID, appointment.getClientID());
        values.put(Date, appointment.getDate() != null ? appointment.getDate() : null);
        values.put(Time, appointment.getTime() != null ? appointment.getTime() : null);
        values.put(StaffID, appointment.getStaffID());
        values.put(StaffName, appointment.getStaffName() != null ? appointment.getStaffName() : null);
        values.put(ServiceName, appointment.getServiceName() != null ? appointment.getServiceName() : null);

        String whereClause = AppointmentID + " = ?";
        String[] whereArgs = {String.valueOf(appointment.getAppointmentID())};

        int rowsUpdated = db.update(Appointment, values, whereClause, whereArgs);
        db.close();
        return rowsUpdated > 0;
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Staff;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int staffId = cursor.getInt(cursor.getColumnIndex(StaffID));
                String staffName = cursor.getString(cursor.getColumnIndex(StaffName));
                float rating = cursor.getFloat(cursor.getColumnIndex(Rating));
                String staffSpecialty = cursor.getString(cursor.getColumnIndex(Specialty));

                staffList.add(new Staff(staffId, staffName, rating, staffSpecialty));
            } while (cursor.moveToNext());
        } else {
            // No staff found, so add them
            addStaff(db, "Aisha", 4.5f, "Haircut");
            addStaff(db, "Fatima", 4.8f, "Hair Dye");
            addStaff(db, "Zainab", 4.2f, "Makeup");

            // Retrieve the staff again after adding them
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int staffId = cursor.getInt(cursor.getColumnIndex(StaffID));
                    String staffName = cursor.getString(cursor.getColumnIndex(StaffName));
                    float rating = cursor.getFloat(cursor.getColumnIndex(Rating));
                    String staffSpecialty = cursor.getString(cursor.getColumnIndex(Specialty));

                    staffList.add(new Staff(staffId, staffName, rating, staffSpecialty));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return staffList;
    }
}
