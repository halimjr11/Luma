package com.halimjr11.luma.view.feature.create.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.FragmentCreateStoryBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.Constants.CAPTURE_IMAGE
import com.halimjr11.luma.utils.Constants.CAPTURE_RESULT
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.create.di.loadCreateModule
import com.halimjr11.luma.view.feature.create.viewmodel.CreateViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class CreateStoryFragment : BaseFragment<FragmentCreateStoryBinding, CreateViewModel>(
    FragmentCreateStoryBinding::inflate
), AndroidScopeComponent {
    override val viewModel: CreateViewModel by viewModel()
    private var currentImageUri: Uri? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val locationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) viewModel.fetchLocation()
    }

    private val legacyGalleryPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        legacyGalleryLauncher.launch(intent)
    }
    private val legacyGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            currentImageUri = uri
            uri?.let { showImagePreview(it) }
        }
    }
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            showImagePreview(it)
        }
    }

    override val scope: Scope by fragmentScope()

    init {
        loadCreateModule()
    }

    override fun setupUI() {
        checkPermissions()
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        setFragmentResultListener(CAPTURE_RESULT) { _, bundle ->
            val imageUri = bundle.getString(CAPTURE_IMAGE)
            imageUri?.let {
                currentImageUri = it.toUri()
                showImagePreview(it.toUri())
            }
        }

        btnCamera.setOnClickListener {
            val action =
                CreateStoryFragmentDirections.actionCreateStoryFragmentToImageCaptureFragment()
            findNavController().navigate(action)
        }

        btnGallery.setOnClickListener {
            openGallery()
        }

        topAppBar.setNavigationOnClickListener {
            activity?.finish()
        }

        buttonAdd.setOnClickListener {
            val message = resources.getString(R.string.upload_story_image_error)
            val descError = resources.getString(R.string.upload_story_desc_error)
            val description = edAddDescription.text.toString()

            when {
                currentImageUri == null && description.isNotBlank() -> Snackbar.make(
                    root,
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()

                description.isBlank() && currentImageUri != null -> descriptionLayout.error =
                    descError

                description.isBlank() && currentImageUri == null -> {
                    descriptionLayout.error = descError
                    Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    descriptionLayout.error = null
                    viewModel.handleImageAndPost(
                        uri = currentImageUri!!,
                        description = description,
                        lat = latitude,
                        lon = longitude
                    )
                }
            }
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(location) { loc ->
            loc?.let {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
        launchAndCollect(createState) { state ->
            when (state) {
                is UiState.Success -> {
                    activity?.finish()
                }

                is UiState.Error -> {
                    val failed = resources.getString(R.string.upload_story_failed)
                    Snackbar.make(
                        binding.root,
                        failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
        super.observeData()
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            context?.let {
                if (ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    legacyGalleryLauncher.launch(intent)
                } else {
                    legacyGalleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun checkPermissions() = context?.let {
        if (ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchLocation()
        } else {
            locationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showImagePreview(uri: Uri) {
        binding.previewImage.setImageURI(uri)
    }
}