package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.core.utils.emojiSetNCU
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction
import javax.inject.Inject

class GetReactionsUseCase @Inject constructor() {

    suspend operator fun invoke(): List<Reaction> = withContext(Dispatchers.IO) {
        emojiSetNCU.map {
            Reaction(it.name, it.getCodeString(), 0, false)
        }
    }

}