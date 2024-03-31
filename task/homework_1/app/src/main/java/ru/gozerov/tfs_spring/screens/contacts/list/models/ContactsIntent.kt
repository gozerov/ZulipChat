package ru.gozerov.tfs_spring.screens.contacts.list.models

sealed class ContactsIntent {

    object LoadContacts: ContactsIntent()

}