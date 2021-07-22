package com.example.memorymaker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import java.io.File
import java.net.URI
import java.util.*

private val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MM, dd"
private const val ARG_MEMORY_ID = "memory_id"

class MemoryFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var memory: Memory
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var favoriteCheckBox: CheckBox
    private lateinit var reportButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView

    private val memoryDetailViewModel: MemoryDetailViewModel by lazy {
        ViewModelProviders.of(this).get(MemoryDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memory = Memory()
        val memoryId: UUID = arguments?.getSerializable(ARG_MEMORY_ID) as UUID
        memoryDetailViewModel.loadMemory(memoryId)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_memory, container, false)

        titleField = view.findViewById(R.id.memory_title) as EditText
        dateButton = view.findViewById(R.id.memory_date) as Button
        favoriteCheckBox = view.findViewById(R.id.memory_favorite) as CheckBox
//        reportButton = view.findViewById(R.id.memory_report) as Button
        photoButton = view.findViewById(R.id.memory_camera) as ImageButton
        photoView = view.findViewById(R.id.memory_photo) as ImageView

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memoryDetailViewModel.memoryData.observe(
            viewLifecycleOwner,
            Observer { memory ->
                memory?.let {
                    this.memory = memory
                    photoFile = memoryDetailViewModel.getPhotoFile(memory)
                    photoUri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.example.memorymaker.fileprovider",
                        photoFile
                    )
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // left blank on purpose
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                memory.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // left blank on purpose
            }

        }

        titleField.addTextChangedListener(titleWatcher)

        favoriteCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                memory.isFavorite = isChecked
            }

            dateButton.setOnClickListener {
                DatePickerFragment.newInstance(memory.date).apply {
                    show(this@MemoryFragment.requireFragmentManager(), DIALOG_DATE)
                }
            }

//            reportButton.setOnClickListener {
//                Intent(Intent.ACTION_SEND).apply {
//                    type = "text/plain"
//                    putExtra(Intent.EXTRA_TEXT, getCrimeReport())
//                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
//                }.also { intent ->
//                    val chooserIntent =
//                        Intent.createChooser(intent, getString(R.string.send_memory))
//                    startActivity(chooserIntent)
//                }
//            }



            photoButton.apply {
                val packageManager: PackageManager = requireActivity().packageManager

                val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                if (resolvedActivity == null){
                    isEnabled = false
                }
                setOnClickListener{
                    val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                    for (cameraActivity in cameraActivities){
                        requireActivity().grantUriPermission(
                            cameraActivity.activityInfo.packageName,
                            photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }

                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(captureImage, REQUEST_PHOTO)
                }
            }


        }
    }


    override fun onStop() {
        super.onStop()
        memoryDetailViewModel.saveMemory(memory)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(
            photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    fun updateUI() {
        titleField.setText(memory.title)
        dateButton.text = memory.date.toString()
        favoriteCheckBox.apply {
            isChecked = memory.isFavorite
            jumpDrawablesToCurrentState()
        }

    }

    private fun updatePhotoView(){

        if (photoFile.exists()){
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageBitmap(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    override fun onDateSelected(date: Date) {
        memory.date = date
        updateUI()
    }

    private fun getCrimeReport(): String {
        val solvedString = if (memory.isFavorite) {
            getString(R.string.crime_report_solved)
        } else {
//            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, memory.date).toString()


        return getString(R.string.memory_report, memory.title, dateString, solvedString, memory.pet)
    }

    companion object {
        fun newInstance(memoryId: UUID): MemoryFragment {
            val args = Bundle().apply {
                putSerializable(ARG_MEMORY_ID, memoryId)
            }
            return MemoryFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.remove_menu -> {
                true
            }
            R.id.send_menu -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }.also { intent ->
                    val chooserIntent =
                        Intent.createChooser(intent, getString(R.string.send_memory))
                    startActivity(chooserIntent)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


}
