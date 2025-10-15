package com.halimjr11.luma.view.feature.create.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.halimjr11.luma.databinding.FragmentImageCaptureBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.utils.Constants.CAPTURE_IMAGE
import com.halimjr11.luma.utils.Constants.CAPTURE_RESULT
import timber.log.Timber
import java.io.File

class ImageCaptureFragment : BaseFragment<FragmentImageCaptureBinding, ViewModel>(
    FragmentImageCaptureBinding::inflate
) {
    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->
        if (permissions) startCamera()
    }

    override fun setupUI() {
        checkPermission()
        super.setupUI()
    }

    private fun checkPermission() = context?.let {
        if (ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun setupListeners() = with(binding) {
        ibCaptureImage.setOnClickListener { takePhoto() }
        ibSwitchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        super.setupListeners()
    }

    private fun startCamera() = context?.let { ctx ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.viewFinder.surfaceProvider
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Timber.tag("ImageCaptureFragment").e(exc, "Jalanan ==> Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(ctx))
    }

    private fun takePhoto() = context?.let {
        val imageCapture = imageCapture ?: return@let

        val photoFile = File(
            requireContext().externalCacheDir,
            "IMG_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(it),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Timber.tag("ImageCaptureFragment").d("Jalanan ==> Photo saved: $savedUri")
                    val result = Bundle().apply {
                        putString(CAPTURE_IMAGE, savedUri.toString())
                    }
                    setFragmentResult(CAPTURE_RESULT, result)
                    findNavController().popBackStack()
                }

                override fun onError(exc: ImageCaptureException) {
                    Timber.e(exc, "Jalanan ==> Photo capture failed: ${exc.message}")
                }
            }
        )
    }
}