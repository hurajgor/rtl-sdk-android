package com.copart.rtlaisdk.ui.vehicleDetails.composables

import android.Manifest
import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.ImagePlaceholder
import com.copart.rtlaisdk.data.model.PrimaryDamagesItem
import com.copart.rtlaisdk.data.model.SellersListItem
import com.copart.rtlaisdk.data.model.VehicleMakesResponseBody
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponseBody
import com.copart.rtlaisdk.ui.common.CustomDropDown
import com.copart.rtlaisdk.ui.common.CustomTextField
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.GreyChateau
import com.copart.rtlaisdk.ui.theme.WhiteSmoke
import com.copart.rtlaisdk.ui.theme.labelBold14
import com.copart.rtlaisdk.ui.theme.labelNormal16
import com.copart.rtlaisdk.utils.createTempPictureUri
import com.copart.rtlaisdk.utils.getWindowWidth
import com.copart.rtlaisdk.utils.overrideParentHorizontalPadding
import com.copart.rtlaisdk.utils.toDp
import compose.icons.TablerIcons
import compose.icons.tablericons.Key

@Composable
fun VINDecode(
    yearList: List<VehicleYearsResponseBody>,
    makeList: List<VehicleMakesResponseBody>,
    modelsResponse: VehicleModelsResponse,
    sellersList: List<SellersListItem>,
    primaryDamages: List<PrimaryDamagesItem>,
    imageUris: List<Uri?>,
    onVinChanged: (String) -> Unit,
    onYearSelected: (String, String) -> Unit,
    onMakeSelected: (String, String) -> Unit,
    onModelSelected: (String, String) -> Unit,
    onGenerateRTL: () -> Unit,
    onImageUrisChanged: (Uri?, Int) -> Unit,
    onSellerSelected: (String, String) -> Unit,
    onPrimaryDamageSelected: (String, String) -> Unit,
    isAirBagsDeployed: (String, String) -> Unit
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteSmoke)
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        VINDecodeHeader()
        ImageTilePicker(imageUris, onImageUrisChanged)
        CustomTextField(
            stringResource(id = R.string.vin),
            stringResource(R.string.vin_placeholder),
            onTextChanged = onVinChanged
        )
        CustomDropDown(
            fieldName = "Year",
            showHeader = true,
            options = yearList.map { Pair(it.code.toString(), it.code.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onYearSelected
        )
        CustomDropDown(
            fieldName = "Make",
            showHeader = true,
            options = makeList.map { Pair(it.code.toString(), it.desc.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = onMakeSelected
        )
        modelsResponse.body?.list?.map { Pair(it.desc.toString(), it.desc.toString()) }?.let {
            CustomDropDown(
                fieldName = "Model",
                showHeader = true,
                options = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onValueSelected = onModelSelected
            )
        }
        CustomDropDown(
            options = sellersList.map {
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
            onValueSelected = onSellerSelected
        )
        CustomDropDown(
            options = primaryDamages.map {
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
            onValueSelected = onPrimaryDamageSelected
        )
        CustomDropDown(
            options = listOf(Pair("Yes", "Yes"), Pair("No", " No")),
            fieldName = "Airbags Deployed?",
            showHeader = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onValueSelected = isAirBagsDeployed,
            selectedValue = "No",
            selectedKey = "No"
        )
        DecodeButton(onGenerateRTL)
    }
}

@Composable
fun initializeImagePlaceholders(): List<ImagePlaceholder> {
    val placeholders = listOf(
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_01),
            "Front Side Left Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_02),
            "Front Side Right Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_03),
            "Rear Side Right Angle"
        ),
        ImagePlaceholder(
            painterResource(id = R.drawable.placeholder_04),
            "Rear Side Left Angle"
        )
    )
    return placeholders
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageTilePicker(imageUris: List<Uri?>, onImageUrisChanged: (Uri?, Int) -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var currentTileIndex by remember { mutableStateOf(-1) } // Track the current tile index
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val imagePlaceholders = initializeImagePlaceholders()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showDialog = true
            } else {
                // Handle permission denied
            }
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess && currentTileIndex != -1) {
                onImageUrisChanged(tempPhotoUri, currentTileIndex)
            }
        }
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null && currentTileIndex != -1) {
                onImageUrisChanged(uri, currentTileIndex)
            }
        }
    )

    val state = rememberLazyListState()

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.overrideParentHorizontalPadding(16),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    ) {
        itemsIndexed(imagePlaceholders) { index, item ->
            Column {
                Card(
                    modifier = Modifier
                        .width((getWindowWidth().toDp * 0.9).dp)
                        .size(200.dp)
                        .clickable {
                            currentTileIndex = index // Set the current tile index
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Image(
                        painter = if (imageUris[index] === null) item.painter else rememberAsyncImagePainter(
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
                        tempPhotoUri = context.createTempPictureUri()
                        takePictureLauncher.launch(tempPhotoUri)
                        showDialog = false
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

@Composable
fun DecodeButton(onGenerateRTL: () -> Unit) {
    var isValid = true // Replace with your validation logic
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onGenerateRTL() },
            shape = ButtonDefaults.shape, // This uses the default rounded shape
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isValid) CopartBlue else GreyChateau,
                contentColor = Color.White
            ),
            enabled = isValid,
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
        yearList = emptyList(),
        makeList = emptyList(),
        modelsResponse = VehicleModelsResponse(),
        sellersList = emptyList(),
        imageUris = remember {
            mutableStateListOf<Uri?>(null, null, null, null)
        },
        primaryDamages = emptyList(),
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