package com.microblink.documentscanflow.document

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.recognition.BaseRecognition
import java.util.LinkedHashMap

internal class DocumentDescriptionBuilder {

    private val documentDescriptions = LinkedHashMap<DocumentType, DocumentDescription>()

    init {
        id(GenericDocumentDescriptions.id)
        drivingLicence(GenericDocumentDescriptions.drivingLicenceMrtd)
        passport(GenericDocumentDescriptions.passport)
        visa(GenericDocumentDescriptions.visa)
    }

    fun id(recognition: BaseRecognition, fullySupported: Boolean = true): DocumentDescriptionBuilder {
        return id(DocumentDescription(fullySupported, R.string.mb_id_card, recognition))
    }

    fun id(documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        return add(DocumentType.ID, documentDescription)
    }

    fun passport(recognition: BaseRecognition): DocumentDescriptionBuilder {
        return add(DocumentType.PASSPORT, DocumentDescription(true, R.string.mb_passport, recognition))
    }

    fun passport(documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        return add(DocumentType.PASSPORT, documentDescription)
    }

    fun drivingLicence(recognition: BaseRecognition, fullySupported: Boolean = true): DocumentDescriptionBuilder {
        return add(DocumentType.DL, DocumentDescription(fullySupported, R.string.mb_driver_license, recognition))
    }

    fun drivingLicence(documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        return add(DocumentType.DL, documentDescription)
    }

    fun residencePermit(documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        return add(DocumentType.RESIDENCE_PERMIT, documentDescription)
    }

    fun visa(documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        return add(DocumentType.TRAVEL_DOCUMENT_VISA, documentDescription)
    }

    fun oldId(recognition: BaseRecognition): DocumentDescriptionBuilder {
        return add(DocumentType.OLD_ID, DocumentDescription(true, R.string.mb_old_id_card, recognition))
    }

    fun newId(recognition: BaseRecognition): DocumentDescriptionBuilder {
        return add(DocumentType.NEW_ID, DocumentDescription(true, R.string.mb_new_id_card, recognition))
    }

    fun add(documentType: DocumentType, documentDescription: DocumentDescription): DocumentDescriptionBuilder {
        documentDescriptions[documentType] = documentDescription
        return this
    }

    fun remove(documentType: DocumentType): DocumentDescriptionBuilder {
        documentDescriptions.remove(documentType)
        return this
    }

    fun build(): Map<DocumentType, DocumentDescription> {
        return documentDescriptions
    }

}