package ru.gozerov.tfs_spring.screens.contacts.details.models

sealed class ContactDetailsIntent {

    class LoadContact(val id: Int) : ContactDetailsIntent()

}