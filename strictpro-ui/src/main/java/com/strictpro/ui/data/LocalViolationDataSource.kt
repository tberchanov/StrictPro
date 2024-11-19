package com.strictpro.ui.data

import android.annotation.SuppressLint
import android.os.strictmode.Violation
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import com.strictpro.ui.domain.model.StrictProViolation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class LocalViolationDataSource(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun saveViolation(violation: StrictProViolation) {
        dataStore.edit { preferences ->
            val key = byteArrayPreferencesKey(name = violation.dateMillis.toString())
            preferences[key] = serializeViolation(violation.violation)
        }
    }

    fun observeViolations(): Flow<List<StrictProViolation>> {
        return dataStore.data.map { preferences ->
            val prefsMap = preferences.asMap()
            val sortedKeys = prefsMap.keys.sortedBy { it.name.toLong() }
            sortedKeys.mapNotNull { key ->
                createStrictProViolation(
                    dateMillis = key.name.toLong(),
                    violationBytes = prefsMap[key] as? ByteArray?,
                )
            }
        }
    }

    private fun createStrictProViolation(
        dateMillis: Long,
        violationBytes: ByteArray?,
    ): StrictProViolation? {
        return violationBytes?.let {
            StrictProViolation(
                dateMillis = dateMillis,
                violation = deserializeViolation(violationBytes)
            )
        }
    }

    private fun serializeViolation(violation: Violation): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(violation)
        objectOutputStream.flush()
        val byteArray = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()
        objectOutputStream.close()
        return byteArray
    }

    @SuppressLint("NewApi")
    private fun deserializeViolation(byteArray: ByteArray): Violation {
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val violation = objectInputStream.readObject() as Violation
        byteArrayInputStream.close()
        objectInputStream.close()
        return violation
    }
}