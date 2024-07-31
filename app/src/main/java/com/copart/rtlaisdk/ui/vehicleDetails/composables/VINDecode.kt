package com.copart.rtlaisdk.ui.vehicleDetails.composables

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.copart.rtlaisdk.CameraActivity
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.ImagePlaceholder
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.ui.common.CustomDropDown
import com.copart.rtlaisdk.ui.common.CustomTextField
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.WhiteSmoke
import com.copart.rtlaisdk.ui.theme.labelBold14
import com.copart.rtlaisdk.ui.theme.labelNormal16
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsContract
import com.copart.rtlaisdk.utils.getWindowWidth
import com.copart.rtlaisdk.utils.overrideParentHorizontalPadding
import com.copart.rtlaisdk.utils.toDp
import compose.icons.TablerIcons
import compose.icons.tablericons.Key

@Composable
fun VINDecode(
    state: VehicleDetailsContract.State,
    onVinChanged: (String) -> Unit,
    onYearSelected: (String, String) -> Unit,
    onMakeSelected: (String, String) -> Unit,
    onModelSelected: (String, String) -> Unit,
    onGenerateRTL: (Context) -> Unit,
    onImageUrisChanged: (Uri?, Int) -> Unit,
    onSellerSelected: (String, String) -> Unit,
    onPrimaryDamageSelected: (String, String) -> Unit,
    isAirBagsDeployed: (String, String) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val imagePickerState = rememberLazyListState()
    val selectedVin = state.vinNumber
    val selectedYear = state.year
    val selectedMake = state.make
    val selectedModel = state.model
    val selectedSeller = state.selectedSeller
    val selectedPrimaryDamage = state.selectedPrimaryDamage
    val selectedAirBags = state.isAirBagsDeployed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteSmoke)
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        VINDecodeHeader()
        ImageTilePicker(
            state.imageUris,
            onImageUrisChanged,
            context,
            imagePickerState
        )
        CustomTextField(
            stringResource(id = R.string.vin),
            stringResource(R.string.vin_placeholder),
            onTextChanged = onVinChanged,
            value = selectedVin,
        )
        CustomDropDown(
            fieldName = "Year",
            showHeader = true,
            options = state.yearsList.map { Pair(it.code.toString(), it.code.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onYearSelected,
            selectedValue = selectedYear,
            selectedKey = selectedYear
        )
        CustomDropDown(
            fieldName = "Make",
            showHeader = true,
            options = state.makesList.map { Pair(it.code.toString(), it.desc.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onMakeSelected,
            selectedValue = selectedMake,
            selectedKey = state.makesList.find { it.desc == selectedMake }?.code.toString()
        )
        state.modelsResponse.body?.list?.map { Pair(it.desc.toString(), it.desc.toString()) }?.let {
            CustomDropDown(
                fieldName = "Model",
                showHeader = true,
                options = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onValueSelected = onModelSelected,
                selectedValue = selectedModel,
                selectedKey = selectedModel
            )
        }
        CustomDropDown(
            options = state.sellersList.map {
                Pair(
                    it.id,
                    "${it.id} - ${it.label} (${it.sc}, ${it.ss})"
                )
            },
            fieldName = "Seller",
            showHeader = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onSellerSelected,
            selectedValue = if (selectedSeller != null) "${selectedSeller.id} - ${selectedSeller.label} (${selectedSeller.sc}, ${selectedSeller.ss})" else "",
            selectedKey = selectedSeller?.id ?: ""

        )
        CustomDropDown(
            options = state.primaryDamages.map {
                Pair(
                    it.code,
                    it.desc
                )
            },
            fieldName = "Primary Damages",
            showHeader = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onPrimaryDamageSelected,
            selectedValue = selectedPrimaryDamage?.desc ?: "",
            selectedKey = selectedPrimaryDamage?.code ?: ""
        )
        CustomDropDown(
            options = listOf(Pair("Yes", "Yes"), Pair("No", " No")),
            fieldName = "Airbags Deployed?",
            showHeader = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = isAirBagsDeployed,
            selectedValue = selectedAirBags,
            selectedKey = selectedAirBags
        )
        DecodeButton(onGenerateRTL, context)
    }
}

@Composable
fun initializeImagePlaceholders(): List<ImagePlaceholder> {
    val placeholders = listOf(
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_01),
            R.drawable.overlay_01,
            "Front Side Left Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_02),
            R.drawable.overlay_02,
            "Front Side Right Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_03),
            R.drawable.overlay_03,
            "Rear Side Right Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_04),
            R.drawable.overlay_04,
            "Rear Side Left Angle"
        )
    )
    return placeholders
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageTilePicker(
    imageUris: List<Uri?>,
    onImageUrisChanged: (Uri?, Int) -> Unit,
    context: Context,
    imagePickerState: LazyListState
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentTileIndex by remember { mutableStateOf(-1) } // Track the current tile index
    val imagePlaceholders = initializeImagePlaceholders()
    var showCustomCamera by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                showDialog = true
            } else {
                // Handle permission denied
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.getStringExtra("capturedImageUri")?.let { Uri.parse(it) }
            if (imageUri != null && currentTileIndex != -1) {
                onImageUrisChanged(imageUri, currentTileIndex)
            }
        }
        showCustomCamera = false
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null && currentTileIndex != -1) {
                onImageUrisChanged(uri, currentTileIndex)
            }
        }
    )
    if (showCustomCamera) {
        LaunchedEffect(Unit) {
            val intent = Intent(context, CameraActivity::class.java).apply {
                putExtra("overlayResId", imagePlaceholders[currentTileIndex].overlay.hashCode())
            }
            cameraLauncher.launch(intent)
        }
    } else {
        LazyRow(
            state = imagePickerState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.overrideParentHorizontalPadding(16),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = imagePickerState)
        ) {
            itemsIndexed(imagePlaceholders, key = { index, _ -> index }) { index, item ->
                Column {
                    Card(
                        modifier = Modifier
                            .width((getWindowWidth().toDp * 0.9).dp)
                            .size(200.dp)
                            .clickable {
                                currentTileIndex = index // Set the current tile index
                                val permissions = arrayListOf(Manifest.permission.CAMERA)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                    permissions.addAll(
                                        arrayOf(
                                            READ_MEDIA_IMAGES,
                                            READ_MEDIA_VIDEO,
                                            READ_MEDIA_VISUAL_USER_SELECTED
                                        )
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissions.addAll(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
                                } else {
                                    permissions.addAll(arrayOf(READ_EXTERNAL_STORAGE))
                                }
                                cameraPermissionLauncher.launch(permissions.toTypedArray())
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            painter = if (imageUris[index] === null) item.placeholder else rememberAsyncImagePainter(
                                imageUris[index]
                            ),
                            contentDescription = imagePlaceholders[index].contentDescription,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = if (imageUris[index] === null) ContentScale.FillBounds else ContentScale.FillBounds
                        )
                    }
                    Text(
                        text = imagePlaceholders[index].contentDescription,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        style = labelBold14
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Choose an action") },
                text = { Text("Select an image from the gallery or capture a new one.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            showCustomCamera = true
                        }
                    ) {
                        Text("Capture")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            pickImageLauncher.launch("image/*")
                            showDialog = false
                        }
                    ) {
                        Text("Gallery")
                    }
                }
            )
        }
    }
}

