package com.practicum.playlistmaker
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBack = findViewById<ImageView>(R.id.arrow_back)
        val viewShare = findViewById<TextView>(R.id.viewShare)
        val viewService = findViewById<TextView>(R.id.viewService)
        val viewUserAgreement = findViewById<TextView>(R.id.viewUserAgreement)

        arrowBack.setOnClickListener {
            finish()
        }
        viewShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_url))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
        }

        viewService.setOnClickListener {
            val serviceIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.person_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(serviceIntent)
        }

        viewUserAgreement.setOnClickListener {
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(browseIntent)
        }
    }
}
