@file:OptIn(ExperimentalMaterial3Api::class)

package com.mongodb.realmlocal.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mongodb.realmlocal.User

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC5)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5)
        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                Scaffold(topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Sharing Datalayer",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = Color(0xFF3700B3),
                            titleContentColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(onClick = { /* doSomething() */ }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description",
                                    tint = Color.White
                                )
                            }
                        },
                    )
                }) {
                    Container(it.calculateTopPadding())
                }
            }
        }
    }


}

@Composable
fun Container(topPadding: Dp) {
    val homeVM = viewModel<MainViewModel>()

    val onUserClick = { user: User ->
        homeVM.saveUserInfo(user)
    }

    val userList = homeVM.users.observeAsState(initial = emptyList())
    val user = remember { mutableStateOf(User(), neverEqualPolicy()) }

    Column(modifier = Modifier.padding(top = topPadding)) {

        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val buttonLabel = if (user.value.name.isEmpty()) "Save" else "Update"

            OutlinedTextField(
                label = { Text(text = "Name") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp),
                value = user.value.name,
                onValueChange = {
                    user.value = user.value.apply {
                        name = it
                    }
                },
                textStyle = TextStyle(fontWeight = FontWeight.Bold)
            )


            OutlinedTextField(
                label = { Text(text = "Twitter Handle") },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally),
                value = user.value.twitterHandle ?: "",
                onValueChange = {
                    user.value = user.value.apply {
                        twitterHandle = it
                    }
                },
                textStyle = TextStyle(fontWeight = FontWeight.Bold),
            )

            Row(horizontalArrangement = Arrangement.Center) {
                Button(
                    modifier = Modifier.padding(top = 12.dp, end = 6.dp),
                    onClick = {
                        onUserClick(user.value)
                        user.value = User()
                    }) {
                    Text(text = buttonLabel)
                }
            }
        }

        Divider(color = Color.Black)

        OutlinedButton(
            modifier = Modifier
                .align(CenterHorizontally),
            onClick = {
                homeVM.getUsers()
            }) {
            Text(text = "Pull to get Latest")
        }

        Divider(color = Color.Black)

        LazyColumn {
            items(count = userList.value.size) { position ->
                UserItemRow(user = userList.value[position], onUpdateClick = {
                    user.value = it
                })
            }
        }
    }
}

@Composable
fun UserItemRow(user: User, onUpdateClick: (user: User) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${user.name} \n${user.twitterHandle}")

        OutlinedButton(
            onClick = {
                val userCopy = User().apply {
                    this._id = user._id
                    this.name = user.name
                    this.twitterHandle = user.twitterHandle
                }
                onUpdateClick(userCopy)

            }) {
            Text(text = "Update")
        }
    }
}


