package com.example.customermanagement

import android.os.Bundle
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    data class Customer(val id: Long, val name: String, val email: String, val mobile: String)

    class CustomerAdapter(context: Context) : SQLiteOpenHelper(context, "smtbiz", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS customer (Id INTEGER PRIMARY KEY, Name TEXT, Email TEXT, Mobile TEXT)")
            resetTable(db)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS customer")
            onCreate(db)
        }

        private fun resetTable(db: SQLiteDatabase) {
            db.beginTransaction()
            try {
                db.delete("customer", null, null)
                for (i in 1..5) {
                    val values = ContentValues().apply {
                        put("Id", i)
                        put("Name", "Customer $i")
                        put("Email", "customer$i@example.com")
                        put("Mobile", "12345678$i")
                    }
                    db.insert("customer", null, values)
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

        fun insertCustomer(customer: Customer) {
            try {
                val values = ContentValues().apply {
                    put("Id", customer.id)
                    put("Name", customer.name)
                    put("Email", customer.email)
                    put("Mobile", customer.mobile)
                }
                writableDatabase.insertOrThrow("customer", null, values)
            } catch (e: Exception) {
                // handle exception
            }
        }

        fun deleteCustomerById(id: Long) {
            try {
                writableDatabase.delete("customer", "Id = ?", arrayOf(id.toString()))
            } catch (e: Exception) {
                // handle exception
            }
        }

        fun searchCustomerByName(name: String): List<Customer> {
            val customers = mutableListOf<Customer>()
            try {
                val cursor = readableDatabase.query(
                    "customer",
                    arrayOf("Id", "Name", "Email", "Mobile"),
                    "Name LIKE ?",
                    arrayOf("%$name%"),
                    null,
                    null,
                    null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(0)
                    val name = cursor.getString(1)
                    val email = cursor.getString(2)
                    val mobile = cursor.getString(3)
                    customers.add(Customer(id, name, email, mobile))
                }
                cursor.close()
            } catch (e: Exception) {
                // handle exception
            }
            return customers
        }

        fun getAllCustomers(): List<Customer> {
            val customers = mutableListOf<Customer>()
            try {
                val cursor = readableDatabase.query(
                    "customer",
                    arrayOf("Id", "Name", "Email", "Mobile"),
                    null,
                    null,
                    null,
                    null,
                    null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(0)
                    val name = cursor.getString(1)
                    val email = cursor.getString(2)
                    val mobile = cursor.getString(3)
                    customers.add(Customer(id, name, email, mobile))
                }
                cursor.close()
            } catch (e: Exception) {
                // handle exception
            }
            return customers
        }



        fun updateCustomer(customer: Customer) {
            val values = ContentValues().apply {
                put("Name", customer.name)
                put("Email", customer.email)
                put("Mobile", customer.mobile)
            }
            writableDatabase.update(
                "customer",
                values,
                "Id = ?",
                arrayOf(customer.id.toString())
            )
            try {
                // code that may throw an exception
            } catch (e: Exception) {
                // handle the exception here
            }

        }

    }
}

