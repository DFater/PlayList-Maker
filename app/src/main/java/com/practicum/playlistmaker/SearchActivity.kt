package com.practicum.playlistmaker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "http://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)


    private val arrowBack: ImageView by lazy { findViewById(R.id.arrow_search_back) }
    private val inputEditText: EditText by lazy { findViewById(R.id.inputEditText) }
    private val clearButton: ImageView by lazy { findViewById(R.id.clearIcon) }
    private val rvTrack: RecyclerView by lazy { findViewById(R.id.rvTrack) }
    private val placeholderNotice: LinearLayout by lazy { findViewById(R.id.placeholderNotice) }
    private val textNotice: TextView by lazy { findViewById(R.id.placeholderTextView) }
    private val imageNotice: ImageView by lazy { findViewById(R.id.placeholderImageView) }
    private val historyNotice: LinearLayout by lazy { findViewById(R.id.historySearchLinearLayout) }
    private val rvHistoryTrack: RecyclerView by lazy { findViewById(R.id.rvHistoryTrack) }
    private val clearHistoryButton: Button by lazy { findViewById(R.id.clearHistoryButton) }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(
            PRACTICUM_PREFERENCES,
            MODE_PRIVATE
        )
    }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }

    private var searchText: String = ""

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private val tracks = ArrayList<Tracks>()
    private val searchHistory = SearchHistory()
    private val adapter = TrackAdapter(tracks) {
        if (clickDebounce()) {
            searchHistory.setTrack(it, sharedPreferences)
            val showPlayerActivity = Intent(this, PlayerActivity::class.java)
            startActivity(showPlayerActivity)
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val simpleTextWatcher = object : SimpleTextWatcher {


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    showNotice(NetworkStatus.SUCCESS)
                    handler.removeCallbacks(searchRunnable)
                    if (!searchHistory.read(sharedPreferences).isEmpty()) {
                        historyNotice.visibility =
                            View.VISIBLE
                        saveAndOpen()
                    }
                } else {
                    historyNotice.visibility = View.GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = inputEditText.text.toString()
            }
        }

        arrowBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            showNotice(NetworkStatus.SUCCESS)
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            saveAndOpen()
        }

        val buttonRepeat = findViewById<Button>(R.id.buttonRefresh)
        buttonRepeat.setOnClickListener {
            search()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search()
                }
            }
            false
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        rvTrack.adapter = adapter

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyNotice.visibility =
                if (hasFocus && inputEditText.text.isEmpty() && searchHistory.read(sharedPreferences)
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
            saveAndOpen()
        }
        clearHistoryButton.setOnClickListener {
            searchHistory.clearTracksHistory(sharedPreferences)
            historyNotice.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_TEXT))
        inputEditText.setSelection(inputEditText.text.length)
    }

    private fun search() {
        progressBar.visibility = View.VISIBLE
        iTunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>, response: Response<TracksResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            showNotice(NetworkStatus.SUCCESS)
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showNotice(NetworkStatus.EMPTY)
                        }
                    } else {
                        showNotice(
                            NetworkStatus.ERROR
                        )
                    }

                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showNotice(NetworkStatus.ERROR)
                }

            })
    }

    private fun showNotice(status: NetworkStatus) {
        val buttonRefresh = findViewById<Button>(R.id.buttonRefresh)
        buttonRefresh.visibility = View.GONE
        placeholderNotice.visibility = View.VISIBLE
        tracks.clear()
        adapter.notifyDataSetChanged()
        when (status) {
            NetworkStatus.SUCCESS -> {
                placeholderNotice.visibility = View.GONE
            }
            NetworkStatus.EMPTY -> {
                textNotice.text = getString(R.string.nothing_found)
                imageNotice.setImageResource(R.drawable.nothing_found)
            }
            NetworkStatus.ERROR -> {
                textNotice.text = getString(R.string.no_internet)
                imageNotice.setImageResource(R.drawable.no_internet)
                buttonRefresh.visibility = View.VISIBLE
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun saveAndOpen() {
        rvHistoryTrack.adapter =
            TrackAdapter(searchHistory.read(sharedPreferences)) {
                searchHistory.setTrack(it, sharedPreferences)
                val showPlayerActivity =
                    Intent(this@SearchActivity, PlayerActivity::class.java)
                startActivity(showPlayerActivity)
            }
    }
}

private interface SimpleTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}

enum class NetworkStatus {
    SUCCESS, EMPTY, ERROR
}