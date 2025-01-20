

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.enovo.contactmanager.dataModel.Contact

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert Contact
    fun insertContact(name: String, email: String, phone: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    // Update Contact
    fun updateContact(id: String, name: String, email: String, phone: String): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
        }
        return db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Delete Contact
    fun deleteContact(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Get All Contacts
    fun getAllContacts(): List<Contact> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_NAME ASC")
        val contacts = mutableListOf<Contact>()

        if (cursor.moveToFirst()) {
            do {
                contacts.add(
                    Contact(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)).toString(),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return contacts
    }
}