@Composable
fun DecodeButton(onGenerateRTL: (Context) -> Unit, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onGenerateRTL(context) },
            shape = ButtonDefaults.shape, // This uses the default rounded shape
            colors = ButtonDefaults.buttonColors(
                containerColor = CopartBlue,
                contentColor = Color.White
            ),
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    TablerIcons.Key,
                    contentDescription = "Send Icon",
                    modifier = Modifier.rotate(45f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.generate))
            }
        }
    }
}

@Composable
fun VINDecodeHeader() {
    Text(
        text = stringResource(R.string.vehicle_details),
        style = labelNormal16,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun VINDecodePreview() {
    VINDecode(
        state = VehicleDetailsContract.State(
            vinNumber = "",
            year = "",
            make = "",
            model = "",
            yearsList = emptyList(),
            makesList = emptyList(),
            modelsResponse = VehicleModelsResponse(),
            imageUris = listOf<Uri?>(null, null, null, null),
            sellersList = arrayListOf(),
            selectedSeller = null,
            primaryDamages = emptyList(),
            selectedPrimaryDamage = null,
            isAirBagsDeployed = "No",
            isLoading = false,
            isError = false,
            isRTLSuccess = false,
            isRTLFailure = false,
            errorMessage = ""
        ),
        onVinChanged = {},
        onGenerateRTL = {},
        onMakeSelected = { _, _ -> },
        onYearSelected = { _, _ -> },
        onModelSelected = { _, _ -> },
        onImageUrisChanged = { _, _ -> },
        onSellerSelected = { _, _ -> },
        onPrimaryDamageSelected = { _, _ -> },
        isAirBagsDeployed = { _, _ -> }
    )
}