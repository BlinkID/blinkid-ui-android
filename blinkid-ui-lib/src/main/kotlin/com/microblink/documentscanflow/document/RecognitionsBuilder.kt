package com.microblink.documentscanflow.document

import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.implementations.GenericRecognition
import java.util.LinkedHashMap

internal class RecognitionsBuilder {

    private val recognitionByDocumentType = LinkedHashMap<DocumentType, BaseRecognition>()

    var id: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.ID, value)
        }

    var passport: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.PASSPORT, value)
        }

    var drivingLicence: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.DL, value)
        }

    var visa: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.TRAVEL_DOCUMENT_VISA, value)
        }

    var residencePermit: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.RESIDENCE_PERMIT, value)
        }

    var newId: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.NEW_ID, value)
        }

    var oldId: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.OLD_ID, value)
        }

    var immigratorId: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.IMMIGRATOR_ID, value)
        }

    var militaryId: BaseRecognition? = null
        set(value) {
            field = value
            addRecognition(DocumentType.MILITARY_ID, value)
        }

    init {
        id = GenericRecognition.id
        drivingLicence = GenericRecognition.drivingLicence
        passport = GenericRecognition.passport
        visa = GenericRecognition.visa
    }

    private fun addRecognition(documentType: DocumentType, value: BaseRecognition?) {
        if (value != null) {
            recognitionByDocumentType[documentType] = value
        } else {
            recognitionByDocumentType.remove(documentType)
        }
    }

    fun build(): Map<DocumentType, BaseRecognition> {
        return recognitionByDocumentType
    }

}