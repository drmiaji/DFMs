package com.drmiaji.fortyahadith

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drmiaji.fortyahadith.activity.About
import com.drmiaji.fortyahadith.activity.SettingsActivity
import com.drmiaji.fortyahadith.ui.ChapterListActivity
import com.drmiaji.fortyahadith.ui.WebViewActivity
import com.drmiaji.fortyahadith.ui.theme.MyAppTheme
import com.drmiaji.fortyahadith.ui.theme.topBarColors
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAppTheme {
                MainScreen(
                    onNavigateToContents = { goToContents() },
                    onNavigateToSettings = { goToSettings() },
                    onLogoClick = { goToContents() }
                )
            }
        }
    }

    private fun goToContents() {
        startActivity(Intent(this, ChapterListActivity::class.java)) // ← replace with your actual Activity
    }
    private fun goToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java)) // ← replace with your actual Activity
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToContents: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogoClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val groupedMenuItems = listOf(
        DrawerMenuGroup(
            groupTitle = "ইউটিউব চ্যানেলসমূহ",
            items = listOf(
                DrawerItem("ড. মু. তাহিরুল কাদেরী", "YouTube", R.drawable.youtube, linkUrl = "https://youtube.com/tahirulqadri"),
                DrawerItem("Dr Miaji|Official", "YouTube", R.drawable.youtube, linkUrl = "https://www.youtube.com/@bmiaji"),
            )
        ),
        DrawerMenuGroup(
            groupTitle = "এবাউট এপ",
            items = listOf(
                DrawerItem("সেটিংস", "এপ নিয়ন্ত্রণ করুন", Icons.Default.Settings, activityClass = SettingsActivity::class.java),
                DrawerItem("এবাউট", "আমাদের সম্পর্কে", Icons.Default.Architecture, activityClass = About::class.java),
            )
        ),
        DrawerMenuGroup(
            groupTitle = "ফেসবুক",
            items = listOf(
                DrawerItem("ড. মিয়াজী", "Facebook", Icons.Default.Facebook, linkUrl = "https://www.facebook.com/batenmiaji2")
            )
        ),
        DrawerMenuGroup(
            groupTitle = "ওয়েবসাইটসমূহ",
            items = listOf(
                DrawerItem("Minhaj-ul-Quran", "Official Site", R.drawable.weblink, linkUrl = "https://www.minhaj.org"),
                DrawerItem("Dr Miaji", "Official Site", R.drawable.weblink, linkUrl = "https://www.drmiaji.com")
            )
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    ),
                drawerContainerColor = Color.Transparent
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Logo and header section (fixed at top)
                    MyLogo(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    )

                    HorizontalDivider()

                    // State for tracking selected item
                    var selectedItem by remember { mutableStateOf<DrawerItem?>(null) }

                    // Scrollable content section
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        groupedMenuItems.forEach { group ->
                            item {
                                Text(
                                    text = group.groupTitle,
                                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 4.dp),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.solaimanlipi))
                                    )
                                )
                            }

                            itemsIndexed(group.items) { _, item ->
                                DrawerCardItem(
                                    item = item,
                                    selected = item == selectedItem,
                                    onClick = {
                                        selectedItem = item
                                        when {
                                            item.activityClass != null -> {
                                                context.startActivity(Intent(context, item.activityClass))
                                            }
                                            item.linkUrl != null -> {
                                                if (item.linkUrl.contains("facebook.com")) {
                                                    // Open in external browser
                                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.linkUrl))
                                                    context.startActivity(browserIntent)
                                                } else {
                                                    // Open in WebViewActivity
                                                    val intent = Intent(context, WebViewActivity::class.java).apply {
                                                        putExtra("title", item.title)
                                                        putExtra("url", item.linkUrl)
                                                    }
                                                    context.startActivity(intent)
                                                }
                                            }
                                        }
                                        scope.launch { drawerState.close() }
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp)) // space between groups
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name),
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.solaimanlipi)),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        ) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { showMenu = !showMenu }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("About") },
                                    onClick = { context.startActivity(Intent(context,
                                        About::class.java)); showMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = { context.startActivity(Intent(context,
                                        SettingsActivity::class.java)); showMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Privacy Policy") },
                                    onClick = {
                                        val url = "https://drmiaji.github.io/Tajweed/privacy_policy.html\n"
                                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                        context.startActivity(intent)
                                        showMenu = false
                                    }
                                )
                            }
                        }
                    },
                    colors = topBarColors()
                )
            }
        ) { innerPadding ->
            val colors = MaterialTheme.colorScheme
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colors.secondaryContainer,  // softer shade for top
                                colors.secondary            // stronger shade for bottom
                            )
                        )
                    )
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.coverpage),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onLogoClick() }
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                )
            }
        }
    }
}

data class DrawerMenuGroup(
    val groupTitle: String,
    val items: List<DrawerItem>
)

// Data class for drawer items
data class DrawerItem(
    val title: String,
    val subtitle: String = "",
    val icon: Any,
    val activityClass: Class<out Activity>? = null,
    val linkUrl: String? = null
)

@Composable
fun MyLogo(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primary.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorScheme.primary,
                            colorScheme.primary.copy(alpha = 0.8f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Decorative circles in background (subtle branding element)
            Canvas(modifier = Modifier.matchParentSize()) {
                val circleColor = colorScheme.onPrimary.copy(alpha = 0.07f)
                drawCircle(
                    color = circleColor,
                    radius = size.width * 0.2f,
                    center = Offset(size.width * 0.85f, size.height * 0.2f)
                )
                drawCircle(
                    color = circleColor,
                    radius = size.width * 0.1f,
                    center = Offset(size.width * 0.15f, size.height * 0.8f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App icon with border and shadow
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(4.dp, CircleShape)
                        .background(colorScheme.surface, CircleShape)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    // App name with slightly enhanced styling
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.fillMaxWidth(), // Ensures horizontal centering space
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.solaimanlipi)),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp,
                            textAlign = TextAlign.Center, // Aligns text within its bounds
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.2f),
                                offset = Offset(1f, 1f),
                                blurRadius = 2f
                            )
                        ),
                        color = colorScheme.onPrimary
                    )

                    Spacer(Modifier.height(4.dp))

                    // Author name with slightly different styling
                    Text(
                        text = "ডক্টর আব্দুল বাতেন মিয়াজী",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.solaimanlipi)),
                            letterSpacing = 0.2.sp
                        ),
                        color = colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerCardItem(
    item: DrawerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val shape = RoundedCornerShape(12.dp)

    // Animate background color
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "bgColor"
    )

    // Border and icon tint colors
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val iconTint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Handle different icon types
                when (item.icon) {
                    is ImageVector -> {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = iconTint,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    is Int -> {
                        Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape),
                            colorFilter = if (selected) ColorFilter.tint(iconTint) else null
                        )
                    }
                    is Painter -> {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.title,
                            tint = iconTint,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.solaimanlipi))
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (item.subtitle.isNotEmpty()) {
                        Text(
                            text = item.subtitle,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.solaimanlipi))
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = iconTint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyAppTheme {
        MainScreen(onLogoClick = {})
    }
}
