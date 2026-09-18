package com.threadly.felixx.dev

import com.threadly.felixx.dev.mail.cleanMessageBody
import com.threadly.felixx.dev.mail.normalizeSubject
import org.junit.Assert.assertEquals
import org.junit.Test

class MailModelsTest {
    @Test fun normalizesReplyPrefixes() {
        assertEquals("club update", normalizeSubject(" Re: FWD: Club Update "))
    }

    @Test fun removesQuotedLines() {
        assertEquals("New text", cleanMessageBody("New text\n> old text\n> older text"))
    }
}
