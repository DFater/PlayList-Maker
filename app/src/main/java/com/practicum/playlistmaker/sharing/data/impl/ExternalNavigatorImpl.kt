package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.models.Email
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.ShareData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareApp(link: ShareData) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getUrlString(link))
            context.startActivity(
                Intent.createChooser(shareIntent, getUrlTitleString(link))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (_: Exception) {
        }
    }

    override fun openTermsOfUse(link: ShareData) {
        try {
            val browseIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getUrlString(link)))
            browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browseIntent)
        } catch (_: Exception) {
        }
    }

    override fun openSupport(email: EmailData) {
        val emailInfo = getEmail(email)
        try {
            val serviceIntent = Intent(Intent.ACTION_SENDTO)
            serviceIntent.data = Uri.parse(context.getString(R.string.mailto))
            serviceIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(emailInfo.address)
            )
            serviceIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                emailInfo.subject
            )
            serviceIntent.putExtra(Intent.EXTRA_TEXT, emailInfo.body)
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(serviceIntent)
        } catch (_: Exception) {
        }
    }

    private fun getUrlString(link: ShareData): String {
        return when (link) {
            ShareData.SHARE_URL -> context.getString(R.string.share_url)
            ShareData.TERMS_OF_USE -> context.getString(R.string.user_agreement_url)
        }
    }

    private fun getUrlTitleString(link: ShareData): String {
        return when (link) {
            ShareData.SHARE_URL -> context.getString(R.string.share_app)
            ShareData.TERMS_OF_USE -> ""
        }
    }

    private fun getEmail(email: EmailData): Email {
        return when (email) {
            EmailData.SUPPORT_EMAIL -> Email(
                context.getString(R.string.person_email),
                context.getString(R.string.email_subject),
                context.getString(R.string.email_text)

            )
        }
    }
}